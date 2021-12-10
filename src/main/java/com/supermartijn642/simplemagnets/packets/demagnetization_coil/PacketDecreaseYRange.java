package com.supermartijn642.simplemagnets.packets.demagnetization_coil;

import com.supermartijn642.core.network.PacketContext;
import com.supermartijn642.core.network.TileEntityBasePacket;
import com.supermartijn642.simplemagnets.DemagnetizationCoilTile;
import net.minecraft.core.BlockPos;

/**
 * Created 7/15/2020 by SuperMartijn642
 */
public class PacketDecreaseYRange extends TileEntityBasePacket<DemagnetizationCoilTile> {

    public PacketDecreaseYRange(BlockPos pos){
        super(pos);
    }

    public PacketDecreaseYRange(){
    }

    @Override
    protected void handle(DemagnetizationCoilTile tile, PacketContext context){
        tile.setRangeY(tile.rangeY - 1);
    }
}
