package com.supermartijn642.simplemagnets.packets.demagnetization_coil;

import com.supermartijn642.simplemagnets.DemagnetizationCoilTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created 7/15/2020 by SuperMartijn642
 */
public class PacketDecreaseXRange extends DemagnetizationCoilPacket {
    public PacketDecreaseXRange(BlockPos pos){
        super(pos);
    }

    public PacketDecreaseXRange(PacketBuffer buffer){
        super(buffer);
    }

    public static PacketDecreaseXRange decode(PacketBuffer buffer){
        return new PacketDecreaseXRange(buffer);
    }

    @Override
    protected void handle(PlayerEntity player, World world, DemagnetizationCoilTile tile){
        tile.setRangeX(tile.rangeX - 1);
    }
}
