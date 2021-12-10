package com.supermartijn642.simplemagnets.packets.magnet;

import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.network.BasePacket;
import com.supermartijn642.core.network.PacketContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.util.UUID;

/**
 * Created 1/8/2021 by SuperMartijn642
 */
public class PacketItemInfo implements BasePacket {

    private static final Field PICKUP_DELAY = ObfuscationReflectionHelper.findField(ItemEntity.class, "pickupDelay");

    private int target;
    private UUID thrower;
    private int pickupDelay;

    public PacketItemInfo(ItemEntity itemEntity){
        this.target = itemEntity.getId();
        this.thrower = itemEntity.getThrower();
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
        buffer.writeUUID(this.thrower);
        buffer.writeInt(this.pickupDelay);
    }

    @Override
    public void read(PacketBuffer buffer){
        this.target = buffer.readInt();
        this.thrower = buffer.readUUID();
        this.pickupDelay = buffer.readInt();
    }

    @Override
    public void handle(PacketContext context){
        PlayerEntity player = ClientUtils.getPlayer();
        if(player != null && player.level != null){
            context.queueTask(() -> {
                Entity entity = player.level.getEntity(this.target);
                if(entity instanceof ItemEntity){
                    ((ItemEntity)entity).setThrower(this.thrower);
                    ((ItemEntity)entity).setPickUpDelay(this.pickupDelay);
                }
            });
        }
    }
}
