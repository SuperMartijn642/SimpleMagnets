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

    private static ItemSpawnHandler getInstance(World level){
        return level.isRemote ? CLIENT : SERVER;
    }

    public static void add(DemagnetizationCoilBlockEntity entity){
        if(entity == null || entity.isInvalid() || !entity.hasWorld())
            return;

        ItemSpawnHandler handler = getInstance(entity.getWorld());
        handler.blocks.putIfAbsent(entity.getWorld().provider.getDimensionType(), new LinkedList<>());
        handler.blocks.get(entity.getWorld().provider.getDimensionType()).add(new WeakReference<>(entity));
    }

    private final HashMap<DimensionType,List<WeakReference<DemagnetizationCoilBlockEntity>>> blocks = new HashMap<>();

    @SubscribeEvent
    public static void onEntitySpawn(EntityJoinWorldEvent e){
        if(!(e.getEntity() instanceof EntityItem))
            return;

        EntityItem item = (EntityItem)e.getEntity();

        ItemSpawnHandler handler = getInstance(e.getWorld());
        handler.blocks.putIfAbsent(e.getWorld().provider.getDimensionType(), new LinkedList<>());

        List<WeakReference<DemagnetizationCoilBlockEntity>> toRemove = new ArrayList<>();

        List<WeakReference<DemagnetizationCoilBlockEntity>> list = handler.blocks.get(e.getWorld().provider.getDimensionType());
        for(WeakReference<DemagnetizationCoilBlockEntity> reference : list){
            DemagnetizationCoilBlockEntity entity = reference.get();
            if(entity == null || entity.isInvalid() || !entity.hasWorld()){
                toRemove.add(reference);
                continue;
            }

            if(entity.getArea().contains(item.getPositionVector()) && entity.shouldEffectItem(item.getItem())){
                item.getEntityData().setBoolean("PreventRemoteMovement", true);
                item.getEntityData().setBoolean("AllowMachineRemoteMovement", true);
            }
        }

        list.removeAll(toRemove);
    }

}
