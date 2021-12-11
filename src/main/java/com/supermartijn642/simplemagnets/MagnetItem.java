package com.supermartijn642.simplemagnets;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.simplemagnets.packets.magnet.PacketItemInfo;
import com.supermartijn642.simplemagnets.packets.magnet.PacketToggleMagnetMessage;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
@Mod.EventBusSubscriber
public abstract class MagnetItem extends Item implements ICapabilityProvider {

    public MagnetItem(String registryName){
        super();
        this.setRegistryName(registryName);
        this.setUnlocalizedName(SimpleMagnets.MODID + "." + registryName);

        this.setMaxStackSize(1);
        this.setCreativeTab(SimpleMagnets.GROUP);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn){
        ItemStack stack = playerIn.getHeldItem(handIn);
        toggleMagnet(playerIn, stack);
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
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
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected){
        NBTTagCompound tag = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
        if(tag.hasKey("active") && tag.getBoolean("active")){
            if(this.canPickupItems(tag)){
                int r = this.getRangeItems(tag);
                AxisAlignedBB area = new AxisAlignedBB(entityIn.getPositionVector().addVector(-r, -r, -r), entityIn.getPositionVector().addVector(r, r, r));

                List<EntityItem> items = worldIn.getEntitiesWithinAABB(EntityItem.class, area,
                    item -> item.isEntityAlive() && (!worldIn.isRemote || item.ticksExisted > 1) &&
                        (!item.getEntityData().hasKey("simplemagnets:throwerIdMost") || !item.getEntityData().getUniqueId("simplemagnets:throwerId").equals(entityIn.getUniqueID()) || !item.cannotPickup()) &&
                        !item.getItem().isEmpty() && !item.getEntityData().hasKey("PreventRemoteMovement") && this.canPickupStack(tag, item.getItem())

                );
                items.forEach(item -> item.setPosition(entityIn.posX, entityIn.posY, entityIn.posZ));
            }

            if(!worldIn.isRemote && this.canPickupXp(tag) && entityIn instanceof EntityPlayer){
                int r = this.getRangeXp(tag);
                AxisAlignedBB area = new AxisAlignedBB(entityIn.getPositionVector().addVector(-r, -r, -r), entityIn.getPositionVector().addVector(r, r, r));

                EntityPlayer player = (EntityPlayer)entityIn;
                List<EntityXPOrb> orbs = worldIn.getEntitiesWithinAABB(EntityXPOrb.class, area);
                orbs.forEach(orb -> {
                    orb.delayBeforeCanPickup = 0;
                    player.xpCooldown = 0;
                    orb.onCollideWithPlayer(player);
                });
            }
        }
        stack.setTagCompound(tag);
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

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn){
        tooltip.add(TextComponents.fromTextComponent(this.getTooltip()).color(TextFormatting.AQUA).format());
        super.addInformation(stack, worldIn, tooltip, flagIn);
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
