package com.supermartijn642.simplemagnets.packets.demagnetization_coil;

import com.supermartijn642.simplemagnets.DemagnetizationCoilTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created 7/15/2020 by SuperMartijn642
 */
public class PacketToggleDurability extends DemagnetizationCoilPacket {
    public PacketToggleDurability(BlockPos pos){
        super(pos);
    }

    public PacketToggleDurability(PacketBuffer buffer){
        super(buffer);
    }

    public static PacketToggleDurability decode(PacketBuffer buffer){
        return new PacketToggleDurability(buffer);
    }

    @Override
    protected void handle(PlayerEntity player, World world, DemagnetizationCoilTile tile){
        tile.filterDurability = !tile.filterDurability;
        tile.dataChanged();
    }
}
