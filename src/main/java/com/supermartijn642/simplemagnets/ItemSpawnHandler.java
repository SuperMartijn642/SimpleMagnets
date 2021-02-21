package com.supermartijn642.simplemagnets;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created 2/21/2021 by SuperMartijn642
 */
@Mod.EventBusSubscriber
public class ItemSpawnHandler {

    private static final ItemSpawnHandler SERVER = new ItemSpawnHandler(), CLIENT = new ItemSpawnHandler();

    private static ItemSpawnHandler getInstance(World world){
        return world.isRemote ? CLIENT : SERVER;
    }

    public static void add(DemagnetizationCoilTile tile){
        if(tile == null || tile.isInvalid() || !tile.hasWorld())
            return;

        ItemSpawnHandler handler = getInstance(tile.getWorld());
        handler.tiles.putIfAbsent(tile.getWorld().provider.getDimensionType(), new LinkedList<>());
        handler.tiles.get(tile.getWorld().provider.getDimensionType()).add(new WeakReference<>(tile));
    }

    private final HashMap<DimensionType,List<WeakReference<DemagnetizationCoilTile>>> tiles = new HashMap<>();

    @SubscribeEvent
    public static void onEntitySpawn(EntityJoinWorldEvent e){
        if(!(e.getEntity() instanceof EntityItem))
            return;

        EntityItem item = (EntityItem)e.getEntity();

        ItemSpawnHandler handler = getInstance(e.getWorld());
        handler.tiles.putIfAbsent(e.getWorld().provider.getDimensionType(), new LinkedList<>());

        List<WeakReference<DemagnetizationCoilTile>> toRemove = new ArrayList<>();

        List<WeakReference<DemagnetizationCoilTile>> list = handler.tiles.get(e.getWorld().provider.getDimensionType());
        for(WeakReference<DemagnetizationCoilTile> reference : list){
            DemagnetizationCoilTile tile = reference.get();
            if(tile == null || tile.isInvalid() || !tile.hasWorld()){
                toRemove.add(reference);
                continue;
            }

            if(tile.getArea().contains(item.getPositionVector()) && tile.shouldEffectItem(item.getItem())){
                item.getEntityData().setBoolean("PreventRemoteMovement", true);
                item.getEntityData().setBoolean("AllowMachineRemoteMovement", true);
            }
        }

        list.removeAll(toRemove);
    }

}
