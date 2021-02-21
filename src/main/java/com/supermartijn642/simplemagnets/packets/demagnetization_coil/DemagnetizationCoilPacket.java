package com.supermartijn642.simplemagnets.packets.demagnetization_coil;

import com.supermartijn642.simplemagnets.DemagnetizationCoilTile;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created 7/15/2020 by SuperMartijn642
 */
public abstract class DemagnetizationCoilPacket<T extends DemagnetizationCoilPacket> implements IMessage, IMessageHandler<T,IMessage> {

    protected BlockPos pos;

    public DemagnetizationCoilPacket(BlockPos pos){
        this.pos = pos;
    }

    public DemagnetizationCoilPacket(){
    }

    @Override
    public void toBytes(ByteBuf buf){
        buf.writeInt(this.pos.getX());
        buf.writeInt(this.pos.getY());
        buf.writeInt(this.pos.getZ());
    }

    @Override
    public void fromBytes(ByteBuf buf){
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    @Override
    public IMessage onMessage(T message, MessageContext ctx){
        this.pos = message.pos;

        EntityPlayerMP player = ctx.getServerHandler().player;
        if(player == null || player.getPositionVector().squareDistanceTo(this.pos.getX(), this.pos.getY(), this.pos.getZ()) > 32 * 32)
            return null;
        WorldServer world = player.getServerWorld();
        if(world == null)
            return null;
        TileEntity tile = world.getTileEntity(this.pos);
        if(tile instanceof DemagnetizationCoilTile)
            world.addScheduledTask(() -> this.handle(player, world, (DemagnetizationCoilTile)tile));
        return null;
    }

    protected abstract void handle(EntityPlayer player, World world, DemagnetizationCoilTile tile);
}
