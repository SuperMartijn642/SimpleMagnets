package com.supermartijn642.simplemagnets.gui;

import com.supermartijn642.core.gui.BaseContainerType;
import com.supermartijn642.core.gui.BlockEntityBaseContainer;
import com.supermartijn642.simplemagnets.DemagnetizationCoilBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public abstract class BaseDemagnetizationCoilContainer extends BlockEntityBaseContainer<DemagnetizationCoilBlockEntity> {

    public final int width, height;

    public BaseDemagnetizationCoilContainer(BaseContainerType<?> type, Player player, BlockPos pos, int width, int height, boolean hasSlots){
        super(type, player, pos);
        this.width = width;
        this.height = height;

        this.addSlots();
        if(hasSlots)
            this.addPlayerSlots(32, height - 82);
    }

    @Override
    public DemagnetizationCoilBlockEntity getObject(DemagnetizationCoilBlockEntity oldObject){
        return super.getObject(oldObject);
    }

    public BlockPos getBlockEntityPos(){
        return this.blockEntityPos;
    }
}
