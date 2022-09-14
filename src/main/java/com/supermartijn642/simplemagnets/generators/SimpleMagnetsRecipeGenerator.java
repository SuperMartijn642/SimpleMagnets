package com.supermartijn642.simplemagnets.generators;

import com.supermartijn642.core.generator.RecipeGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.simplemagnets.SimpleMagnets;
import net.minecraft.init.Items;

/**
 * Created 14/09/2022 by SuperMartijn642
 */
public class SimpleMagnetsRecipeGenerator extends RecipeGenerator {

    public SimpleMagnetsRecipeGenerator(ResourceCache cache){
        super("simplemagnets", cache);
    }

    @Override
    public void generate(){
        this.shaped(SimpleMagnets.simple_magnet)
            .pattern("AAB")
            .pattern("AC ")
            .pattern("AAD")
            .input('A', "ingotIron")
            .input('B', "gemLapis")
            .input('C', "enderpearl")
            .input('D', "dustRedstone")
            .unlockedByOreDict("ingotIron");
        this.shaped(SimpleMagnets.advanced_magnet)
            .pattern("AAB")
            .pattern("CDE")
            .pattern("AAF")
            .input('A', "ingotGold")
            .input('B', "gemLapis")
            .input('C', SimpleMagnets.simple_magnet)
            .input('D', Items.ENDER_EYE)
            .input('E', "gemDiamond")
            .input('F', "dustRedstone")
            .unlockedBy(SimpleMagnets.simple_magnet);
        this.shaped(SimpleMagnets.basic_demagnetization_coil.asItem())
            .pattern(" A ")
            .pattern("BCB")
            .pattern("CCC")
            .input('A', "ingotGold")
            .input('B', "dustRedstone")
            .input('C', "ingotIron")
            .unlockedByOreDict("ingotIron");
        this.shaped(SimpleMagnets.advanced_demagnetization_coil.asItem())
            .pattern(" A ")
            .pattern("BCB")
            .pattern("CDC")
            .input('A', "dustGlowstone")
            .input('B', "dustRedstone")
            .input('C', "ingotGold")
            .input('D', SimpleMagnets.basic_demagnetization_coil.asItem())
            .unlockedBy(SimpleMagnets.basic_demagnetization_coil.asItem());
    }
}
