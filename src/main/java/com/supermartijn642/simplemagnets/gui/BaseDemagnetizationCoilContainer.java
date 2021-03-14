package com.supermartijn642.simplemagnets.gui;

import com.supermartijn642.core.gui.TileEntityBaseContainer;
import com.supermartijn642.simplemagnets.DemagnetizationCoilTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public abstract class BaseDemagnetizationCoilContainer extends TileEntityBaseContainer<DemagnetizationCoilTile> {

    public final int width, height;

    public BaseDemagnetizationCoilContainer(EntityPlayer player, BlockPos pos, int width, int height, boolean hasSlots){
        super(player, pos);
        this.width = width;
        this.height = height;

        this.addSlots();
        if(hasSlots)
            this.addPlayerSlots(32, height - 82);
    }

    @Override
    protected DemagnetizationCoilTile getObjectOrClose(){
        return super.getObjectOrClose();
    }

    public BlockPos getTilePos(){
        return this.tilePos;
    }
}
