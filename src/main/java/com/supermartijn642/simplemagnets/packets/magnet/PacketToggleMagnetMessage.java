package com.supermartijn642.simplemagnets.packets.magnet;

import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.network.BasePacket;
import com.supermartijn642.core.network.PacketContext;
import com.supermartijn642.simplemagnets.SMConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

/**
 * Created 1/28/2021 by SuperMartijn642
 */
public class PacketToggleMagnetMessage implements BasePacket {

    private boolean on;

    public PacketToggleMagnetMessage(boolean on){
        this.on = on;
    }

    public PacketToggleMagnetMessage(){
    }

    @Override
    public void write(FriendlyByteBuf buffer){
        buffer.writeBoolean(this.on);
    }

    @Override
    public void read(FriendlyByteBuf buffer){
        this.on = buffer.readBoolean();
    }

    @Override
    public void handle(PacketContext context){
        Player player = ClientUtils.getPlayer();
        if(player != null){
            if(this.on){
                if(SMConfig.showToggleMessage.get())
                    player.displayClientMessage(TextComponents.translation("simplemagnets.magnets.toggle_message").color(ChatFormatting.YELLOW).string(" ").translation("simplemagnets.magnets.toggle_message.off").color(ChatFormatting.RED).get(), true);
                if(SMConfig.playToggleSound.get())
                    player.level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.NOTE_BLOCK_BELL, SoundSource.PLAYERS, 0.4f, 0.01f);
            }else{
                if(SMConfig.showToggleMessage.get())
                    player.displayClientMessage(TextComponents.translation("simplemagnets.magnets.toggle_message").color(ChatFormatting.YELLOW).string(" ").translation("simplemagnets.magnets.toggle_message.on").color(ChatFormatting.GREEN).get(), true);
                if(SMConfig.playToggleSound.get())
                    player.level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.NOTE_BLOCK_BELL, SoundSource.PLAYERS, 0.4f, 0.9f);
            }
        }
    }
}
