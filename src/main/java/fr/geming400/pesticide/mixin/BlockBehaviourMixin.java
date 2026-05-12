package fr.geming400.pesticide.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import fr.geming400.pesticide.content.blockentities.InfestedFarmlandBlockEntity;
import fr.geming400.pesticide.content.blocks.InfestedFarmBlock;
import fr.geming400.pesticide.content.blocks.ModBlocks;
import fr.geming400.pesticide.content.effects.ModEffects;
import fr.geming400.pesticide.content.items.food.ModFoodProperties;
import fr.geming400.pesticide.content.pesticides.PesticideType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;
import java.util.List;

/**
 * This mixin is used to change the drop of crop blocks when they have been infected by {@link InfestedFarmBlock}s
 */
@Mixin(BlockBehaviour.class)
public class BlockBehaviourMixin {
    @WrapMethod(method = "getDrops")
    private List<ItemStack> getDrops(BlockState blockState, LootParams.Builder builder, Operation<List<ItemStack>> original) {
        ServerLevel level = builder.getLevel();

        if (blockState.getBlock() instanceof CropBlock) {
            Vec3 origin = builder.getOptionalParameter(LootContextParams.ORIGIN);
            Entity thisEntity = builder.getOptionalParameter(LootContextParams.THIS_ENTITY);

            if (origin != null) {
                BlockPos cropPos = BlockPos.containing(origin);

                BlockPos farmlandPos = cropPos.below();
                BlockState farmlandBlockState = level.getBlockState(farmlandPos);
                BlockEntity blockEntity = level.getBlockEntity(farmlandPos);
                if (farmlandBlockState.is(ModBlocks.INFESTED_FARMLAND) && blockEntity instanceof InfestedFarmlandBlockEntity infestedFarmlandBlockEntity) {
                    PesticideType pesticideType = infestedFarmlandBlockEntity.getPesticideType();

                    List<ItemStack> drops = original.call(blockState, builder);

                    // Special case for wheat blocks (because they drop wheat and not food)
                    if (blockState.is(Blocks.WHEAT)) {
                        List<ItemStack> newDrops = new ArrayList<>();

                        // We don't return an item stack with 'drops.size()' suspicious wheats because some
                        // mods might add their own output
                        drops.forEach(
                                itemStack -> newDrops.add(pesticideType.createSuspiciousWheat())
                        );

                        return newDrops;
                    } else {
                        //noinspection DataFlowIssue
                        drops.forEach(itemStack ->
                                itemStack.set(DataComponents.CONSUMABLE, ModFoodProperties.createPesticibleConsumable(itemStack.get(DataComponents.CONSUMABLE), pesticideType)));

                        return drops;
                    }
                } else if (thisEntity != null) {
                    if (thisEntity instanceof LivingEntity livingEntity && livingEntity.hasEffect(ModEffects.BAD_FARMER)) {
                        List<ItemStack> drops = original.call(blockState, builder);

                        drops.forEach(itemStack -> {
                            //noinspection DataFlowIssue
                            itemStack.set(DataComponents.CONSUMABLE, ModFoodProperties.createEmptyConsumable(itemStack.get(DataComponents.CONSUMABLE)));
                            //noinspection DataFlowIssue
                            itemStack.set(DataComponents.FOOD, ModFoodProperties.createEmptyFoodProperties(itemStack.get(DataComponents.FOOD)));
                        });

                        return drops;
                    }
                }
            }
        }

        return original.call(blockState, builder);
    }
}
