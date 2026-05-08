package fr.geming400.pesticide.content.items;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.Consumable;

public final class ModFoodProperties {
    public static final FoodProperties PESTICIDE = new FoodProperties.Builder()
            .alwaysEdible()
            .build();

    public static final Consumable PESTICIBLE_CONSUMABLE = Consumable.builder()
            .onConsume(new ModConsumeEffects.PesticideConsumeEffect())
            .animation(ItemUseAnimation.DRINK)
            .sound(SoundEvents.GENERIC_DRINK)
            .hasConsumeParticles(false)
            .build();

    public static final Consumable WATER_BOTTLE_LIKE_CONSUMABLE = Consumable.builder()
            .animation(ItemUseAnimation.DRINK)
            .sound(SoundEvents.GENERIC_DRINK)
            .hasConsumeParticles(false)
            .build();


    private ModFoodProperties() {}
}
