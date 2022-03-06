package com.supermartijn642.simplemagnets;

import com.supermartijn642.core.network.PacketChannel;
import com.supermartijn642.simplemagnets.integration.BaublesActive;
import com.supermartijn642.simplemagnets.integration.BaublesInactive;
import com.supermartijn642.simplemagnets.packets.demagnetization_coil.*;
import com.supermartijn642.simplemagnets.packets.magnet.*;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
@Mod(modid = SimpleMagnets.MODID, name = SimpleMagnets.NAME, version = SimpleMagnets.VERSION, dependencies = SimpleMagnets.DEPENDENCIES)
public class SimpleMagnets {

    public static final String MODID = "simplemagnets";
    public static final String NAME = "Simple Magnets";
    public static final String VERSION = "1.1.6";
    public static final String DEPENDENCIES = "required-after:forge@[14.23.5.2779,);required-after:supermartijn642corelib@[1.0.16,1.1.0);required-after:supermartijn642configlib@[1.0.9,)";

    public static PacketChannel CHANNEL = PacketChannel.create("simplemagnets");

    public static CreativeTabs GROUP = new CreativeTabs("simplemagnets") {
        @Override
        public ItemStack getTabIconItem(){
            return new ItemStack(basic_magnet);
        }
    };

    @Mod.Instance
    public static SimpleMagnets instance;

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
        // magnets
        CHANNEL.registerMessage(PacketToggleItems.class, PacketToggleItems::new, true);
        CHANNEL.registerMessage(PacketIncreaseItemRange.class, PacketIncreaseItemRange::new, true);
        CHANNEL.registerMessage(PacketDecreaseItemRange.class, PacketDecreaseItemRange::new, true);
        CHANNEL.registerMessage(PacketToggleXp.class, PacketToggleXp::new, true);
        CHANNEL.registerMessage(PacketIncreaseXpRange.class, PacketIncreaseXpRange::new, true);
        CHANNEL.registerMessage(PacketDecreaseXpRange.class, PacketDecreaseXpRange::new, true);
        CHANNEL.registerMessage(PacketToggleMagnetWhitelist.class, PacketToggleMagnetWhitelist::new, true);
        CHANNEL.registerMessage(PacketToggleMagnet.class, PacketToggleMagnet::new, true);
        CHANNEL.registerMessage(PacketItemInfo.class, PacketItemInfo::new, false);
        CHANNEL.registerMessage(PacketToggleMagnetMessage.class, PacketToggleMagnetMessage::new, true);
        CHANNEL.registerMessage(PacketToggleMagnetDurability.class, PacketToggleMagnetDurability::new, true);

        // demagnetization coils
        CHANNEL.registerMessage(PacketDecreaseXRange.class, PacketDecreaseXRange::new, true);
        CHANNEL.registerMessage(PacketDecreaseYRange.class, PacketDecreaseYRange::new, true);
        CHANNEL.registerMessage(PacketDecreaseZRange.class, PacketDecreaseZRange::new, true);
        CHANNEL.registerMessage(PacketIncreaseXRange.class, PacketIncreaseXRange::new, true);
        CHANNEL.registerMessage(PacketIncreaseYRange.class, PacketIncreaseYRange::new, true);
        CHANNEL.registerMessage(PacketIncreaseZRange.class, PacketIncreaseZRange::new, true);
        CHANNEL.registerMessage(PacketToggleDurability.class, PacketToggleDurability::new, true);
        CHANNEL.registerMessage(PacketToggleWhitelist.class, PacketToggleWhitelist::new, true);

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
