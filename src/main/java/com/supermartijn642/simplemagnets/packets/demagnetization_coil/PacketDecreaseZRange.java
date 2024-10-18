package com.supermartijn642.simplemagnets.packets.demagnetization_coil;

import com.supermartijn642.core.network.BlockEntityBasePacket;
import com.supermartijn642.core.network.PacketContext;
import com.supermartijn642.simplemagnets.DemagnetizationCoilBlockEntity;
import net.minecraft.util.math.BlockPos;

/**
 * Created 7/15/2020 by SuperMartijn642
 */
public class PacketDecreaseZRange extends BlockEntityBasePacket<DemagnetizationCoilBlockEntity> {

    public PacketDecreaseZRange(BlockPos pos){
        super(pos);
    }

    public PacketDecreaseZRange(){
    }

    @Override
    protected void handle(DemagnetizationCoilBlockEntity entity, PacketContext context){
        entity.setRangeZ(entity.getRangeZ() - 1);
    }
}
