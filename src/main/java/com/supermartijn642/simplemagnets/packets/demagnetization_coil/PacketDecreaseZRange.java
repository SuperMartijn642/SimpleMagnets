package com.supermartijn642.simplemagnets.packets.demagnetization_coil;

import com.supermartijn642.simplemagnets.DemagnetizationCoilTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created 7/15/2020 by SuperMartijn642
 */
public class PacketDecreaseZRange extends DemagnetizationCoilPacket {
    public PacketDecreaseZRange(BlockPos pos){
        super(pos);
    }

    public PacketDecreaseZRange(PacketBuffer buffer){
        super(buffer);
    }

    public static PacketDecreaseZRange decode(PacketBuffer buffer){
        return new PacketDecreaseZRange(buffer);
    }

    @Override
    protected void handle(PlayerEntity player, World world, DemagnetizationCoilTile tile){
        tile.setRangeZ(tile.rangeZ - 1);
    }
}
