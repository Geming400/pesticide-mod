package fr.geming400.pesticide.content.items.food;

import fr.geming400.pesticide.content.pesticides.PesticideType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ConsumeEffect;

public final class ModFoodProperties {
    public static final FoodProperties PESTICIDE = new FoodProperties.Builder()
            .alwaysEdible()
            .build();

    public static final Consumable PESTICIBLE_CONTAINER_CONSUMABLE = createDrinkConsumable()
            .onConsume(new ModConsumeEffects.PesticideContainerConsumeEffect())
            .build();

    public static final Consumable WATER_BOTTLE_LIKE_CONSUMABLE = createDrinkConsumable()
            .build();

    /**
     * Mimics another {@link Consumable} to infect it with a {@linkplain PesticideType pesticide}
     * @param from the {@link Consumable} to mimic
     * @param pesticideType the {@link PesticideType} to infect to {@link Consumable} with
     * @return the {@link Consumable} infected by the given {@linkplain PesticideType pesticide}
     */
    public static Consumable createPesticibleConsumable(Consumable from, PesticideType pesticideType) {
        return Consumable.builder()
                .onConsume(ModConsumeEffects.PesticideConsumeEffect.of(pesticideType))
                .hasConsumeParticles(from.hasConsumeParticles())
                .animation(from.animation())
                .consumeSeconds(from.consumeSeconds())
                .sound(from.sound())
                .build();
    }

    /**
     * Mimics another {@link Consumable} to remove the 'on consume' property
     * @param from the {@link Consumable} to mimic
     * @return the {@link Consumable} without the on consume property
     * @see Consumable.Builder#onConsume(ConsumeEffect)
     */
    public static Consumable createEmptyConsumable(Consumable from) {
        return Consumable.builder()
                .hasConsumeParticles(from.hasConsumeParticles())
                .animation(from.animation())
                .consumeSeconds(from.consumeSeconds())
                .soundAfterConsume(from.sound())
                .build();
    }

    /**
     * Mimics another {@link FoodProperties} to remove the 'on consume' property
     * @param from the {@link Consumable} to mimic
     * @return the {@link Consumable} without the on consume property
     * @see Consumable.Builder#onConsume(ConsumeEffect)
     */
    public static FoodProperties createEmptyFoodProperties(FoodProperties from) {
        FoodProperties.Builder builder = new FoodProperties.Builder();
        if (from.canAlwaysEat())
            builder.alwaysEdible();

        return builder.build();
    }

    public static Consumable.Builder createDrinkConsumable() {
        return Consumable.builder()
                .animation(ItemUseAnimation.DRINK)
                .sound(SoundEvents.GENERIC_DRINK)
                .hasConsumeParticles(false);
    }


    private ModFoodProperties() {}
}
