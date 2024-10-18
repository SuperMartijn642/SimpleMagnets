package com.supermartijn642.simplemagnets.gui;

import com.supermartijn642.simplemagnets.DemagnetizationCoilBlockEntity;
import com.supermartijn642.simplemagnets.SimpleMagnets;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

/**
 * Created 7/15/2020 by SuperMartijn642
 */
public class FilteredDemagnetizationCoilContainer extends BaseDemagnetizationCoilContainer {

    public FilteredDemagnetizationCoilContainer(PlayerEntity player, BlockPos pos){
        super(SimpleMagnets.filtered_demagnetization_coil_container, player, pos, 224, 206, true);
    }

    @Override
    protected void addSlots(PlayerEntity player, DemagnetizationCoilBlockEntity entity){
        for(int i = 0; i < 9; i++)
            this.addSlot(new SlotItemHandler(this.itemHandler(), i, 8 + i * 18, 90) {
                @Override
                public boolean mayPickup(PlayerEntity player){
                    return false;
                }
            });
    }

    @Override
    public ItemStack clicked(int slotId, int dragType, ClickType clickType, PlayerEntity player){
        if(!this.validateObjectOrClose())
            return ItemStack.EMPTY;

        if(slotId >= 0 && slotId < 9){
            if(player.inventory.getCarried().isEmpty())
                this.object.updateFilter(slotId, ItemStack.EMPTY);
            else{
                ItemStack stack = player.inventory.getCarried().copy();
                stack.setCount(1);
                this.object.updateFilter(slotId, stack);
            }
            return ItemStack.EMPTY;
        }
        return super.clicked(slotId, dragType, clickType, player);
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index){
        if(!this.validateObjectOrClose())
            return ItemStack.EMPTY;

        if(index >= 0 && index < 9){
            if(player.inventory.getCarried().isEmpty())
                this.object.updateFilter(index, ItemStack.EMPTY);
            else{
                ItemStack stack = player.inventory.getCarried().copy();
                stack.setCount(1);
                this.object.updateFilter(index, stack);
            }
        }else if(!this.getSlot(index).getItem().isEmpty()){
            boolean contains = false;
            int firstEmpty = -1;
            for(int i = 0; i < 9; i++){
                ItemStack stack = this.itemHandler().getStackInSlot(i);
                if(ItemStack.isSame(stack, this.getSlot(index).getItem()) && ItemStack.tagMatches(stack, this.getSlot(index).getItem())){
                    contains = true;
                    break;
                }
                if(stack.isEmpty() && firstEmpty == -1)
                    firstEmpty = i;
            }
            if(!contains && firstEmpty != -1){
                ItemStack stack = this.getSlot(index).getItem().copy();
                stack.setCount(1);
                this.object.updateFilter(firstEmpty, stack);
            }
        }
        return ItemStack.EMPTY;
    }

    private ItemStackHandler itemHandler(){
        return new ItemStackHandler(9) {
            @Nonnull
            @Override
            public ItemStack getStackInSlot(int slot){
                return FilteredDemagnetizationCoilContainer.this.validateObjectOrClose() ? FilteredDemagnetizationCoilContainer.this.object.getFilter().get(slot) : ItemStack.EMPTY;
            }
        };
    }
}
