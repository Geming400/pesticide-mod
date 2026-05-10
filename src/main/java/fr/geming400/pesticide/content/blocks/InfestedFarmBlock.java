package fr.geming400.pesticide.content.blocks;

import fr.geming400.pesticide.Pesticides;
import fr.geming400.pesticide.content.ModAttachments;
import fr.geming400.pesticide.content.blockentities.InfestedFarmlandBlockEntity;
import fr.geming400.pesticide.content.blockentities.ModBlockEntities;
import fr.geming400.pesticide.content.effects.BadFarmerEffect;
import fr.geming400.pesticide.content.effects.ModEffects;
import fr.geming400.pesticide.content.pesticides.PesticideType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

public class InfestedFarmBlock extends FarmBlockWithEntity {
    @Range(from = 0, to = 1)
    public static final double INFECTION_CHANCE = 0.1;
    @Range(from = 0, to = 1)
    public static final double MOIST_INFECTION_CHANCE = 0.15;

    public InfestedFarmBlock(Properties properties) {
        super(properties);
    }

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public void stepOn(@NonNull Level level, @NonNull BlockPos blockPos, @NonNull BlockState blockState, @NonNull Entity entity) {
        if (!level.isClientSide() && entity instanceof LivingEntity livingEntity) {
            int timeSpentOnInfestedFarmland = entity.getAttachedOrCreate(ModAttachments.TIME_SPENT_ON_INFESTED_FARMLAND) ;
            entity.setAttached(ModAttachments.TIME_SPENT_ON_INFESTED_FARMLAND, timeSpentOnInfestedFarmland + 1);

            if (timeSpentOnInfestedFarmland >= BadFarmerEffect.TICKS_BEFORE_APPLYING && !livingEntity.hasEffect(ModEffects.BAD_FARMER)) {
                livingEntity.setAttached(ModAttachments.TIME_SPENT_ON_INFESTED_FARMLAND, 0);
                livingEntity.addEffect(
                        new MobEffectInstance(
                                ModEffects.BAD_FARMER,
                                BadFarmerEffect.DEFAULT_APPLY_TIME,
                                0,   // amplifier
                                false, // ambient
                                true,  // showParticles
                                false  // showIcon
                        )
                );
            }
        }
    }

    @Override
    protected void randomTick(@NonNull BlockState blockState, @NonNull ServerLevel serverLevel, @NonNull BlockPos blockPos, @NonNull RandomSource randomSource) {
        super.randomTick(blockState, serverLevel, blockPos, randomSource);

        Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(serverLevel.getRandom());
        BlockPos chosenPosToInfect = blockPos.relative(direction);
        BlockState blockToInfect = serverLevel.getBlockState(chosenPosToInfect);

        BlockEntity blockEntity = serverLevel.getBlockEntity(blockPos);
        if (blockEntity instanceof InfestedFarmlandBlockEntity infestedFarmlandBlockEntity) {
            if (serverLevel.getRandom().nextDouble() <= getInfectionChance(blockToInfect, false) && blockToInfect.is(Blocks.FARMLAND))
                infectBlock(serverLevel, blockToInfect, chosenPosToInfect, infestedFarmlandBlockEntity.getPesticideType());
        }
    }

    @Override
    @Nullable
    public InfestedFarmlandBlockEntity newBlockEntity(@NonNull BlockPos blockPos, @NonNull BlockState blockState) {
        return new InfestedFarmlandBlockEntity(blockPos, blockState);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NonNull Level level, @NonNull BlockState blockState, @NonNull BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlockEntities.INFESTED_FARMLAND_BLOCK_ENTITY, InfestedFarmlandBlockEntity::tick);
    }


    public static void infectBlock(Level level, BlockState blockState, BlockPos blockPos, PesticideType pesticideType) {
        if (blockState.hasProperty(FarmBlock.MOISTURE)) {
            level.setBlock(blockPos, createStateFromFarmland(blockState), Block.UPDATE_ALL);

            InfestedFarmlandBlockEntity infestFarmlandBlockEntity = Objects.requireNonNull(ModBlocks.INFESTED_FARMLAND.newBlockEntity(blockPos, createStateFromFarmland(blockState)));
            infestFarmlandBlockEntity.setPesticideType(pesticideType);

            level.setBlockEntity(infestFarmlandBlockEntity);
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

    public static double getInfectionChance(BlockState farmBlockState, boolean isFaucet) {
        if (isFaucet) {
            return INFECTION_CHANCE;
        } else {
            return farmBlockState.getValueOrElse(MOISTURE, 0) >= 0
                    ? INFECTION_CHANCE
                    : MOIST_INFECTION_CHANCE;
        }
    }
}
