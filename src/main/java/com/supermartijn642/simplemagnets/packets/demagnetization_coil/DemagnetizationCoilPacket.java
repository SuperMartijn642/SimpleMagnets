package com.supermartijn642.simplemagnets.packets.demagnetization_coil;

import com.supermartijn642.simplemagnets.DemagnetizationCoilTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Created 7/15/2020 by SuperMartijn642
 */
public abstract class DemagnetizationCoilPacket {

    protected BlockPos pos;

    public DemagnetizationCoilPacket(BlockPos pos){
        this.pos = pos;
    }

    public DemagnetizationCoilPacket(PacketBuffer buffer){
        this.decodeBuffer(buffer);
    }

    public void encode(PacketBuffer buffer){
        buffer.writeBlockPos(this.pos);
    }

    protected void decodeBuffer(PacketBuffer buffer){
        this.pos = buffer.readBlockPos();
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier){
        contextSupplier.get().setPacketHandled(true);

        PlayerEntity player = contextSupplier.get().getSender();
        if(player == null || player.position().distanceToSqr(this.pos.getX(), this.pos.getY(), this.pos.getZ()) > 32 * 32)
            return;
        World world = player.level;
        if(world == null)
            return;
        TileEntity tile = world.getBlockEntity(this.pos);
        if(tile instanceof DemagnetizationCoilTile)
            contextSupplier.get().enqueueWork(() -> this.handle(player, world, (DemagnetizationCoilTile)tile));
    }

    protected abstract void handle(PlayerEntity player, World world, DemagnetizationCoilTile tile);
}
