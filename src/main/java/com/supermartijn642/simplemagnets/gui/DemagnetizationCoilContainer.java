package com.supermartijn642.simplemagnets.gui;

import com.supermartijn642.simplemagnets.DemagnetizationCoilBlockEntity;
import com.supermartijn642.simplemagnets.SimpleMagnets;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

/**
 * Created 7/15/2020 by SuperMartijn642
 */
public class DemagnetizationCoilContainer extends BaseDemagnetizationCoilContainer {

    public DemagnetizationCoilContainer(PlayerEntity player, BlockPos pos){
        super(SimpleMagnets.demagnetization_coil_container, player, pos, 170, 82, false);
    }

    @Override
    protected void addSlots(PlayerEntity playerEntity, DemagnetizationCoilBlockEntity demagnetizationCoilBlockEntity){
    }
}
