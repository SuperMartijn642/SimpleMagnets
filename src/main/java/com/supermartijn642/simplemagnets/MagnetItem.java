package com.supermartijn642.simplemagnets;

import com.supermartijn642.core.CommonUtils;
import com.supermartijn642.core.item.BaseItem;
import com.supermartijn642.core.item.ItemProperties;
import com.supermartijn642.simplemagnets.extensions.SimpleMagnetsItemEntity;
import com.supermartijn642.simplemagnets.packets.magnet.PacketItemInfo;
import com.supermartijn642.simplemagnets.packets.magnet.PacketToggleMagnetMessage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public abstract class MagnetItem extends BaseItem {

    public MagnetItem(){
        super(ItemProperties.create().group(SimpleMagnets.GROUP).maxStackSize(1));
    }

    @Override
    public ItemUseResult interact(ItemStack stack, Player player, InteractionHand hand, Level level){
        toggleMagnet(player, stack);
        return ItemUseResult.success(stack);
    }

    public static void toggleMagnet(Player player, ItemStack stack){
        if(!player.level.isClientSide && stack.getItem() instanceof MagnetItem){
            boolean active = stack.getOrCreateTag().contains("active") && stack.getOrCreateTag().getBoolean("active");
            stack.getOrCreateTag().putBoolean("active", !active);
            // let the client decide whether to show the toggle message and play a sound
            SimpleMagnets.CHANNEL.sendToPlayer(player, new PacketToggleMagnetMessage(active));
        }
    }

    @Override
    public void inventoryUpdate(ItemStack stack, Level level, Entity entity, int itemSlot, boolean isSelected){
        // Prevent spectators from picking up items
        if(entity.isSpectator())
            return;

        CompoundTag tag = stack.getOrCreateTag();
        if(tag.contains("active") && tag.getBoolean("active")){
            if(this.canPickupItems(tag)){
                int r = this.getRangeItems(tag);
                AABB area = new AABB(entity.position().add(-r, -r, -r), entity.position().add(r, r, r));

                // TODO look for alternative to 'PreventRemoteMovement' tag on Fabric

                List<ItemEntity> items = level.getEntities(EntityType.ITEM, area,
                    item -> item.isAlive() && (!level.isClientSide || item.tickCount > 1) &&
                        (item.getThrower() == null || !item.getThrower().equals(entity.getUUID()) || !item.hasPickUpDelay()) &&
                        !item.getItem().isEmpty() && ((SimpleMagnetsItemEntity)item).simplemagnetsCanBePickedUp() && this.canPickupStack(tag, item.getItem())
                );
                items.forEach(item -> item.setPos(entity.getX(), entity.getY(), entity.getZ()));
                // Directly add items to the player's inventory when ItemPhysic is installed
                if(!level.isClientSide && entity instanceof Player && CommonUtils.isModLoaded("itemphysic"))
                    items.forEach(item -> playerTouch(item, (Player)entity));
            }

            if(!level.isClientSide && this.canPickupXp(tag) && entity instanceof Player){
                int r = this.getRangeXp(tag);
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
        if(!itemEntity.level.isClientSide){
            ItemStack itemstack = itemEntity.getItem();
            Item item = itemstack.getItem();
            int i = itemstack.getCount();

            if(!itemEntity.hasPickUpDelay() && (itemEntity.getOwner() == null || itemEntity.getOwner().equals(player.getUUID())) && i <= 0 || player.getInventory().add(itemstack)){
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

    protected abstract boolean canPickupItems(CompoundTag tag);

    protected abstract boolean canPickupStack(CompoundTag tag, ItemStack stack);

    protected abstract boolean canPickupXp(CompoundTag tag);

    protected abstract int getRangeItems(CompoundTag tag);

    protected abstract int getRangeXp(CompoundTag tag);

    @Override
    public boolean isFoil(ItemStack stack){
        return stack.getOrCreateTag().contains("active") && stack.getOrCreateTag().getBoolean("active");
    }

    @Override
    protected void appendItemInformation(ItemStack stack, @Nullable BlockGetter level, Consumer<Component> info, boolean advanced){
        info.accept(this.getTooltip());
    }

    protected abstract Component getTooltip();

    public static void onStartTracking(Entity target, Player player){
        if(!player.level.isClientSide && target instanceof ItemEntity && ((ItemEntity)target).getThrower() != null)
            SimpleMagnets.CHANNEL.sendToPlayer(player, new PacketItemInfo((ItemEntity)target));
    }
}
