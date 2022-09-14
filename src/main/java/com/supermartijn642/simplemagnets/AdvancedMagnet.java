package com.supermartijn642.simplemagnets;

import com.supermartijn642.core.CommonUtils;
import com.supermartijn642.core.TextComponents;
import com.supermartijn642.simplemagnets.gui.MagnetContainer;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public class AdvancedMagnet extends MagnetItem {

    @Override
    public ItemUseResult interact(ItemStack stack, Player player, InteractionHand hand, Level level){
        if(!player.isShiftKeyDown())
            return super.interact(stack, player, hand, level);

        int slot = hand == InteractionHand.MAIN_HAND ? player.getInventory().selected : 40;
        if(!level.isClientSide)
            CommonUtils.openContainer(new MagnetContainer(player, slot));
        return ItemUseResult.success(stack);
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
