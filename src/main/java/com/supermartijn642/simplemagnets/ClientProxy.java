package com.supermartijn642.simplemagnets;

import com.supermartijn642.simplemagnets.packets.PacketToggleMagnet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy {

    private static KeyBinding TOGGLE_MAGNET_KEY;

    public static void init(){
        if(Loader.isModLoaded("baubles")){
            TOGGLE_MAGNET_KEY = new KeyBinding("keys.simplemagnets.toggle", 35/*'h'*/, "keys.category.simplemagnets");
            ClientRegistry.registerKeyBinding(TOGGLE_MAGNET_KEY);
        }
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent e){
        ModelLoader.setCustomModelResourceLocation(SimpleMagnets.basic_magnet, 0, new ModelResourceLocation(SimpleMagnets.basic_magnet.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(SimpleMagnets.advanced_magnet, 0, new ModelResourceLocation(SimpleMagnets.advanced_magnet.getRegistryName(), "inventory"));
    }

    public static EntityPlayer getPlayer(){
        return Minecraft.getMinecraft().player;
    }

    @SubscribeEvent
    public static void onKey(InputEvent.KeyInputEvent e){
        if(TOGGLE_MAGNET_KEY != null && TOGGLE_MAGNET_KEY.isPressed() && Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().currentScreen == null)
            SimpleMagnets.channel.sendToServer(new PacketToggleMagnet());
    }

}
