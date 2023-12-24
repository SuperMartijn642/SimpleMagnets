package com.supermartijn642.simplemagnets.generators;

import com.supermartijn642.core.generator.RecipeGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.simplemagnets.SimpleMagnets;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;

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
            .input('A', Tags.Items.INGOTS_IRON)
            .input('B', Tags.Items.GEMS_LAPIS)
            .input('C', Tags.Items.ENDER_PEARLS)
            .input('D', Tags.Items.DUSTS_REDSTONE)
            .unlockedBy(Tags.Items.INGOTS_IRON);
        this.shaped(SimpleMagnets.advanced_magnet)
            .pattern("AAB")
            .pattern("CDE")
            .pattern("AAF")
            .input('A', Tags.Items.INGOTS_GOLD)
            .input('B', Tags.Items.GEMS_LAPIS)
            .input('C', SimpleMagnets.simple_magnet)
            .input('D', Items.ENDER_EYE)
            .input('E', Tags.Items.GEMS_DIAMOND)
            .input('F', Tags.Items.DUSTS_REDSTONE)
            .unlockedBy(SimpleMagnets.simple_magnet);
        this.shaped(SimpleMagnets.basic_demagnetization_coil)
            .pattern(" A ")
            .pattern("BCB")
            .pattern("CCC")
            .input('A', Tags.Items.INGOTS_GOLD)
            .input('B', Tags.Items.DUSTS_REDSTONE)
            .input('C', Tags.Items.INGOTS_IRON)
            .unlockedBy(Tags.Items.INGOTS_IRON);
        this.shaped(SimpleMagnets.advanced_demagnetization_coil)
            .pattern(" A ")
            .pattern("BCB")
            .pattern("CDC")
            .input('A', Tags.Items.DUSTS_GLOWSTONE)
            .input('B', Tags.Items.DUSTS_REDSTONE)
            .input('C', Tags.Items.INGOTS_GOLD)
            .input('D', SimpleMagnets.basic_demagnetization_coil)
            .unlockedBy(SimpleMagnets.basic_demagnetization_coil);
    }
}
