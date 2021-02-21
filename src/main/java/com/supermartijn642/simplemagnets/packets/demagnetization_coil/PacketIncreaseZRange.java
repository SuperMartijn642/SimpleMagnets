package com.supermartijn642.simplemagnets.packets.demagnetization_coil;

import com.supermartijn642.simplemagnets.DemagnetizationCoilTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created 7/15/2020 by SuperMartijn642
 */
public class PacketIncreaseZRange extends DemagnetizationCoilPacket<PacketIncreaseZRange> {
    public PacketIncreaseZRange(BlockPos pos){
        super(pos);
    }

    public PacketIncreaseZRange(){
    }

    @Override
    protected void handle(EntityPlayer player, World world, DemagnetizationCoilTile tile){
        tile.setRangeZ(tile.rangeZ + 1);
    }
}
