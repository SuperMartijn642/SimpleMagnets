package com.supermartijn642.simplemagnets.packets.magnet;

import com.supermartijn642.simplemagnets.ClientProxy;
import com.supermartijn642.simplemagnets.SMConfig;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created 1/28/2021 by SuperMartijn642
 */
public class PacketToggleMagnetMessage implements IMessage, IMessageHandler<PacketToggleMagnetMessage,IMessage> {

    private boolean on;

    public PacketToggleMagnetMessage(boolean on){
        this.on = on;
    }

    public PacketToggleMagnetMessage(){
    }

    @Override
    public void fromBytes(ByteBuf buffer){
        this.on = buffer.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buffer){
        buffer.writeBoolean(this.on);
    }

    @Override
    public IMessage onMessage(PacketToggleMagnetMessage message, MessageContext ctx){
        EntityPlayer player = ClientProxy.getPlayer();
        if(player != null){
            if(message.on){
                if(SMConfig.showToggleMessage.get())
                    player.sendStatusMessage(new TextComponentTranslation("simplemagnets.magnets.toggle_message").setStyle(new Style().setColor(TextFormatting.YELLOW)).appendText(" ").appendSibling(new TextComponentTranslation("simplemagnets.magnets.toggle_message.off").setStyle(new Style().setColor(TextFormatting.RED))), true);
                if(SMConfig.playToggleSound.get())
                    player.world.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_NOTE_BELL, SoundCategory.PLAYERS, 0.4f, 0.01f);
            }else{
                if(SMConfig.showToggleMessage.get())
                    player.sendStatusMessage(new TextComponentTranslation("simplemagnets.magnets.toggle_message").setStyle(new Style().setColor(TextFormatting.YELLOW)).appendText(" ").appendSibling(new TextComponentTranslation("simplemagnets.magnets.toggle_message.on").setStyle(new Style().setColor(TextFormatting.GREEN))), true);
                if(SMConfig.playToggleSound.get())
                    player.world.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_NOTE_BELL, SoundCategory.PLAYERS, 0.4f, 0.9f);
            }
        }
        return null;
    }
}
