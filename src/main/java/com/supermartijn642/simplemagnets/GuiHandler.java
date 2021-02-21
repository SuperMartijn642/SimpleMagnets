package com.supermartijn642.simplemagnets;

import com.supermartijn642.simplemagnets.gui.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

/**
 * Created 7/9/2020 by SuperMartijn642
 */
public class GuiHandler implements IGuiHandler {
    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
        if(ID != 99)
            return new MagnetContainer(player, ID);
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity tile = world.getTileEntity(pos);
        return
            tile instanceof DemagnetizationCoilTile ?
                ((DemagnetizationCoilTile)tile).hasFilter() ?
                    new FilteredDemagnetizationCoilContainer(player, pos) :
                    new DemagnetizationCoilContainer(player, pos) :
                null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
        if(ID != 99)
            return new MagnetContainerScreen(new MagnetContainer(player, ID), player.inventory);
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity tile = world.getTileEntity(pos);
        return
            tile instanceof DemagnetizationCoilTile ?
                ((DemagnetizationCoilTile)tile).hasFilter() ?
                    new FilteredDemagnetizationCoilContainerScreen(new FilteredDemagnetizationCoilContainer(player, pos)) :
                    new DemagnetizationCoilContainerScreen(new DemagnetizationCoilContainer(player, pos)) :
                null;
    }
}
