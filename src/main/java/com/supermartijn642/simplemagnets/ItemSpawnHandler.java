package com.supermartijn642.simplemagnets;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
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

    private static ItemSpawnHandler getInstance(World world){
        return world.isRemote ? CLIENT : SERVER;
    }

    public static void add(DemagnetizationCoilTile tile){
        if(tile == null || tile.isRemoved() || !tile.hasWorld())
            return;

        ItemSpawnHandler handler = getInstance(tile.getWorld());
        handler.tiles.putIfAbsent(tile.getWorld().getDimension().getType(), new LinkedList<>());
        handler.tiles.get(tile.getWorld().getDimension().getType()).add(new WeakReference<>(tile));
    }

    private final HashMap<DimensionType,List<WeakReference<DemagnetizationCoilTile>>> tiles = new HashMap<>();

    @SubscribeEvent
    public static void onEntitySpawn(EntityJoinWorldEvent e){
        if(!(e.getEntity() instanceof ItemEntity))
            return;

        ItemEntity item = (ItemEntity)e.getEntity();

        ItemSpawnHandler handler = getInstance(e.getWorld());
        handler.tiles.putIfAbsent(e.getWorld().getDimension().getType(), new LinkedList<>());

        List<WeakReference<DemagnetizationCoilTile>> toRemove = new ArrayList<>();

        List<WeakReference<DemagnetizationCoilTile>> list = handler.tiles.get(e.getWorld().getDimension().getType());
        for(WeakReference<DemagnetizationCoilTile> reference : list){
            DemagnetizationCoilTile tile = reference.get();
            if(tile == null || tile.isRemoved() || !tile.hasWorld()){
                toRemove.add(reference);
                continue;
            }

            if(tile.getArea().contains(item.getPositionVec()) && tile.shouldEffectItem(item.getItem())){
                item.getPersistentData().putBoolean("PreventRemoteMovement", true);
                item.getPersistentData().putBoolean("AllowMachineRemoteMovement", true);
            }
        }

        list.removeAll(toRemove);
    }

}
