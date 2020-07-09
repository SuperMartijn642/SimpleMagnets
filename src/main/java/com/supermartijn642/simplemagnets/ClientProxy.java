package com.supermartijn642.simplemagnets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy {

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent e){
        ModelLoader.setCustomModelResourceLocation(SimpleMagnets.basic_magnet, 0, new ModelResourceLocation(SimpleMagnets.basic_magnet.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(SimpleMagnets.advanced_magnet, 0, new ModelResourceLocation(SimpleMagnets.advanced_magnet.getRegistryName(), "inventory"));
    }

    public static EntityPlayer getPlayer(){
        return Minecraft.getMinecraft().player;
    }

}
