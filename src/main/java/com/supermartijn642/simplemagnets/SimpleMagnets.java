package com.supermartijn642.simplemagnets;

import com.supermartijn642.simplemagnets.integration.BaublesActive;
import com.supermartijn642.simplemagnets.integration.BaublesInactive;
import com.supermartijn642.simplemagnets.packets.*;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
@Mod(modid = SimpleMagnets.MODID, name = SimpleMagnets.NAME, version = SimpleMagnets.VERSION)
public class SimpleMagnets {

    public static final String MODID = "simplemagnets";
    public static final String NAME = "Simple Magnets";
    public static final String VERSION = "1.0.8b";

    @Mod.Instance
    public static SimpleMagnets instance;

    public static SimpleNetworkWrapper channel;

    public static BaublesInactive baubles;

    @GameRegistry.ObjectHolder("simplemagnets:basicmagnet")
    public static Item basic_magnet;
    @GameRegistry.ObjectHolder("simplemagnets:advancedmagnet")
    public static Item advanced_magnet;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e){
        channel = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

        channel.registerMessage(PacketToggleItems.class, PacketToggleItems.class, 0, Side.SERVER);
        channel.registerMessage(PacketIncreaseItemRange.class, PacketIncreaseItemRange.class, 1, Side.SERVER);
        channel.registerMessage(PacketDecreaseItemRange.class, PacketDecreaseItemRange.class, 2, Side.SERVER);
        channel.registerMessage(PacketToggleXp.class, PacketToggleXp.class, 3, Side.SERVER);
        channel.registerMessage(PacketIncreaseXpRange.class, PacketIncreaseXpRange.class, 4, Side.SERVER);
        channel.registerMessage(PacketDecreaseXpRange.class, PacketDecreaseXpRange.class, 5, Side.SERVER);
        channel.registerMessage(PacketToggleWhitelist.class, PacketToggleWhitelist.class, 6, Side.SERVER);
        channel.registerMessage(PacketToggleMagnet.class, PacketToggleMagnet.class, 7, Side.SERVER);

        baubles = Loader.isModLoaded("baubles") ? new BaublesActive() : new BaublesInactive();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e){
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

        if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
            ClientProxy.init();
    }

    @Mod.EventBusSubscriber
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onItemRegistry(final RegistryEvent.Register<Item> e){
            e.getRegistry().register(new BasicMagnet());
            e.getRegistry().register(new AdvancedMagnet());
        }
    }

}
