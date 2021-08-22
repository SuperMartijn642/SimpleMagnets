package com.supermartijn642.simplemagnets;

import com.supermartijn642.simplemagnets.gui.DemagnetizationCoilContainer;
import com.supermartijn642.simplemagnets.gui.FilteredDemagnetizationCoilContainer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
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
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * Created 2/19/2021 by SuperMartijn642
 */
public class DemagnetizationCoilBlock extends Block implements EntityBlock {

    private static final VoxelShape[] SHAPES;

    static{
        SHAPES = new VoxelShape[Direction.values().length];
        SHAPES[Direction.DOWN.get3DDataValue()] = Shapes.or(
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
        SHAPES[Direction.UP.get3DDataValue()] = Shapes.or(
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
        SHAPES[Direction.NORTH.get3DDataValue()] = Shapes.or(
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
        SHAPES[Direction.EAST.get3DDataValue()] = Shapes.or(
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
        SHAPES[Direction.SOUTH.get3DDataValue()] = Shapes.or(
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
        SHAPES[Direction.WEST.get3DDataValue()] = Shapes.or(
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
    private final BiFunction<BlockPos,BlockState,? extends DemagnetizationCoilTile> tileSupplier;

    public DemagnetizationCoilBlock(String registryName, Supplier<Integer> maxRange, Supplier<Boolean> hasFilter, BiFunction<BlockPos,BlockState,? extends DemagnetizationCoilTile> tileSupplier){
        super(Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(3.0F, 5.0F).sound(SoundType.METAL));
        this.setRegistryName(registryName);
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
                    return new TextComponent("");
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
            tooltip.add(new TranslatableComponent("simplemagnets.demagnetization_coil.info.filtered", this.maxRange.get()).withStyle(ChatFormatting.AQUA));
        else
            tooltip.add(new TranslatableComponent("simplemagnets.demagnetization_coil.info.no_filter", this.maxRange.get()).withStyle(ChatFormatting.AQUA));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context){
        return this.defaultBlockState().setValue(FACING, context.getClickedFace().getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context){
        return SHAPES[state.getValue(FACING).get3DDataValue()];
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
