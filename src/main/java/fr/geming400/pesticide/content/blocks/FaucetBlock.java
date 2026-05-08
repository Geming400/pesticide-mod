package fr.geming400.pesticide.content.blocks;

import com.mojang.serialization.MapCodec;
import fr.geming400.pesticide.content.ModDataComponents;
import fr.geming400.pesticide.content.blockentities.FaucetBlockEntity;
import fr.geming400.pesticide.content.blockentities.ModBlockEntities;
import fr.geming400.pesticide.content.items.ModItems;
import fr.geming400.pesticide.content.pesticides.PesticideType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Map;

public class FaucetBlock extends BaseEntityBlock {
    /// The radius {@linkplain CropBlock crop blocks} will try to search
    /// for {@linkplain FaucetBlock faucet blocks}
    public static final int FIND_RADIUS = 5;

    public static final BooleanProperty SINGLE = BooleanProperty.create("single");
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape FULL_SHAPE = Block.box(0, 12, 0, 16, 14, 16);
    private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateHorizontal(Block.box(0, 12, 0, 16, 14, 7));

    public FaucetBlock(Properties properties) {
        super(properties);
    }

    @Override
    @NonNull
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(FaucetBlock::new);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(
                FACING,
                SINGLE
        );
    }

    @Override
    @NonNull
    protected BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    @Override
    @NonNull
    protected BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState()
                .setValue(FACING, blockPlaceContext.getHorizontalDirection())
                .setValue(SINGLE, true);
    }

    @Override
    @NonNull
    protected InteractionResult useWithoutItem(@NonNull BlockState blockState, @NonNull Level level, @NonNull BlockPos blockPos, @NonNull Player player, @NonNull BlockHitResult blockHitResult) {
        if (player.isCrouching()) {
            level.setBlock(blockPos, blockState.setValue(SINGLE, !blockState.getValue(SINGLE)), Block.UPDATE_ALL);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    @NonNull
    protected InteractionResult useItemOn(
            @NonNull ItemStack itemStack,
            @NonNull BlockState blockState,
            @NonNull Level level,
            @NonNull BlockPos blockPos,
            @NonNull Player player,
            @NonNull InteractionHand interactionHand,
            @NonNull BlockHitResult blockHitResult
    ) {
        if (itemStack.is(ModItems.PESTICIDE_CONTAINER) && itemStack.has(ModDataComponents.PESTICIDE_TYPE)) {
            PesticideType itemPesticideType = itemStack.get(ModDataComponents.PESTICIDE_TYPE);

            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof FaucetBlockEntity faucetBlockEntity) {
                if (faucetBlockEntity.fill(itemPesticideType)) {
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Override
    @NonNull
    protected VoxelShape getShape(@NonNull BlockState blockState, @NonNull BlockGetter blockGetter, @NonNull BlockPos blockPos, @NonNull CollisionContext collisionContext) {
        return blockState.getValue(SINGLE)
            ? SHAPES.get(blockState.getValue(FACING))
            : FULL_SHAPE;
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(@NonNull BlockPos blockPos, @NonNull BlockState blockState) {
        return new FaucetBlockEntity(blockPos, blockState);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NonNull Level level, @NonNull BlockState blockState, @NonNull BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.FAUCET_BLOCK_ENTITY, FaucetBlockEntity::tick);
    }
}
