package com.supermartijn642.simplemagnets.generators;

import com.supermartijn642.core.generator.ItemInfoGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.simplemagnets.SimpleMagnets;

/**
 * Created 23/12/2024 by SuperMartijn642
 */
public class SimpleMagnetsItemInfoGenerator extends ItemInfoGenerator {

    public SimpleMagnetsItemInfoGenerator(ResourceCache cache){
        super("simplemagnets", cache);
    }

    @Override
    public void generate(){
        this.simpleInfo(SimpleMagnets.simple_magnet, "item/basicmagnet");
        this.simpleInfo(SimpleMagnets.advanced_magnet, "item/advancedmagnet");
        this.simpleInfo(SimpleMagnets.basic_demagnetization_coil, "basic_demagnetization_coil");
        this.simpleInfo(SimpleMagnets.advanced_demagnetization_coil, "advanced_demagnetization_coil");
    }
}
