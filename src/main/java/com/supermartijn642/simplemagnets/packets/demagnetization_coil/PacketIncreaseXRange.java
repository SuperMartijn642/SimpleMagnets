package com.supermartijn642.simplemagnets.packets.demagnetization_coil;

import com.supermartijn642.simplemagnets.DemagnetizationCoilTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created 7/15/2020 by SuperMartijn642
 */
public class PacketIncreaseXRange extends DemagnetizationCoilPacket {
    public PacketIncreaseXRange(BlockPos pos){
        super(pos);
    }

    public PacketIncreaseXRange(PacketBuffer buffer){
        super(buffer);
    }

    public static PacketIncreaseXRange decode(PacketBuffer buffer){
        return new PacketIncreaseXRange(buffer);
    }

    @Override
    protected void handle(PlayerEntity player, World world, DemagnetizationCoilTile tile){
        tile.setRangeX(tile.rangeX + 1);
    }
}
