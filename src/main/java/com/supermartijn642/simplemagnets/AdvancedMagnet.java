package com.supermartijn642.simplemagnets;

import com.supermartijn642.core.CommonUtils;
import com.supermartijn642.core.TextComponents;
import com.supermartijn642.simplemagnets.gui.MagnetContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public class AdvancedMagnet extends MagnetItem {

    @Override
    public ItemUseResult interact(ItemStack stack, PlayerEntity player, Hand hand, World level){
        if(!player.isSneaking())
            return super.interact(stack, player, hand, level);

        int slot = hand == Hand.MAIN_HAND ? player.inventory.selected : 40;
        if(!level.isClientSide)
            CommonUtils.openContainer(new MagnetContainer(player, slot));
        return ItemUseResult.success(stack);
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
