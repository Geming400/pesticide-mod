package fr.geming400.pesticide.content.blockentities;

import fr.geming400.pesticide.content.ModRegistries;
import fr.geming400.pesticide.content.blocks.FaucetBlock;
import fr.geming400.pesticide.content.pesticides.PesticideType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.time.Duration;
import java.util.Objects;

public class FaucetBlockEntity extends BlockEntity {
    public static final long TICKS_TO_FULLY_DRAIN = Duration.ofMinutes(20).getSeconds() * 20;
    public static final int MB_TO_DRAIN = 1000;
    public static final float DRAINING_AMOUNT = (float) MB_TO_DRAIN / TICKS_TO_FULLY_DRAIN;

    private float mbLeft = 0f;
    @Nullable
    private PesticideType pesticideType;

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
    }

    @Override
    @NonNull
    public CompoundTag getUpdateTag(HolderLookup.@NonNull Provider registryLookup) {
        return saveWithoutMetadata(registryLookup);
    }

    /// Gets the number of mb (milli-buckets) left
    /// in this faucet
    public float getMbLeft() {
        return this.mbLeft;
    }

    public void setMbLeft(float mbLeft) {
        this.mbLeft = mbLeft;
        this.normalizeLeftMb();

        setChanged();
    }

    public void drainMb(BlockState faucetBlockState) {
        // We drain more pesticide when there are 2 jets in the faucet block
        if (faucetBlockState.getValueOrElse(FaucetBlock.SINGLE, false)) {
            this.mbLeft -= DRAINING_AMOUNT;
        } else {
            this.mbLeft -= DRAINING_AMOUNT * 2;
        }

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
        setChanged();
    }

    public boolean fill(PesticideType type) {
        if (this.pesticideType == type || this.pesticideType == null) {
            this.mbLeft += 1000;
            this.pesticideType = type;

            setChanged();

            return true;
        }

        return false;
    }

    public boolean isActive() {
        return this.mbLeft > 0;
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, FaucetBlockEntity faucetBlockEntity) {
        if (blockState.getValueOrElse(FaucetBlock.ENABLED, false))
            faucetBlockEntity.drainMb(blockState);
    }
}
