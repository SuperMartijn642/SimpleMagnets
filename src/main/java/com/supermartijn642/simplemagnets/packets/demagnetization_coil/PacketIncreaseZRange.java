package com.supermartijn642.simplemagnets.packets.demagnetization_coil;

import com.supermartijn642.simplemagnets.DemagnetizationCoilTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created 7/15/2020 by SuperMartijn642
 */
public class PacketIncreaseZRange extends DemagnetizationCoilPacket {
    public PacketIncreaseZRange(BlockPos pos){
        super(pos);
    }

    public PacketIncreaseZRange(PacketBuffer buffer){
        super(buffer);
    }

    public static PacketIncreaseZRange decode(PacketBuffer buffer){
        return new PacketIncreaseZRange(buffer);
    }

    @Override
    protected void handle(PlayerEntity player, World world, DemagnetizationCoilTile tile){
        tile.setRangeZ(tile.rangeZ + 1);
    }
}
