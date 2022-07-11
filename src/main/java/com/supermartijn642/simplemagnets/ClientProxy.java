package com.supermartijn642.simplemagnets;

import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.simplemagnets.gui.*;
import com.supermartijn642.simplemagnets.packets.magnet.PacketToggleMagnet;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientProxy {

    private static KeyMapping MAGNET_TOGGLE_KEY;

    @SubscribeEvent
    public static void registerKeyBindings(RegisterKeyMappingsEvent e){
        MAGNET_TOGGLE_KEY = new KeyMapping("keys.simplemagnets.toggle", 72/*'h'*/, "keys.category.simplemagnets");
        e.register(MAGNET_TOGGLE_KEY);
        MinecraftForge.EVENT_BUS.addListener(ClientProxy::onKey);
    }

    public static void registerScreen(){
        MenuScreens.register(SimpleMagnets.container, MagnetContainerScreen::new);
        MenuScreens.register(SimpleMagnets.demagnetization_coil_container, (MenuScreens.ScreenConstructor<DemagnetizationCoilContainer,DemagnetizationCoilContainerScreen>)(container, inventory, title) -> new DemagnetizationCoilContainerScreen(container));
        MenuScreens.register(SimpleMagnets.filtered_demagnetization_coil_container, (MenuScreens.ScreenConstructor<FilteredDemagnetizationCoilContainer,FilteredDemagnetizationCoilContainerScreen>)(container, inventory, title) -> new FilteredDemagnetizationCoilContainerScreen(container));
    }

    public static void onKey(InputEvent.Key e){
        if(MAGNET_TOGGLE_KEY != null && MAGNET_TOGGLE_KEY.consumeClick() && ClientUtils.getWorld() != null && Minecraft.getInstance().screen == null)
            SimpleMagnets.CHANNEL.sendToServer(new PacketToggleMagnet());
    }

}
