package com.supermartijn642.simplemagnets.generators;

import com.supermartijn642.core.generator.RecipeGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.simplemagnets.SimpleMagnets;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.world.item.Items;

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
            .input('A', ConventionalItemTags.IRON_INGOTS)
            .input('B', ConventionalItemTags.LAPIS)
            .input('C', Items.ENDER_PEARL)
            .input('D', ConventionalItemTags.REDSTONE_DUSTS)
            .unlockedBy(ConventionalItemTags.IRON_INGOTS);
        this.shaped(SimpleMagnets.advanced_magnet)
            .pattern("AAB")
            .pattern("CDE")
            .pattern("AAF")
            .input('A', ConventionalItemTags.GOLD_INGOTS)
            .input('B', ConventionalItemTags.LAPIS)
            .input('C', SimpleMagnets.simple_magnet)
            .input('D', Items.ENDER_EYE)
            .input('E', ConventionalItemTags.DIAMONDS)
            .input('F', ConventionalItemTags.REDSTONE_DUSTS)
            .unlockedBy(SimpleMagnets.simple_magnet);
        this.shaped(SimpleMagnets.basic_demagnetization_coil)
            .pattern(" A ")
            .pattern("BCB")
            .pattern("CCC")
            .input('A', ConventionalItemTags.GOLD_INGOTS)
            .input('B', ConventionalItemTags.REDSTONE_DUSTS)
            .input('C', ConventionalItemTags.IRON_INGOTS)
            .unlockedBy(ConventionalItemTags.IRON_INGOTS);
        this.shaped(SimpleMagnets.advanced_demagnetization_coil)
            .pattern(" A ")
            .pattern("BCB")
            .pattern("CDC")
            .input('A', Items.GLOWSTONE_DUST)
            .input('B', ConventionalItemTags.REDSTONE_DUSTS)
            .input('C', ConventionalItemTags.GOLD_INGOTS)
            .input('D', SimpleMagnets.basic_demagnetization_coil)
            .unlockedBy(SimpleMagnets.basic_demagnetization_coil);
    }
}
