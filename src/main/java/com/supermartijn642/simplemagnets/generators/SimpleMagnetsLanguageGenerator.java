package com.supermartijn642.simplemagnets.generators;

import com.supermartijn642.core.generator.LanguageGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.simplemagnets.SimpleMagnets;

/**
 * Created 14/09/2022 by SuperMartijn642
 */
public class SimpleMagnetsLanguageGenerator extends LanguageGenerator {

    public SimpleMagnetsLanguageGenerator(ResourceCache cache){
        super("simplemagnets", cache, "en_us");
    }

    @Override
    public void generate(){
        // Item group
        this.itemGroup(SimpleMagnets.GROUP, "Simple Magnets");

        // Magnets
        this.item(SimpleMagnets.simple_magnet, "Magnet");
        this.item(SimpleMagnets.advanced_magnet, "Advanced Magnet");
        this.translation("simplemagnets.basicmagnet.info", "Can pick up items and xp up to %d blocks away when activated");
        this.translation("simplemagnets.advancedmagnet.info", "Can pick up items and xp up to %d blocks away when activated, items can be filtered");
        this.translation("simplemagnets.magnets.toggle_message", "Magnet:");
        this.translation("simplemagnets.magnets.toggle_message.on", "On");
        this.translation("simplemagnets.magnets.toggle_message.off", "Off");

        // Demagnetization coils
        this.block(SimpleMagnets.basic_demagnetization_coil, "Demagnetization Coil");
        this.block(SimpleMagnets.advanced_demagnetization_coil, "Advanced Demagnetization Coil");
        this.translation("simplemagnets.demagnetization_coil.info.no_filter", "Prevents items up to %d blocks away from being attracted by magnets");
        this.translation("simplemagnets.demagnetization_coil.info.filtered", "Prevents items up to %d blocks away from being attracted by magnets, items can be filtered");

        // Magnet screen
        this.translation("simplemagnets.gui.magnet.title", "Advanced Magnet");
        this.translation("simplemagnets.gui.magnet.items", "Items");
        this.translation("simplemagnets.gui.magnet.xp", "Experience");
        this.translation("simplemagnets.gui.magnet.filter", "Item Filter");
        this.translation("simplemagnets.gui.magnet.items.on", "On");
        this.translation("simplemagnets.gui.magnet.items.off", "Off");
        this.translation("simplemagnets.gui.magnet.items.increase", "Increase Range");
        this.translation("simplemagnets.gui.magnet.items.decrease", "Decrease Range");
        this.translation("simplemagnets.gui.magnet.items.range", "Range (%1$dx%1$dx%1$d Blocks)");
        this.translation("simplemagnets.gui.magnet.xp.on", "On");
        this.translation("simplemagnets.gui.magnet.xp.off", "Off");
        this.translation("simplemagnets.gui.magnet.xp.increase", "Increase Range");
        this.translation("simplemagnets.gui.magnet.xp.decrease", "Decrease Range");
        this.translation("simplemagnets.gui.magnet.xp.range", "Range (%1$dx%1$dx%1$d Blocks)");
        this.translation("simplemagnets.gui.magnet.whitelist.on", "Whitelist");
        this.translation("simplemagnets.gui.magnet.whitelist.off", "Blacklist");

        // Demagnetization coil screen
        this.translation("simplemagnets.gui.demagnetization_coil.durability.on", "Match NBT");
        this.translation("simplemagnets.gui.demagnetization_coil.durability.off", "Ignore NBT");
        this.translation("simplemagnets.gui.demagnetization_coil.range.increase", "Increase Range");
        this.translation("simplemagnets.gui.demagnetization_coil.range.decrease", "Decrease Range");
        this.translation("simplemagnets.gui.demagnetization_coil.range", "Range (%1$dx%1$dx%1$d Blocks)");

        // Toggle key
        this.translation("simplemagnets.keys.category", "Simple Magnets");
        this.translation("simplemagnets.keys.toggle", "Toggle Magnet");
    }
}
