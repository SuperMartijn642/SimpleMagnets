package com.supermartijn642.simplemagnets.gui;

import com.supermartijn642.core.gui.ItemBaseContainer;
import com.supermartijn642.simplemagnets.AdvancedMagnet;
import com.supermartijn642.simplemagnets.SimpleMagnets;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public class MagnetContainer extends ItemBaseContainer {

    public final int slot;
    private final ItemStackHandler itemHandler = new ItemStackHandler(9) {
        @Nonnull
        @Override
        public ItemStack getStackInSlot(int slot){
            CompoundNBT tag = MagnetContainer.this.getTagOrClose();
            return (tag != null && tag.contains("filter" + slot)) ? ItemStack.read(tag.getCompound("filter" + slot)) : ItemStack.EMPTY;
        }
    };

    public MagnetContainer(int id, PlayerEntity player, int slot){
        super(SimpleMagnets.container, id, player, slot);
        this.slot = slot;

        this.addSlots();
    }

    @Override
    protected void addSlots(PlayerEntity player, ItemStack stack){
        PlayerInventory inventory = player.inventory;

        for(int column = 0; column < 9; column++)
            this.addSlot(new SlotItemHandler(this.itemHandler, column, 8 + column * 18, 80) {
                @Override
                public boolean canTakeStack(PlayerEntity playerIn){
                    return false;
                }
            });

        // player
        for(int row = 0; row < 3; row++){
            for(int column = 0; column < 9; column++){
                this.addSlot(new Slot(inventory, row * 9 + column + 9, 32 + 18 * column, 114 + 18 * row));
            }
        }

        // hot bar
        for(int column = 0; column < 9; column++){
            int index = column;
            this.addSlot(new Slot(inventory, index, 32 + 18 * column, 172) {
                public boolean canTakeStack(PlayerEntity playerIn){
                    return index != MagnetContainer.this.slot;
                }
            });
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn){
        return true;
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player){
        if(clickTypeIn == ClickType.SWAP && dragType == this.slot)
            return ItemStack.EMPTY;
        if(slotId < 9 && slotId >= 0){
            CompoundNBT tag = this.getTagOrClose();
            if(tag != null){
                if(player.inventory.getItemStack().isEmpty())
                    tag.remove("filter" + slotId);
                else{
                    ItemStack stack = player.inventory.getItemStack().copy();
                    stack.setCount(1);
                    tag.put("filter" + slotId, stack.write(new CompoundNBT()));
                }
            }
            return ItemStack.EMPTY;
        }
        return super.slotClick(slotId, dragType, clickTypeIn, player);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int index){
        if(index < 9){
            CompoundNBT tag = this.getTagOrClose();
            if(tag != null){
                if(player.inventory.getItemStack().isEmpty())
                    tag.remove("filter" + index);
                else{
                    ItemStack stack = player.inventory.getItemStack().copy();
                    stack.setCount(1);
                    tag.put("filter" + index, stack.write(new CompoundNBT()));
                }
            }
        }else if(!this.getSlot(index).getStack().isEmpty()){
            boolean contains = false;
            int firstEmpty = -1;
            for(int i = 0; i < 9; i++){
                ItemStack stack = this.itemHandler.getStackInSlot(i);
                if(ItemStack.areItemsEqual(stack, this.getSlot(index).getStack()) && ItemStack.areItemStackTagsEqual(stack, this.getSlot(index).getStack())){
                    contains = true;
                    break;
                }
                if(stack.isEmpty() && firstEmpty == -1)
                    firstEmpty = i;
            }
            if(!contains && firstEmpty != -1){
                CompoundNBT tag = this.getTagOrClose();
                if(tag != null){
                    ItemStack stack = this.getSlot(index).getStack().copy();
                    stack.setCount(1);
                    tag.put("filter" + firstEmpty, stack.write(new CompoundNBT()));
                }
            }
        }
        return ItemStack.EMPTY;
    }

    public CompoundNBT getTagOrClose(){
        ItemStack stack = this.getObjectOrClose();
        if(stack.getItem() instanceof AdvancedMagnet)
            return stack.getOrCreateTag();
        return null;
    }
}
