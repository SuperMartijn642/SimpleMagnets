package com.supermartijn642.simplemagnets;

import com.supermartijn642.simplemagnets.gui.DemagnetizationCoilContainer;
import com.supermartijn642.simplemagnets.gui.FilteredDemagnetizationCoilContainer;
import com.supermartijn642.simplemagnets.gui.MagnetContainer;
import com.supermartijn642.simplemagnets.packets.demagnetization_coil.*;
import com.supermartijn642.simplemagnets.packets.magnet.*;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.ObjectHolder;
import top.theillusivec4.curios.api.CuriosAPI;
import top.theillusivec4.curios.api.imc.CurioIMCMessage;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
@Mod("simplemagnets")
public class SimpleMagnets {

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation("simplemagnets", "main"), () -> "1", "1"::equals, "1"::equals);

    @ObjectHolder("simplemagnets:basicmagnet")
    public static Item simple_magnet;
    @ObjectHolder("simplemagnets:advancedmagnet")
    public static Item advanced_magnet;
    @ObjectHolder("simplemagnets:basic_demagnetization_coil")
    public static Block basic_demagnetization_coil;
    @ObjectHolder("simplemagnets:advanced_demagnetization_coil")
    public static Block advanced_demagnetization_coil;

    @ObjectHolder("simplemagnets:basic_demagnetization_coil_tile")
    public static TileEntityType<?> basic_demagnetization_coil_tile;
    @ObjectHolder("simplemagnets:advanced_demagnetization_coil_tile")
    public static TileEntityType<?> advanced_demagnetization_coil_tile;

    @ObjectHolder("simplemagnets:container")
    public static ContainerType<MagnetContainer> container;
    @ObjectHolder("simplemagnets:demagnetization_coil_container")
    public static ContainerType<DemagnetizationCoilContainer> demagnetization_coil_container;
    @ObjectHolder("simplemagnets:filtered_demagnetization_coil_container")
    public static ContainerType<FilteredDemagnetizationCoilContainer> filtered_demagnetization_coil_container;

    public SimpleMagnets(){
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::interModEnqueue);

        // magnets
        CHANNEL.registerMessage(0, PacketToggleItems.class, PacketToggleItems::encode, PacketToggleItems::decode, PacketToggleItems::handle);
        CHANNEL.registerMessage(1, PacketIncreaseItemRange.class, PacketIncreaseItemRange::encode, PacketIncreaseItemRange::decode, PacketIncreaseItemRange::handle);
        CHANNEL.registerMessage(2, PacketDecreaseItemRange.class, PacketDecreaseItemRange::encode, PacketDecreaseItemRange::decode, PacketDecreaseItemRange::handle);
        CHANNEL.registerMessage(3, PacketToggleXp.class, PacketToggleXp::encode, PacketToggleXp::decode, PacketToggleXp::handle);
        CHANNEL.registerMessage(4, PacketIncreaseXpRange.class, PacketIncreaseXpRange::encode, PacketIncreaseXpRange::decode, PacketIncreaseXpRange::handle);
        CHANNEL.registerMessage(5, PacketDecreaseXpRange.class, PacketDecreaseXpRange::encode, PacketDecreaseXpRange::decode, PacketDecreaseXpRange::handle);
        CHANNEL.registerMessage(6, PacketToggleMagnetWhitelist.class, PacketToggleMagnetWhitelist::encode, PacketToggleMagnetWhitelist::decode, PacketToggleMagnetWhitelist::handle);
        CHANNEL.registerMessage(7, PacketToggleMagnet.class, PacketToggleMagnet::encode, PacketToggleMagnet::decode, PacketToggleMagnet::handle);
        CHANNEL.registerMessage(8, PacketItemInfo.class, PacketItemInfo::encode, PacketItemInfo::decode, PacketItemInfo::handle);
        CHANNEL.registerMessage(9, PacketToggleMagnetMessage.class, PacketToggleMagnetMessage::encode, PacketToggleMagnetMessage::decode, PacketToggleMagnetMessage::handle);
        CHANNEL.registerMessage(10, PacketToggleMagnetDurability.class, PacketToggleMagnetDurability::encode, PacketToggleMagnetDurability::decode, PacketToggleMagnetDurability::handle);

        // demagnetization coils
        CHANNEL.registerMessage(11, PacketDecreaseXRange.class, PacketDecreaseXRange::encode, PacketDecreaseXRange::decode, PacketDecreaseXRange::handle);
        CHANNEL.registerMessage(12, PacketDecreaseYRange.class, PacketDecreaseYRange::encode, PacketDecreaseYRange::decode, PacketDecreaseYRange::handle);
        CHANNEL.registerMessage(13, PacketDecreaseZRange.class, PacketDecreaseZRange::encode, PacketDecreaseZRange::decode, PacketDecreaseZRange::handle);
        CHANNEL.registerMessage(14, PacketIncreaseXRange.class, PacketIncreaseXRange::encode, PacketIncreaseXRange::decode, PacketIncreaseXRange::handle);
        CHANNEL.registerMessage(15, PacketIncreaseYRange.class, PacketIncreaseYRange::encode, PacketIncreaseYRange::decode, PacketIncreaseYRange::handle);
        CHANNEL.registerMessage(16, PacketIncreaseZRange.class, PacketIncreaseZRange::encode, PacketIncreaseZRange::decode, PacketIncreaseZRange::handle);
        CHANNEL.registerMessage(17, PacketToggleDurability.class, PacketToggleDurability::encode, PacketToggleDurability::decode, PacketToggleDurability::handle);
        CHANNEL.registerMessage(18, PacketToggleWhitelist.class, PacketToggleWhitelist::encode, PacketToggleWhitelist::decode, PacketToggleWhitelist::handle);
    }

    public void init(FMLCommonSetupEvent e){
        DistExecutor.runWhenOn(Dist.CLIENT, () -> ClientProxy::init);
        DistExecutor.runWhenOn(Dist.CLIENT, () -> ClientProxy::registerScreen);
    }

    public void interModEnqueue(InterModEnqueueEvent e){
        InterModComms.sendTo("curios", CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("charm").setSize(1).setEnabled(true));
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onItemRegistry(final RegistryEvent.Register<Item> e){
            e.getRegistry().register(new BasicMagnet());
            e.getRegistry().register(new AdvancedMagnet());
            e.getRegistry().register(new BlockItem(basic_demagnetization_coil, new Item.Properties().group(ItemGroup.SEARCH)).setRegistryName(basic_demagnetization_coil.getRegistryName()));
            e.getRegistry().register(new BlockItem(advanced_demagnetization_coil, new Item.Properties().group(ItemGroup.SEARCH)).setRegistryName(advanced_demagnetization_coil.getRegistryName()));
        }

        @SubscribeEvent
        public static void onItemBlock(final RegistryEvent.Register<Block> e){
            e.getRegistry().register(new DemagnetizationCoilBlock("basic_demagnetization_coil", SMConfig.basicCoilMaxRange, SMConfig.basicCoilFilter, DemagnetizationCoilTile.BasicDemagnetizationCoilTile::new));
            e.getRegistry().register(new DemagnetizationCoilBlock("advanced_demagnetization_coil", SMConfig.advancedCoilMaxRange, SMConfig.advancedCoilFilter, DemagnetizationCoilTile.AdvancedDemagnetizationCoilTile::new));
        }

        @SubscribeEvent
        public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> e){
            e.getRegistry().register(TileEntityType.Builder.create(DemagnetizationCoilTile.BasicDemagnetizationCoilTile::new, basic_demagnetization_coil).build(null).setRegistryName("basic_demagnetization_coil_tile"));
            e.getRegistry().register(TileEntityType.Builder.create(DemagnetizationCoilTile.AdvancedDemagnetizationCoilTile::new, advanced_demagnetization_coil).build(null).setRegistryName("advanced_demagnetization_coil_tile"));
        }

        @SubscribeEvent
        public static void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> e){
            e.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> new MagnetContainer(windowId, inv.player, data.readInt())).setRegistryName("container"));
            e.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> new DemagnetizationCoilContainer(windowId, inv.player, data.readBlockPos())).setRegistryName("demagnetization_coil_container"));
            e.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> new FilteredDemagnetizationCoilContainer(windowId, inv.player, data.readBlockPos())).setRegistryName("filtered_demagnetization_coil_container"));
        }
    }

}
