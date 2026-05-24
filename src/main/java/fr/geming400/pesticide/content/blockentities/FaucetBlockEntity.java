package fr.geming400.pesticide.content.blockentities;

import fr.geming400.pesticide.content.ModRegistries;
import fr.geming400.pesticide.content.blocks.FaucetBlock;
import fr.geming400.pesticide.content.pesticides.PesticideType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.time.Duration;
import java.util.Objects;

public class FaucetBlockEntity extends BlockEntity {
    public static final long TICKS_TO_FULLY_DRAIN = Duration.ofMinutes(20).getSeconds() * 20;
    public static final int TICKS_TO_BREAK_FILTER = (int) Duration.ofMinutes(20).getSeconds() * 20;

    public static final int MB_TO_DRAIN = 1000;
    public static final float DRAINING_AMOUNT_PER_TICK = (float) MB_TO_DRAIN / TICKS_TO_FULLY_DRAIN;

    private float mbLeft = 0f;
    private int timeBeforeFilterBreaks = TICKS_TO_BREAK_FILTER;
    @Nullable
    private PesticideType pesticideType = null;
    private boolean hasFilter = false;

    public FaucetBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.FAUCET_BLOCK_ENTITY, blockPos, blockState);
    }

    public Identifier getPesticideTypeID() {
        return Objects.requireNonNull(
                ModRegistries.PESTICIDE_TYPE.getKey(Objects.requireNonNull(this.pesticideType))
        );
    }

    @Override
    protected void saveAdditional(@NonNull ValueOutput output) {
        if (this.mbLeft != 0)
            output.putFloat("mbLeft", this.mbLeft);

        if (this.pesticideType != null)
            output.putString("pesticideType", this.getPesticideTypeID().toString());

        if (this.hasFilter) {
            output.putBoolean("hasFilter", true);
            output.putInt("timeBeforeFilterBreaks", this.timeBeforeFilterBreaks);
        }

        super.saveAdditional(output);
    }

    @Override
    protected void loadAdditional(@NonNull ValueInput input) {
        super.loadAdditional(input);

        this.mbLeft = input.getFloatOr("mbLeft", 0);

        boolean hasPesticide = input.getString("pesticideType").isPresent();
        this.pesticideType = hasPesticide
                ? ModRegistries.PESTICIDE_TYPE.getValue(Identifier.parse(input.getString("pesticideType").orElseThrow()))
                : null;

        this.hasFilter = input.getBooleanOr("hasFilter", false);
        this.timeBeforeFilterBreaks = input.getIntOr("timeBeforeFilterBreaks", TICKS_TO_BREAK_FILTER);
    }

    @Override
    @NonNull
    public CompoundTag getUpdateTag(HolderLookup.@NonNull Provider registryLookup) {
        return this.saveWithoutMetadata(registryLookup);
    }

    @NotNull
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    /// Gets the number of mb (milli-buckets) left
    /// in this faucet
    public float getMbLeft() {
        return this.mbLeft;
    }

    public void setMbLeft(float mbLeft) {
        this.mbLeft = mbLeft;
        this.normalizeLeftMb();

        this.setChanged();
    }

    public void drainMb(BlockState faucetBlockState) {
        // We drain more pesticide when there are 2 jets in the faucet block
        if (faucetBlockState.getValueOrElse(FaucetBlock.SINGLE, true)) {
            this.mbLeft -= DRAINING_AMOUNT_PER_TICK;
        } else {
            this.mbLeft -= DRAINING_AMOUNT_PER_TICK * 2;
        }

        // If it drained 1B of pesticide
        // we remove the filter
        if ((int) this.mbLeft % 1000 == 0) {
            this.hasFilter = false;
        }

        this.setChanged();
        this.normalizeLeftMb();
    }

    public void drainMb(float amount) {
        this.mbLeft -= amount;
        this.normalizeLeftMb();
    }

    public void normalizeLeftMb() {
        this.mbLeft = Math.max(0, this.mbLeft);
    }

    @Nullable
    public PesticideType getPesticideType() {
        return this.pesticideType;
    }

    public void setPesticideType(@Nullable PesticideType pesticideType) {
        this.pesticideType = pesticideType;
        this.setChanged();
    }

    public boolean hasFilter() {
        return this.hasFilter;
    }

    public void hasFilter(boolean hasFilter) {
        this.hasFilter = hasFilter;
    }

    public float getTimeBeforeFilterBreaks() {
        return this.timeBeforeFilterBreaks;
    }

    public void setTimeBeforeFilterBreaks(int timeBeforeFilterBreaks) {
        this.timeBeforeFilterBreaks = timeBeforeFilterBreaks;
        this.normalizeTimeBeforeFilterBreaks();

        this.setChanged();
    }

    public void tryBreakingFilter() {
        this.timeBeforeFilterBreaks -= 1;
        if (this.timeBeforeFilterBreaks <= 0) {
            this.hasFilter = false;
            this.timeBeforeFilterBreaks = TICKS_TO_BREAK_FILTER;
        }

        this.normalizeTimeBeforeFilterBreaks();
    }

    public void normalizeTimeBeforeFilterBreaks() {
        this.timeBeforeFilterBreaks = Math.max(0, this.timeBeforeFilterBreaks);
    }

    public boolean fill(PesticideType type) {
        if (this.pesticideType == type || this.pesticideType == null) {
            this.mbLeft += 1000;
            this.pesticideType = type;

            this.setChanged();

            return true;
        }

        return false;
    }

    public boolean isActive() {
        return this.mbLeft > 0;
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, FaucetBlockEntity faucetBlockEntity) {
        if (blockState.getValueOrElse(FaucetBlock.ENABLED, false)) {
            faucetBlockEntity.drainMb(blockState);
            faucetBlockEntity.tryBreakingFilter();
        }
    }
}
