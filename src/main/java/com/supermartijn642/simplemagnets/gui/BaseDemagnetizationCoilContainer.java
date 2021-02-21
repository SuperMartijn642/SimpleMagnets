package com.supermartijn642.simplemagnets.gui;

import com.supermartijn642.simplemagnets.DemagnetizationCoilTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public abstract class BaseDemagnetizationCoilContainer extends Container {

    public final EntityPlayer player;
    public final World world;
    public final BlockPos pos;
    public final int width, height;

    public BaseDemagnetizationCoilContainer(EntityPlayer player, BlockPos pos, int width, int height, boolean hasSlots){
        this.player = player;
        this.world = player.world;
        this.pos = pos;
        this.width = width;
        this.height = height;

        DemagnetizationCoilTile tile = this.getTileOrClose();
        if(tile == null)
            return;

        this.addSlots(tile);
        if(hasSlots)
            this.addPlayerSlots();
    }

    protected abstract void addSlots(DemagnetizationCoilTile tile);

    private void addPlayerSlots(){
        // player
        for(int row = 0; row < 3; row++){
            for(int column = 0; column < 9; column++){
                this.addSlotToContainer(new Slot(this.player.inventory, row * 9 + column + 9, 32 + 18 * column, this.height - 82 + 18 * row));
            }
        }

        // hot bar
        for(int column = 0; column < 9; column++)
            this.addSlotToContainer(new Slot(this.player.inventory, column, 32 + 18 * column, this.height - 24));
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn){
        return true;
    }

    public DemagnetizationCoilTile getTileOrClose(){
        if(this.world != null && this.pos != null){
            TileEntity tile = this.world.getTileEntity(this.pos);
            if(tile instanceof DemagnetizationCoilTile)
                return (DemagnetizationCoilTile)tile;
        }
        this.player.closeScreen();
        return null;
    }
}
