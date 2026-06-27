package com.supermartijn642.simplemagnets.packets.magnet;

import com.supermartijn642.core.CommonUtils;
import com.supermartijn642.core.network.BasePacket;
import com.supermartijn642.core.network.PacketContext;
import com.supermartijn642.simplemagnets.MagnetItem;
import com.supermartijn642.simplemagnets.integration.TrinketsIntegration;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created 7/8/2020 by SuperMartijn642
 */
public class PacketToggleMagnet implements BasePacket {

    @Override
    public void write(FriendlyByteBuf buffer){
    }

    @Override
    public void read(FriendlyByteBuf buffer){
    }

    @Override
    public void handle(PacketContext context){
        Player player = context.getPlayer();
        if(player != null){
            Consumer<Function<ItemStack,ItemStack>> access = findStack(player);
            if(access != null){
                access.accept(stack -> {
                    MagnetItem.toggleMagnet(player, stack);
                    return stack;
                });
            }
        }
    }

    private static Consumer<Function<ItemStack,ItemStack>> findStack(Player player){
        if(CommonUtils.isModLoaded("trinkets")){
            Consumer<Function<ItemStack,ItemStack>> access = TrinketsIntegration.findMagnet(player);
            if(access != null)
                return access;
        }
        Inventory inventory = player.getInventory();
        for(int slot = 0; slot < inventory.getContainerSize(); slot++){
            ItemStack stack = inventory.getItem(slot);
            if(!stack.isEmpty() && stack.getItem() instanceof MagnetItem){
                int finalSlot = slot;
                return updater -> inventory.setItem(finalSlot, updater.apply(stack.copy()));
            }
        }
        return null;
    }
}
