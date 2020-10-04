package com.supermartijn642.simplemagnets;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public abstract class MagnetItem extends Item implements ICapabilityProvider {
    public MagnetItem(String registryName){
        super();
        this.setRegistryName(registryName);
        this.setUnlocalizedName(SimpleMagnets.MODID + ":" + registryName);

        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.SEARCH);
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
            if(active)
                player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_NOTE_BELL, SoundCategory.PLAYERS, 0.4f, 0.01f);
            else
                player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_NOTE_BELL, SoundCategory.PLAYERS, 0.4f, 0.9f);
            stack.setTagCompound(tag);
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
                    item -> !item.getEntityData().hasKey("PreventRemoteMovement") && this.canPickupStack(tag, item.getItem()));
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
}
