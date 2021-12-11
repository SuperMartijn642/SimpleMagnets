package com.supermartijn642.simplemagnets;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.ToolType;
import com.supermartijn642.core.block.BaseBlock;
import com.supermartijn642.core.block.BlockShape;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Supplier;

/**
 * Created 2/19/2021 by SuperMartijn642
 */
public class DemagnetizationCoilBlock extends BaseBlock {

    private static final BlockShape[] SHAPES;

    static{
        BlockShape shape = BlockShape.or(
            BlockShape.createBlockShape(7, 10.5, 7, 9, 11, 9),
            BlockShape.createBlockShape(4.5, 0, 4.5, 11.5, 1, 11.5),
            BlockShape.createBlockShape(6.5, 2, 6.5, 9.5, 10.5, 9.5),
            BlockShape.createBlockShape(5.5, 1, 5.5, 10.5, 1.5, 10.5),
            BlockShape.createBlockShape(6, 1.5, 6, 10, 2, 10),
            BlockShape.createBlockShape(6, 3, 6, 10, 4, 10),
            BlockShape.createBlockShape(5.5, 8, 5.5, 10.5, 9, 10.5),
            BlockShape.createBlockShape(6, 7, 6, 10, 8, 10),
            BlockShape.createBlockShape(6, 9, 6, 10, 10, 10),
            BlockShape.createBlockShape(6, 5, 6, 10, 6, 10),
            BlockShape.createBlockShape(5.5, 4, 5.5, 10.5, 5, 10.5),
            BlockShape.createBlockShape(5.5, 6, 5.5, 10.5, 7, 10.5)
        );
        SHAPES = new BlockShape[EnumFacing.values().length];
        SHAPES[EnumFacing.DOWN.getIndex()] = shape;
        SHAPES[EnumFacing.UP.getIndex()] = shape.rotate(EnumFacing.Axis.X).rotate(EnumFacing.Axis.X);
        SHAPES[EnumFacing.NORTH.getIndex()] = shape.rotate(EnumFacing.Axis.X).rotate(EnumFacing.Axis.X).rotate(EnumFacing.Axis.X);
        SHAPES[EnumFacing.EAST.getIndex()] = shape.rotate(EnumFacing.Axis.Z).rotate(EnumFacing.Axis.Z).rotate(EnumFacing.Axis.Z);
        SHAPES[EnumFacing.SOUTH.getIndex()] = shape.rotate(EnumFacing.Axis.X);
        SHAPES[EnumFacing.WEST.getIndex()] = shape.rotate(EnumFacing.Axis.Z);
    }

    public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class);

    private final Supplier<Integer> maxRange;
    private final Supplier<Boolean> hasFilter;
    private final Supplier<? extends DemagnetizationCoilTile> tileSupplier;

    public DemagnetizationCoilBlock(String registryName, Supplier<Integer> maxRange, Supplier<Boolean> hasFilter, Supplier<? extends DemagnetizationCoilTile> tileSupplier){
        super(registryName, false, Properties.create(Material.IRON, MapColor.GRAY).harvestTool(ToolType.PICKAXE).harvestLevel(0).hardnessAndResistance(3.0F, 5.0F).sound(SoundType.METAL));
        this.maxRange = maxRange;
        this.hasFilter = hasFilter;
        this.tileSupplier = tileSupplier;

        this.setCreativeTab(CreativeTabs.SEARCH);

        this.setDefaultState(this.getDefaultState().withProperty(FACING, EnumFacing.DOWN));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
        if(!worldIn.isRemote)
            playerIn.openGui(SimpleMagnets.instance, 99, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced){
        if(this.hasFilter.get())
            tooltip.add(TextComponents.translation("simplemagnets.demagnetization_coil.info.filtered", this.maxRange.get()).color(TextFormatting.AQUA).format());
        else
            tooltip.add(TextComponents.translation("simplemagnets.demagnetization_coil.info.no_filter", this.maxRange.get()).color(TextFormatting.AQUA).format());
        super.addInformation(stack, world, tooltip, advanced);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand){
        return this.getDefaultState().withProperty(FACING, facing.getOpposite());
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
        return SHAPES[state.getValue(FACING).getIndex()].simplify();
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean isActualState){
        for(AxisAlignedBB bb : SHAPES[state.getValue(FACING).getIndex()].toBoxes())
            addCollisionBoxToList(pos, entityBox, collidingBoxes, bb);
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face){
        return state.getValue(FACING) == face ? BlockFaceShape.CENTER_SMALL : BlockFaceShape.UNDEFINED;
    }

    @Override
    public boolean isFullCube(IBlockState state){
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state){
        return false;
    }

    @Override
    public boolean hasTileEntity(IBlockState state){
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state){
        return this.tileSupplier.get();
    }

    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public int getMetaFromState(IBlockState state){
        return state.getValue(FACING).getIndex();
    }

    @Override
    public IBlockState getStateFromMeta(int meta){
        return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
    }
}
