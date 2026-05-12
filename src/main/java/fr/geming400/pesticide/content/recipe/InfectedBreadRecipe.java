package fr.geming400.pesticide.content.recipe;

import fr.geming400.pesticide.content.ModDataComponents;
import fr.geming400.pesticide.content.items.ModItems;
import fr.geming400.pesticide.content.items.food.ModFoodProperties;
import fr.geming400.pesticide.content.pesticides.PesticideType;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public final class InfectedBreadRecipe extends CustomRecipe {
    public InfectedBreadRecipe(CraftingBookCategory craftingBookCategory) {
        super(craftingBookCategory);
    }

    @Override
    public boolean matches(@NonNull CraftingInput recipeInput, @NonNull Level level) {
        if (recipeInput.width() == 3 && recipeInput.height() == 1 && recipeInput.size() == 3) {
            PesticideType pesticideType = null;
            for (ItemStack itemstack : recipeInput.items()) {
                if (itemstack.is(ModItems.SUSPICIOUS_WHEAT) && itemstack.has(ModDataComponents.PESTICIDE_TYPE)) {
                    pesticideType = itemstack.get(ModDataComponents.PESTICIDE_TYPE);
                } else {
                    // If the item doesn't match any of these
                    // we don't need to continue
                    break;
                }
            }

            return pesticideType != null;
        }

        return false;
    }

    @Override
    @NonNull
    public ItemStack assemble(@NonNull CraftingInput recipeInput, HolderLookup.@NonNull Provider provider) {
        ItemStack output = Items.BREAD.getDefaultInstance();
        PesticideType pesticideType = recipeInput.getItem(0).get(ModDataComponents.PESTICIDE_TYPE);

        Consumable infectedConsumable = ModFoodProperties.createPesticibleConsumable(Consumable.builder().build(), pesticideType);
        output.set(DataComponents.CONSUMABLE, infectedConsumable);

        return output;
    }

    @Override
    @NonNull
    public RecipeSerializer<InfectedBreadRecipe> getSerializer() {
        return ModRecipes.INFECTED_BREAD_RECIPE;
    }
}
