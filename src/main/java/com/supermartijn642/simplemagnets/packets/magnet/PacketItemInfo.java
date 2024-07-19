package com.supermartijn642.simplemagnets.packets.magnet;

import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.network.BasePacket;
import com.supermartijn642.core.network.PacketContext;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

/**
 * Created 1/8/2021 by SuperMartijn642
 */
public class PacketItemInfo implements BasePacket {

    private int target;
    private UUID thrower;
    private int pickupDelay;

    public PacketItemInfo(ItemEntity itemEntity){
        this.target = itemEntity.getId();
        this.thrower = itemEntity.thrower;
        this.pickupDelay = itemEntity.pickupDelay;
    }

    public PacketItemInfo(){
    }

    @Override
    public void write(FriendlyByteBuf buffer){
        buffer.writeInt(this.target);
        buffer.writeUUID(this.thrower);
        buffer.writeInt(this.pickupDelay);
    }

    @Override
    public void read(FriendlyByteBuf buffer){
        this.target = buffer.readInt();
        this.thrower = buffer.readUUID();
        this.pickupDelay = buffer.readInt();
    }

    @Override
    public void handle(PacketContext context){
        Player player = ClientUtils.getPlayer();
        if(player != null && player.level() != null){
            context.queueTask(() -> {
                Entity entity = player.level().getEntity(this.target);
                if(entity instanceof ItemEntity){
                    ((ItemEntity)entity).thrower = this.thrower;
                    ((ItemEntity)entity).setPickUpDelay(this.pickupDelay);
                }
            });
        }
    }
}
