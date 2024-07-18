package com.supermartijn642.simplemagnets.generators;

import com.supermartijn642.core.generator.ModelGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.simplemagnets.SimpleMagnets;
import net.minecraft.resources.ResourceLocation;

/**
 * Created 14/09/2022 by SuperMartijn642
 */
public class SimpleMagnetsModelGenerator extends ModelGenerator {

    public SimpleMagnetsModelGenerator(ResourceCache cache){
        super("simplemagnets", cache);
    }

    @Override
    public void generate(){
        // Magnets
        this.itemGenerated(SimpleMagnets.simple_magnet, ResourceLocation.fromNamespaceAndPath("simplemagnets", "basicmagnet"));
        this.itemGenerated(SimpleMagnets.advanced_magnet, ResourceLocation.fromNamespaceAndPath("simplemagnets", "advancedmagnet"));
        // Demagnetization coils
        this.model("item/basic_demagnetization_coil").parent("basic_demagnetization_coil");
        this.model("item/advanced_demagnetization_coil").parent("advanced_demagnetization_coil");
    }
}
