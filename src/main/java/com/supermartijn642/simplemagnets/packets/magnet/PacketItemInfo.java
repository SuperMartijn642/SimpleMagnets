package com.supermartijn642.simplemagnets.packets.magnet;

import com.supermartijn642.simplemagnets.ClientProxy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.network.NetworkEvent;

import java.lang.reflect.Field;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * Created 1/8/2021 by SuperMartijn642
 */
public class PacketItemInfo {

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

    public PacketItemInfo(int target, UUID thrower, int pickupDelay){
        this.target = target;
        this.thrower = thrower;
        this.pickupDelay = pickupDelay;
    }

    public void encode(PacketBuffer buffer){
        buffer.writeInt(this.target);
        buffer.writeUUID(this.thrower);
        buffer.writeInt(this.pickupDelay);
    }

    public static PacketItemInfo decode(PacketBuffer buffer){
        return new PacketItemInfo(buffer.readInt(), buffer.readUUID(), buffer.readInt());
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier){
        contextSupplier.get().setPacketHandled(true);

        PlayerEntity player = ClientProxy.getPlayer();
        if(player != null && player.level != null){
            contextSupplier.get().enqueueWork(() -> {
                Entity entity = player.level.getEntity(this.target);
                if(entity instanceof ItemEntity){
                    ((ItemEntity)entity).setThrower(this.thrower);
                    ((ItemEntity)entity).setPickUpDelay(this.pickupDelay);
                }
            });
        }
    }

}
