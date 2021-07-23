package com.supermartijn642.simplemagnets.packets.magnet;

import com.supermartijn642.simplemagnets.ClientProxy;
import com.supermartijn642.simplemagnets.SMConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Created 1/28/2021 by SuperMartijn642
 */
public class PacketToggleMagnetMessage {

    private final boolean on;

    public PacketToggleMagnetMessage(boolean on){
        this.on = on;
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeBoolean(this.on);
    }

    public static PacketToggleMagnetMessage decode(FriendlyByteBuf buffer){
        return new PacketToggleMagnetMessage(buffer.readBoolean());
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier){
        contextSupplier.get().setPacketHandled(true);

        Player player = ClientProxy.getPlayer();
        if(player != null){
            if(this.on){
                if(SMConfig.showToggleMessage.get())
                    player.displayClientMessage(new TranslatableComponent("simplemagnets.magnets.toggle_message").withStyle(ChatFormatting.YELLOW).append(" ").append(new TranslatableComponent("simplemagnets.magnets.toggle_message.off").withStyle(ChatFormatting.RED)), true);
                if(SMConfig.playToggleSound.get())
                    player.level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.NOTE_BLOCK_BELL, SoundSource.PLAYERS, 0.4f, 0.01f);
            }else{
                if(SMConfig.showToggleMessage.get())
                    player.displayClientMessage(new TranslatableComponent("simplemagnets.magnets.toggle_message").withStyle(ChatFormatting.YELLOW).append(" ").append(new TranslatableComponent("simplemagnets.magnets.toggle_message.on").withStyle(ChatFormatting.GREEN)), true);
                if(SMConfig.playToggleSound.get())
                    player.level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.NOTE_BLOCK_BELL, SoundSource.PLAYERS, 0.4f, 0.9f);
            }
        }
    }
}
