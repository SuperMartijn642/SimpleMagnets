package com.supermartijn642.simplemagnets;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.simplemagnets.gui.MagnetContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public class AdvancedMagnet extends MagnetItem {

    public AdvancedMagnet(){
        super("advancedmagnet");
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn){
        if(!playerIn.isSneaking())
            return super.use(worldIn, playerIn, handIn);
        int slot = handIn == Hand.MAIN_HAND ? playerIn.inventory.selected : 40;
        if(!worldIn.isClientSide){
            NetworkHooks.openGui((ServerPlayerEntity)playerIn, new INamedContainerProvider() {
                @Override
                public ITextComponent getDisplayName(){
                    return playerIn.getItemInHand(handIn).hasCustomHoverName() ? playerIn.getItemInHand(handIn).getHoverName() : TextComponents.translation("gui.advancedmagnet.title").get();
                }

                @Nullable
                @Override
                public Container createMenu(int windowId, PlayerInventory inventory, PlayerEntity player){
                    return new MagnetContainer(windowId, player, slot);
                }
            }, data -> data.writeInt(slot));
        }
        return new ActionResult<>(ActionResultType.SUCCESS, playerIn.getItemInHand(handIn));
    }

    @Override
    protected boolean canPickupItems(CompoundNBT tag){
        return !(tag.contains("items") && tag.getBoolean("items"));
    }

    @Override
    protected boolean canPickupStack(CompoundNBT tag, ItemStack stack){
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
    protected boolean canPickupXp(CompoundNBT tag){
        return !(tag.contains("xp") && tag.getBoolean("xp"));
    }

    @Override
    protected int getRangeItems(CompoundNBT tag){
        return tag.contains("itemRange") ? tag.getInt("itemRange") : SMConfig.advancedMagnetRange.get();
    }

    @Override
    protected int getRangeXp(CompoundNBT tag){
        return tag.contains("xpRange") ? tag.getInt("xpRange") : SMConfig.advancedMagnetRange.get();
    }

    @Override
    protected ITextComponent getTooltip(){
        return TextComponents.translation("simplemagnets.advancedmagnet.info", TextComponents.number(SMConfig.advancedMagnetMaxRange.get()).color(TextFormatting.GOLD).get()).color(TextFormatting.GRAY).get();
    }

}
