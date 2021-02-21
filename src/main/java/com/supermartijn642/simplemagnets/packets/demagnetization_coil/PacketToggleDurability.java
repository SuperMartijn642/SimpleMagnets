package com.supermartijn642.simplemagnets.packets.demagnetization_coil;

import com.supermartijn642.simplemagnets.DemagnetizationCoilTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created 7/15/2020 by SuperMartijn642
 */
public class PacketToggleDurability extends DemagnetizationCoilPacket<PacketToggleDurability> {
    public PacketToggleDurability(BlockPos pos){
        super(pos);
    }

    public PacketToggleDurability(){
    }

    @Override
    protected void handle(EntityPlayer player, World world, DemagnetizationCoilTile tile){
        tile.filterDurability = !tile.filterDurability;
        tile.dataChanged();
    }
}
