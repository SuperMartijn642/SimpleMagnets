package com.supermartijn642.simplemagnets.packets;

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
                    player.sendStatusMessage(new TranslationTextComponent("simplemagnets.magnets.toggle_message").mergeStyle(TextFormatting.YELLOW).appendString(" ").append(new TranslationTextComponent("simplemagnets.magnets.toggle_message.off").mergeStyle(TextFormatting.RED)), true);
                if(SMConfig.playToggleSound.get())
                    player.world.playSound(player, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.BLOCK_NOTE_BLOCK_BELL, SoundCategory.PLAYERS, 0.4f, 0.01f);
            }else{
                if(SMConfig.showToggleMessage.get())
                    player.sendStatusMessage(new TranslationTextComponent("simplemagnets.magnets.toggle_message").mergeStyle(TextFormatting.YELLOW).appendString(" ").append(new TranslationTextComponent("simplemagnets.magnets.toggle_message.on").mergeStyle(TextFormatting.GREEN)), true);
                if(SMConfig.playToggleSound.get())
                    player.world.playSound(player, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.BLOCK_NOTE_BLOCK_BELL, SoundCategory.PLAYERS, 0.4f, 0.9f);
            }
        }
    }
}
