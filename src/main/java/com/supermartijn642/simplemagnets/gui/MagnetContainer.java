package com.supermartijn642.simplemagnets.gui;

import com.supermartijn642.core.gui.ItemBaseContainer;
import com.supermartijn642.simplemagnets.AdvancedMagnet;
import com.supermartijn642.simplemagnets.MagnetItem;
import com.supermartijn642.simplemagnets.SimpleMagnets;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public class MagnetContainer extends ItemBaseContainer {

    public final int slot;
    private final Function<Integer,ItemStack> itemHandler = slot -> {
        AdvancedMagnet.Settings settings = MagnetContainer.this.object.get(AdvancedMagnet.SETTINGS);
        return settings != null && settings.itemFilter()[slot] != null ? settings.itemFilter()[slot] : ItemStack.EMPTY;
    };

    public MagnetContainer(Player player, int slot){
        super(SimpleMagnets.magnet_container, player, slot, stack -> stack.getItem() instanceof MagnetItem);
        this.slot = slot;

        this.addSlots();
    }

    @Override
    protected void addSlots(Player player, ItemStack stack){
        Inventory inventory = player.getInventory();

        for(int column = 0; column < 9; column++)
            this.addSlot(new DummySlot(column, 8 + column * 18, 80) {
                @Override
                public ItemStack getItem(){
                    return MagnetContainer.this.itemHandler.apply(this.index);
                }

                @Override
                public boolean mayPickup(Player player){
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
                public boolean mayPickup(Player player){
                    return this.index != MagnetContainer.this.slot;
                }
            });
        }
    }

    @Override
    public void clicked(int slotId, int dragType, ClickType clickType, Player player){
        if(!this.validateObjectOrClose())
            return;
        if(clickType == ClickType.SWAP && dragType == this.slot)
            return;

        if(slotId < 9 && slotId >= 0){
            AdvancedMagnet.Settings settings = this.object.get(AdvancedMagnet.SETTINGS);
            if(this.getCarried().isEmpty()){
                if(settings != null)
                    this.object.set(AdvancedMagnet.SETTINGS, settings.itemFilter(slotId, null));
            }else{
                ItemStack stack = this.getCarried().copy();
                stack.setCount(1);
                if(settings == null)
                    settings = AdvancedMagnet.Settings.defaultSettings();
                this.object.set(AdvancedMagnet.SETTINGS, settings.itemFilter(slotId, stack));
            }
            return;
        }
        super.clicked(slotId, dragType, clickType, player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index){
        if(!this.validateObjectOrClose())
            return ItemStack.EMPTY;

        if(index < 9){
            AdvancedMagnet.Settings settings = this.object.get(AdvancedMagnet.SETTINGS);
            if(this.getCarried().isEmpty()){
                if(settings != null)
                    this.object.set(AdvancedMagnet.SETTINGS, settings.itemFilter(index, null));
            }else{
                ItemStack stack = this.getCarried().copy();
                stack.setCount(1);
                if(settings == null)
                    settings = AdvancedMagnet.Settings.defaultSettings();
                this.object.set(AdvancedMagnet.SETTINGS, settings.itemFilter(index, stack));
            }
        }else if(!this.getSlot(index).getItem().isEmpty()){
            boolean contains = false;
            int firstEmpty = -1;
            for(int i = 0; i < 9; i++){
                ItemStack stack = this.itemHandler.apply(i);
                if(ItemStack.isSameItemSameComponents(stack, this.getSlot(index).getItem())){
                    contains = true;
                    break;
                }
                if(stack.isEmpty() && firstEmpty == -1)
                    firstEmpty = i;
            }
            if(!contains && firstEmpty != -1){
                ItemStack stack = this.getSlot(index).getItem().copy();
                stack.setCount(1);
                AdvancedMagnet.Settings settings = this.object.get(AdvancedMagnet.SETTINGS);
                if(settings == null)
                    settings = AdvancedMagnet.Settings.defaultSettings();
                this.object.set(AdvancedMagnet.SETTINGS, settings.itemFilter(firstEmpty, stack));
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack getObject(ItemStack oldObject){
        return super.getObject(oldObject);
    }

    @Override
    public boolean validateObject(ItemStack object){
        return super.validateObject(object);
    }
}
