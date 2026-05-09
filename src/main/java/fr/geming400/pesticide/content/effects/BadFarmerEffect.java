package fr.geming400.pesticide.content.effects;

import fr.geming400.pesticide.content.ModAttachments;
import fr.geming400.pesticide.content.blocks.InfestedFarmBlock;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jspecify.annotations.NonNull;

import java.time.Duration;

// The actual logic for this effect is implemented
// in CropBlockMixin and HoeItemMixin
public class BadFarmerEffect extends MobEffect {
    /// The number of ticks the player needs to step on {@link InfestedFarmBlock}s to get infected
    public static final long TICKS_BEFORE_APPLYING = Duration.ofSeconds(20).getSeconds() * 20;
    public static final int DEFAULT_APPLY_TIME = Math.toIntExact(Duration.ofMinutes(20).getSeconds() * 20);

    protected BadFarmerEffect() {
        super(MobEffectCategory.HARMFUL, 0x966C4A);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public boolean applyEffectTick(@NonNull ServerLevel serverLevel, @NonNull LivingEntity livingEntity, int i) {
        return livingEntity.getAttachedOrGet(ModAttachments.TIME_SPENT_ON_INFESTED_FARMLAND, () -> 0) >= TICKS_BEFORE_APPLYING;
    }
}
