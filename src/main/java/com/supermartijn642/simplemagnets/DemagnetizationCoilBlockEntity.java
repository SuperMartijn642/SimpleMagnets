package com.supermartijn642.simplemagnets;

import com.supermartijn642.core.block.BaseBlockEntity;
import com.supermartijn642.core.block.BaseBlockEntityType;
import com.supermartijn642.core.block.TickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 2/19/2021 by SuperMartijn642
 */
public class DemagnetizationCoilBlockEntity extends BaseBlockEntity implements TickableBlockEntity {

    public static class BasicDemagnetizationCoilBlockEntity extends DemagnetizationCoilBlockEntity {

        public BasicDemagnetizationCoilBlockEntity(BlockPos pos, BlockState state){
            super(SimpleMagnets.basic_demagnetization_coil_tile, pos, state, SMConfig.basicCoilMinRange.get(), SMConfig.basicCoilMaxRange.get(), SMConfig.basicCoilRange.get(), SMConfig.basicCoilFilter.get());
        }
    }

    public static class AdvancedDemagnetizationCoilBlockEntity extends DemagnetizationCoilBlockEntity {

        public AdvancedDemagnetizationCoilBlockEntity(BlockPos pos, BlockState state){
            super(SimpleMagnets.advanced_demagnetization_coil_tile, pos, state, SMConfig.advancedCoilMinRange.get(), SMConfig.advancedCoilMaxRange.get(), SMConfig.advancedCoilRange.get(), SMConfig.advancedCoilFilter.get());
        }
    }

    private final int minRange;
    private final int maxRange;
    private final boolean hasFilter;

    public int rangeX, rangeY, rangeZ;
    public final List<ItemStack> filter = new ArrayList<>(9);
    public boolean filterWhitelist;
    public boolean filterDurability = true; // nbt in 1.14+
    public boolean showRange;

    public DemagnetizationCoilBlockEntity(BaseBlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, int minRange, int maxRange, int range, boolean hasFilter){
        super(blockEntityType, pos, state);
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.rangeX = this.rangeY = this.rangeZ = range;
        this.hasFilter = hasFilter;
        for(int i = 0; i < 9; i++)
            this.filter.add(ItemStack.EMPTY);
    }

    @Override
    public void update(){
        AABB area = this.getArea();

        List<ItemEntity> affectedItems = this.level.getEntities(EntityType.ITEM, area,
            item -> item.isAlive() && this.shouldEffectItem(item.getItem())
        );

        affectedItems.forEach(item -> {
            item.getPersistentData().putBoolean("PreventRemoteMovement", true);
            item.getPersistentData().putBoolean("AllowMachineRemoteMovement", true);
        });
    }

    public AABB getArea(){
        return new AABB(this.worldPosition).inflate(this.rangeX - 1, this.rangeY - 1, this.rangeZ - 1);
    }

    public boolean shouldEffectItem(ItemStack stack){
        if(!this.hasFilter)
            return true;

        if(stack.isEmpty())
            return false;
        for(int i = 0; i < 9; i++){
            ItemStack filter = this.filter.get(i);
            if(ItemStack.isSameItem(filter, stack) &&
                (!this.filterDurability || ItemStack.isSameItemSameComponents(filter, stack)))
                return this.filterWhitelist;
        }
        return !this.filterWhitelist;
    }

    public void setRangeX(int range){
        int old = this.rangeX;
        this.rangeX = Math.min(Math.max(range, this.minRange), this.maxRange);
        if(this.rangeX != old)
            this.dataChanged();
    }

    public void setRangeY(int range){
        int old = this.rangeY;
        this.rangeY = Math.min(Math.max(range, this.minRange), this.maxRange);
        if(this.rangeY != old)
            this.dataChanged();
    }

    public void setRangeZ(int range){
        int old = this.rangeZ;
        this.rangeZ = Math.min(Math.max(range, this.minRange), this.maxRange);
        if(this.rangeZ != old)
            this.dataChanged();
    }

    @Override
    protected CompoundTag writeData(){
        CompoundTag tag = new CompoundTag();
        tag.putInt("rangeX", this.rangeX);
        tag.putInt("rangeY", this.rangeY);
        tag.putInt("rangeZ", this.rangeZ);
        if(this.hasFilter){
            for(int i = 0; i < 9; i++){
                if(!this.filter.get(i).isEmpty())
                    tag.put("filter" + i, this.filter.get(i).save(this.level.registryAccess()));
            }
            tag.putBoolean("filterWhitelist", this.filterWhitelist);
            tag.putBoolean("filterDurability", this.filterDurability);
        }
        tag.putBoolean("showRange", this.showRange);
        return tag;
    }

    @Override
    protected void readData(CompoundTag tag){
        if(tag.contains("rangeX"))
            this.rangeX = tag.getInt("rangeX");
        if(tag.contains("rangeY"))
            this.rangeY = tag.getInt("rangeY");
        if(tag.contains("rangeZ"))
            this.rangeZ = tag.getInt("rangeZ");
        if(this.hasFilter){
            for(int i = 0; i < 9; i++)
                this.filter.set(i, tag.contains("filter" + i) ? ItemStack.parseOptional(CommonUtils.getRegistryAccess(), tag.getCompound("filter" + i)) : ItemStack.EMPTY);
            this.filterWhitelist = tag.contains("filterWhitelist") && tag.getBoolean("filterWhitelist");
            this.filterDurability = tag.contains("filterDurability") && tag.getBoolean("filterDurability");
        }
        this.showRange = tag.getBoolean("showRange");
    }

    @Override
    public void onLoad(){
        ItemSpawnHandler.add(this);
    }
}
