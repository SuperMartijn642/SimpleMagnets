package com.supermartijn642.simplemagnets.packets.demagnetization_coil;

import com.supermartijn642.simplemagnets.DemagnetizationCoilTile;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Created 7/15/2020 by SuperMartijn642
 */
public class PacketDecreaseXRange extends DemagnetizationCoilPacket {
    public PacketDecreaseXRange(BlockPos pos){
        super(pos);
    }

    public PacketDecreaseXRange(FriendlyByteBuf buffer){
        super(buffer);
    }

    public static PacketDecreaseXRange decode(FriendlyByteBuf buffer){
        return new PacketDecreaseXRange(buffer);
    }

    @Override
    protected void handle(Player player, Level world, DemagnetizationCoilTile tile){
        tile.setRangeX(tile.rangeX - 1);
    }
}
