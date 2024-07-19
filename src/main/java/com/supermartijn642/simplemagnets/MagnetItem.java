package com.supermartijn642.simplemagnets;

import com.mojang.serialization.Codec;
import com.supermartijn642.core.CommonUtils;
import com.supermartijn642.core.item.BaseItem;
import com.supermartijn642.core.item.ItemProperties;
import com.supermartijn642.simplemagnets.packets.magnet.PacketItemInfo;
import com.supermartijn642.simplemagnets.packets.magnet.PacketToggleMagnetMessage;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.function.Consumer;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public abstract class MagnetItem extends BaseItem {

    public static final DataComponentType<Boolean> ACTIVE = DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build();

    public MagnetItem(){
        super(ItemProperties.create().group(SimpleMagnets.GROUP).maxStackSize(1));
    }

    @Override
    public ItemUseResult interact(ItemStack stack, Player player, InteractionHand hand, Level level){
        toggleMagnet(player, stack);
        return ItemUseResult.success(stack);
    }

    public static void toggleMagnet(Player player, ItemStack stack){
        if(!player.level().isClientSide && stack.getItem() instanceof MagnetItem){
            //noinspection DataFlowIssue
            boolean active = stack.has(ACTIVE) && stack.get(ACTIVE);
            stack.set(ACTIVE, !active);
            // let the client decide whether to show the toggle message and play a sound
            SimpleMagnets.CHANNEL.sendToPlayer(player, new PacketToggleMagnetMessage(active));
        }
    }

    @Override
    public void inventoryUpdate(ItemStack stack, Level level, Entity entity, int itemSlot, boolean isSelected){
        // Prevent spectators from picking up items
        if(entity.isSpectator())
            return;

        //noinspection DataFlowIssue
        if(stack.has(ACTIVE) && stack.get(ACTIVE)){
            if(this.canPickupItems(stack)){
                int r = this.getRangeItems(stack);
                AABB area = new AABB(entity.position().add(-r, -r, -r), entity.position().add(r, r, r));

                List<ItemEntity> items = level.getEntities(EntityType.ITEM, area,
                    item -> item.isAlive() && (!level.isClientSide || item.tickCount > 1) &&
                        (item.thrower == null || !item.thrower.equals(entity.getUUID()) || !item.hasPickUpDelay()) &&
                        !item.getItem().isEmpty() && !item.getPersistentData().contains("PreventRemoteMovement") && this.canPickupStack(stack, item.getItem())
                );
                items.forEach(item -> item.setPos(entity.getX(), entity.getY(), entity.getZ()));
                // Directly add items to the player's inventory when ItemPhysic is installed
                if(!level.isClientSide && entity instanceof Player && CommonUtils.isModLoaded("itemphysic"))
                    items.forEach(item -> playerTouch(item, (Player)entity));
            }

            if(!level.isClientSide && this.canPickupXp(stack) && entity instanceof Player){
                int r = this.getRangeXp(stack);
                AABB area = new AABB(entity.position().add(-r, -r, -r), entity.position().add(r, r, r));

                Player player = (Player)entity;
                List<ExperienceOrb> orbs = level.getEntitiesOfClass(ExperienceOrb.class, area);
                orbs.forEach(orb -> {
                    orb.invulnerableTime = 0;
                    player.takeXpDelay = 0;
                    orb.playerTouch(player);
                });
            }
        }
    }

    /**
     * Copied from {@link ItemEntity#playerTouch(Player)}. Use this when ItemPhysic is installed to still pick up items.
     */
    private static void playerTouch(ItemEntity itemEntity, Player player){
        if(!itemEntity.level().isClientSide){
            if(itemEntity.hasPickUpDelay()) return;
            ItemStack itemstack = itemEntity.getItem();
            Item item = itemstack.getItem();
            int i = itemstack.getCount();

            int hook = net.minecraftforge.event.ForgeEventFactory.onItemPickup(itemEntity, player);
            if(hook < 0) return;

            ItemStack copy = itemstack.copy();
            if(!itemEntity.hasPickUpDelay() && (itemEntity.getOwner() == null || itemEntity.lifespan - itemEntity.getAge() <= 200 || itemEntity.getOwner().equals(player.getUUID())) && (hook == 1 || i <= 0 || player.getInventory().add(itemstack))){
                copy.setCount(copy.getCount() - itemstack.getCount());
                ForgeEventFactory.firePlayerItemPickupEvent(player, itemEntity, copy);
                player.take(itemEntity, i);
                if(itemstack.isEmpty()){
                    itemEntity.discard();
                    itemstack.setCount(i);
                }

                player.awardStat(Stats.ITEM_PICKED_UP.get(item), i);
                player.onItemPickup(itemEntity);
            }
        }
    }

    protected abstract boolean canPickupItems(ItemStack magnet);

    protected abstract boolean canPickupStack(ItemStack magnet, ItemStack stack);

    protected abstract boolean canPickupXp(ItemStack magnet);

    protected abstract int getRangeItems(ItemStack magnet);

    protected abstract int getRangeXp(ItemStack magnet);

    @Override
    public boolean isFoil(ItemStack stack){
        //noinspection DataFlowIssue
        return stack.has(ACTIVE) && stack.get(ACTIVE);
    }

    @Override
    protected void appendItemInformation(ItemStack stack, Consumer<Component> info, boolean advanced){
        info.accept(this.getTooltip());
    }

    protected abstract Component getTooltip();

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking e){
        if(!e.getEntity().level().isClientSide && e.getTarget() instanceof ItemEntity && ((ItemEntity)e.getTarget()).thrower != null)
            SimpleMagnets.CHANNEL.sendToPlayer(e.getEntity(), new PacketItemInfo((ItemEntity)e.getTarget()));
    }
}
