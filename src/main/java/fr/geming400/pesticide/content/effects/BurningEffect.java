package fr.geming400.pesticide.content.effects;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jspecify.annotations.NonNull;

public class BurningEffect extends MobEffect {
    protected BurningEffect() {
        super(MobEffectCategory.HARMFUL, 0xFF6A00);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(@NonNull ServerLevel serverLevel, LivingEntity livingEntity, int amplifier) {
        livingEntity.igniteForTicks(10 * (amplifier + 1) * (serverLevel.getDifficulty().getId() + 1));
        livingEntity.hurtServer(serverLevel, livingEntity.damageSources().inFire(), amplifier + 1);

        return true;
    }
}
