package com.supermartijn642.simplemagnets;

import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.gui.WidgetContainerScreen;
import com.supermartijn642.core.registry.ClientRegistrationHandler;
import com.supermartijn642.simplemagnets.gui.DemagnetizationCoilContainerScreen;
import com.supermartijn642.simplemagnets.gui.FilteredDemagnetizationCoilContainerScreen;
import com.supermartijn642.simplemagnets.gui.MagnetContainerScreen;
import com.supermartijn642.simplemagnets.packets.magnet.PacketToggleMagnet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SimpleMagnetsClient {

    private static KeyBinding MAGNET_TOGGLE_KEY;

    public static void register(){
        ClientRegistrationHandler handler = ClientRegistrationHandler.get("simplemagnets");
        // Screens
        handler.registerContainerScreen(() -> SimpleMagnets.magnet_container, container -> WidgetContainerScreen.of(new MagnetContainerScreen(), container, false));
        handler.registerContainerScreen(() -> SimpleMagnets.demagnetization_coil_container, container -> WidgetContainerScreen.of(new DemagnetizationCoilContainerScreen(), container, false));
        handler.registerContainerScreen(() -> SimpleMagnets.filtered_demagnetization_coil_container, container -> WidgetContainerScreen.of(new FilteredDemagnetizationCoilContainerScreen(), container, false));
        // Block entity renderer
        handler.registerCustomBlockEntityRenderer(() -> SimpleMagnets.basic_demagnetization_coil_tile, DemagnetizationCoilBlockEntityRenderer::new);
        handler.registerCustomBlockEntityRenderer(() -> SimpleMagnets.advanced_demagnetization_coil_tile, DemagnetizationCoilBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerKeyBindings(FMLClientSetupEvent e){
        MAGNET_TOGGLE_KEY = new KeyBinding("simplemagnets.keys.toggle", 72/*'h'*/, "simplemagnets.keys.category");
        ClientRegistry.registerKeyBinding(MAGNET_TOGGLE_KEY);
        MinecraftForge.EVENT_BUS.addListener(SimpleMagnetsClient::onKey);
    }

    public static void onKey(InputEvent.KeyInputEvent e){
        if(MAGNET_TOGGLE_KEY != null && MAGNET_TOGGLE_KEY.consumeClick() && ClientUtils.getWorld() != null && Minecraft.getInstance().screen == null)
            SimpleMagnets.CHANNEL.sendToServer(new PacketToggleMagnet());
    }
}
