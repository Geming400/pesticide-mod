package fr.geming400.pesticide.client.jei;

import fr.geming400.pesticide.Pesticides;
import fr.geming400.pesticide.Utils;
import fr.geming400.pesticide.content.ModRegistries;
import fr.geming400.pesticide.content.items.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

final class JeiRecipes {
    public static List<RecipeHolder<CraftingRecipe>> createPesticideContainerRecipes() {
        List<RecipeHolder<CraftingRecipe>> pesticideContainerRecipes = new ArrayList<>();
        ModRegistries.PESTICIDE_TYPE.forEach(pesticideType ->
                pesticideContainerRecipes.add(pesticideType.createJeiRecipe())
        );

        return pesticideContainerRecipes;
    }

    public static List<RecipeHolder<CraftingRecipe>> createFoodInfectionRecipes() {
        List<RecipeHolder<CraftingRecipe>> foodInfectionRecipes = new ArrayList<>();
        BuiltInRegistries.ITEM.forEach(foodItem -> {
            ItemStack foodItemstack = foodItem.getDefaultInstance();
            if (Utils.isInfectable(foodItemstack)) {
                Utils.addIsInfectedLore(foodItemstack);

                CraftingRecipe recipe = new ShapelessRecipe(
                        "food_infection",
                        CraftingBookCategory.MISC,
                        foodItemstack,
                        List.of(
                                Ingredient.of(ModItems.PESTICIDE_CONTAINER),
                                Ingredient.of(foodItem)
                        )
                );

                ResourceKey<Recipe<?>> recipeResourceKey = createRecipeKey("food_infection", foodItem);

                foodInfectionRecipes.add(new RecipeHolder<>(recipeResourceKey, recipe));
            }
        });

        return foodInfectionRecipes;
    }

    public static List<RecipeHolder<CraftingRecipe>> createBreadInfectionRecipe() {
        ItemStack breadItem = Items.BREAD.getDefaultInstance();
        Utils.addIsInfectedLore(breadItem);

        CraftingRecipe recipe = new ShapedRecipe(
                "food_infection",
                CraftingBookCategory.MISC,
                ShapedRecipePattern.of(
                        Map.of('b', Ingredient.of(ModItems.SUSPICIOUS_WHEAT)),
                        "bbb"
                ),
                breadItem
        );

        ResourceKey<Recipe<?>> recipeResourceKey = createRecipeKey("food_infection/bread_from_suspicious_wheat");

        return List.of(new RecipeHolder<>(recipeResourceKey, recipe));
    }


    private static ResourceKey<Recipe<?>> createRecipeKey(String name) {
        Identifier recipeID = Identifier.fromNamespaceAndPath(
                Pesticides.MOD_ID,
                name
        );

        return ResourceKey.create(Registries.RECIPE, recipeID);
    }

    private static ResourceKey<Recipe<?>> createRecipeKey(String name, ItemLike suffix) {
        Identifier recipeID = Identifier.fromNamespaceAndPath(
                Pesticides.MOD_ID,
                name + "/" + BuiltInRegistries.ITEM.getKey(suffix.asItem()).getPath()
        );

        return ResourceKey.create(Registries.RECIPE, recipeID);
    }

    private JeiRecipes() {}
}
