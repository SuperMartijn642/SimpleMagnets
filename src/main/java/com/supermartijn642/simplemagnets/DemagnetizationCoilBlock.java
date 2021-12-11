package com.supermartijn642.simplemagnets;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.block.BaseBlock;
import com.supermartijn642.core.block.BlockShape;
import com.supermartijn642.simplemagnets.gui.DemagnetizationCoilContainer;
import com.supermartijn642.simplemagnets.gui.FilteredDemagnetizationCoilContainer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * Created 2/19/2021 by SuperMartijn642
 */
public class DemagnetizationCoilBlock extends BaseBlock implements EntityBlock {

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
    private final BiFunction<BlockPos,BlockState,? extends DemagnetizationCoilTile> tileSupplier;

    public DemagnetizationCoilBlock(String registryName, Supplier<Integer> maxRange, Supplier<Boolean> hasFilter, BiFunction<BlockPos,BlockState,? extends DemagnetizationCoilTile> tileSupplier){
        super(registryName, false, Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(3.0F, 5.0F).sound(SoundType.METAL));
        this.maxRange = maxRange;
        this.hasFilter = hasFilter;
        this.tileSupplier = tileSupplier;

        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.DOWN));
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit){
        if(!worldIn.isClientSide)
            NetworkHooks.openGui((ServerPlayer)player, new MenuProvider() {
                @Override
                public Component getDisplayName(){
                    return TextComponents.empty().get();
                }

                @Nullable
                @Override
                public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player){
                    return DemagnetizationCoilBlock.this.hasFilter.get() ?
                        new FilteredDemagnetizationCoilContainer(id, player, pos) :
                        new DemagnetizationCoilContainer(id, player, pos);
                }
            }, pos);
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn){
        if(this.hasFilter.get())
            tooltip.add(TextComponents.translation("simplemagnets.demagnetization_coil.info.filtered", TextComponents.number(this.maxRange.get()).color(ChatFormatting.GOLD).get()).color(ChatFormatting.GRAY).get());
        else
            tooltip.add(TextComponents.translation("simplemagnets.demagnetization_coil.info.no_filter", TextComponents.number(this.maxRange.get()).color(ChatFormatting.GOLD).get()).color(ChatFormatting.GRAY).get());
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context){
        return this.defaultBlockState().setValue(FACING, context.getClickedFace().getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context){
        return SHAPES[state.getValue(FACING).get3DDataValue()].getUnderlying();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block,BlockState> builder){
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state){
        return this.tileSupplier.apply(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> blockEntityType){
        return (world2, pos, state2, entity) -> {
            if(entity instanceof DemagnetizationCoilTile)
                ((DemagnetizationCoilTile)entity).tick();
        };
    }
}
