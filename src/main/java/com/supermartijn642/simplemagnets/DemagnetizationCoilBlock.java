package com.supermartijn642.simplemagnets;

import net.minecraft.block.Block;
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
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Supplier;

/**
 * Created 2/19/2021 by SuperMartijn642
 */
public class DemagnetizationCoilBlock extends Block {

    private static final AxisAlignedBB[][] SHAPES;
    private static final AxisAlignedBB[] SHAPES_COMBINED = new AxisAlignedBB[6];

    static{
        SHAPES = new AxisAlignedBB[EnumFacing.values().length][];
        SHAPES[EnumFacing.DOWN.getIndex()] = new AxisAlignedBB[]{
            new AxisAlignedBB(7 / 16d, 10.5 / 16d, 7 / 16d, 9 / 16d, 11 / 16d, 9 / 16d),
            new AxisAlignedBB(4.5 / 16d, 0 / 16d, 4.5 / 16d, 11.5 / 16d, 1 / 16d, 11.5 / 16d),
            new AxisAlignedBB(6.5 / 16d, 2 / 16d, 6.5 / 16d, 9.5 / 16d, 10.5 / 16d, 9.5 / 16d),
            new AxisAlignedBB(5.5 / 16d, 1 / 16d, 5.5 / 16d, 10.5 / 16d, 1.5 / 16d, 10.5 / 16d),
            new AxisAlignedBB(6 / 16d, 1.5 / 16d, 6 / 16d, 10 / 16d, 2 / 16d, 10 / 16d),
            new AxisAlignedBB(6 / 16d, 3 / 16d, 6 / 16d, 10 / 16d, 4 / 16d, 10 / 16d),
            new AxisAlignedBB(5.5 / 16d, 8 / 16d, 5.5 / 16d, 10.5 / 16d, 9 / 16d, 10.5 / 16d),
            new AxisAlignedBB(6 / 16d, 7 / 16d, 6 / 16d, 10 / 16d, 8 / 16d, 10 / 16d),
            new AxisAlignedBB(6 / 16d, 9 / 16d, 6 / 16d, 10 / 16d, 10 / 16d, 10 / 16d),
            new AxisAlignedBB(6 / 16d, 5 / 16d, 6 / 16d, 10 / 16d, 6 / 16d, 10 / 16d),
            new AxisAlignedBB(5.5 / 16d, 4 / 16d, 5.5 / 16d, 10.5 / 16d, 5 / 16d, 10.5 / 16d),
            new AxisAlignedBB(5.5 / 16d, 6 / 16d, 5.5 / 16d, 10.5 / 16d, 7 / 16d, 10.5 / 16d)
        };
        SHAPES[EnumFacing.UP.getIndex()] = new AxisAlignedBB[]{
            new AxisAlignedBB(7 / 16d, 5 / 16d, 7 / 16d, 9 / 16d, 5.5 / 16d, 9 / 16d),
            new AxisAlignedBB(4.5 / 16d, 15 / 16d, 4.5 / 16d, 11.5 / 16d, 16 / 16d, 11.5 / 16d),
            new AxisAlignedBB(6.5 / 16d, 5.5 / 16d, 6.5 / 16d, 9.5 / 16d, 14 / 16d, 9.5 / 16d),
            new AxisAlignedBB(5.5 / 16d, 14.5 / 16d, 5.5 / 16d, 10.5 / 16d, 15 / 16d, 10.5 / 16d),
            new AxisAlignedBB(6 / 16d, 14 / 16d, 6 / 16d, 10 / 16d, 14.5 / 16d, 10 / 16d),
            new AxisAlignedBB(6 / 16d, 12 / 16d, 6 / 16d, 10 / 16d, 13 / 16d, 10 / 16d),
            new AxisAlignedBB(5.5 / 16d, 7 / 16d, 5.5 / 16d, 10.5 / 16d, 8 / 16d, 10.5 / 16d),
            new AxisAlignedBB(6 / 16d, 8 / 16d, 6 / 16d, 10 / 16d, 9 / 16d, 10 / 16d),
            new AxisAlignedBB(6 / 16d, 6 / 16d, 6 / 16d, 10 / 16d, 7 / 16d, 10 / 16d),
            new AxisAlignedBB(6 / 16d, 10 / 16d, 6 / 16d, 10 / 16d, 11 / 16d, 10 / 16d),
            new AxisAlignedBB(5.5 / 16d, 11 / 16d, 5.5 / 16d, 10.5 / 16d, 12 / 16d, 10.5 / 16d),
            new AxisAlignedBB(5.5 / 16d, 9 / 16d, 5.5 / 16d, 10.5 / 16d, 10 / 16d, 10.5 / 16d)
        };
        SHAPES[EnumFacing.NORTH.getIndex()] = new AxisAlignedBB[]{
            new AxisAlignedBB(7 / 16d, 7 / 16d, 10.5 / 16d, 9 / 16d, 9 / 16d, 11 / 16d),
            new AxisAlignedBB(4.5 / 16d, 4.5 / 16d, 0 / 16d, 11.5 / 16d, 11.5 / 16d, 1 / 16d),
            new AxisAlignedBB(6.5 / 16d, 6.5 / 16d, 2 / 16d, 9.5 / 16d, 9.5 / 16d, 10.5 / 16d),
            new AxisAlignedBB(5.5 / 16d, 5.5 / 16d, 1 / 16d, 10.5 / 16d, 10.5 / 16d, 1.5 / 16d),
            new AxisAlignedBB(6 / 16d, 6 / 16d, 1.5 / 16d, 10 / 16d, 10 / 16d, 2 / 16d),
            new AxisAlignedBB(6 / 16d, 6 / 16d, 3 / 16d, 10 / 16d, 10 / 16d, 4 / 16d),
            new AxisAlignedBB(5.5 / 16d, 5.5 / 16d, 8 / 16d, 10.5 / 16d, 10.5 / 16d, 9 / 16d),
            new AxisAlignedBB(6 / 16d, 6 / 16d, 7 / 16d, 10 / 16d, 10 / 16d, 8 / 16d),
            new AxisAlignedBB(6 / 16d, 6 / 16d, 9 / 16d, 10 / 16d, 10 / 16d, 10 / 16d),
            new AxisAlignedBB(6 / 16d, 6 / 16d, 5 / 16d, 10 / 16d, 10 / 16d, 6 / 16d),
            new AxisAlignedBB(5.5 / 16d, 5.5 / 16d, 4 / 16d, 10.5 / 16d, 10.5 / 16d, 5 / 16d),
            new AxisAlignedBB(5.5 / 16d, 5.5 / 16d, 6 / 16d, 10.5 / 16d, 10.5 / 16d, 7 / 16d)
        };
        SHAPES[EnumFacing.EAST.getIndex()] = new AxisAlignedBB[]{
            new AxisAlignedBB(5 / 16d, 7 / 16d, 7 / 16d, 5.5 / 16d, 9 / 16d, 9 / 16d),
            new AxisAlignedBB(15 / 16d, 4.5 / 16d, 4.5 / 16d, 16 / 16d, 11.5 / 16d, 11.5 / 16d),
            new AxisAlignedBB(5.5 / 16d, 6.5 / 16d, 6.5 / 16d, 14 / 16d, 9.5 / 16d, 9.5 / 16d),
            new AxisAlignedBB(14.5 / 16d, 5.5 / 16d, 5.5 / 16d, 15 / 16d, 10.5 / 16d, 10.5 / 16d),
            new AxisAlignedBB(14 / 16d, 6 / 16d, 6 / 16d, 14.5 / 16d, 10 / 16d, 10 / 16d),
            new AxisAlignedBB(12 / 16d, 6 / 16d, 6 / 16d, 13 / 16d, 10 / 16d, 10 / 16d),
            new AxisAlignedBB(7 / 16d, 5.5 / 16d, 5.5 / 16d, 8 / 16d, 10.5 / 16d, 10.5 / 16d),
            new AxisAlignedBB(8 / 16d, 6 / 16d, 6 / 16d, 9 / 16d, 10 / 16d, 10 / 16d),
            new AxisAlignedBB(6 / 16d, 6 / 16d, 6 / 16d, 7 / 16d, 10 / 16d, 10 / 16d),
            new AxisAlignedBB(10 / 16d, 6 / 16d, 6 / 16d, 11 / 16d, 10 / 16d, 10 / 16d),
            new AxisAlignedBB(11 / 16d, 5.5 / 16d, 5.5 / 16d, 12 / 16d, 10.5 / 16d, 10.5 / 16d),
            new AxisAlignedBB(9 / 16d, 5.5 / 16d, 5.5 / 16d, 10 / 16d, 10.5 / 16d, 10.5 / 16d)
        };
        SHAPES[EnumFacing.SOUTH.getIndex()] = new AxisAlignedBB[]{
            new AxisAlignedBB(7 / 16d, 7 / 16d, 5 / 16d, 9 / 16d, 9 / 16d, 5.5 / 16d),
            new AxisAlignedBB(4.5 / 16d, 4.5 / 16d, 15 / 16d, 11.5 / 16d, 11.5 / 16d, 16 / 16d),
            new AxisAlignedBB(6.5 / 16d, 6.5 / 16d, 5.5 / 16d, 9.5 / 16d, 9.5 / 16d, 14 / 16d),
            new AxisAlignedBB(5.5 / 16d, 5.5 / 16d, 14.5 / 16d, 10.5 / 16d, 10.5 / 16d, 15 / 16d),
            new AxisAlignedBB(6 / 16d, 6 / 16d, 14 / 16d, 10 / 16d, 10 / 16d, 14.5 / 16d),
            new AxisAlignedBB(6 / 16d, 6 / 16d, 12 / 16d, 10 / 16d, 10 / 16d, 13 / 16d),
            new AxisAlignedBB(5.5 / 16d, 5.5 / 16d, 7 / 16d, 10.5 / 16d, 10.5 / 16d, 8 / 16d),
            new AxisAlignedBB(6 / 16d, 6 / 16d, 8 / 16d, 10 / 16d, 10 / 16d, 9 / 16d),
            new AxisAlignedBB(6 / 16d, 6 / 16d, 6 / 16d, 10 / 16d, 10 / 16d, 7 / 16d),
            new AxisAlignedBB(6 / 16d, 6 / 16d, 10 / 16d, 10 / 16d, 10 / 16d, 11 / 16d),
            new AxisAlignedBB(5.5 / 16d, 5.5 / 16d, 11 / 16d, 10.5 / 16d, 10.5 / 16d, 12 / 16d),
            new AxisAlignedBB(5.5 / 16d, 5.5 / 16d, 9 / 16d, 10.5 / 16d, 10.5 / 16d, 10 / 16d)
        };
        SHAPES[EnumFacing.WEST.getIndex()] = new AxisAlignedBB[]{
            new AxisAlignedBB(10.5 / 16d, 7 / 16d, 7 / 16d, 11 / 16d, 9 / 16d, 9 / 16d),
            new AxisAlignedBB(0 / 16d, 4.5 / 16d, 4.5 / 16d, 1 / 16d, 11.5 / 16d, 11.5 / 16d),
            new AxisAlignedBB(2 / 16d, 6.5 / 16d, 6.5 / 16d, 10.5 / 16d, 9.5 / 16d, 9.5 / 16d),
            new AxisAlignedBB(1 / 16d, 5.5 / 16d, 5.5 / 16d, 1.5 / 16d, 10.5 / 16d, 10.5 / 16d),
            new AxisAlignedBB(1.5 / 16d, 6 / 16d, 6 / 16d, 2 / 16d, 10 / 16d, 10 / 16d),
            new AxisAlignedBB(3 / 16d, 6 / 16d, 6 / 16d, 4 / 16d, 10 / 16d, 10 / 16d),
            new AxisAlignedBB(8 / 16d, 5.5 / 16d, 5.5 / 16d, 9 / 16d, 10.5 / 16d, 10.5 / 16d),
            new AxisAlignedBB(7 / 16d, 6 / 16d, 6 / 16d, 8 / 16d, 10 / 16d, 10 / 16d),
            new AxisAlignedBB(9 / 16d, 6 / 16d, 6 / 16d, 10 / 16d, 10 / 16d, 10 / 16d),
            new AxisAlignedBB(5 / 16d, 6 / 16d, 6 / 16d, 6 / 16d, 10 / 16d, 10 / 16d),
            new AxisAlignedBB(4 / 16d, 5.5 / 16d, 5.5 / 16d, 5 / 16d, 10.5 / 16d, 10.5 / 16d),
            new AxisAlignedBB(6 / 16d, 5.5 / 16d, 5.5 / 16d, 7 / 16d, 10.5 / 16d, 10.5 / 16d)
        };

        for(EnumFacing facing : EnumFacing.values()){
            AxisAlignedBB bb = SHAPES[facing.getIndex()][0];
            for(AxisAlignedBB bb2 : SHAPES[facing.getIndex()])
                bb = bb.union(bb2);
            SHAPES_COMBINED[facing.getIndex()] = bb;
        }
    }

    public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class);

    private final Supplier<Integer> maxRange;
    private final Supplier<Boolean> hasFilter;
    private final Supplier<? extends DemagnetizationCoilTile> tileSupplier;

    public DemagnetizationCoilBlock(String registryName, Supplier<Integer> maxRange, Supplier<Boolean> hasFilter, Supplier<? extends DemagnetizationCoilTile> tileSupplier){
        super(Material.IRON, MapColor.GRAY);
        this.setRegistryName(registryName);
        this.setUnlocalizedName(SimpleMagnets.MODID + "." + registryName);
        this.maxRange = maxRange;
        this.hasFilter = hasFilter;
        this.tileSupplier = tileSupplier;

        this.setCreativeTab(CreativeTabs.SEARCH);
        this.setHarvestLevel("pickaxe", 0);
        this.setHardness(3);
        this.setResistance(5);
        this.setSoundType(SoundType.METAL);

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
            tooltip.add(new TextComponentTranslation("simplemagnets.demagnetization_coil.info.filtered", this.maxRange.get()).setStyle(new Style().setColor(TextFormatting.AQUA)).getFormattedText());
        else
            tooltip.add(new TextComponentTranslation("simplemagnets.demagnetization_coil.info.no_filter", this.maxRange.get()).setStyle(new Style().setColor(TextFormatting.AQUA)).getFormattedText());
        super.addInformation(stack, world, tooltip, advanced);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand){
        return this.getDefaultState().withProperty(FACING, facing.getOpposite());
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
        return SHAPES_COMBINED[state.getValue(FACING).getIndex()];
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean isActualState){
        for(AxisAlignedBB bb : SHAPES[state.getValue(FACING).getIndex()])
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
