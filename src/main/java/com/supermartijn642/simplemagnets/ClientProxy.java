package com.supermartijn642.simplemagnets;

import com.supermartijn642.simplemagnets.gui.MagnetContainerScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public class ClientProxy {

    public static PlayerEntity getPlayer(){
        return Minecraft.getInstance().player;
    }

    public static void registerScreen(){
        ScreenManager.registerFactory(SimpleMagnets.container, MagnetContainerScreen::new);
    }

}
