package fr.geming400.pesticide.content.items;

import fr.geming400.pesticide.content.ModDataComponents;
import fr.geming400.pesticide.content.blockentities.InfestedFarmlandBlockEntity;
import fr.geming400.pesticide.content.blocks.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;

public class CottonSwab extends Item {
    public CottonSwab(Properties properties) {
        super(properties);
    }

    private InteractionResult tryUsing(ServerLevel level, BlockPos infectedFarmlandPos, ItemStack itemStack) {
        BlockEntity blockEntity = level.getBlockEntity(infectedFarmlandPos);
        if (blockEntity instanceof InfestedFarmlandBlockEntity infestedFarmlandBlockEntity) {
            // When the infection progress it at 50%, the cotton swab
            // will show (at 100% chance) that it's infected
            //
            // But in all cases it will get marked as "used"
            if (level.getRandom().nextFloat() <= infestedFarmlandBlockEntity.getInfectionProgress() * 2) {
                itemStack.set(ModDataComponents.PESTICIDE_TYPE, infestedFarmlandBlockEntity.getPesticideType());
            }

            setUsed(itemStack);
            return InteractionResult.SUCCESS_SERVER;
        }

        return InteractionResult.PASS;
    }

    @Override
    @NonNull
    public InteractionResult useOn(@NonNull UseOnContext useOnContext) {
        ItemStack itemStack = useOnContext.getItemInHand();
        if (itemStack.has(ModDataComponents.COTTON_SWAB_USED) || itemStack.has(ModDataComponents.PESTICIDE_TYPE))
            return InteractionResult.PASS;

        if (!useOnContext.getLevel().isClientSide()) {
            ServerLevel level = (ServerLevel) useOnContext.getLevel();

            boolean markUsed = false;

            BlockPos block = useOnContext.getClickedPos();
            BlockState blockState = level.getBlockState(block);
            if (blockState.getBlock() instanceof CropBlock) {
                BlockPos farmlandPos = block.below();
                BlockState farmlandState = level.getBlockState(farmlandPos);

                if (farmlandState.is(Blocks.FARMLAND)) {
                    markUsed = true;
                } else if (farmlandState.is(ModBlocks.INFESTED_FARMLAND)) {
                    return this.tryUsing(level, farmlandPos, itemStack);
                }
            } else if (blockState.is(ModBlocks.INFESTED_FARMLAND)) {
                return this.tryUsing(level, useOnContext.getClickedPos(), itemStack);
            } else if (blockState.is(Blocks.FARMLAND)) {
                markUsed = true;
            }

            if (markUsed) {
                setUsed(itemStack);
                return InteractionResult.SUCCESS;
            }

            return InteractionResult.PASS;
        }

        return InteractionResult.PASS;
    }

    @Override
    @NonNull
    public InteractionResult use(@NonNull Level level, @NonNull Player player, @NonNull InteractionHand interactionHand) {
        if (player.isCrouching() && isDirty(player.getMainHandItem())) {
            reset(player.getMainHandItem());
            return InteractionResult.SUCCESS;
        } else {
            return super.use(level, player, interactionHand);
        }
    }

    @Override
    @NonNull
    public Component getName(@NonNull ItemStack itemStack) {
        if (isInfected(itemStack)) {
            return Component.translatable(this.descriptionId + ".infected");
        } else if (isDirty(itemStack)) {
            return Component.translatable(this.descriptionId + ".dirty");
        }

        return super.getName(itemStack);
    }

    public static boolean isInfected(ItemStack itemStack) {
        return itemStack.has(ModDataComponents.PESTICIDE_TYPE);
    }

    public static boolean isDirty(ItemStack itemStack) {
        return itemStack.has(ModDataComponents.COTTON_SWAB_USED);
    }

    private static ItemStack setUsed(ItemStack itemStack) {
        itemStack.set(ModDataComponents.COTTON_SWAB_USED, true);
        return itemStack;
    }

    private static ItemStack reset(ItemStack itemStack) {
        itemStack.remove(ModDataComponents.PESTICIDE_TYPE);
        itemStack.remove(ModDataComponents.COTTON_SWAB_USED);

        return itemStack;
    }
}
