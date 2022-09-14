package com.supermartijn642.simplemagnets.gui;

import com.supermartijn642.simplemagnets.DemagnetizationCoilBlockEntity;
import com.supermartijn642.simplemagnets.SimpleMagnets;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

/**
 * Created 7/15/2020 by SuperMartijn642
 */
public class FilteredDemagnetizationCoilContainer extends BaseDemagnetizationCoilContainer {

    public FilteredDemagnetizationCoilContainer(EntityPlayer player, BlockPos pos){
        super(SimpleMagnets.filtered_demagnetization_coil_container, player, pos, 224, 206, true);
    }

    @Override
    protected void addSlots(EntityPlayer player, DemagnetizationCoilBlockEntity tile){
        for(int i = 0; i < 9; i++)
            this.addSlot(new SlotItemHandler(this.itemHandler(), i, 8 + i * 18, 90) {
                @Override
                public boolean canTakeStack(EntityPlayer playerIn){
                    return false;
                }
            });
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player){
        if(!this.validateObjectOrClose())
            return ItemStack.EMPTY;

        if(slotId >= 0 && slotId < 9){
            if(player.inventory.getItemStack().isEmpty())
                this.object.filter.set(slotId, ItemStack.EMPTY);
            else{
                ItemStack stack = player.inventory.getItemStack().copy();
                stack.setCount(1);
                this.object.filter.set(slotId, stack);
            }
            return ItemStack.EMPTY;
        }
        return super.slotClick(slotId, dragType, clickTypeIn, player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index){
        if(!this.validateObjectOrClose())
            return ItemStack.EMPTY;

        if(index >= 0 && index < 9){
            if(player.inventory.getItemStack().isEmpty())
                this.object.filter.set(index, ItemStack.EMPTY);
            else{
                ItemStack stack = player.inventory.getItemStack().copy();
                stack.setCount(1);
                this.object.filter.set(index, stack);
            }
        }else if(!this.getSlot(index).getStack().isEmpty()){
            boolean contains = false;
            int firstEmpty = -1;
            for(int i = 0; i < 9; i++){
                ItemStack stack = this.itemHandler().getStackInSlot(i);
                if(ItemStack.areItemsEqual(stack, this.getSlot(index).getStack()) && ItemStack.areItemStackTagsEqual(stack, this.getSlot(index).getStack())){
                    contains = true;
                    break;
                }
                if(stack.isEmpty() && firstEmpty == -1)
                    firstEmpty = i;
            }
            if(!contains && firstEmpty != -1){
                ItemStack stack = this.getSlot(index).getStack().copy();
                stack.setCount(1);
                this.object.filter.set(firstEmpty, stack);
            }
        }
        return ItemStack.EMPTY;
    }

    private ItemStackHandler itemHandler(){
        return new ItemStackHandler(9) {
            @Nonnull
            @Override
            public ItemStack getStackInSlot(int slot){
                return FilteredDemagnetizationCoilContainer.this.validateObjectOrClose() ? FilteredDemagnetizationCoilContainer.this.object.filter.get(slot) : ItemStack.EMPTY;
            }
        };
    }
}
