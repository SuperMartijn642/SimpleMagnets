package com.supermartijn642.simplemagnets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public abstract class MagnetItem extends Item {
    public MagnetItem(String registryName){
        super(new Properties().group(ItemGroup.SEARCH).maxStackSize(1));
        this.setRegistryName(registryName);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn){
        ItemStack stack = playerIn.getHeldItem(handIn);
        if(!worldIn.isRemote){
            boolean active = stack.getOrCreateTag().contains("active") && stack.getOrCreateTag().getBoolean("active");
            stack.getOrCreateTag().putBoolean("active", !active);
            if(active)
                worldIn.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.BLOCK_NOTE_BLOCK_BELL, SoundCategory.PLAYERS, 0.4f, 0.01f);
            else
                worldIn.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.BLOCK_NOTE_BLOCK_BELL, SoundCategory.PLAYERS, 0.4f, 0.9f);
        }
        return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected){
        CompoundNBT tag = stack.getOrCreateTag();
        if(tag.contains("active") && tag.getBoolean("active")){
            if(this.canPickupItems(tag)){
                int r = this.getRangeItems(tag);
                AxisAlignedBB area = new AxisAlignedBB(entityIn.getPositionVec().add(-r, -r, -r), entityIn.getPositionVec().add(r, r, r));

                List<ItemEntity> items = worldIn.getEntitiesWithinAABB(EntityType.ITEM, area, item -> this.canPickupStack(tag, item.getItem()));
                items.forEach(item -> item.setPosition(entityIn.getPosX(), entityIn.getPosY(), entityIn.getPosZ()));
            }

            if(!worldIn.isRemote && this.canPickupXp(tag) && entityIn instanceof PlayerEntity){
                int r = this.getRangeXp(tag);
                AxisAlignedBB area = new AxisAlignedBB(entityIn.getPositionVec().add(-r, -r, -r), entityIn.getPositionVec().add(r, r, r));

                PlayerEntity player = (PlayerEntity)entityIn;
                List<ExperienceOrbEntity> orbs = worldIn.getEntitiesWithinAABB(ExperienceOrbEntity.class, area);
                orbs.forEach(orb -> {
                    player.giveExperiencePoints(orb.getXpValue());
                    orb.remove();
                });
            }
        }
    }

    protected abstract boolean canPickupItems(CompoundNBT tag);

    protected abstract boolean canPickupStack(CompoundNBT tag, ItemStack stack);

    protected abstract boolean canPickupXp(CompoundNBT tag);

    protected abstract int getRangeItems(CompoundNBT tag);

    protected abstract int getRangeXp(CompoundNBT tag);

    @Override
    public boolean hasEffect(ItemStack stack){
        return stack.getOrCreateTag().contains("active") && stack.getOrCreateTag().getBoolean("active");
    }
}
