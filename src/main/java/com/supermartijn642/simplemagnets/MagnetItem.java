package com.supermartijn642.simplemagnets;

import com.supermartijn642.core.CommonUtils;
import com.supermartijn642.core.item.BaseItem;
import com.supermartijn642.core.item.ItemProperties;
import com.supermartijn642.simplemagnets.packets.magnet.PacketItemInfo;
import com.supermartijn642.simplemagnets.packets.magnet.PacketToggleMagnetMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.hooks.BasicEventHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public abstract class MagnetItem extends BaseItem {

    public MagnetItem(){
        super(ItemProperties.create().group(SimpleMagnets.GROUP).maxStackSize(1));
    }

    @Override
    public ItemUseResult interact(ItemStack stack, PlayerEntity player, Hand hand, World level){
        toggleMagnet(player, stack);
        return ItemUseResult.success(stack);
    }

    public static void toggleMagnet(PlayerEntity player, ItemStack stack){
        if(!player.level.isClientSide && stack.getItem() instanceof MagnetItem){
            boolean active = stack.getOrCreateTag().contains("active") && stack.getOrCreateTag().getBoolean("active");
            stack.getOrCreateTag().putBoolean("active", !active);
            // let the client decide whether to show the toggle message and play a sound
            SimpleMagnets.CHANNEL.sendToPlayer(player, new PacketToggleMagnetMessage(active));
        }
    }

    @Override
    public void inventoryUpdate(ItemStack stack, World level, Entity entity, int itemSlot, boolean isSelected){
        // Prevent spectators from picking up items
        if(entity.isSpectator())
            return;

        CompoundNBT tag = stack.getOrCreateTag();
        if(tag.contains("active") && tag.getBoolean("active")){
            if(this.canPickupItems(tag)){
                int r = this.getRangeItems(tag);
                AxisAlignedBB area = new AxisAlignedBB(entity.position().add(-r, -r, -r), entity.position().add(r, r, r));

                List<ItemEntity> items = level.getEntities(EntityType.ITEM, area,
                    item -> item.isAlive() && (!level.isClientSide || item.tickCount > 1) &&
                        (item.getThrower() == null || !item.getThrower().equals(entity.getUUID()) || !item.hasPickUpDelay()) &&
                        !item.getItem().isEmpty() && !item.getPersistentData().contains("PreventRemoteMovement") && this.canPickupStack(tag, item.getItem())
                );
                items.forEach(item -> item.setPos(entity.getX(), entity.getY(), entity.getZ()));
                // Directly add items to the player's inventory when ItemPhysic is installed
                if(!level.isClientSide && entity instanceof PlayerEntity && CommonUtils.isModLoaded("itemphysic"))
                    items.forEach(item -> playerTouch(item, (PlayerEntity)entity));
            }

            if(!level.isClientSide && this.canPickupXp(tag) && entity instanceof PlayerEntity){
                int r = this.getRangeXp(tag);
                AxisAlignedBB area = new AxisAlignedBB(entity.position().add(-r, -r, -r), entity.position().add(r, r, r));

                PlayerEntity player = (PlayerEntity)entity;
                List<ExperienceOrbEntity> orbs = level.getEntitiesOfClass(ExperienceOrbEntity.class, area);
                orbs.forEach(orb -> {
                    orb.invulnerableTime = 0;
                    player.takeXpDelay = 0;
                    orb.playerTouch(player);
                });
            }
        }
    }

    /**
     * Copied from {@link ItemEntity#playerTouch(PlayerEntity)}. Use this when ItemPhysic is installed to still pick up items.
     */
    private static void playerTouch(ItemEntity itemEntity, PlayerEntity player){
        if(!itemEntity.level.isClientSide){
            if(itemEntity.hasPickUpDelay()) return;
            ItemStack itemstack = itemEntity.getItem();
            Item item = itemstack.getItem();
            int i = itemstack.getCount();

            int hook = net.minecraftforge.event.ForgeEventFactory.onItemPickup(itemEntity, player);
            if(hook < 0) return;

            ItemStack copy = itemstack.copy();
            if(!itemEntity.hasPickUpDelay() && (itemEntity.getOwner() == null || itemEntity.lifespan - itemEntity.getAge() <= 200 || itemEntity.getOwner().equals(player.getUUID())) && (hook == 1 || i <= 0 || player.inventory.add(itemstack))){
                copy.setCount(copy.getCount() - itemstack.getCount());
                BasicEventHooks.firePlayerItemPickupEvent(player, itemEntity, copy);
                player.take(itemEntity, i);
                if(itemstack.isEmpty()){
                    itemEntity.remove();
                    itemstack.setCount(i);
                }

                player.awardStat(Stats.ITEM_PICKED_UP.get(item), i);
                player.onItemPickup(itemEntity);
            }
        }
    }

    protected abstract boolean canPickupItems(CompoundNBT tag);

    protected abstract boolean canPickupStack(CompoundNBT tag, ItemStack stack);

    protected abstract boolean canPickupXp(CompoundNBT tag);

    protected abstract int getRangeItems(CompoundNBT tag);

    protected abstract int getRangeXp(CompoundNBT tag);

    @Override
    public boolean isFoil(ItemStack stack){
        return stack.getOrCreateTag().contains("active") && stack.getOrCreateTag().getBoolean("active");
    }

    @Override
    protected void appendItemInformation(ItemStack stack, @Nullable IBlockReader level, Consumer<ITextComponent> info, boolean advanced){
        info.accept(this.getTooltip());
    }

    protected abstract ITextComponent getTooltip();

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking e){
        if(!e.getPlayer().level.isClientSide && e.getTarget() instanceof ItemEntity && ((ItemEntity)e.getTarget()).getThrower() != null)
            SimpleMagnets.CHANNEL.sendToPlayer(e.getPlayer(), new PacketItemInfo((ItemEntity)e.getTarget()));
    }
}
