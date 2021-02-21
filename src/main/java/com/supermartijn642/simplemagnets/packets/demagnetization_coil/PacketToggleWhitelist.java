package com.supermartijn642.simplemagnets.packets.demagnetization_coil;

import com.supermartijn642.simplemagnets.DemagnetizationCoilTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created 7/15/2020 by SuperMartijn642
 */
public class PacketToggleWhitelist extends DemagnetizationCoilPacket {
    public PacketToggleWhitelist(BlockPos pos){
        super(pos);
    }

    public PacketToggleWhitelist(PacketBuffer buffer){
        super(buffer);
    }

    public static PacketToggleWhitelist decode(PacketBuffer buffer){
        return new PacketToggleWhitelist(buffer);
    }

    @Override
    protected void handle(PlayerEntity player, World world, DemagnetizationCoilTile tile){
        tile.filterWhitelist = !tile.filterWhitelist;
        tile.dataChanged();
    }
}
