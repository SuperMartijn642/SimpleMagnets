package com.supermartijn642.simplemagnets.packets.magnet;

import com.supermartijn642.simplemagnets.AdvancedMagnet;
import com.supermartijn642.simplemagnets.SMConfig;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Created 7/8/2020 by SuperMartijn642
 */
public class PacketDecreaseItemRange {

    public void encode(FriendlyByteBuf buffer){
    }

    public static PacketDecreaseItemRange decode(FriendlyByteBuf buffer){
        return new PacketDecreaseItemRange();
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier){
        contextSupplier.get().setPacketHandled(true);

        Player player = contextSupplier.get().getSender();
        if(player != null){
            ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);

            if(stack.getItem() instanceof AdvancedMagnet)
                stack.getOrCreateTag().putInt("itemRange", Math.max(SMConfig.advancedMagnetMinRange.get(), (stack.getOrCreateTag().contains("itemRange") ? stack.getOrCreateTag().getInt("itemRange") : SMConfig.advancedMagnetRange.get()) - 1));
        }
    }
}
