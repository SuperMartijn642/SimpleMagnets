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
            return (tag != null && tag.contains("filter" + slot)) ? ItemStack.of(tag.getCompound("filter" + slot)) : ItemStack.EMPTY;
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
                public boolean mayPickup(PlayerEntity playerIn){
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
                public boolean mayPickup(PlayerEntity playerIn){
                    return index != MagnetContainer.this.slot;
                }
            });
        }
    }

    @Override
    public boolean stillValid(PlayerEntity playerIn){
        return true;
    }

    @Override
    public ItemStack clicked(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player){
        if(clickTypeIn == ClickType.SWAP && dragType == this.slot)
            return ItemStack.EMPTY;
        if(slotId < 9 && slotId >= 0){
            CompoundNBT tag = this.getTagOrClose();
            if(tag != null){
                if(player.inventory.getCarried().isEmpty())
                    tag.remove("filter" + slotId);
                else{
                    ItemStack stack = player.inventory.getCarried().copy();
                    stack.setCount(1);
                    tag.put("filter" + slotId, stack.save(new CompoundNBT()));
                }
            }
            return ItemStack.EMPTY;
        }
        return super.clicked(slotId, dragType, clickTypeIn, player);
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index){
        if(index < 9){
            CompoundNBT tag = this.getTagOrClose();
            if(tag != null){
                if(player.inventory.getCarried().isEmpty())
                    tag.remove("filter" + index);
                else{
                    ItemStack stack = player.inventory.getCarried().copy();
                    stack.setCount(1);
                    tag.put("filter" + index, stack.save(new CompoundNBT()));
                }
            }
        }else if(!this.getSlot(index).getItem().isEmpty()){
            boolean contains = false;
            int firstEmpty = -1;
            for(int i = 0; i < 9; i++){
                ItemStack stack = this.itemHandler.getStackInSlot(i);
                if(ItemStack.isSame(stack, this.getSlot(index).getItem()) && ItemStack.tagMatches(stack, this.getSlot(index).getItem())){
                    contains = true;
                    break;
                }
                if(stack.isEmpty() && firstEmpty == -1)
                    firstEmpty = i;
            }
            if(!contains && firstEmpty != -1){
                CompoundNBT tag = this.getTagOrClose();
                if(tag != null){
                    ItemStack stack = this.getSlot(index).getItem().copy();
                    stack.setCount(1);
                    tag.put("filter" + firstEmpty, stack.save(new CompoundNBT()));
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
