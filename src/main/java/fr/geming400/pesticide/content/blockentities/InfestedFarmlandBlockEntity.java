package fr.geming400.pesticide.content.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NonNull;

import java.time.Duration;

public class InfestedFarmlandBlockEntity extends BlockEntity {
    private static final float INFECTION_TIME = Duration.ofMinutes(30).getSeconds() * 20;
    private static final float INFECTION_STEP = 1 / INFECTION_TIME;
    @Range(from = 0, to = 1)
    private float infectionProgress = 0f;

    public InfestedFarmlandBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.INFESTED_FARMLAND_BLOCK_ENTITY, blockPos, blockState);
    }

    @Override
    protected void saveAdditional(@NonNull ValueOutput output) {
        if (this.infectionProgress != 0)
            output.putFloat("infectionProgress", this.infectionProgress);

        super.saveAdditional(output);
    }

    @Override
    protected void loadAdditional(@NonNull ValueInput input) {
        super.loadAdditional(input);

        this.infectionProgress = input.getFloatOr("infectionProgress", 0);
    }

    @Override
    @NonNull
    public CompoundTag getUpdateTag(HolderLookup.@NonNull Provider registryLookup) {
        return saveWithoutMetadata(registryLookup);
    }

    @Range(from = 0, to = 1)
    public float getInfectionProgress() {
        return this.infectionProgress;
    }

    public void incrementInfectionProgress(Level level, BlockPos pos, BlockState blockState) {
        if (this.infectionProgress < 1) {
            this.infectionProgress += INFECTION_STEP;
            this.infectionProgress = Math.min(this.infectionProgress, 1);

            if (level.isClientSide()) {
                // This allows for the block's tint to get updated dynamically
                //
                // Minecraft store tint values in a cache, sending
                // a block update updates the cache for this block
                level.sendBlockUpdated(
                        pos,
                        blockState,
                        blockState,
                        Block.UPDATE_CLIENTS
                );
            }
        }
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, InfestedFarmlandBlockEntity blockEntity) {
        blockEntity.incrementInfectionProgress(level, blockPos, blockState);
    }
}
