package com.supermartijn642.simplemagnets;

import com.supermartijn642.core.CommonUtils;
import com.supermartijn642.core.item.BaseItem;
import com.supermartijn642.core.item.ItemProperties;
import com.supermartijn642.simplemagnets.packets.magnet.PacketItemInfo;
import com.supermartijn642.simplemagnets.packets.magnet.PacketToggleMagnetMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
@Mod.EventBusSubscriber
public abstract class MagnetItem extends BaseItem implements ICapabilityProvider {

    public MagnetItem(){
        super(ItemProperties.create().group(SimpleMagnets.GROUP).maxStackSize(1));
    }

    @Override
    public ItemUseResult interact(ItemStack stack, EntityPlayer player, EnumHand hand, World level){
        toggleMagnet(player, stack);
        return ItemUseResult.success(stack);
    }

    public static void toggleMagnet(EntityPlayer player, ItemStack stack){
        if(!player.world.isRemote && stack.getItem() instanceof MagnetItem){
            NBTTagCompound tag = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
            boolean active = tag.hasKey("active") && tag.getBoolean("active");
            tag.setBoolean("active", !active);
            stack.setTagCompound(tag);
            // let the client decide whether to show the toggle message and play a sound
            SimpleMagnets.CHANNEL.sendToPlayer(player, new PacketToggleMagnetMessage(active));
        }
    }

    @Override
    public void onUpdate(ItemStack stack, World level, Entity entity, int itemSlot, boolean isSelected){
        NBTTagCompound tag = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
        if(tag.hasKey("active") && tag.getBoolean("active")){
            if(this.canPickupItems(tag)){
                int r = this.getRangeItems(tag);
                AxisAlignedBB area = new AxisAlignedBB(entity.getPositionVector().addVector(-r, -r, -r), entity.getPositionVector().addVector(r, r, r));

                List<EntityItem> items = level.getEntitiesWithinAABB(EntityItem.class, area,
                    item -> item.isEntityAlive() && (!level.isRemote || item.ticksExisted > 1) &&
                        (!item.getEntityData().hasKey("simplemagnets:throwerIdMost") || !item.getEntityData().getUniqueId("simplemagnets:throwerId").equals(entity.getUniqueID()) || !item.cannotPickup()) &&
                        !item.getItem().isEmpty() && !item.getEntityData().hasKey("PreventRemoteMovement") && this.canPickupStack(tag, item.getItem())
                );
                items.forEach(item -> item.setPosition(entity.posX, entity.posY, entity.posZ));
                // Directly add items to the player's inventory when ItemPhysic is installed
                if(!level.isRemote && entity instanceof EntityPlayer && CommonUtils.isModLoaded("itemphysic"))
                    items.forEach(item -> playerTouch(item, (EntityPlayer)entity));
            }

            if(!level.isRemote && this.canPickupXp(tag) && entity instanceof EntityPlayer){
                int r = this.getRangeXp(tag);
                AxisAlignedBB area = new AxisAlignedBB(entity.getPositionVector().addVector(-r, -r, -r), entity.getPositionVector().addVector(r, r, r));

                EntityPlayer player = (EntityPlayer)entity;
                List<EntityXPOrb> orbs = level.getEntitiesWithinAABB(EntityXPOrb.class, area);
                orbs.forEach(orb -> {
                    orb.delayBeforeCanPickup = 0;
                    player.xpCooldown = 0;
                    orb.onCollideWithPlayer(player);
                });
            }
        }
        stack.setTagCompound(tag);
    }

    /**
     * Copied from {@link EntityItem#onCollideWithPlayer(EntityPlayer)}. Use this when ItemPhysic is installed to still pick up items.
     */
    private static void playerTouch(EntityItem itemEntity, EntityPlayer player){
        if(!itemEntity.world.isRemote){
            if(itemEntity.cannotPickup()) return;
            ItemStack itemstack = itemEntity.getItem();
            Item item = itemstack.getItem();
            int i = itemstack.getCount();

            int hook = net.minecraftforge.event.ForgeEventFactory.onItemPickup(itemEntity, player);
            if(hook < 0) return;

            ItemStack copy = itemstack.copy();
            if(!itemEntity.cannotPickup() && (itemEntity.getOwner() == null || itemEntity.lifespan - itemEntity.getAge() <= 200 || itemEntity.getOwner().equals(player.getName())) && (hook == 1 || i <= 0 || player.inventory.addItemStackToInventory(itemstack) || copy.getCount() > itemEntity.getItem().getCount())){
                copy.setCount(copy.getCount() - itemstack.getCount());
                net.minecraftforge.fml.common.FMLCommonHandler.instance().firePlayerItemPickupEvent(player, itemEntity, copy);

                if(itemstack.isEmpty()){
                    player.onItemPickup(itemEntity, i);
                    itemEntity.setDead();
                    itemstack.setCount(i);
                }

                player.addStat(StatList.getObjectsPickedUpStats(item), i);
            }
        }
    }

    protected abstract boolean canPickupItems(NBTTagCompound tag);

    protected abstract boolean canPickupStack(NBTTagCompound tag, ItemStack stack);

    protected abstract boolean canPickupXp(NBTTagCompound tag);

    protected abstract int getRangeItems(NBTTagCompound tag);

    protected abstract int getRangeXp(NBTTagCompound tag);

    @Override
    public boolean hasEffect(ItemStack stack){
        return stack.hasTagCompound() && stack.getTagCompound().hasKey("active") && stack.getTagCompound().getBoolean("active");
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt){
        return this;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing){
        return SimpleMagnets.baubles.isBaubleCapability(capability);
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing){
        return SimpleMagnets.baubles.isBaubleCapability(capability) ? SimpleMagnets.baubles.getBaubleCapability(capability, this) : null;
    }

    @Override
    protected void appendItemInformation(ItemStack stack, @Nullable IBlockAccess level, Consumer<ITextComponent> info, boolean advanced){
        info.accept(this.getTooltip());
    }

    protected abstract ITextComponent getTooltip();

    @SubscribeEvent
    public static void onItemToss(ItemTossEvent e){
        if(e.getEntityItem() != null && !e.getEntityItem().world.isRemote && e.getPlayer() != null)
            e.getEntityItem().getEntityData().setUniqueId("simplemagnets:throwerId", e.getPlayer().getUniqueID());
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking e){
        if(!e.getEntityPlayer().world.isRemote && e.getTarget() instanceof EntityItem && e.getTarget().getEntityData().hasKey("simplemagnets:throwerIdMost"))
            SimpleMagnets.CHANNEL.sendToPlayer(e.getEntityPlayer(), new PacketItemInfo((EntityItem)e.getTarget()));
    }
}
