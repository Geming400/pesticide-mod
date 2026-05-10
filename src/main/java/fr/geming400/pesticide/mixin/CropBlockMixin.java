package fr.geming400.pesticide.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import fr.geming400.pesticide.content.blockentities.FaucetBlockEntity;
import fr.geming400.pesticide.content.blocks.FaucetBlock;
import fr.geming400.pesticide.content.blocks.InfestedFarmBlock;
import fr.geming400.pesticide.content.blocks.ModBlocks;
import fr.geming400.pesticide.content.effects.BadFarmerEffect;
import fr.geming400.pesticide.content.effects.ModEffects;
import fr.geming400.pesticide.content.pesticides.PesticideType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiPredicate;

@Mixin(CropBlock.class)
abstract class CropBlockMixin {
    @Unique
    private static <B extends Block> boolean iterateOverRadius(
            int radius,
            BlockPos center,
            ServerLevel serverLevel,
            Class<B> clazz,
            BiPredicate<BlockPos, BlockState> whenFound
    ) {
        for (int x = -radius/2; x < radius/2; x++) {
            for (int y = -radius/2; y < radius/2; y++) {
                for (int z = -radius/2; z < radius/2; z++) {
                    BlockPos pos = center.offset(x, y, z);
                    BlockState blockState = serverLevel.getBlockState(pos);
                    if (clazz.isAssignableFrom(blockState.getBlock().getClass())) {
                        return whenFound.test(pos, blockState);
                    }
                }
            }
        }

        return false;
    }

    @Unique
    private static boolean isPositionFacedAgainstDirection(BlockPos originPos, BlockPos otherPos, Direction direction) {
        return switch (direction) {
            case NORTH -> otherPos.getZ() <= originPos.getZ();
            case SOUTH -> otherPos.getZ() >= originPos.getZ();
            case WEST -> otherPos.getX() >= originPos.getX();
            case EAST -> otherPos.getX() <= originPos.getX();
            case UP -> otherPos.getY() >= originPos.getY();
            case DOWN -> otherPos.getY() <= originPos.getY();
        };
    }

    @Unique
    private static IterationResult checkIfHasFaucetNearCrop(ServerLevel serverLevel, BlockPos cropPos) {
        AtomicReference<PesticideType> pesticideType = new AtomicReference<>();
        boolean succeeded = iterateOverRadius(
                FaucetBlock.FIND_RADIUS,
                cropPos,
                serverLevel,
                FaucetBlock.class,
                (faucetPos, faucetState) -> {
                    Direction faucetDirection = faucetState.getValue(FaucetBlock.FACING);
                    // Determines if the block is single, aka has no girlfriend or boyfriend idk
                    // Or maybe blocks are with enby people ??
                    boolean isSingle = faucetState.getValue(FaucetBlock.SINGLE);
                    boolean isToggled = faucetState.getValue(FaucetBlock.ENABLED);

                    // If the faucet is in "single mode" and the crop block is not in the direction
                    // the faucet is facing, then we return false to indicate
                    // that this operation didn't succeed
                    if (isSingle && !isPositionFacedAgainstDirection(cropPos, faucetPos, faucetDirection))
                        return false;
                    else if (cropPos.getY() > faucetPos.getY())
                        return false;
                    else if (!isToggled)
                        return false;

                    BlockEntity blockEntity = serverLevel.getBlockEntity(faucetPos);
                    if (blockEntity instanceof FaucetBlockEntity faucetBlockEntity) {
                        if (faucetBlockEntity.getPesticideType() != null && faucetBlockEntity.isActive()) {
                            pesticideType.set(faucetBlockEntity.getPesticideType());
                            return true;
                        }
                    }

                    return false;
                }
        );

        return new IterationResult(pesticideType.get(), succeeded);
    }

    @WrapMethod(method = "mayPlaceOn")
    private boolean mayPlaceOn(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Operation<Boolean> original) {
        return original.call(blockState, blockGetter, blockPos) || blockState.is(ModBlocks.INFESTED_FARMLAND);
    }

    @Inject(at = @At("TAIL"), method = "entityInside")
    private void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity, InsideBlockEffectApplier insideBlockEffectApplier, boolean bl, CallbackInfo ci) {
        if (
                !level.isClientSide()
                && entity instanceof LivingEntity livingEntity
                && livingEntity.hasEffect(ModEffects.BAD_FARMER)
                && entity.getRandom().nextDouble() <= BadFarmerEffect.CHANCE_TO_BREAK_CROP
        ) {
            level.destroyBlock(blockPos, true, entity);
        }
    }

    @Inject(at = @At("HEAD"), method = "randomTick", cancellable = true)
    private void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos cropPos, RandomSource randomSource, CallbackInfo ci) {
        BlockState farmblockState = serverLevel.getBlockState(cropPos.below());
        if (farmblockState.is(ModBlocks.INFESTED_FARMLAND)) {
            ci.cancel();
        } else if (serverLevel.random.nextDouble() <= InfestedFarmBlock.getInfectionChance(farmblockState, true) && checkIfHasFaucetNearCrop(serverLevel, cropPos).succeeded()) {
            InfestedFarmBlock.infectBlock(serverLevel, farmblockState, cropPos.below());

            double x = cropPos.getX() + 0.5;
            double y = cropPos.getY() + 0.5;
            double z = cropPos.getZ() + 0.5;
            serverLevel.addParticle(new DustParticleOptions(0x540000, 0.5f), x, y, z, 0.0, 0.0, 0.0);
        }
    }

    @Inject(at = @At("HEAD"), method = "growCrops", cancellable = true)
    private void growCrops(Level level, BlockPos cropPos, BlockState blockState, CallbackInfo ci) {
        BlockState belowBlockState = level.getBlockState(cropPos.below());
        if (belowBlockState.is(ModBlocks.INFESTED_FARMLAND))
            ci.cancel();
    }

    @Inject(at = @At("HEAD"), method = "isValidBonemealTarget", cancellable = true)
    private void isValidBonemealTarget(LevelReader level, BlockPos cropPos, BlockState blockState, CallbackInfoReturnable<Boolean> cir) {
        BlockState belowBlockState = level.getBlockState(cropPos.below());
        if (belowBlockState.is(ModBlocks.INFESTED_FARMLAND))
            cir.setReturnValue(false);
    }

    @Inject(at = @At("HEAD"), method = "isBonemealSuccess", cancellable = true)
    private void isBonemealSuccess(Level level, RandomSource randomSource, BlockPos cropPos, BlockState blockState, CallbackInfoReturnable<Boolean> cir) {
        BlockState belowBlockState = level.getBlockState(cropPos.below());
        if (belowBlockState.is(ModBlocks.INFESTED_FARMLAND))
            cir.setReturnValue(false);
    }

    @ModifyConstant(method = "randomTick", constant = @Constant(floatValue = 25.0f))
    private float modifyGrowSpeed(float value, BlockState blockState, ServerLevel serverLevel, BlockPos cropPos, RandomSource randomSource) {
        IterationResult hasFaucetNearby = checkIfHasFaucetNearCrop(serverLevel, cropPos);

        return hasFaucetNearby.succeeded()
                ? value * (1 / hasFaucetNearby.pesticideType().growSpeedFactor())
                : value;
    }

    record IterationResult(PesticideType pesticideType, boolean succeeded) {}
}
