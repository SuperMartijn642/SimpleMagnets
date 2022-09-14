package com.supermartijn642.simplemagnets;

import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.gui.WidgetContainerScreen;
import com.supermartijn642.core.registry.ClientRegistrationHandler;
import com.supermartijn642.simplemagnets.gui.DemagnetizationCoilContainerScreen;
import com.supermartijn642.simplemagnets.gui.FilteredDemagnetizationCoilContainerScreen;
import com.supermartijn642.simplemagnets.gui.MagnetContainerScreen;
import com.supermartijn642.simplemagnets.packets.magnet.PacketToggleMagnet;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
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
public class SimpleMagnetsClient {

    private static KeyMapping MAGNET_TOGGLE_KEY;

    public static void register(){
        ClientRegistrationHandler handler = ClientRegistrationHandler.get("simplemagnets");
        // Screens
        handler.registerContainerScreen(() -> SimpleMagnets.magnet_container, container -> WidgetContainerScreen.of(new MagnetContainerScreen(), container, false));
        handler.registerContainerScreen(() -> SimpleMagnets.demagnetization_coil_container, container -> WidgetContainerScreen.of(new DemagnetizationCoilContainerScreen(), container, false));
        handler.registerContainerScreen(() -> SimpleMagnets.filtered_demagnetization_coil_container, container -> WidgetContainerScreen.of(new FilteredDemagnetizationCoilContainerScreen(), container, false));
    }

    @SubscribeEvent
    public static void registerKeyBindings(RegisterKeyMappingsEvent e){
        MAGNET_TOGGLE_KEY = new KeyMapping("simplemagnets.keys.toggle", 72/*'h'*/, "simplemagnets.keys.category");
        e.register(MAGNET_TOGGLE_KEY);
        MinecraftForge.EVENT_BUS.addListener(SimpleMagnetsClient::onKey);
    }

    public static void onKey(InputEvent.Key e){
        if(MAGNET_TOGGLE_KEY != null && MAGNET_TOGGLE_KEY.consumeClick() && ClientUtils.getWorld() != null && Minecraft.getInstance().screen == null)
            SimpleMagnets.CHANNEL.sendToServer(new PacketToggleMagnet());
    }
}
