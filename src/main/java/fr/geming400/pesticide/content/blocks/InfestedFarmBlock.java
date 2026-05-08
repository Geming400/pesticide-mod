package fr.geming400.pesticide.content.blocks;

import fr.geming400.pesticide.Pesticides;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Range;

public class InfestedFarmBlock extends FarmBlock {
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
}
