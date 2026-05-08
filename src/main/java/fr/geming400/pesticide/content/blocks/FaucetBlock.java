package fr.geming400.pesticide.content.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;

import java.util.Map;

public class FaucetBlock extends HorizontalDirectionalBlock {
    public static final BooleanProperty SINGLE = BooleanProperty.create("single");

    private static final VoxelShape FULL_SHAPE = Block.box(0, 12, 0, 16, 14, 16);
    private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateHorizontal(Block.box(0, 12, 0, 16, 14, 7));

    public FaucetBlock(Properties properties) {
        super(properties);
    }

    @Override
    @NonNull
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
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
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState()
                .setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite())
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
    protected VoxelShape getShape(@NonNull BlockState blockState, @NonNull BlockGetter blockGetter, @NonNull BlockPos blockPos, @NonNull CollisionContext collisionContext) {
        if (blockState.getValue(SINGLE)) {
            return SHAPES.get(blockState.getValue(FACING));
        } else {
            return FULL_SHAPE;
        }
    }
}
