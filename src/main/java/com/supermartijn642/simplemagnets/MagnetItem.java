package com.supermartijn642.simplemagnets;

import com.supermartijn642.simplemagnets.packets.PacketItemInfo;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.List;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public abstract class MagnetItem extends Item {
    public MagnetItem(String registryName){
        super(new Properties().group(ItemGroup.SEARCH).maxStackSize(1));
        this.setRegistryName(registryName);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn){
        ItemStack stack = playerIn.getHeldItem(handIn);
        toggleMagnet(playerIn, stack);
        return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }

    public static void toggleMagnet(PlayerEntity player, ItemStack stack){
        if(!player.world.isRemote && stack.getItem() instanceof MagnetItem){
            boolean active = stack.getOrCreateTag().contains("active") && stack.getOrCreateTag().getBoolean("active");
            stack.getOrCreateTag().putBoolean("active", !active);
            if(active)
                player.world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.BLOCK_NOTE_BLOCK_BELL, SoundCategory.PLAYERS, 0.4f, 0.01f);
            else
                player.world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.BLOCK_NOTE_BLOCK_BELL, SoundCategory.PLAYERS, 0.4f, 0.9f);
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected){
        CompoundNBT tag = stack.getOrCreateTag();
        if(tag.contains("active") && tag.getBoolean("active")){
            if(this.canPickupItems(tag)){
                int r = this.getRangeItems(tag);
                AxisAlignedBB area = new AxisAlignedBB(entityIn.getPositionVec().add(-r, -r, -r), entityIn.getPositionVec().add(r, r, r));

                List<ItemEntity> items = worldIn.getEntitiesWithinAABB(EntityType.ITEM, area,
                    item ->
                        !item.getPersistentData().contains("PreventRemoteMovement") && this.canPickupStack(tag, item.getItem()) &&
                            (item.getThrowerId() == null || !item.getThrowerId().equals(entityIn.getUniqueID()) || !item.cannotPickup())
                );
                items.forEach(item -> item.setPosition(entityIn.getPosX(), entityIn.getPosY(), entityIn.getPosZ()));
            }

            if(!worldIn.isRemote && this.canPickupXp(tag) && entityIn instanceof PlayerEntity){
                int r = this.getRangeXp(tag);
                AxisAlignedBB area = new AxisAlignedBB(entityIn.getPositionVec().add(-r, -r, -r), entityIn.getPositionVec().add(r, r, r));

                PlayerEntity player = (PlayerEntity)entityIn;
                List<ExperienceOrbEntity> orbs = worldIn.getEntitiesWithinAABB(ExperienceOrbEntity.class, area);
                orbs.forEach(orb -> {
                    orb.delayBeforeCanPickup = 0;
                    player.xpCooldown = 0;
                    orb.onCollideWithPlayer(player);
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

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn){
        tooltip.add(this.getTooltip().mergeStyle(TextFormatting.AQUA));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    protected abstract TextComponent getTooltip();

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking e){
        if(!e.getPlayer().world.isRemote && e.getTarget() instanceof ItemEntity && ((ItemEntity)e.getTarget()).getThrowerId() != null)
            SimpleMagnets.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)e.getPlayer()), new PacketItemInfo((ItemEntity)e.getTarget()));
    }
}
