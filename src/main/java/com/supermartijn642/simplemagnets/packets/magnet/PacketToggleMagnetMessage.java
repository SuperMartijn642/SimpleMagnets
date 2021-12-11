package com.supermartijn642.simplemagnets.packets.magnet;

import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.network.BasePacket;
import com.supermartijn642.core.network.PacketContext;
import com.supermartijn642.simplemagnets.SMConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;

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
    public void write(PacketBuffer buffer){
        buffer.writeBoolean(this.on);
    }

    @Override
    public void read(PacketBuffer buffer){
        this.on = buffer.readBoolean();
    }

    @Override
    public void handle(PacketContext context){
        EntityPlayer player = ClientUtils.getPlayer();
        if(player != null){
            if(this.on){
                if(SMConfig.showToggleMessage.get())
                    player.sendStatusMessage(TextComponents.translation("simplemagnets.magnets.toggle_message").color(TextFormatting.YELLOW).string(" ").translation("simplemagnets.magnets.toggle_message.off").color(TextFormatting.RED).get(), true);
                if(SMConfig.playToggleSound.get())
                    player.world.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_NOTE_BELL, SoundCategory.PLAYERS, 0.4f, 0.01f);
            }else{
                if(SMConfig.showToggleMessage.get())
                    player.sendStatusMessage(TextComponents.translation("simplemagnets.magnets.toggle_message").color(TextFormatting.YELLOW).string(" ").translation("simplemagnets.magnets.toggle_message.on").color(TextFormatting.GREEN).get(), true);
                if(SMConfig.playToggleSound.get())
                    player.world.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_NOTE_BELL, SoundCategory.PLAYERS, 0.4f, 0.9f);
            }
        }
    }
}
