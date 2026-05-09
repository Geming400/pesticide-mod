package fr.geming400.pesticide.content.recipe;

import fr.geming400.pesticide.content.ModRegistries;
import fr.geming400.pesticide.content.pesticides.PesticideType;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class PesticideContainerRecipe extends CustomRecipe {
    public PesticideContainerRecipe(CraftingBookCategory craftingBookCategory) {
        super(craftingBookCategory);
    }

    @Nullable
    public PesticideType getPesticideTypeFromIngredients(List<ItemStack> ingredients) {
        for (PesticideType pesticide : ModRegistries.PESTICIDE_TYPE) {
            if (pesticide.ingredientMatches(ingredients))
                return pesticide;
        }

        return null;
    }

    @Override
    public boolean matches(@NonNull CraftingInput recipeInput, @NonNull Level level) {
        return this.getPesticideTypeFromIngredients(recipeInput.items()) != null;
    }

    @Override
    @NonNull
    public ItemStack assemble(@NonNull CraftingInput recipeInput, HolderLookup.@NonNull Provider provider) {
        // noinspection DataFlowIssue
        return this.getPesticideTypeFromIngredients(recipeInput.items()).createContainer();
    }

    @Override
    @NonNull
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return ModRecipes.PESTICIDE_CONTAINER_RECIPE;
    }
}
