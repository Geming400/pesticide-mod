package fr.geming400.pesticide.content.effects;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jspecify.annotations.NonNull;

public class FreezingEffect extends MobEffect {
    protected FreezingEffect() {
        super(MobEffectCategory.HARMFUL, 0x59FFFF);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(@NonNull ServerLevel serverLevel, LivingEntity livingEntity, int amplifier) {
        livingEntity.setTicksFrozen(livingEntity.getTicksRequiredToFreeze());
        livingEntity.setIsInPowderSnow(true);
        livingEntity.hurtServer(serverLevel, livingEntity.damageSources().freeze(), amplifier + 1);
        livingEntity.isFreezing();

        return livingEntity.canFreeze();
    }
}
