package com.supermartijn642.simplemagnets.gui;

import com.supermartijn642.core.gui.TileEntityBaseContainer;
import com.supermartijn642.simplemagnets.DemagnetizationCoilTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public abstract class BaseDemagnetizationCoilContainer extends TileEntityBaseContainer<DemagnetizationCoilTile> {

    public final int width, height;

    public BaseDemagnetizationCoilContainer(ContainerType<?> type, int id, PlayerEntity player, BlockPos pos, int width, int height, boolean hasSlots){
        super(type, id, player, pos);
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
