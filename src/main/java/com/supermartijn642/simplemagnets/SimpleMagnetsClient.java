package com.supermartijn642.simplemagnets;

import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.gui.WidgetContainerScreen;
import com.supermartijn642.core.registry.ClientRegistrationHandler;
import com.supermartijn642.simplemagnets.extensions.SimpleMagnetsKeyMappingCategory;
import com.supermartijn642.simplemagnets.gui.DemagnetizationCoilContainerScreen;
import com.supermartijn642.simplemagnets.gui.FilteredDemagnetizationCoilContainerScreen;
import com.supermartijn642.simplemagnets.gui.MagnetContainerScreen;
import com.supermartijn642.simplemagnets.packets.magnet.PacketToggleMagnet;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public class SimpleMagnetsClient {

    private static KeyMapping MAGNET_TOGGLE_KEY;

    public static void register(FMLJavaModLoadingContext context){
        RegisterKeyMappingsEvent.BUS.addListener(SimpleMagnetsClient::registerKeyBindings);
        InputEvent.Key.BUS.addListener(SimpleMagnetsClient::onKey);

        ClientRegistrationHandler handler = ClientRegistrationHandler.get("simplemagnets");
        // Screens
        handler.registerContainerScreen(() -> SimpleMagnets.magnet_container, container -> WidgetContainerScreen.of(new MagnetContainerScreen(), container, false));
        handler.registerContainerScreen(() -> SimpleMagnets.demagnetization_coil_container, container -> WidgetContainerScreen.of(new DemagnetizationCoilContainerScreen(), container, false));
        handler.registerContainerScreen(() -> SimpleMagnets.filtered_demagnetization_coil_container, container -> WidgetContainerScreen.of(new FilteredDemagnetizationCoilContainerScreen(), container, false));
        // Block entity renderer
        handler.registerCustomBlockEntityRenderer(() -> SimpleMagnets.basic_demagnetization_coil_tile, DemagnetizationCoilBlockEntityRenderer::new);
        handler.registerCustomBlockEntityRenderer(() -> SimpleMagnets.advanced_demagnetization_coil_tile, DemagnetizationCoilBlockEntityRenderer::new);
    }

    public static void registerKeyBindings(RegisterKeyMappingsEvent e){
        KeyMapping.Category keyCategory = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("simplemagnets", "keys"));
        //noinspection DataFlowIssue
        ((SimpleMagnetsKeyMappingCategory)(Object)keyCategory).simplemagnetsOverwriteLabel(Component.translatable("simplemagnets.keys.category"));
        MAGNET_TOGGLE_KEY = new KeyMapping("simplemagnets.keys.toggle", 72/*'h'*/, keyCategory);
        e.register(MAGNET_TOGGLE_KEY);
    }

    public static void onKey(InputEvent.Key e){
        if(MAGNET_TOGGLE_KEY != null && MAGNET_TOGGLE_KEY.consumeClick() && ClientUtils.getWorld() != null && Minecraft.getInstance().screen == null)
            SimpleMagnets.CHANNEL.sendToServer(new PacketToggleMagnet());
    }
}
