package com.supermartijn642.simplemagnets;

import com.supermartijn642.simplemagnets.integration.BaublesActive;
import com.supermartijn642.simplemagnets.integration.BaublesInactive;
import com.supermartijn642.simplemagnets.packets.demagnetization_coil.*;
import com.supermartijn642.simplemagnets.packets.magnet.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
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
@Mod(modid = SimpleMagnets.MODID, name = SimpleMagnets.NAME, version = SimpleMagnets.VERSION, dependencies = SimpleMagnets.DEPENDENCIES)
public class SimpleMagnets {

    public static final String MODID = "simplemagnets";
    public static final String NAME = "Simple Magnets";
    public static final String VERSION = "1.1.0";
    public static final String DEPENDENCIES = "required-after:forge@[14.23.5.2779,);required-after:supermartijn642configlib@[1.0.5,)";

    @Mod.Instance
    public static SimpleMagnets instance;

    public static SimpleNetworkWrapper channel;

    public static BaublesInactive baubles;

    @GameRegistry.ObjectHolder("simplemagnets:basicmagnet")
    public static Item basic_magnet;
    @GameRegistry.ObjectHolder("simplemagnets:advancedmagnet")
    public static Item advanced_magnet;
    @GameRegistry.ObjectHolder("simplemagnets:basic_demagnetization_coil")
    public static Block basic_demagnetization_coil;
    @GameRegistry.ObjectHolder("simplemagnets:advanced_demagnetization_coil")
    public static Block advanced_demagnetization_coil;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e){
        channel = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

        // magnets
        channel.registerMessage(PacketToggleItems.class, PacketToggleItems.class, 0, Side.SERVER);
        channel.registerMessage(PacketIncreaseItemRange.class, PacketIncreaseItemRange.class, 1, Side.SERVER);
        channel.registerMessage(PacketDecreaseItemRange.class, PacketDecreaseItemRange.class, 2, Side.SERVER);
        channel.registerMessage(PacketToggleXp.class, PacketToggleXp.class, 3, Side.SERVER);
        channel.registerMessage(PacketIncreaseXpRange.class, PacketIncreaseXpRange.class, 4, Side.SERVER);
        channel.registerMessage(PacketDecreaseXpRange.class, PacketDecreaseXpRange.class, 5, Side.SERVER);
        channel.registerMessage(PacketToggleMagnetWhitelist.class, PacketToggleMagnetWhitelist.class, 6, Side.SERVER);
        channel.registerMessage(PacketToggleMagnet.class, PacketToggleMagnet.class, 7, Side.SERVER);
        channel.registerMessage(PacketItemInfo.class, PacketItemInfo.class, 8, Side.CLIENT);
        channel.registerMessage(PacketToggleMagnetMessage.class, PacketToggleMagnetMessage.class, 9, Side.CLIENT);
        channel.registerMessage(PacketToggleMagnetDurability.class, PacketToggleMagnetDurability.class, 10, Side.SERVER);

        // demagnetization coils
        channel.registerMessage(PacketDecreaseXRange.class, PacketDecreaseXRange.class, 11, Side.SERVER);
        channel.registerMessage(PacketDecreaseYRange.class, PacketDecreaseYRange.class, 12, Side.SERVER);
        channel.registerMessage(PacketDecreaseZRange.class, PacketDecreaseZRange.class, 13, Side.SERVER);
        channel.registerMessage(PacketIncreaseXRange.class, PacketIncreaseXRange.class, 14, Side.SERVER);
        channel.registerMessage(PacketIncreaseYRange.class, PacketIncreaseYRange.class, 151, Side.SERVER);
        channel.registerMessage(PacketIncreaseZRange.class, PacketIncreaseZRange.class, 16, Side.SERVER);
        channel.registerMessage(PacketToggleDurability.class, PacketToggleDurability.class, 17, Side.SERVER);
        channel.registerMessage(PacketToggleWhitelist.class, PacketToggleWhitelist.class, 18, Side.SERVER);

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
            e.getRegistry().register(new ItemBlock(basic_demagnetization_coil).setRegistryName(basic_demagnetization_coil.getRegistryName()));
            e.getRegistry().register(new ItemBlock(advanced_demagnetization_coil).setRegistryName(advanced_demagnetization_coil.getRegistryName()));
        }

        @SubscribeEvent
        public static void onItemBlock(final RegistryEvent.Register<Block> e){
            e.getRegistry().register(new DemagnetizationCoilBlock("basic_demagnetization_coil", SMConfig.basicCoilMaxRange, SMConfig.basicCoilFilter, DemagnetizationCoilTile.BasicDemagnetizationCoilTile::new));
            e.getRegistry().register(new DemagnetizationCoilBlock("advanced_demagnetization_coil", SMConfig.advancedCoilMaxRange, SMConfig.advancedCoilFilter, DemagnetizationCoilTile.AdvancedDemagnetizationCoilTile::new));
            GameRegistry.registerTileEntity(DemagnetizationCoilTile.BasicDemagnetizationCoilTile.class, new ResourceLocation(MODID, "basic_demagnetization_coil_tile"));
            GameRegistry.registerTileEntity(DemagnetizationCoilTile.AdvancedDemagnetizationCoilTile.class, new ResourceLocation(MODID, "advanced_demagnetization_coil_tile"));
        }
    }

}
