package fr.geming400.pesticide.client.jei;

import fr.geming400.pesticide.Pesticides;
import fr.geming400.pesticide.Utils;
import fr.geming400.pesticide.content.ModDataComponents;
import fr.geming400.pesticide.content.ModRegistries;
import fr.geming400.pesticide.content.blocks.ModBlocks;
import fr.geming400.pesticide.content.items.ModItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.crafting.*;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JeiPlugin
public class JeiVanillaPlugin implements IModPlugin {
    public static final Identifier ID = Identifier.fromNamespaceAndPath(Pesticides.MOD_ID, "vanilla_plugin");

    @Override
    @NonNull
    public Identifier getPluginUid() {
        return ID;
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerFromDataComponentTypes(ModItems.PESTICIDE_CONTAINER, ModDataComponents.PESTICIDE_TYPE);
    }

    @Override
    public void registerRecipes(@NonNull IRecipeRegistration registration) {
        HolderLookup<Item> itemLookup = Objects.requireNonNull(Minecraft.getInstance().level).holderLookup(BuiltInRegistries.ITEM.key());

        List<RecipeHolder<CraftingRecipe>> pesticideContainerRecipes = new ArrayList<>();
        ModRegistries.PESTICIDE_TYPE.forEach(pesticideType ->
                pesticideContainerRecipes.add(pesticideType.createJeiRecipe(itemLookup))
        );

        registration.addRecipes(RecipeTypes.CRAFTING, pesticideContainerRecipes);

        List<RecipeHolder<CraftingRecipe>> foodInfectionRecipes = new ArrayList<>();
        BuiltInRegistries.ITEM.forEach(foodItem -> {
            ItemStack foodItemstack = foodItem.getDefaultInstance();
            if (Utils.isInfectable(foodItemstack)) {
                ItemLore itemLore = foodItemstack.has(DataComponents.LORE)
                        ? Objects.requireNonNull(foodItemstack.get(DataComponents.LORE))
                        : new ItemLore(List.of());
                Component infectedTooltip = Component.translatable(ModItems.PESTICIDE_CONTAINER.getDescriptionId() + ".infectedTooltip")
                        .withStyle(ChatFormatting.DARK_GREEN);

                foodItemstack.set(DataComponents.LORE, itemLore.withLineAdded(infectedTooltip));

                CraftingRecipe recipe = new ShapelessRecipe(
                        "food_infection",
                        CraftingBookCategory.MISC,
                        foodItemstack,
                        List.of(
                                Ingredient.of(ModItems.PESTICIDE_CONTAINER),
                                Ingredient.of(foodItem)
                        )
                );

                Identifier recipeID = Identifier.fromNamespaceAndPath(
                        Pesticides.MOD_ID,
                        "food_infection/" + BuiltInRegistries.ITEM.getKey(foodItem).getPath()
                );
                ResourceKey<Recipe<?>> recipeResourceKey = ResourceKey.create(Registries.RECIPE, recipeID);

                foodInfectionRecipes.add(new RecipeHolder<>(recipeResourceKey, recipe));
            }
        });

        registration.addRecipes(RecipeTypes.CRAFTING, foodInfectionRecipes);

        registration.addIngredientInfo(ModItems.ZOMBIE_BONE, Component.translatable(ModItems.ZOMBIE_BONE.getDescriptionId() + ".info"));
        registration.addIngredientInfo(ModItems.FAUCET_ANALYSER, Component.translatable(ModItems.FAUCET_ANALYSER.getDescriptionId() + ".info"));
        registration.addIngredientInfo(ModBlocks.FAUCET, Component.translatable(ModBlocks.FAUCET.getDescriptionId() + ".info"));
    }
}
