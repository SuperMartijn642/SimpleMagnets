package com.supermartijn642.simplemagnets.packets.demagnetization_coil;

import com.supermartijn642.core.network.BlockEntityBasePacket;
import com.supermartijn642.core.network.PacketContext;
import com.supermartijn642.simplemagnets.DemagnetizationCoilBlockEntity;
import net.minecraft.core.BlockPos;

/**
 * Created 7/15/2020 by SuperMartijn642
 */
public class PacketDecreaseYRange extends BlockEntityBasePacket<DemagnetizationCoilBlockEntity> {

    public PacketDecreaseYRange(BlockPos pos){
        super(pos);
    }

    public PacketDecreaseYRange(){
    }

    @Override
    protected void handle(DemagnetizationCoilBlockEntity entity, PacketContext context){
        entity.setRangeY(entity.getRangeY() - 1);
    }
}
