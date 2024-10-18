package com.supermartijn642.simplemagnets.packets.demagnetization_coil;

import com.supermartijn642.core.network.BlockEntityBasePacket;
import com.supermartijn642.core.network.PacketContext;
import com.supermartijn642.simplemagnets.DemagnetizationCoilBlockEntity;
import net.minecraft.core.BlockPos;

/**
 * Created 7/15/2020 by SuperMartijn642
 */
public class PacketIncreaseYRange extends BlockEntityBasePacket<DemagnetizationCoilBlockEntity> {

    public PacketIncreaseYRange(BlockPos pos){
        super(pos);
    }

    public PacketIncreaseYRange(){
    }

    @Override
    protected void handle(DemagnetizationCoilBlockEntity entity, PacketContext context){
        entity.setRangeY(entity.getRangeY() + 1);
    }
}
