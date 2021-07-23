package com.supermartijn642.simplemagnets.gui;

import com.supermartijn642.core.gui.ItemBaseContainer;
import com.supermartijn642.simplemagnets.AdvancedMagnet;
import com.supermartijn642.simplemagnets.SimpleMagnets;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
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
            CompoundTag tag = MagnetContainer.this.getTagOrClose();
            return (tag != null && tag.contains("filter" + slot)) ? ItemStack.of(tag.getCompound("filter" + slot)) : ItemStack.EMPTY;
        }
    };

    public MagnetContainer(int id, Player player, int slot){
        super(SimpleMagnets.container, id, player, slot);
        this.slot = slot;

        this.addSlots();
    }

    @Override
    protected void addSlots(Player player, ItemStack stack){
        Inventory inventory = player.getInventory();

        for(int column = 0; column < 9; column++)
            this.addSlot(new SlotItemHandler(this.itemHandler, column, 8 + column * 18, 80) {
                @Override
                public boolean mayPickup(Player playerIn){
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
                public boolean mayPickup(Player playerIn){
                    return index != MagnetContainer.this.slot;
                }
            });
        }
    }

    @Override
    public boolean stillValid(Player playerIn){
        return true;
    }

    @Override
    public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player){
        if(clickTypeIn == ClickType.SWAP && dragType == this.slot)
            return;
        if(slotId < 9 && slotId >= 0){
            CompoundTag tag = this.getTagOrClose();
            if(tag != null){
                if(this.getCarried().isEmpty())
                    tag.remove("filter" + slotId);
                else{
                    ItemStack stack = this.getCarried().copy();
                    stack.setCount(1);
                    tag.put("filter" + slotId, stack.save(new CompoundTag()));
                }
            }
            return;
        }
        super.clicked(slotId, dragType, clickTypeIn, player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index){
        if(index < 9){
            CompoundTag tag = this.getTagOrClose();
            if(tag != null){
                if(this.getCarried().isEmpty())
                    tag.remove("filter" + index);
                else{
                    ItemStack stack = this.getCarried().copy();
                    stack.setCount(1);
                    tag.put("filter" + index, stack.save(new CompoundTag()));
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
                CompoundTag tag = this.getTagOrClose();
                if(tag != null){
                    ItemStack stack = this.getSlot(index).getItem().copy();
                    stack.setCount(1);
                    tag.put("filter" + firstEmpty, stack.save(new CompoundTag()));
                }
            }
        }
        return ItemStack.EMPTY;
    }

    public CompoundTag getTagOrClose(){
        ItemStack stack = this.getObjectOrClose();
        if(stack.getItem() instanceof AdvancedMagnet)
            return stack.getOrCreateTag();
        return null;
    }
}
