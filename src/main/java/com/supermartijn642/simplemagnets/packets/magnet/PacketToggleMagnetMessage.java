package com.supermartijn642.simplemagnets.packets.magnet;

import com.supermartijn642.simplemagnets.ClientProxy;
import com.supermartijn642.simplemagnets.SMConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Created 1/28/2021 by SuperMartijn642
 */
public class PacketToggleMagnetMessage {

    private final boolean on;

    public PacketToggleMagnetMessage(boolean on){
        this.on = on;
    }

    public void encode(PacketBuffer buffer){
        buffer.writeBoolean(this.on);
    }

    public static PacketToggleMagnetMessage decode(PacketBuffer buffer){
        return new PacketToggleMagnetMessage(buffer.readBoolean());
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier){
        contextSupplier.get().setPacketHandled(true);

        PlayerEntity player = ClientProxy.getPlayer();
        if(player != null){
            if(this.on){
                if(SMConfig.showToggleMessage.get())
                    player.displayClientMessage(new TranslationTextComponent("simplemagnets.magnets.toggle_message").withStyle(TextFormatting.YELLOW).append(" ").append(new TranslationTextComponent("simplemagnets.magnets.toggle_message.off").withStyle(TextFormatting.RED)), true);
                if(SMConfig.playToggleSound.get())
                    player.level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.NOTE_BLOCK_BELL, SoundCategory.PLAYERS, 0.4f, 0.01f);
            }else{
                if(SMConfig.showToggleMessage.get())
                    player.displayClientMessage(new TranslationTextComponent("simplemagnets.magnets.toggle_message").withStyle(TextFormatting.YELLOW).append(" ").append(new TranslationTextComponent("simplemagnets.magnets.toggle_message.on").withStyle(TextFormatting.GREEN)), true);
                if(SMConfig.playToggleSound.get())
                    player.level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.NOTE_BLOCK_BELL, SoundCategory.PLAYERS, 0.4f, 0.9f);
            }
        }
    }
}
