package com.supermartijn642.simplemagnets;

import com.supermartijn642.simplemagnets.extensions.SimpleMagnetsItemEntity;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created 2/21/2021 by SuperMartijn642
 */
public class ItemSpawnHandler {

    private static final ItemSpawnHandler SERVER = new ItemSpawnHandler(), CLIENT = new ItemSpawnHandler();

    private static ItemSpawnHandler getInstance(Level level){
        return level.isClientSide ? CLIENT : SERVER;
    }

    public static void registerEventListeners(){
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> onEntitySpawn(entity));
    }

    public static void add(DemagnetizationCoilBlockEntity entity){
        if(entity == null || entity.isRemoved() || !entity.hasLevel())
            return;

        ItemSpawnHandler handler = getInstance(entity.getLevel());
        handler.blocks.putIfAbsent(entity.getLevel().dimension(), new LinkedList<>());
        handler.blocks.get(entity.getLevel().dimension()).add(new WeakReference<>(entity));
    }

    private final HashMap<ResourceKey<Level>,List<WeakReference<DemagnetizationCoilBlockEntity>>> blocks = new HashMap<>();

    private static void onEntitySpawn(Entity spawnedEntity){
        if(!(spawnedEntity instanceof ItemEntity))
            return;

        ItemEntity item = (ItemEntity)spawnedEntity;

        ItemSpawnHandler handler = getInstance(item.getLevel());
        handler.blocks.putIfAbsent(item.getLevel().dimension(), new LinkedList<>());

        List<WeakReference<DemagnetizationCoilBlockEntity>> toRemove = new ArrayList<>();

        List<WeakReference<DemagnetizationCoilBlockEntity>> list = handler.blocks.get(item.getLevel().dimension());
        for(WeakReference<DemagnetizationCoilBlockEntity> reference : list){
            DemagnetizationCoilBlockEntity entity = reference.get();
            if(entity == null || entity.isRemoved() || !entity.hasLevel()){
                toRemove.add(reference);
                continue;
            }

            if(entity.getArea().contains(item.position()) && entity.shouldEffectItem(item.getItem()))
                ((SimpleMagnetsItemEntity)item).simplemagnetsMarkDontPickUp();
        }

        list.removeAll(toRemove);
    }

}
