package fr.geming400.pesticide.content.effects;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jspecify.annotations.NonNull;

// The actual logic for this effect is implemented
// in CropBlockMixin and HoeItemMixin
public class BadFarmerEffect extends MobEffect {
    protected BadFarmerEffect() {
        super(MobEffectCategory.HARMFUL, 0x966C4A);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(@NonNull ServerLevel serverLevel, @NonNull LivingEntity livingEntity, int i) {
        return true;
    }
}
