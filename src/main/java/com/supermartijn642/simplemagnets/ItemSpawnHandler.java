package com.supermartijn642.simplemagnets;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created 2/21/2021 by SuperMartijn642
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ItemSpawnHandler {

    private static final ItemSpawnHandler SERVER = new ItemSpawnHandler(), CLIENT = new ItemSpawnHandler();

    private static ItemSpawnHandler getInstance(Level world){
        return world.isClientSide ? CLIENT : SERVER;
    }

    public static void add(DemagnetizationCoilTile tile){
        if(tile == null || tile.isRemoved() || !tile.hasLevel())
            return;

        ItemSpawnHandler handler = getInstance(tile.getLevel());
        handler.tiles.putIfAbsent(tile.getLevel().dimension(), new LinkedList<>());
        handler.tiles.get(tile.getLevel().dimension()).add(new WeakReference<>(tile));
    }

    private final HashMap<ResourceKey<Level>,List<WeakReference<DemagnetizationCoilTile>>> tiles = new HashMap<>();

    @SubscribeEvent
    public static void onEntitySpawn(EntityJoinLevelEvent e){
        if(!(e.getEntity() instanceof ItemEntity))
            return;

        ItemEntity item = (ItemEntity)e.getEntity();

        ItemSpawnHandler handler = getInstance(e.getLevel());
        handler.tiles.putIfAbsent(e.getLevel().dimension(), new LinkedList<>());

        List<WeakReference<DemagnetizationCoilTile>> toRemove = new ArrayList<>();

        List<WeakReference<DemagnetizationCoilTile>> list = handler.tiles.get(e.getLevel().dimension());
        for(WeakReference<DemagnetizationCoilTile> reference : list){
            DemagnetizationCoilTile tile = reference.get();
            if(tile == null || tile.isRemoved() || !tile.hasLevel()){
                toRemove.add(reference);
                continue;
            }

            if(tile.getArea().contains(item.position()) && tile.shouldEffectItem(item.getItem())){
                item.getPersistentData().putBoolean("PreventRemoteMovement", true);
                item.getPersistentData().putBoolean("AllowMachineRemoteMovement", true);
            }
        }

        list.removeAll(toRemove);
    }

}
