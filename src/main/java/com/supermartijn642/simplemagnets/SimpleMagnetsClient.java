package com.supermartijn642.simplemagnets;

import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.gui.WidgetContainerScreen;
import com.supermartijn642.core.registry.ClientRegistrationHandler;
import com.supermartijn642.simplemagnets.gui.DemagnetizationCoilContainerScreen;
import com.supermartijn642.simplemagnets.gui.FilteredDemagnetizationCoilContainerScreen;
import com.supermartijn642.simplemagnets.gui.MagnetContainerScreen;
import com.supermartijn642.simplemagnets.packets.magnet.PacketToggleMagnet;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public class SimpleMagnetsClient implements ClientModInitializer {

    private static KeyMapping MAGNET_TOGGLE_KEY;

    @Override
    public void onInitializeClient(){
        ClientRegistrationHandler handler = ClientRegistrationHandler.get("simplemagnets");
        // Screens
        handler.registerContainerScreen(() -> SimpleMagnets.magnet_container, container -> WidgetContainerScreen.of(new MagnetContainerScreen(), container, false));
        handler.registerContainerScreen(() -> SimpleMagnets.demagnetization_coil_container, container -> WidgetContainerScreen.of(new DemagnetizationCoilContainerScreen(), container, false));
        handler.registerContainerScreen(() -> SimpleMagnets.filtered_demagnetization_coil_container, container -> WidgetContainerScreen.of(new FilteredDemagnetizationCoilContainerScreen(), container, false));
        // Block entity renderer
        handler.registerCustomBlockEntityRenderer(() -> SimpleMagnets.basic_demagnetization_coil_tile, DemagnetizationCoilBlockEntityRenderer::new);
        handler.registerCustomBlockEntityRenderer(() -> SimpleMagnets.advanced_demagnetization_coil_tile, DemagnetizationCoilBlockEntityRenderer::new);

        MAGNET_TOGGLE_KEY = new KeyMapping("simplemagnets.keys.toggle", 72/*'h'*/, "simplemagnets.keys.category");
        KeyBindingHelper.registerKeyBinding(MAGNET_TOGGLE_KEY);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while(MAGNET_TOGGLE_KEY.consumeClick())
                onKey();
        });
    }

    public static void onKey(){
        if(MAGNET_TOGGLE_KEY != null && ClientUtils.getWorld() != null && Minecraft.getInstance().screen == null)
            SimpleMagnets.CHANNEL.sendToServer(new PacketToggleMagnet());
    }
}
