package fr.geming400.pesticide.content.items;

import com.mojang.serialization.MapCodec;
import fr.geming400.pesticide.Pesticides;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public final class ModConsumeEffects {
    public static final ConsumeEffect.Type<PesticideConsumeEffect> APPLY_PESTICIDES_EFFECTS = registerType(
            "apply_pesticides_effects", PesticideConsumeEffect.CODEC, PesticideConsumeEffect.STREAM_CODEC
    );

    public static void initialize() {}

    private static <T extends ConsumeEffect> ConsumeEffect.Type<T> registerType(
            String name, MapCodec<T> mapCodec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec
    ) {
        return Registry.register(BuiltInRegistries.CONSUME_EFFECT_TYPE, Identifier.fromNamespaceAndPath(Pesticides.MOD_ID, name), new ConsumeEffect.Type<>(mapCodec, streamCodec));
    }

    public record PesticideConsumeEffect() implements ConsumeEffect {
        public static final MapCodec<PesticideConsumeEffect> CODEC = MapCodec.unit(new PesticideConsumeEffect());
        public static final StreamCodec<RegistryFriendlyByteBuf, PesticideConsumeEffect> STREAM_CODEC = StreamCodec.of(
                (bytebuf, pesticideConsumeEffect) -> {},
                pesticideConsumeEffect -> new PesticideConsumeEffect()
        );

        @Override
        @NonNull
        public Type<? extends ConsumeEffect> getType() {
            return Type.APPLY_EFFECTS;
        }

        @Override
        public boolean apply(@NonNull Level level, @NonNull ItemStack itemStack, @NonNull LivingEntity livingEntity) {
            if (itemStack.getItem() instanceof PesticideContainer pesticideContainer) {
                boolean res = false;
                for (MobEffectInstance mobEffectInstance : pesticideContainer.getPesticideType(itemStack).effects()) {
                    if (livingEntity.addEffect(new MobEffectInstance(mobEffectInstance))) {
                        res = true;
                    }
                }

                return res;
            }

            return false;
        }
    }

    private ModConsumeEffects() {}
}
