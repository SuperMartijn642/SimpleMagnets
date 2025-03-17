package com.supermartijn642.simplemagnets.generators;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import top.theillusivec4.curios.api.CuriosDataProvider;

import java.util.concurrent.CompletableFuture;

/**
 * Created 17/03/2025 by SuperMartijn642
 */
public class SimpleMagnetsCuriosDataProvider extends CuriosDataProvider {
    public SimpleMagnetsCuriosDataProvider(PackOutput output, ExistingFileHelper fileHelper, CompletableFuture<HolderLookup.Provider> registries){
        super("simplemagnets", output, fileHelper, registries);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper fileHelper){
        this.createSlot("charm").addCosmetic(false).size(1);
        this.createEntities("player_charm_slot").addPlayer().addSlots("charm");
    }
}
