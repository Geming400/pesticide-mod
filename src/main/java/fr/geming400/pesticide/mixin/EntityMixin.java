package fr.geming400.pesticide.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import fr.geming400.pesticide.content.effects.ModEffects;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Entity.class)
abstract class EntityMixin {
    @WrapMethod(method = "isEyeInFluid")
    public boolean isEyeInFluid(TagKey<Fluid> tagKey, Operation<Boolean> original) {
        // noinspection ConstantValue
        if (
                (Object) this instanceof LivingEntity livingEntity
                && livingEntity.hasEffect(ModEffects.DROWNING)
                && tagKey == FluidTags.WATER
        ) {
            return true;
        }

        return original.call(tagKey);
    }
}
