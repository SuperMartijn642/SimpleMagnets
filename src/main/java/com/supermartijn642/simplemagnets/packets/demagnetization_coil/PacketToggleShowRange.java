package com.supermartijn642.simplemagnets.packets.demagnetization_coil;

import com.supermartijn642.core.network.BlockEntityBasePacket;
import com.supermartijn642.core.network.PacketContext;
import com.supermartijn642.simplemagnets.DemagnetizationCoilBlockEntity;
import net.minecraft.util.math.BlockPos;

/**
 * Created 11/10/2023 by SuperMartijn642
 */
public class PacketToggleShowRange extends BlockEntityBasePacket<DemagnetizationCoilBlockEntity> {

    public PacketToggleShowRange(BlockPos pos){
        super(pos);
    }

    public PacketToggleShowRange(){
    }

    @Override
    protected void handle(DemagnetizationCoilBlockEntity entity, PacketContext context){
        entity.showRange = !entity.showRange;
        entity.dataChanged();
    }
}
