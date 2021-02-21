package com.supermartijn642.simplemagnets.packets.demagnetization_coil;

import com.supermartijn642.simplemagnets.DemagnetizationCoilTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created 7/15/2020 by SuperMartijn642
 */
public class PacketIncreaseYRange extends DemagnetizationCoilPacket {
    public PacketIncreaseYRange(BlockPos pos){
        super(pos);
    }

    public PacketIncreaseYRange(PacketBuffer buffer){
        super(buffer);
    }

    public static PacketIncreaseYRange decode(PacketBuffer buffer){
        return new PacketIncreaseYRange(buffer);
    }

    @Override
    protected void handle(PlayerEntity player, World world, DemagnetizationCoilTile tile){
        tile.setRangeY(tile.rangeY + 1);
    }
}
