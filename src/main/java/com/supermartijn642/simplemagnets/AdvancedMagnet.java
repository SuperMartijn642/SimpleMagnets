package com.supermartijn642.simplemagnets;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.simplemagnets.gui.MagnetContainer;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public class AdvancedMagnet extends MagnetItem {

    public AdvancedMagnet(){
        super("advancedmagnet");
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn){
        if(!playerIn.isShiftKeyDown())
            return super.use(worldIn, playerIn, handIn);
        int slot = handIn == InteractionHand.MAIN_HAND ? playerIn.getInventory().selected : 40;
        if(!worldIn.isClientSide){
            NetworkHooks.openGui((ServerPlayer)playerIn, new MenuProvider() {
                @Override
                public Component getDisplayName(){
                    return playerIn.getItemInHand(handIn).hasCustomHoverName() ? playerIn.getItemInHand(handIn).getHoverName() : TextComponents.translation("gui.advancedmagnet.title").get();
                }

                @Nullable
                @Override
                public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player){
                    return new MagnetContainer(windowId, player, slot);
                }
            }, data -> data.writeInt(slot));
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, playerIn.getItemInHand(handIn));
    }

    @Override
    protected boolean canPickupItems(CompoundTag tag){
        return !(tag.contains("items") && tag.getBoolean("items"));
    }

    @Override
    protected boolean canPickupStack(CompoundTag tag, ItemStack stack){
        boolean whitelist = tag.contains("whitelist") && tag.getBoolean("whitelist");
        boolean filterDurability = tag.contains("filterDurability") && tag.getBoolean("filterDurability");
        for(int slot = 0; slot < 9; slot++){
            if(tag.contains("filter" + slot)){
                ItemStack stack1 = ItemStack.of(tag.getCompound("filter" + slot));
                boolean equal = ItemStack.isSame(stack, stack1) && (!filterDurability || ItemStack.tagMatches(stack, stack1));
                if(equal)
                    return whitelist;
            }
        }
        return !whitelist;
    }

    @Override
    protected boolean canPickupXp(CompoundTag tag){
        return !(tag.contains("xp") && tag.getBoolean("xp"));
    }

    @Override
    protected int getRangeItems(CompoundTag tag){
        return tag.contains("itemRange") ? tag.getInt("itemRange") : SMConfig.advancedMagnetRange.get();
    }

    @Override
    protected int getRangeXp(CompoundTag tag){
        return tag.contains("xpRange") ? tag.getInt("xpRange") : SMConfig.advancedMagnetRange.get();
    }

    @Override
    protected Component getTooltip(){
        return TextComponents.translation("simplemagnets.advancedmagnet.info", TextComponents.number(SMConfig.advancedMagnetMaxRange.get()).color(ChatFormatting.GOLD).get()).color(ChatFormatting.GRAY).get();
    }

}
