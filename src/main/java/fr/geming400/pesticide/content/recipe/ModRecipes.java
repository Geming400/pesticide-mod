package fr.geming400.pesticide.content.recipe;

import fr.geming400.pesticide.Pesticides;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public final class ModRecipes {
    public static final RecipeSerializer<PesticideContainerRecipe> PESTICIDE_CONTAINER_RECIPE = register("crafting_special_pesticide_container", new CustomRecipe.Serializer<>(PesticideContainerRecipe::new));

    private static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String name, S recipeSerializer) {
        return Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, Identifier.fromNamespaceAndPath(Pesticides.MOD_ID, name), recipeSerializer);
    }

    public static void initialize() {}

    private ModRecipes() {}
}
