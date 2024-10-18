package com.supermartijn642.simplemagnets.packets.demagnetization_coil;

import com.supermartijn642.core.network.BlockEntityBasePacket;
import com.supermartijn642.core.network.PacketContext;
import com.supermartijn642.simplemagnets.DemagnetizationCoilBlockEntity;
import net.minecraft.core.BlockPos;

/**
 * Created 7/15/2020 by SuperMartijn642
 */
public class PacketDecreaseXRange extends BlockEntityBasePacket<DemagnetizationCoilBlockEntity> {

    public PacketDecreaseXRange(BlockPos pos){
        super(pos);
    }

    public PacketDecreaseXRange(){
    }

    @Override
    protected void handle(DemagnetizationCoilBlockEntity entity, PacketContext context){
        entity.setRangeX(entity.getRangeX() - 1);
    }
}
