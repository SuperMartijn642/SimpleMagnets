package com.supermartijn642.simplemagnets.generators;

import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.generator.TagGenerator;
import com.supermartijn642.simplemagnets.SimpleMagnets;

/**
 * Created 14/09/2022 by SuperMartijn642
 */
public class SimpleMagnetsTagGenerator extends TagGenerator {

    public SimpleMagnetsTagGenerator(ResourceCache cache){
        super("simplemagnets", cache);
    }

    @Override
    public void generate(){
        this.blockMineableWithPickaxe().add(SimpleMagnets.basic_demagnetization_coil).add(SimpleMagnets.advanced_demagnetization_coil);

        // Add the magnets to Trinkets' tags
        this.itemTag("trinkets", "hand/ring")
            .add(SimpleMagnets.simple_magnet)
            .add(SimpleMagnets.advanced_magnet);
        this.itemTag("trinkets", "offhand/ring")
            .add(SimpleMagnets.simple_magnet)
            .add(SimpleMagnets.advanced_magnet);
    }
}
