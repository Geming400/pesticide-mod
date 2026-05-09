package fr.geming400.pesticide.content.blocks;

import fr.geming400.pesticide.Pesticides;
import fr.geming400.pesticide.content.blockentities.InfestedFarmlandBlockEntity;
import fr.geming400.pesticide.content.blockentities.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class InfestedFarmBlock extends FarmBlockWithEntity {
    @Range(from = 0, to = 1)
    public static final double INFECTION_CHANCE = 0.05;

    public InfestedFarmBlock(Properties properties) {
        super(properties);
    }

    public static void infestBlock(Level level, BlockState blockState, BlockPos blockPos) {
        if (blockState.hasProperty(FarmBlock.MOISTURE)) {
            level.setBlock(blockPos, createStateFromFarmland(blockState), Block.UPDATE_ALL);
        } else {
            Pesticides.LOGGER.error("Couldn't infect block {} at pos {} because it's not a farm block", blockState, blockPos);
        }
    }

    public static BlockState createStateFromFarmland(BlockState state) {
        if (state.hasProperty(FarmBlock.MOISTURE)) {
            return ModBlocks.INFESTED_FARMLAND.defaultBlockState()
                    .setValue(FarmBlock.MOISTURE, state.getValue(FarmBlock.MOISTURE));
        } else {
            throw new RuntimeException("Tried copying farmland's state to InfestedFarmBlock but failed because %s doesn't have the %s property".formatted(state, FarmBlock.MOISTURE));
        }
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(@NonNull BlockPos blockPos, @NonNull BlockState blockState) {
        return new InfestedFarmlandBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NonNull Level level, @NonNull BlockState blockState, @NonNull BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlockEntities.INFESTED_FARMLAND_BLOCK_ENTITY, InfestedFarmlandBlockEntity::tick);
    }
}
