package fr.geming400.pesticide.content.blockentities;

import fr.geming400.pesticide.content.ModRegistries;
import fr.geming400.pesticide.content.pesticides.PesticideType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NonNull;

import java.time.Duration;
import java.util.Objects;

public class InfestedFarmlandBlockEntity extends BlockEntity {
    /// The time for an infest farmland to fully show as "infected" (aka the block has finished tinting)
    private static final float INFECTION_TIME = Duration.ofMinutes(30).getSeconds() * 20;
    private static final float INFECTION_STEP = 1 / INFECTION_TIME;
    private static final int TIME_BEFORE_BLOCK_UPDATE = 20 * 30; // Every 30 seconds

    /// Used to prevent crashing
    private static final PesticideType DEFAULT_PESTICIDE_TYPE = Objects.requireNonNull(ModRegistries.PESTICIDE_TYPE.byId(0));

    @Range(from = 0, to = 1)
    private float infectionProgress = 0f;
    private PesticideType pesticideType;
    private int blockUpdateCountdown = 0; // Every 30 seconds

    public InfestedFarmlandBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.INFESTED_FARMLAND_BLOCK_ENTITY, blockPos, blockState);
    }

    @Override
    protected void saveAdditional(@NonNull ValueOutput output) {
        if (this.infectionProgress != 0)
            output.putFloat("infectionProgress", this.infectionProgress);

        // Should never happen but we never know ig
        if (this.pesticideType != null)
            output.putString("pesticideType", this.pesticideType.getID().toString());

        super.saveAdditional(output);
    }

    @Override
    protected void loadAdditional(@NonNull ValueInput input) {
        super.loadAdditional(input);

        this.infectionProgress = input.getFloatOr("infectionProgress", 0);
        this.pesticideType = PesticideType.fromID(
                Identifier.parse(
                        input.getStringOr("pesticideType", DEFAULT_PESTICIDE_TYPE.getID().toString())
                )
        );
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

            this.blockUpdateCountdown += 1;
            if (level.isClientSide() && this.blockUpdateCountdown > TIME_BEFORE_BLOCK_UPDATE) {
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

                this.blockUpdateCountdown = 0;
            }
        }
    }

    @NonNull
    public PesticideType getPesticideType() {
        return this.pesticideType == null ? DEFAULT_PESTICIDE_TYPE : this.pesticideType;
    }

    public void setPesticideType(PesticideType pesticideType) {
        this.pesticideType = pesticideType;
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, InfestedFarmlandBlockEntity blockEntity) {
        blockEntity.incrementInfectionProgress(level, blockPos, blockState);
    }
}
