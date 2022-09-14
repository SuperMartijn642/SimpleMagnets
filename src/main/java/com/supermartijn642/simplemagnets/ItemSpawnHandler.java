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

    private static ItemSpawnHandler getInstance(Level level){
        return level.isClientSide ? CLIENT : SERVER;
    }

    public static void add(DemagnetizationCoilBlockEntity entity){
        if(entity == null || entity.isRemoved() || !entity.hasLevel())
            return;

        ItemSpawnHandler handler = getInstance(entity.getLevel());
        handler.blocks.putIfAbsent(entity.getLevel().dimension(), new LinkedList<>());
        handler.blocks.get(entity.getLevel().dimension()).add(new WeakReference<>(entity));
    }

    private final HashMap<ResourceKey<Level>,List<WeakReference<DemagnetizationCoilBlockEntity>>> blocks = new HashMap<>();

    @SubscribeEvent
    public static void onEntitySpawn(EntityJoinLevelEvent e){
        if(!(e.getEntity() instanceof ItemEntity))
            return;

        ItemEntity item = (ItemEntity)e.getEntity();

        ItemSpawnHandler handler = getInstance(e.getLevel());
        handler.blocks.putIfAbsent(e.getLevel().dimension(), new LinkedList<>());

        List<WeakReference<DemagnetizationCoilBlockEntity>> toRemove = new ArrayList<>();

        List<WeakReference<DemagnetizationCoilBlockEntity>> list = handler.blocks.get(e.getLevel().dimension());
        for(WeakReference<DemagnetizationCoilBlockEntity> reference : list){
            DemagnetizationCoilBlockEntity entity = reference.get();
            if(entity == null || entity.isRemoved() || !entity.hasLevel()){
                toRemove.add(reference);
                continue;
            }

            if(entity.getArea().contains(item.position()) && entity.shouldEffectItem(item.getItem())){
                item.getPersistentData().putBoolean("PreventRemoteMovement", true);
                item.getPersistentData().putBoolean("AllowMachineRemoteMovement", true);
            }
        }

        list.removeAll(toRemove);
    }

}
