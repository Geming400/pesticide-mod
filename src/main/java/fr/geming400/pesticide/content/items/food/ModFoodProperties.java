package fr.geming400.pesticide.content.items.food;

import fr.geming400.pesticide.content.pesticides.PesticideType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.Consumable;

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
     * Mimics another {@link Consumable}'s property to
     * @param from the {@link Consumable} to mimic
     * @param pesticideType the {@link PesticideType} to infect to {@link Consumable} with
     * @return the {@link Consumable} infected by the given {@linkplain PesticideType pesticide}
     */
    public static Consumable createPesticibleConsumable(Consumable from, PesticideType pesticideType) {
        return Consumable.builder()
                .onConsume(new ModConsumeEffects.PesticideConsumeEffect(pesticideType))
                .hasConsumeParticles(from.hasConsumeParticles())
                .animation(from.animation())
                .consumeSeconds(from.consumeSeconds())
                .soundAfterConsume(from.sound())
                .build();
    }

    public static Consumable.Builder createDrinkConsumable() {
        return Consumable.builder()
                .animation(ItemUseAnimation.DRINK)
                .sound(SoundEvents.GENERIC_DRINK)
                .hasConsumeParticles(false);
    }


    private ModFoodProperties() {}
}
