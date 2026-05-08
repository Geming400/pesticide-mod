package fr.geming400.pesticide.mixin;

import fr.geming400.pesticide.content.blockentities.FaucetBlockEntity;
import fr.geming400.pesticide.content.blocks.FaucetBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiPredicate;

@Debug(export = true)
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

    @ModifyConstant(method = "randomTick", constant = @Constant(floatValue = 25.0f))
    private float randomTick(float value, BlockState blockState, ServerLevel serverLevel, BlockPos cropPos, RandomSource randomSource) {
        AtomicReference<Float> growSpeedFactor = new AtomicReference<>();
        boolean succeeded = iterateOverRadius(
                FaucetBlock.FIND_RADIUS,
                cropPos,
                serverLevel,
                FaucetBlock.class,
                (faucetPos, faucetState) -> {
                    Direction faucetDirection = faucetState.getValue(FaucetBlock.FACING);
                    boolean isSingle = faucetState.getValue(FaucetBlock.SINGLE);

                    // If the faucet is in "single mode" and the crop block is not in the direction
                    // the faucet is facing, then we return false to indicate
                    // that this operation didn't succeed
                    if (isSingle && !isPositionFacedAgainstDirection(cropPos, faucetPos, faucetDirection))
                        return false;

                    if (cropPos.getY() > faucetPos.getY())
                        return false;

                    BlockEntity blockEntity = serverLevel.getBlockEntity(faucetPos);
                    if (blockEntity instanceof FaucetBlockEntity faucetBlockEntity) {
                        if (faucetBlockEntity.getPesticideType() != null && faucetBlockEntity.isActive()) {
                            growSpeedFactor.set(faucetBlockEntity.getPesticideType().growSpeedFactor());
                            return true;
                        }
                    }

                    return false;
                }
        );

        return succeeded
                ? value * 1 / (growSpeedFactor.get())
                : value;
    }
}
