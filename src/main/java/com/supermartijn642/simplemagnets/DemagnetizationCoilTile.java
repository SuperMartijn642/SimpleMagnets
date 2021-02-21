package com.supermartijn642.simplemagnets;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created 2/19/2021 by SuperMartijn642
 */
public class DemagnetizationCoilTile extends TileEntity implements ITickable {

    public static class BasicDemagnetizationCoilTile extends DemagnetizationCoilTile {
        public BasicDemagnetizationCoilTile(){
            super(SMConfig.basicCoilMinRange.get(), SMConfig.basicCoilMaxRange.get(), SMConfig.basicCoilRange.get(), SMConfig.basicCoilFilter.get());
        }
    }

    public static class AdvancedDemagnetizationCoilTile extends DemagnetizationCoilTile {
        public AdvancedDemagnetizationCoilTile(){
            super(SMConfig.advancedCoilMinRange.get(), SMConfig.advancedCoilMaxRange.get(), SMConfig.advancedCoilRange.get(), SMConfig.advancedCoilFilter.get());
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

    public DemagnetizationCoilTile(int minRange, int maxRange, int range, boolean hasFilter){
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.rangeX = this.rangeY = this.rangeZ = range;
        this.hasFilter = hasFilter;
        for(int i = 0; i < 9; i++)
            this.filter.add(ItemStack.EMPTY);
    }

    @Override
    public void update(){
        AxisAlignedBB area = this.getArea();

        List<EntityItem> affectedItems = this.world.getEntitiesWithinAABB(EntityItem.class, area,
            item -> item.isEntityAlive() && this.shouldEffectItem(item.getItem())
        );

        affectedItems.forEach(item -> {
            item.getEntityData().setBoolean("PreventRemoteMovement", true);
            item.getEntityData().setBoolean("AllowMachineRemoteMovement", true);
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

    public boolean hasFilter(){
        return this.hasFilter;
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

    private NBTTagCompound getChangedData(){
        return this.dataChanged ? this.getData() : null;
    }

    private NBTTagCompound getData(){
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("rangeX", this.rangeX);
        tag.setInteger("rangeY", this.rangeY);
        tag.setInteger("rangeZ", this.rangeZ);
        if(this.hasFilter){
            for(int i = 0; i < 9; i++){
                if(!this.filter.get(i).isEmpty())
                    tag.setTag("filter" + i, this.filter.get(i).writeToNBT(new NBTTagCompound()));
            }
            tag.setBoolean("filterWhitelist", this.filterWhitelist);
            tag.setBoolean("filterDurability", this.filterDurability);
        }
        return tag;
    }

    private void handleData(NBTTagCompound tag){
        if(tag.hasKey("rangeX"))
            this.rangeX = tag.getInteger("rangeX");
        if(tag.hasKey("rangeY"))
            this.rangeY = tag.getInteger("rangeY");
        if(tag.hasKey("rangeZ"))
            this.rangeZ = tag.getInteger("rangeZ");
        if(this.hasFilter){
            for(int i = 0; i < 9; i++)
                this.filter.set(i, tag.hasKey("filter" + i) ? new ItemStack(tag.getCompoundTag("filter" + i)) : ItemStack.EMPTY);
            this.filterWhitelist = tag.hasKey("filterWhitelist") && tag.getBoolean("filterWhitelist");
            this.filterDurability = tag.hasKey("filterDurability") && tag.getBoolean("filterDurability");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound){
        super.writeToNBT(compound);
        compound.setTag("data", this.getData());
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound){
        super.readFromNBT(compound);
        if(compound.hasKey("data"))
            this.handleData(compound.getCompoundTag("data"));
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket(){
        NBTTagCompound tag = this.getChangedData();
        return tag == null || tag.hasNoTags() ? null : new SPacketUpdateTileEntity(this.pos, 0, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt){
        this.handleData(pkt.getNbtCompound());
    }

    @Override
    public NBTTagCompound getUpdateTag(){
        NBTTagCompound tag = super.getUpdateTag();
        tag.setTag("data", this.getData());
        return tag;
    }

    @Override
    public void onLoad(){
        ItemSpawnHandler.add(this);
    }

    public IBlockState getBlockState(){
        return this.world.getBlockState(this.pos);
    }
}
