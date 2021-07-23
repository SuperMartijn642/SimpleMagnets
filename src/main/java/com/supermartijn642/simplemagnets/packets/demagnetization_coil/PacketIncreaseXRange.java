package com.supermartijn642.simplemagnets.packets.demagnetization_coil;

import com.supermartijn642.simplemagnets.DemagnetizationCoilTile;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Created 7/15/2020 by SuperMartijn642
 */
public class PacketIncreaseXRange extends DemagnetizationCoilPacket {
    public PacketIncreaseXRange(BlockPos pos){
        super(pos);
    }

    public PacketIncreaseXRange(FriendlyByteBuf buffer){
        super(buffer);
    }

    public static PacketIncreaseXRange decode(FriendlyByteBuf buffer){
        return new PacketIncreaseXRange(buffer);
    }

    @Override
    protected void handle(Player player, Level world, DemagnetizationCoilTile tile){
        tile.setRangeX(tile.rangeX + 1);
    }
}
