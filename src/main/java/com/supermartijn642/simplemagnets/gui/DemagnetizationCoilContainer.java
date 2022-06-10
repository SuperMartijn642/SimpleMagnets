package com.supermartijn642.simplemagnets.gui;

import com.supermartijn642.simplemagnets.DemagnetizationCoilTile;
import com.supermartijn642.simplemagnets.SimpleMagnets;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Created 7/15/2020 by SuperMartijn642
 */
public class DemagnetizationCoilContainer extends BaseDemagnetizationCoilContainer {

    public DemagnetizationCoilContainer(int id, Player player, BlockPos pos){
        super(SimpleMagnets.demagnetization_coil_container, id, player, pos, 170, 82, false);
    }

    @Override
    protected void addSlots(Player playerEntity, DemagnetizationCoilTile demagnetizationCoilTile){
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slot){
        return null; // TODO implement this in all branches
    }
}
