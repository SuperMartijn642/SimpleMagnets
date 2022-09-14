package com.supermartijn642.simplemagnets.generators;

import com.supermartijn642.core.generator.LootTableGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.simplemagnets.SimpleMagnets;

/**
 * Created 14/09/2022 by SuperMartijn642
 */
public class SimpleMagnetsLootTableGenerator extends LootTableGenerator {

    public SimpleMagnetsLootTableGenerator(ResourceCache cache){
        super("simplemagnets", cache);
    }

    @Override
    public void generate(){
        this.dropSelf(SimpleMagnets.basic_demagnetization_coil);
        this.dropSelf(SimpleMagnets.advanced_demagnetization_coil);
    }
}
