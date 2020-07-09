package com.supermartijn642.simplemagnets.gui;

import com.supermartijn642.simplemagnets.AdvancedMagnet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public class MagnetContainer extends Container {

    public EntityPlayer player;
    public final int slot;
    private final ItemStackHandler itemHandler = new ItemStackHandler(9) {
        @Nonnull
        @Override
        public ItemStack getStackInSlot(int slot){
            NBTTagCompound tag = MagnetContainer.this.getTagOrClose();
            return (tag != null && tag.hasKey("filter" + slot)) ? new ItemStack(tag.getCompoundTag("filter" + slot)) : ItemStack.EMPTY;
        }
    };

    public MagnetContainer(EntityPlayer player, int slot){
        this.player = player;
        this.slot = slot;

        this.addSlots(player.inventory);
    }

    private void addSlots(InventoryPlayer inventory){
        for(int column = 0; column < 9; column++)
            this.addSlotToContainer(new SlotItemHandler(this.itemHandler, column, 8 + column * 18, 80) {
                public boolean canTakeStack(EntityPlayer playerIn){
                    return false;
                }
            });

        // player
        for(int row = 0; row < 3; row++){
            for(int column = 0; column < 9; column++){
                this.addSlotToContainer(new Slot(inventory, row * 9 + column + 9, 21 + 18 * column, 114 + 18 * row));
            }
        }

        // hot bar
        for(int column = 0; column < 9; column++){
            int index = column;
            this.addSlotToContainer(new Slot(inventory, index, 21 + 18 * column, 172) {
                public boolean canTakeStack(EntityPlayer playerIn){
                    return index != MagnetContainer.this.slot;
                }
            });
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn){
        return true;
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player){
        if(clickTypeIn == ClickType.SWAP && dragType == this.slot)
            return ItemStack.EMPTY;
        if(slotId < 9 && slotId >= 0){
            NBTTagCompound tag = this.getTagOrClose();
            if(tag != null){
                if(player.inventory.getItemStack().isEmpty())
                    tag.removeTag("filter" + slotId);
                else{
                    ItemStack stack = player.inventory.getItemStack().copy();
                    stack.setCount(1);
                    tag.setTag("filter" + slotId, stack.writeToNBT(new NBTTagCompound()));
                }
            }
            return ItemStack.EMPTY;
        }
        return super.slotClick(slotId, dragType, clickTypeIn, player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index){
        if(index < 9){
            NBTTagCompound tag = this.getTagOrClose();
            if(tag != null){
                if(player.inventory.getItemStack().isEmpty())
                    tag.removeTag("filter" + index);
                else{
                    ItemStack stack = player.inventory.getItemStack().copy();
                    stack.setCount(1);
                    tag.setTag("filter" + index, stack.writeToNBT(new NBTTagCompound()));
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
                NBTTagCompound tag = this.getTagOrClose();
                if(tag != null){
                    ItemStack stack = this.getSlot(index).getStack().copy();
                    stack.setCount(1);
                    tag.setTag("filter" + firstEmpty, stack.writeToNBT(new NBTTagCompound()));
                }
            }
        }
        return ItemStack.EMPTY;
    }

    public NBTTagCompound getTagOrClose(){
        ItemStack stack = this.player.inventory.getStackInSlot(this.slot);
        if(stack.getItem() instanceof AdvancedMagnet){
            NBTTagCompound tag = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
            stack.setTagCompound(tag);
            return tag;
        }
        this.player.closeScreen();
        return null;
    }
}
