package com.supermartijn642.simplemagnets.packets.magnet;

import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.network.BasePacket;
import com.supermartijn642.core.network.PacketContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.lang.reflect.Field;
import java.util.UUID;

/**
 * Created 1/8/2021 by SuperMartijn642
 */
public class PacketItemInfo implements BasePacket {

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
    public void write(PacketBuffer buffer){
        buffer.writeInt(this.target);
        buffer.writeBoolean(this.thrower != null);
        if(this.thrower != null)
            ByteBufUtils.writeUTF8String(buffer, this.thrower.toString());
        buffer.writeInt(this.pickupDelay);
    }

    @Override
    public void read(PacketBuffer buffer){
        this.target = buffer.readInt();
        this.thrower = buffer.readBoolean() ? UUID.fromString(ByteBufUtils.readUTF8String(buffer)) : null;
        this.pickupDelay = buffer.readInt();
    }

    @Override
    public void handle(PacketContext context){
        EntityPlayer player = ClientUtils.getPlayer();
        if(player != null && player.world != null){
            context.queueTask(() -> {
                Entity entity = player.world.getEntityByID(this.target);
                if(entity instanceof EntityItem){
                    entity.getEntityData().setUniqueId("simplemagnets:throwerId", this.thrower);
                    ((EntityItem)entity).setPickupDelay(this.pickupDelay);
                }
            });
        }
    }
}
