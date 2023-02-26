package com.supermartijn642.simplemagnets.integration;

import com.supermartijn642.simplemagnets.SimpleMagnets;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * Created 26/02/2023 by SuperMartijn642
 */
public class TrinketsIntegration {

    public static void initialize(){
        // Make sure magnet items get ticked when in a trinket slot
        Trinket magnetTrinket = new Trinket() {
            @Override
            public void tick(ItemStack stack, SlotReference slot, LivingEntity entity){
                stack.inventoryTick(entity.getLevel(), entity, -1, false);
            }
        };
        TrinketsApi.registerTrinket(SimpleMagnets.simple_magnet, magnetTrinket);
        TrinketsApi.registerTrinket(SimpleMagnets.advanced_magnet, magnetTrinket);
    }
}
