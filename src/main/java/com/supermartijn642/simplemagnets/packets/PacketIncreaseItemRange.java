package com.supermartijn642.simplemagnets.packets;

import com.supermartijn642.simplemagnets.AdvancedMagnet;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Created 7/8/2020 by SuperMartijn642
 */
public class PacketIncreaseItemRange {

    public void encode(PacketBuffer buffer){
    }

    public static PacketIncreaseItemRange decode(PacketBuffer buffer){
        return new PacketIncreaseItemRange();
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier){
        contextSupplier.get().setPacketHandled(true);

        PlayerEntity player = contextSupplier.get().getSender();
        if(player != null){
            ItemStack stack = player.getHeldItem(Hand.MAIN_HAND);

            if(stack.getItem() instanceof AdvancedMagnet)
                stack.getOrCreateTag().putInt("itemRange", Math.min(AdvancedMagnet.MAX_RANGE, (stack.getOrCreateTag().contains("itemRange") ? stack.getOrCreateTag().getInt("itemRange") : AdvancedMagnet.DEFAULT_RANGE) + 1));
        }
    }
}
