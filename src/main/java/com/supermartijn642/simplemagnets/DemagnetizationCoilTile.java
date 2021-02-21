package com.supermartijn642.simplemagnets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created 2/19/2021 by SuperMartijn642
 */
public class DemagnetizationCoilTile extends TileEntity implements ITickableTileEntity {

    public static class BasicDemagnetizationCoilTile extends DemagnetizationCoilTile {
        public BasicDemagnetizationCoilTile(){
            super(SimpleMagnets.basic_demagnetization_coil_tile, SMConfig.basicCoilMinRange.get(), SMConfig.basicCoilMaxRange.get(), SMConfig.basicCoilRange.get(), SMConfig.basicCoilFilter.get());
        }
    }

    public static class AdvancedDemagnetizationCoilTile extends DemagnetizationCoilTile {
        public AdvancedDemagnetizationCoilTile(){
            super(SimpleMagnets.advanced_demagnetization_coil_tile, SMConfig.advancedCoilMinRange.get(), SMConfig.advancedCoilMaxRange.get(), SMConfig.advancedCoilRange.get(), SMConfig.advancedCoilFilter.get());
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

    public DemagnetizationCoilTile(TileEntityType<?> tileEntityType, int minRange, int maxRange, int range, boolean hasFilter){
        super(tileEntityType);
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.rangeX = this.rangeY = this.rangeZ = range;
        this.hasFilter = hasFilter;
        for(int i = 0; i < 9; i++)
            this.filter.add(ItemStack.EMPTY);
    }

    @Override
    public void tick(){
        AxisAlignedBB area = this.getArea();

        List<Entity> affectedItems = this.world.getEntitiesWithinAABB(EntityType.ITEM, area,
            item -> item instanceof ItemEntity && item.isAlive() && this.shouldEffectItem(((ItemEntity)item).getItem())
        );

        affectedItems.forEach(item -> {
            item.getPersistentData().putBoolean("PreventRemoteMovement", true);
            item.getPersistentData().putBoolean("AllowMachineRemoteMovement", true);
        });
    }

    public AxisAlignedBB getArea(){
        return new AxisAlignedBB(this.pos).grow(this.rangeX - 1, this.rangeY - 1, this.rangeZ - 1);
    }

    public boolean shouldEffectItem(ItemStack stack){
        if(!this.hasFilter)
            return true;

        if(stack.isEmpty())
            return false;
        for(int i = 0; i < 9; i++){
            ItemStack filter = this.filter.get(i);
            if(ItemStack.areItemsEqual(filter, stack) &&
                (!this.filterDurability || ItemStack.areItemStackTagsEqual(filter, stack)))
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
        if(!this.world.isRemote){
            this.dataChanged = true;
            this.world.notifyBlockUpdate(this.pos, this.getBlockState(), this.getBlockState(), 2);
        }
    }

    private CompoundNBT getChangedData(){
        return this.dataChanged ? this.getData() : null;
    }

    private CompoundNBT getData(){
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("rangeX", this.rangeX);
        tag.putInt("rangeY", this.rangeY);
        tag.putInt("rangeZ", this.rangeZ);
        if(this.hasFilter){
            for(int i = 0; i < 9; i++){
                if(!this.filter.get(i).isEmpty())
                    tag.put("filter" + i, this.filter.get(i).write(new CompoundNBT()));
            }
            tag.putBoolean("filterWhitelist", this.filterWhitelist);
            tag.putBoolean("filterDurability", this.filterDurability);
        }
        return tag;
    }

    private void handleData(CompoundNBT tag){
        if(tag.contains("rangeX"))
            this.rangeX = tag.getInt("rangeX");
        if(tag.contains("rangeY"))
            this.rangeY = tag.getInt("rangeY");
        if(tag.contains("rangeZ"))
            this.rangeZ = tag.getInt("rangeZ");
        if(this.hasFilter){
            for(int i = 0; i < 9; i++)
                this.filter.set(i, tag.contains("filter" + i) ? ItemStack.read(tag.getCompound("filter" + i)) : ItemStack.EMPTY);
            this.filterWhitelist = tag.contains("filterWhitelist") && tag.getBoolean("filterWhitelist");
            this.filterDurability = tag.contains("filterDurability") && tag.getBoolean("filterDurability");
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound){
        super.write(compound);
        compound.put("data", this.getData());
        return compound;
    }

    @Override
    public void read(CompoundNBT compound){
        super.read(compound);
        if(compound.contains("data"))
            this.handleData(compound.getCompound("data"));
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket(){
        CompoundNBT tag = this.getChangedData();
        return tag == null || tag.isEmpty() ? null : new SUpdateTileEntityPacket(this.pos, 0, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt){
        this.handleData(pkt.getNbtCompound());
    }

    @Override
    public CompoundNBT getUpdateTag(){
        CompoundNBT tag = super.getUpdateTag();
        tag.put("data", this.getData());
        return tag;
    }

    @Override
    public void onLoad(){
        ItemSpawnHandler.add(this);
    }
}
