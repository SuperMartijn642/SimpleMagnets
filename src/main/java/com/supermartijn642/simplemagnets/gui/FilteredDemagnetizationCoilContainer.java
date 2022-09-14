package com.supermartijn642.simplemagnets.gui;

import com.supermartijn642.simplemagnets.DemagnetizationCoilBlockEntity;
import com.supermartijn642.simplemagnets.SimpleMagnets;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

/**
 * Created 7/15/2020 by SuperMartijn642
 */
public class FilteredDemagnetizationCoilContainer extends BaseDemagnetizationCoilContainer {

    public FilteredDemagnetizationCoilContainer(Player player, BlockPos pos){
        super(SimpleMagnets.filtered_demagnetization_coil_container, player, pos, 224, 206, true);
    }

    @Override
    protected void addSlots(Player player, DemagnetizationCoilBlockEntity entity){
        for(int i = 0; i < 9; i++)
            this.addSlot(new SlotItemHandler(this.itemHandler(), i, 8 + i * 18, 90) {
                @Override
                public boolean mayPickup(Player player){
                    return false;
                }
            });
    }

    @Override
    public void clicked(int slotId, int dragType, ClickType clickType, Player player){
        if(!this.validateObjectOrClose())
            return;

        if(slotId >= 0 && slotId < 9){
            if(this.getCarried().isEmpty())
                this.object.filter.set(slotId, ItemStack.EMPTY);
            else{
                ItemStack stack = this.getCarried().copy();
                stack.setCount(1);
                this.object.filter.set(slotId, stack);
            }
        }
        super.clicked(slotId, dragType, clickType, player);
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index){
        if(!this.validateObjectOrClose())
            return ItemStack.EMPTY;

        if(index >= 0 && index < 9){
            if(this.getCarried().isEmpty())
                this.object.filter.set(index, ItemStack.EMPTY);
            else{
                ItemStack stack = this.getCarried().copy();
                stack.setCount(1);
                this.object.filter.set(index, stack);
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
