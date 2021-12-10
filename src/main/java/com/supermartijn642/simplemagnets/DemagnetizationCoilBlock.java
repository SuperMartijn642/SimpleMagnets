package com.supermartijn642.simplemagnets;

import com.supermartijn642.simplemagnets.gui.DemagnetizationCoilContainer;
import com.supermartijn642.simplemagnets.gui.FilteredDemagnetizationCoilContainer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

/**
 * Created 2/19/2021 by SuperMartijn642
 */
public class DemagnetizationCoilBlock extends Block {

    private static final VoxelShape[] SHAPES;

    static{
        SHAPES = new VoxelShape[Direction.values().length];
        SHAPES[Direction.DOWN.get3DDataValue()] = VoxelShapes.or(
            Block.box(7, 10.5, 7, 9, 11, 9),
            Block.box(4.5, 0, 4.5, 11.5, 1, 11.5),
            Block.box(6.5, 2, 6.5, 9.5, 10.5, 9.5),
            Block.box(5.5, 1, 5.5, 10.5, 1.5, 10.5),
            Block.box(6, 1.5, 6, 10, 2, 10),
            Block.box(6, 3, 6, 10, 4, 10),
            Block.box(5.5, 8, 5.5, 10.5, 9, 10.5),
            Block.box(6, 7, 6, 10, 8, 10),
            Block.box(6, 9, 6, 10, 10, 10),
            Block.box(6, 5, 6, 10, 6, 10),
            Block.box(5.5, 4, 5.5, 10.5, 5, 10.5),
            Block.box(5.5, 6, 5.5, 10.5, 7, 10.5)
        );
        SHAPES[Direction.UP.get3DDataValue()] = VoxelShapes.or(
            Block.box(7, 5, 7, 9, 5.5, 9),
            Block.box(4.5, 15, 4.5, 11.5, 16, 11.5),
            Block.box(6.5, 5.5, 6.5, 9.5, 14, 9.5),
            Block.box(5.5, 14.5, 5.5, 10.5, 15, 10.5),
            Block.box(6, 14, 6, 10, 14.5, 10),
            Block.box(6, 12, 6, 10, 13, 10),
            Block.box(5.5, 7, 5.5, 10.5, 8, 10.5),
            Block.box(6, 8, 6, 10, 9, 10),
            Block.box(6, 6, 6, 10, 7, 10),
            Block.box(6, 10, 6, 10, 11, 10),
            Block.box(5.5, 11, 5.5, 10.5, 12, 10.5),
            Block.box(5.5, 9, 5.5, 10.5, 10, 10.5)
        );
        SHAPES[Direction.NORTH.get3DDataValue()] = VoxelShapes.or(
            Block.box(7, 7, 10.5, 9, 9, 11),
            Block.box(4.5, 4.5, 0, 11.5, 11.5, 1),
            Block.box(6.5, 6.5, 2, 9.5, 9.5, 10.5),
            Block.box(5.5, 5.5, 1, 10.5, 10.5, 1.5),
            Block.box(6, 6, 1.5, 10, 10, 2),
            Block.box(6, 6, 3, 10, 10, 4),
            Block.box(5.5, 5.5, 8, 10.5, 10.5, 9),
            Block.box(6, 6, 7, 10, 10, 8),
            Block.box(6, 6, 9, 10, 10, 10),
            Block.box(6, 6, 5, 10, 10, 6),
            Block.box(5.5, 5.5, 4, 10.5, 10.5, 5),
            Block.box(5.5, 5.5, 6, 10.5, 10.5, 7)
        );
        SHAPES[Direction.EAST.get3DDataValue()] = VoxelShapes.or(
            Block.box(5, 7, 7, 5.5, 9, 9),
            Block.box(15, 4.5, 4.5, 16, 11.5, 11.5),
            Block.box(5.5, 6.5, 6.5, 14, 9.5, 9.5),
            Block.box(14.5, 5.5, 5.5, 15, 10.5, 10.5),
            Block.box(14, 6, 6, 14.5, 10, 10),
            Block.box(12, 6, 6, 13, 10, 10),
            Block.box(7, 5.5, 5.5, 8, 10.5, 10.5),
            Block.box(8, 6, 6, 9, 10, 10),
            Block.box(6, 6, 6, 7, 10, 10),
            Block.box(10, 6, 6, 11, 10, 10),
            Block.box(11, 5.5, 5.5, 12, 10.5, 10.5),
            Block.box(9, 5.5, 5.5, 10, 10.5, 10.5)
        );
        SHAPES[Direction.SOUTH.get3DDataValue()] = VoxelShapes.or(
            Block.box(7, 7, 5, 9, 9, 5.5),
            Block.box(4.5, 4.5, 15, 11.5, 11.5, 16),
            Block.box(6.5, 6.5, 5.5, 9.5, 9.5, 14),
            Block.box(5.5, 5.5, 14.5, 10.5, 10.5, 15),
            Block.box(6, 6, 14, 10, 10, 14.5),
            Block.box(6, 6, 12, 10, 10, 13),
            Block.box(5.5, 5.5, 7, 10.5, 10.5, 8),
            Block.box(6, 6, 8, 10, 10, 9),
            Block.box(6, 6, 6, 10, 10, 7),
            Block.box(6, 6, 10, 10, 10, 11),
            Block.box(5.5, 5.5, 11, 10.5, 10.5, 12),
            Block.box(5.5, 5.5, 9, 10.5, 10.5, 10)
        );
        SHAPES[Direction.WEST.get3DDataValue()] = VoxelShapes.or(
            Block.box(10.5, 7, 7, 11, 9, 9),
            Block.box(0, 4.5, 4.5, 1, 11.5, 11.5),
            Block.box(2, 6.5, 6.5, 10.5, 9.5, 9.5),
            Block.box(1, 5.5, 5.5, 1.5, 10.5, 10.5),
            Block.box(1.5, 6, 6, 2, 10, 10),
            Block.box(3, 6, 6, 4, 10, 10),
            Block.box(8, 5.5, 5.5, 9, 10.5, 10.5),
            Block.box(7, 6, 6, 8, 10, 10),
            Block.box(9, 6, 6, 10, 10, 10),
            Block.box(5, 6, 6, 6, 10, 10),
            Block.box(4, 5.5, 5.5, 5, 10.5, 10.5),
            Block.box(6, 5.5, 5.5, 7, 10.5, 10.5)
        );
    }

    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;

    private final Supplier<Integer> maxRange;
    private final Supplier<Boolean> hasFilter;
    private final Supplier<? extends DemagnetizationCoilTile> tileSupplier;

    public DemagnetizationCoilBlock(String registryName, Supplier<Integer> maxRange, Supplier<Boolean> hasFilter, Supplier<? extends DemagnetizationCoilTile> tileSupplier){
        super(Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).harvestTool(ToolType.PICKAXE).harvestLevel(0).strength(3.0F, 5.0F).sound(SoundType.METAL));
        this.setRegistryName(registryName);
        this.maxRange = maxRange;
        this.hasFilter = hasFilter;
        this.tileSupplier = tileSupplier;

        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.DOWN));
    }

    @Override
    public boolean use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit){
        if(!worldIn.isClientSide)
            NetworkHooks.openGui((ServerPlayerEntity)player, new INamedContainerProvider() {
                @Override
                public ITextComponent getDisplayName(){
                    return new StringTextComponent("");
                }

                @Nullable
                @Override
                public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player){
                    return DemagnetizationCoilBlock.this.hasFilter.get() ?
                        new FilteredDemagnetizationCoilContainer(id, player, pos) :
                        new DemagnetizationCoilContainer(id, player, pos);
                }
            }, pos);
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn){
        if(this.hasFilter.get())
            tooltip.add(new TranslationTextComponent("simplemagnets.demagnetization_coil.info.filtered", this.maxRange.get()).withStyle(TextFormatting.AQUA));
        else
            tooltip.add(new TranslationTextComponent("simplemagnets.demagnetization_coil.info.no_filter", this.maxRange.get()).withStyle(TextFormatting.AQUA));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context){
        return this.defaultBlockState().setValue(FACING, context.getClickedFace().getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context){
        return SHAPES[state.getValue(FACING).get3DDataValue()];
    }

    @Override
    public boolean hasTileEntity(BlockState state){
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world){
        return this.tileSupplier.get();
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block,BlockState> builder){
        builder.add(FACING);
    }
}
