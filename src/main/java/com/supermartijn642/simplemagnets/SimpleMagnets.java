package com.supermartijn642.simplemagnets;

import com.supermartijn642.core.CommonUtils;
import com.supermartijn642.core.block.BaseBlock;
import com.supermartijn642.core.block.BaseBlockEntityType;
import com.supermartijn642.core.gui.BaseContainerType;
import com.supermartijn642.core.item.BaseBlockItem;
import com.supermartijn642.core.item.BaseItem;
import com.supermartijn642.core.item.CreativeItemGroup;
import com.supermartijn642.core.item.ItemProperties;
import com.supermartijn642.core.network.PacketChannel;
import com.supermartijn642.core.registry.GeneratorRegistrationHandler;
import com.supermartijn642.core.registry.RegistrationHandler;
import com.supermartijn642.core.registry.RegistryEntryAcceptor;
import com.supermartijn642.simplemagnets.generators.*;
import com.supermartijn642.simplemagnets.gui.DemagnetizationCoilContainer;
import com.supermartijn642.simplemagnets.gui.FilteredDemagnetizationCoilContainer;
import com.supermartijn642.simplemagnets.gui.MagnetContainer;
import com.supermartijn642.simplemagnets.integration.TrinketsIntegration;
import com.supermartijn642.simplemagnets.packets.demagnetization_coil.*;
import com.supermartijn642.simplemagnets.packets.magnet.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerBlockEntityEvents;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public class SimpleMagnets implements ModInitializer {

    public static final PacketChannel CHANNEL = PacketChannel.create("simplemagnets");

    @RegistryEntryAcceptor(namespace = "simplemagnets", identifier = "basicmagnet", registry = RegistryEntryAcceptor.Registry.ITEMS)
    public static BaseItem simple_magnet;
    @RegistryEntryAcceptor(namespace = "simplemagnets", identifier = "advancedmagnet", registry = RegistryEntryAcceptor.Registry.ITEMS)
    public static BaseItem advanced_magnet;
    @RegistryEntryAcceptor(namespace = "simplemagnets", identifier = "basic_demagnetization_coil", registry = RegistryEntryAcceptor.Registry.BLOCKS)
    public static BaseBlock basic_demagnetization_coil;
    @RegistryEntryAcceptor(namespace = "simplemagnets", identifier = "advanced_demagnetization_coil", registry = RegistryEntryAcceptor.Registry.BLOCKS)
    public static BaseBlock advanced_demagnetization_coil;

    @RegistryEntryAcceptor(namespace = "simplemagnets", identifier = "basic_demagnetization_coil_tile", registry = RegistryEntryAcceptor.Registry.BLOCK_ENTITY_TYPES)
    public static BaseBlockEntityType<?> basic_demagnetization_coil_tile;
    @RegistryEntryAcceptor(namespace = "simplemagnets", identifier = "advanced_demagnetization_coil_tile", registry = RegistryEntryAcceptor.Registry.BLOCK_ENTITY_TYPES)
    public static BaseBlockEntityType<?> advanced_demagnetization_coil_tile;

    @RegistryEntryAcceptor(namespace = "simplemagnets", identifier = "container", registry = RegistryEntryAcceptor.Registry.MENU_TYPES)
    public static BaseContainerType<MagnetContainer> magnet_container;
    @RegistryEntryAcceptor(namespace = "simplemagnets", identifier = "demagnetization_coil_container", registry = RegistryEntryAcceptor.Registry.MENU_TYPES)
    public static BaseContainerType<DemagnetizationCoilContainer> demagnetization_coil_container;
    @RegistryEntryAcceptor(namespace = "simplemagnets", identifier = "filtered_demagnetization_coil_container", registry = RegistryEntryAcceptor.Registry.MENU_TYPES)
    public static BaseContainerType<FilteredDemagnetizationCoilContainer> filtered_demagnetization_coil_container;

    public static final CreativeItemGroup GROUP = CreativeItemGroup.create("simplemagnets", () -> simple_magnet);

    @Override
    public void onInitialize(){
        ItemSpawnHandler.registerEventListeners();
        ServerBlockEntityEvents.BLOCK_ENTITY_LOAD.register((entity, world) -> {
            if(entity instanceof DemagnetizationCoilBlockEntity)
                ((DemagnetizationCoilBlockEntity)entity).onLoad();
        });

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

        register();
        registerGenerators();
    }

    private static void register(){
        RegistrationHandler handler = RegistrationHandler.get("simplemagnets");
        // Items
        handler.registerItem("basicmagnet", BasicMagnet::new);
        handler.registerItem("advancedmagnet", AdvancedMagnet::new);
        handler.registerItem("basic_demagnetization_coil", () -> new BaseBlockItem(basic_demagnetization_coil, ItemProperties.create().group(GROUP)));
        handler.registerItem("advanced_demagnetization_coil", () -> new BaseBlockItem(advanced_demagnetization_coil, ItemProperties.create().group(GROUP)));
        // Blocks
        handler.registerBlock("basic_demagnetization_coil", () -> new DemagnetizationCoilBlock(SMConfig.basicCoilMaxRange, SMConfig.basicCoilFilter, () -> basic_demagnetization_coil_tile));
        handler.registerBlock("advanced_demagnetization_coil", () -> new DemagnetizationCoilBlock(SMConfig.advancedCoilMaxRange, SMConfig.advancedCoilFilter, () -> advanced_demagnetization_coil_tile));
        // Block entity types
        handler.registerBlockEntityType("basic_demagnetization_coil_tile", () -> BaseBlockEntityType.create(DemagnetizationCoilBlockEntity.BasicDemagnetizationCoilBlockEntity::new, basic_demagnetization_coil));
        handler.registerBlockEntityType("advanced_demagnetization_coil_tile", () -> BaseBlockEntityType.create(DemagnetizationCoilBlockEntity.AdvancedDemagnetizationCoilBlockEntity::new, advanced_demagnetization_coil));
        // Container types
        handler.registerMenuType("container", BaseContainerType.create((container, data) -> data.writeInt(container.slot), (player, data) -> new MagnetContainer(player, data.readInt())));
        handler.registerMenuType("demagnetization_coil_container", BaseContainerType.create((container, data) -> data.writeBlockPos(container.getBlockEntityPos()), (player, data) -> new DemagnetizationCoilContainer(player, data.readBlockPos())));
        handler.registerMenuType("filtered_demagnetization_coil_container", BaseContainerType.create((container, data) -> data.writeBlockPos(container.getBlockEntityPos()), (player, data) -> new FilteredDemagnetizationCoilContainer(player, data.readBlockPos())));

        // Add trinkets
        if(CommonUtils.isModLoaded("trinkets"))
            handler.registerItemCallback(helper -> TrinketsIntegration.initialize());
    }

    private static void registerGenerators(){
        GeneratorRegistrationHandler handler = GeneratorRegistrationHandler.get("simplemagnets");
        handler.addGenerator(SimpleMagnetsModelGenerator::new);
        handler.addGenerator(SimpleMagnetsBlockStateGenerator::new);
        handler.addGenerator(SimpleMagnetsLanguageGenerator::new);
        handler.addGenerator(SimpleMagnetsLootTableGenerator::new);
        handler.addGenerator(SimpleMagnetsRecipeGenerator::new);
        handler.addGenerator(SimpleMagnetsTagGenerator::new);
    }
}
