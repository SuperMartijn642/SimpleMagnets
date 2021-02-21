package com.supermartijn642.simplemagnets.packets.magnet;

import com.supermartijn642.simplemagnets.ClientProxy;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.lang.reflect.Field;
import java.util.UUID;

/**
 * Created 1/8/2021 by SuperMartijn642
 */
public class PacketItemInfo implements IMessage, IMessageHandler<PacketItemInfo,IMessage> {

    private static final Field PICKUP_DELAY = ObfuscationReflectionHelper.findField(EntityItem.class, "field_145804_b");

    private int target;
    private UUID thrower;
    private int pickupDelay;

    public PacketItemInfo(EntityItem itemEntity){
        this.target = itemEntity.getEntityId();
        this.thrower = itemEntity.getEntityData().hasKey("simplemagnets:throwerIdMost") ? itemEntity.getEntityData().getUniqueId("simplemagnets:throwerId") : null;
        try{
            PICKUP_DELAY.setAccessible(true);
            this.pickupDelay = PICKUP_DELAY.getInt(itemEntity);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public PacketItemInfo(){
    }

    @Override
    public void fromBytes(ByteBuf buffer){
        this.target = buffer.readInt();
        this.thrower = buffer.readBoolean() ? UUID.fromString(ByteBufUtils.readUTF8String(buffer)) : null;
        this.pickupDelay = buffer.readInt();
    }

    @Override
    public void toBytes(ByteBuf buffer){
        buffer.writeInt(this.target);
        buffer.writeBoolean(this.thrower != null);
        if(this.thrower != null)
            ByteBufUtils.writeUTF8String(buffer, this.thrower.toString());
        buffer.writeInt(this.pickupDelay);
    }

    @Override
    public IMessage onMessage(PacketItemInfo message, MessageContext ctx){
        EntityPlayer player = ClientProxy.getPlayer();
        if(player != null && player.world != null){
            ClientProxy.queTask(() -> {
                Entity entity = player.world.getEntityByID(message.target);
                if(entity instanceof EntityItem){
                    entity.getEntityData().setUniqueId("simplemagnets:throwerId", message.thrower);
                    ((EntityItem)entity).setPickupDelay(message.pickupDelay);
                }
            });
        }
        return null;
    }

}
