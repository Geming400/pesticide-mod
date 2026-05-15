package fr.geming400.pesticide.content.recipe;

import fr.geming400.pesticide.Pesticides;
import fr.geming400.pesticide.content.items.ModPotions;
import net.fabricmc.fabric.api.recipe.v1.sync.RecipeSynchronization;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public final class ModRecipes {
    public static final RecipeSerializer<InfectFoodRecipe> INFECT_FOOD_RECIPE = register("infect_food", new CustomRecipe.Serializer<>(InfectFoodRecipe::new));
    public static final RecipeSerializer<InfectedBreadRecipe> INFECTED_BREAD_RECIPE = register("infect_bread", new CustomRecipe.Serializer<>(InfectedBreadRecipe::new));

    private static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String name, S recipeSerializer) {
        S serializer = Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, Identifier.fromNamespaceAndPath(Pesticides.MOD_ID, name), recipeSerializer);
        RecipeSynchronization.synchronizeRecipeSerializer(serializer);

        return serializer;
    }

    public static void initialize() {
        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
            builder.addMix(
                    Potions.AWKWARD,
                    Items.BLAZE_ROD,
                    ModPotions.BURNING_POTION
            );
            builder.addMix(
                    ModPotions.BURNING_POTION,
                    Items.GLOWSTONE,
                    ModPotions.BURNING_POTION_LONG
            );

            builder.addMix(
                    Potions.AWKWARD,
                    Items.PACKED_ICE,
                    ModPotions.FREEZING_POTION
            );
            builder.addMix(
                    ModPotions.FREEZING_POTION,
                    Items.GLOWSTONE,
                    ModPotions.FREEZING_POTION_LONG
            );

            builder.addMix(
                    Potions.AWKWARD,
                    Items.TROPICAL_FISH,
                    ModPotions.DROWNING_POTION
            );
            builder.addMix(
                    ModPotions.DROWNING_POTION,
                    Items.GLOWSTONE,
                    ModPotions.DROWNING_POTION_LONG
            );
        });
    }

    private ModRecipes() {}
}
