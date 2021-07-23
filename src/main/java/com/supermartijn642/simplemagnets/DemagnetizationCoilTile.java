package com.supermartijn642.simplemagnets;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created 2/19/2021 by SuperMartijn642
 */
public class DemagnetizationCoilTile extends BlockEntity {

    public static class BasicDemagnetizationCoilTile extends DemagnetizationCoilTile {

        public BasicDemagnetizationCoilTile(BlockPos pos, BlockState state){
            super(SimpleMagnets.basic_demagnetization_coil_tile, pos, state, SMConfig.basicCoilMinRange.get(), SMConfig.basicCoilMaxRange.get(), SMConfig.basicCoilRange.get(), SMConfig.basicCoilFilter.get());
        }
    }

    public static class AdvancedDemagnetizationCoilTile extends DemagnetizationCoilTile {

        public AdvancedDemagnetizationCoilTile(BlockPos pos, BlockState state){
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
    private boolean dataChanged;

    public DemagnetizationCoilTile(BlockEntityType<?> tileEntityType, BlockPos pos, BlockState state, int minRange, int maxRange, int range, boolean hasFilter){
        super(tileEntityType, pos, state);
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.rangeX = this.rangeY = this.rangeZ = range;
        this.hasFilter = hasFilter;
        for(int i = 0; i < 9; i++)
            this.filter.add(ItemStack.EMPTY);
    }

    public void tick(){
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
            if(ItemStack.isSame(filter, stack) &&
                (!this.filterDurability || ItemStack.tagMatches(filter, stack)))
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

    public void dataChanged(){
        if(!this.level.isClientSide){
            this.dataChanged = true;
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 2);
        }
    }

    private CompoundTag getChangedData(){
        return this.dataChanged ? this.getData() : null;
    }

    private CompoundTag getData(){
        CompoundTag tag = new CompoundTag();
        tag.putInt("rangeX", this.rangeX);
        tag.putInt("rangeY", this.rangeY);
        tag.putInt("rangeZ", this.rangeZ);
        if(this.hasFilter){
            for(int i = 0; i < 9; i++){
                if(!this.filter.get(i).isEmpty())
                    tag.put("filter" + i, this.filter.get(i).save(new CompoundTag()));
            }
            tag.putBoolean("filterWhitelist", this.filterWhitelist);
            tag.putBoolean("filterDurability", this.filterDurability);
        }
        return tag;
    }

    private void handleData(CompoundTag tag){
        if(tag.contains("rangeX"))
            this.rangeX = tag.getInt("rangeX");
        if(tag.contains("rangeY"))
            this.rangeY = tag.getInt("rangeY");
        if(tag.contains("rangeZ"))
            this.rangeZ = tag.getInt("rangeZ");
        if(this.hasFilter){
            for(int i = 0; i < 9; i++)
                this.filter.set(i, tag.contains("filter" + i) ? ItemStack.of(tag.getCompound("filter" + i)) : ItemStack.EMPTY);
            this.filterWhitelist = tag.contains("filterWhitelist") && tag.getBoolean("filterWhitelist");
            this.filterDurability = tag.contains("filterDurability") && tag.getBoolean("filterDurability");
        }
    }

    @Override
    public CompoundTag save(CompoundTag compound){
        super.save(compound);
        compound.put("data", this.getData());
        return compound;
    }

    @Override
    public void load(CompoundTag compound){
        super.load(compound);
        if(compound.contains("data"))
            this.handleData(compound.getCompound("data"));
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket(){
        CompoundTag tag = this.getChangedData();
        return tag == null || tag.isEmpty() ? null : new ClientboundBlockEntityDataPacket(this.worldPosition, 0, tag);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt){
        this.handleData(pkt.getTag());
    }

    @Override
    public CompoundTag getUpdateTag(){
        CompoundTag tag = super.getUpdateTag();
        tag.put("data", this.getData());
        return tag;
    }

    @Override
    public void onLoad(){
        ItemSpawnHandler.add(this);
    }
}
