package fr.geming400.pesticide.content.items;

import fr.geming400.pesticide.Pesticides;
import fr.geming400.pesticide.content.effects.ModEffects;
import fr.geming400.pesticide.content.recipe.ModRecipes;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;

/// @see ModRecipes#initialize() ModRecipes.initialize() for potion brewing recipes
public final class ModPotions {
    public static final Holder<Potion> BURNING_POTION = create(
            "burning",
            new MobEffectInstance(ModEffects.BURNING, 10 * 20)
    );

    public static final Holder<Potion> BURNING_POTION_LONG = create(
            "burning_long",
            new MobEffectInstance(ModEffects.BURNING, 20 * 20, 1)
    );


    public static final Holder<Potion> FREEZING_POTION = create(
            "freezing",
            new MobEffectInstance(ModEffects.FREEZING, 10 * 20)
    );

    public static final Holder<Potion> FREEZING_POTION_LONG = create(
            "freezing_long",
            new MobEffectInstance(ModEffects.FREEZING, 20 * 20, 1)
    );


    public static final Holder<Potion> DROWNING_POTION = create(
            "drowning",
            new MobEffectInstance(ModEffects.DROWNING, 10 * 20)
    );

    public static final Holder<Potion> DROWNING_POTION_LONG = create(
            "drowning_long",
            new MobEffectInstance(ModEffects.DROWNING, 20 * 20)
    );


    private static Holder<Potion> create(String name, MobEffectInstance... effects) {
        return Registry.registerForHolder(
                BuiltInRegistries.POTION,
                Identifier.fromNamespaceAndPath(Pesticides.MOD_ID, name),
                new Potion(name, effects)
        );

    }

    public static void initialize() {}

    private ModPotions() {}
}
