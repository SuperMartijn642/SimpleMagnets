package com.supermartijn642.simplemagnets;

import com.supermartijn642.core.CommonUtils;
import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.block.*;
import com.supermartijn642.simplemagnets.gui.DemagnetizationCoilContainer;
import com.supermartijn642.simplemagnets.gui.FilteredDemagnetizationCoilContainer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created 2/19/2021 by SuperMartijn642
 */
public class DemagnetizationCoilBlock extends BaseBlock implements EntityHoldingBlock {

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
        SHAPES = new BlockShape[Direction.values().length];
        SHAPES[Direction.DOWN.get3DDataValue()] = shape;
        SHAPES[Direction.UP.get3DDataValue()] = shape.rotate(Direction.Axis.X).rotate(Direction.Axis.X);
        SHAPES[Direction.NORTH.get3DDataValue()] = shape.rotate(Direction.Axis.X).rotate(Direction.Axis.X).rotate(Direction.Axis.X);
        SHAPES[Direction.EAST.get3DDataValue()] = shape.rotate(Direction.Axis.Z).rotate(Direction.Axis.Z).rotate(Direction.Axis.Z);
        SHAPES[Direction.SOUTH.get3DDataValue()] = shape.rotate(Direction.Axis.X);
        SHAPES[Direction.WEST.get3DDataValue()] = shape.rotate(Direction.Axis.Z);
    }

    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;

    private final Supplier<Integer> maxRange;
    private final Supplier<Boolean> hasFilter;
    private final Supplier<BaseBlockEntityType<?>> blockEntityType;

    public DemagnetizationCoilBlock(Supplier<Integer> maxRange, Supplier<Boolean> hasFilter, Supplier<BaseBlockEntityType<?>> blockEntityType){
        super(false, BlockProperties.create(Material.METAL, MaterialColor.COLOR_GRAY).requiresCorrectTool().destroyTime(3).explosionResistance(5).sound(SoundType.METAL));
        this.maxRange = maxRange;
        this.hasFilter = hasFilter;
        this.blockEntityType = blockEntityType;

        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.DOWN));
    }

    @Override
    protected InteractionFeedback interact(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, Direction hitSide, Vector3d hitLocation){
        if(!level.isClientSide)
            CommonUtils.openContainer(this.hasFilter.get() ?
                new FilteredDemagnetizationCoilContainer(player, pos) :
                new DemagnetizationCoilContainer(player, pos)
            );
        return InteractionFeedback.SUCCESS;
    }

    @Override
    protected void appendItemInformation(ItemStack stack, @Nullable IBlockReader level, Consumer<ITextComponent> info, boolean advanced){
        if(this.hasFilter.get())
            info.accept(TextComponents.translation("simplemagnets.demagnetization_coil.info.filtered", TextComponents.number(this.maxRange.get()).color(TextFormatting.GOLD).get()).color(TextFormatting.GRAY).get());
        else
            info.accept(TextComponents.translation("simplemagnets.demagnetization_coil.info.no_filter", TextComponents.number(this.maxRange.get()).color(TextFormatting.GOLD).get()).color(TextFormatting.GRAY).get());
    }

    @Override
    public TileEntity createNewBlockEntity(){
        return this.blockEntityType.get().create();
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context){
        return this.defaultBlockState().setValue(FACING, context.getClickedFace().getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader level, BlockPos pos, ISelectionContext context){
        return SHAPES[state.getValue(FACING).get3DDataValue()].getUnderlying();
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block,BlockState> builder){
        builder.add(FACING);
    }
}
