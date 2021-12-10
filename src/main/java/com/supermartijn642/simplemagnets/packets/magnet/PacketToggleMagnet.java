package com.supermartijn642.simplemagnets.packets.magnet;

import com.supermartijn642.simplemagnets.MagnetItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.network.NetworkEvent;
import top.theillusivec4.curios.api.capability.CuriosCapability;
import top.theillusivec4.curios.api.capability.ICurioItemHandler;
import top.theillusivec4.curios.api.inventory.CurioStackHandler;

import java.util.function.Supplier;

/**
 * Created 7/8/2020 by SuperMartijn642
 */
public class PacketToggleMagnet {

    public void encode(PacketBuffer buffer){
    }

    public static PacketToggleMagnet decode(PacketBuffer buffer){
        return new PacketToggleMagnet();
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier){
        contextSupplier.get().setPacketHandled(true);

        PlayerEntity player = contextSupplier.get().getSender();
        if(player != null){
            ItemStack stack = findStack(player);
            if(stack != null && !stack.isEmpty())
                MagnetItem.toggleMagnet(player, stack);
        }
    }

    private static ItemStack findStack(PlayerEntity player){
        ItemStack stack = findCuriosStack(player);
        if(stack != null && !stack.isEmpty() && stack.getItem() instanceof MagnetItem)
            return stack;

        for(int slot = 0; slot < player.inventory.getContainerSize(); slot++){
            stack = player.inventory.getItem(slot);
            if(!stack.isEmpty() && stack.getItem() instanceof MagnetItem)
                return stack;
        }

        return null;
    }

    private static ItemStack findCuriosStack(PlayerEntity player){
        if(ModList.get().isLoaded("curios")){
            ICurioItemHandler handler = player.getCapability(CuriosCapability.INVENTORY).orElse(null);
            if(handler != null){
                for(CurioStackHandler stackHandler : handler.getCurioMap().values()){
                    for(int slot = 0; slot < stackHandler.getSlots(); slot++){
                        ItemStack stack = stackHandler.getStackInSlot(slot);
                        if(stack.getItem() instanceof MagnetItem)
                            return stack;
                    }
                }
            }
        }

        return null;
    }

}
