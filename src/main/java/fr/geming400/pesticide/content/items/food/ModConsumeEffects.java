package fr.geming400.pesticide.content.items.food;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.geming400.pesticide.Pesticides;
import fr.geming400.pesticide.content.items.PesticideContainer;
import fr.geming400.pesticide.content.pesticides.PesticideType;
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
    public static final ConsumeEffect.Type<PesticideContainerConsumeEffect> APPLY_CONTAINER_PESTICIDES_EFFECTS = registerType(
            "apply_container_pesticides_effects", PesticideContainerConsumeEffect.CODEC, PesticideContainerConsumeEffect.STREAM_CODEC
    );
    public static final ConsumeEffect.Type<PesticideConsumeEffect> APPLY_PESTICIDES_EFFECTS = registerType(
            "apply_pesticides_effects", PesticideConsumeEffect.CODEC, PesticideConsumeEffect.STREAM_CODEC
    );

    public static void initialize() {}

    private static <T extends ConsumeEffect> ConsumeEffect.Type<T> registerType(
            String name, MapCodec<T> mapCodec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec
    ) {
        return Registry.register(
                BuiltInRegistries.CONSUME_EFFECT_TYPE,
                Identifier.fromNamespaceAndPath(Pesticides.MOD_ID, name),
                new ConsumeEffect.Type<>(mapCodec, streamCodec)
        );
    }

    public record PesticideContainerConsumeEffect() implements ConsumeEffect {
        public static final MapCodec<PesticideContainerConsumeEffect> CODEC = MapCodec.unit(new PesticideContainerConsumeEffect());

        public static final StreamCodec<RegistryFriendlyByteBuf, PesticideContainerConsumeEffect> STREAM_CODEC = StreamCodec.of(
                (bytebuf, pesticideConsumeEffect) -> {},
                byteBuf -> new PesticideContainerConsumeEffect()
        );

        @Override
        @NonNull
        public Type<PesticideContainerConsumeEffect> getType() {
            return APPLY_CONTAINER_PESTICIDES_EFFECTS;
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

    public record PesticideConsumeEffect(Identifier pesticideID) implements ConsumeEffect {
        public static final MapCodec<PesticideConsumeEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Identifier.CODEC.fieldOf("pesticideID").forGetter(PesticideConsumeEffect::pesticideID)
        ).apply(instance, PesticideConsumeEffect::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, PesticideConsumeEffect> STREAM_CODEC = StreamCodec.of(
                (bytebuf, pesticideConsumeEffect) ->
                        bytebuf.writeIdentifier(pesticideConsumeEffect.pesticideID),
                byteBuf ->
                        new PesticideConsumeEffect(byteBuf.readIdentifier())
        );

        @Override
        @NonNull
        public Type<PesticideConsumeEffect> getType() {
            return APPLY_PESTICIDES_EFFECTS;
        }

        @Override
        public boolean apply(@NonNull Level level, @NonNull ItemStack itemStack, @NonNull LivingEntity livingEntity) {
            boolean res = false;

            try {
                PesticideType pesticideType = PesticideType.fromID(this.pesticideID);

                for (MobEffectInstance mobEffectInstance : pesticideType.effects()) {
                    if (livingEntity.addEffect(new MobEffectInstance(mobEffectInstance))) {
                        res = true;
                    }
                }
            } catch (NullPointerException e) {
                // noinspection StringConcatenationArgumentToLogCall
                Pesticides.LOGGER.error("Tried to apply PesticideConsumeEffect for pesticide %s but failed because it doesn't seem to exist !".formatted(this.pesticideID), e);
            }

            return res;
        }

        public static PesticideConsumeEffect of(PesticideType pesticideType) {
            return new PesticideConsumeEffect(pesticideType.getID());
        }
    }

    private ModConsumeEffects() {}
}
