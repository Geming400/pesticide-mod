package fr.geming400.pesticide.content.recipe;

import fr.geming400.pesticide.Utils;
import fr.geming400.pesticide.content.ModDataComponents;
import fr.geming400.pesticide.content.items.food.ModFoodProperties;
import fr.geming400.pesticide.content.pesticides.PesticideType;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public final class InfectFoodRecipe extends CustomRecipe {
    public InfectFoodRecipe(CraftingBookCategory craftingBookCategory) {
        super(craftingBookCategory);
    }

    @Override
    public boolean matches(@NonNull CraftingInput recipeInput, @NonNull Level level) {
        if (recipeInput.size() == 2) {
            PesticideType pesticideType = null;
            ItemStack consumableItem = null;
            for (ItemStack itemstack : recipeInput.items()) {
                if (Utils.isInfectable(itemstack))
                    consumableItem = itemstack;

                if (itemstack.has(ModDataComponents.PESTICIDE_TYPE))
                    pesticideType = itemstack.get(ModDataComponents.PESTICIDE_TYPE);
            }

            return pesticideType != null && consumableItem != null;
        }

        return false;
    }

    @Override
    @NonNull
    public ItemStack assemble(@NonNull CraftingInput recipeInput, HolderLookup.@NonNull Provider provider) {
        PesticideType pesticideType = null;
        ItemStack consumableItem = null;
        for (ItemStack item : recipeInput.items()) {
            if (item.has(DataComponents.CONSUMABLE))
                consumableItem = item.copyWithCount(1);

            if (item.has(ModDataComponents.PESTICIDE_TYPE))
                pesticideType = item.get(ModDataComponents.PESTICIDE_TYPE);
        }



        //noinspection DataFlowIssue
        Consumable infectedConsumable = ModFoodProperties.createPesticibleConsumable(consumableItem.get(DataComponents.CONSUMABLE), pesticideType);
        consumableItem.set(DataComponents.CONSUMABLE, infectedConsumable);

        return consumableItem;
    }

    @Override
    @NonNull
    public RecipeSerializer<InfectFoodRecipe> getSerializer() {
        return ModRecipes.INFECT_FOOD_RECIPE;
    }
}
