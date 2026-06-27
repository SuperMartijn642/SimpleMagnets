package com.supermartijn642.simplemagnets.integration;

import com.supermartijn642.simplemagnets.MagnetItem;
import com.supermartijn642.simplemagnets.SimpleMagnets;
import eu.pb4.trinkets.api.TrinketAttachment;
import eu.pb4.trinkets.api.TrinketSlotAccess;
import eu.pb4.trinkets.api.TrinketsApi;
import eu.pb4.trinkets.api.callback.TrinketCallback;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created 26/02/2023 by SuperMartijn642
 */
public class TrinketsIntegration {

    public static void initialize(){
        // Make sure magnet items get ticked when in a trinket slot
        TrinketCallback magnetTrinket = new TrinketCallback() {
            @Override
            public void tick(ItemStack stack, TrinketSlotAccess slot, LivingEntity entity){
                stack.inventoryTick(entity.level(), entity, null);
            }
        };
        TrinketCallback.setCallback(SimpleMagnets.simple_magnet, magnetTrinket);
        TrinketCallback.setCallback(SimpleMagnets.advanced_magnet, magnetTrinket);
    }

    public static Consumer<Function<ItemStack,ItemStack>> findMagnet(Player player){
        TrinketAttachment attachment = TrinketsApi.getAttachment(player);
        if(attachment == null)
            return null;
        TrinketSlotAccess slot = attachment.findFirst(stack -> stack.getItem() instanceof MagnetItem).orElse(null);
        if(slot == null)
            return null;
        return updater -> slot.set(updater.apply(slot.get().copy()));
    }
}
