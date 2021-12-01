package com.supermartijn642.simplemagnets;

import com.supermartijn642.simplemagnets.gui.*;
import com.supermartijn642.simplemagnets.packets.magnet.PacketToggleMagnet;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public class ClientProxy {

    private static KeyMapping MAGNET_TOGGLE_KEY;

    public static void init(){
        MAGNET_TOGGLE_KEY = new KeyMapping("keys.simplemagnets.toggle", 72/*'h'*/, "keys.category.simplemagnets");
        ClientRegistry.registerKeyBinding(MAGNET_TOGGLE_KEY);
        MinecraftForge.EVENT_BUS.addListener(ClientProxy::onKey);
    }

    public static Player getPlayer(){
        return Minecraft.getInstance().player;
    }

    public static void registerScreen(){
        MenuScreens.register(SimpleMagnets.container, MagnetContainerScreen::new);
        MenuScreens.register(SimpleMagnets.demagnetization_coil_container, (MenuScreens.ScreenConstructor<DemagnetizationCoilContainer,DemagnetizationCoilContainerScreen>)(container, inventory, title) -> new DemagnetizationCoilContainerScreen(container));
        MenuScreens.register(SimpleMagnets.filtered_demagnetization_coil_container, (MenuScreens.ScreenConstructor<FilteredDemagnetizationCoilContainer,FilteredDemagnetizationCoilContainerScreen>)(container, inventory, title) -> new FilteredDemagnetizationCoilContainerScreen(container));
    }

    public static void onKey(InputEvent.KeyInputEvent e){
        if(MAGNET_TOGGLE_KEY != null && MAGNET_TOGGLE_KEY.consumeClick() && Minecraft.getInstance().level != null && Minecraft.getInstance().screen == null)
            SimpleMagnets.CHANNEL.sendToServer(new PacketToggleMagnet());
    }

}
