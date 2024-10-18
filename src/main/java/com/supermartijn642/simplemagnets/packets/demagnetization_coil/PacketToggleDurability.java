package com.supermartijn642.simplemagnets.packets.demagnetization_coil;

import com.supermartijn642.core.network.BlockEntityBasePacket;
import com.supermartijn642.core.network.PacketContext;
import com.supermartijn642.simplemagnets.DemagnetizationCoilBlockEntity;
import net.minecraft.util.math.BlockPos;

/**
 * Created 7/15/2020 by SuperMartijn642
 */
public class PacketToggleDurability extends BlockEntityBasePacket<DemagnetizationCoilBlockEntity> {

    public PacketToggleDurability(BlockPos pos){
        super(pos);
    }

    public PacketToggleDurability(){
    }

    @Override
    protected void handle(DemagnetizationCoilBlockEntity entity, PacketContext context){
        entity.toggleFilterDurability();
    }
}
