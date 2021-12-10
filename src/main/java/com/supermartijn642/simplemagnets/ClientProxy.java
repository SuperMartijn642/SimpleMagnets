package com.supermartijn642.simplemagnets;

import com.supermartijn642.simplemagnets.gui.*;
import com.supermartijn642.simplemagnets.packets.magnet.PacketToggleMagnet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public class ClientProxy {

    private static KeyBinding MAGNET_TOGGLE_KEY;

    public static void init(){
        MAGNET_TOGGLE_KEY = new KeyBinding("keys.simplemagnets.toggle", 72/*'h'*/, "keys.category.simplemagnets");
        ClientRegistry.registerKeyBinding(MAGNET_TOGGLE_KEY);
        MinecraftForge.EVENT_BUS.addListener(ClientProxy::onKey);
    }

    public static PlayerEntity getPlayer(){
        return Minecraft.getInstance().player;
    }

    public static void registerScreen(){
        ScreenManager.register(SimpleMagnets.container, MagnetContainerScreen::new);
        ScreenManager.register(SimpleMagnets.demagnetization_coil_container, (ScreenManager.IScreenFactory<DemagnetizationCoilContainer,DemagnetizationCoilContainerScreen>)(container, inventory, title) -> new DemagnetizationCoilContainerScreen(container));
        ScreenManager.register(SimpleMagnets.filtered_demagnetization_coil_container, (ScreenManager.IScreenFactory<FilteredDemagnetizationCoilContainer,FilteredDemagnetizationCoilContainerScreen>)(container, inventory, title) -> new FilteredDemagnetizationCoilContainerScreen(container));
    }

    public static void onKey(InputEvent.KeyInputEvent e){
        if(MAGNET_TOGGLE_KEY != null && MAGNET_TOGGLE_KEY.consumeClick() && Minecraft.getInstance().level != null && Minecraft.getInstance().screen == null)
            SimpleMagnets.CHANNEL.sendToServer(new PacketToggleMagnet());
    }

}
