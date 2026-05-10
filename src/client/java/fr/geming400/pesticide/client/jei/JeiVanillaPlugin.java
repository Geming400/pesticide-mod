package fr.geming400.pesticide.client.jei;

import fr.geming400.pesticide.Pesticides;
import fr.geming400.pesticide.content.ModDataComponents;
import fr.geming400.pesticide.content.ModRegistries;
import fr.geming400.pesticide.content.blocks.ModBlocks;
import fr.geming400.pesticide.content.items.ModItems;
import fr.geming400.pesticide.content.pesticides.PesticideType;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.*;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Comparator;
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

        List<RecipeHolder<CraftingRecipe>> recipes = new ArrayList<>();
        for (PesticideType pesticideType : ModRegistries.PESTICIDE_TYPE) {
            ShapelessRecipeBuilder recipeBuilder = ShapelessRecipeBuilder.shapeless(
                    itemLookup,
                    RecipeCategory.MISC,
                    pesticideType.createContainer()
            );
            pesticideType.ingredients().forEach(recipeBuilder::requires);

            CraftingRecipe recipe = new ShapelessRecipe(
                    "pesticide_container",
                    CraftingBookCategory.MISC,
                    pesticideType.createContainer(),
                    pesticideType.ingredients()
                            .stream()
                            .sorted(Comparator.comparingInt(BuiltInRegistries.ITEM::getId))
                            .map(Ingredient::of)
                            .toList()
            );

            // noinspection DataFlowIssue
            Identifier recipeID = Identifier.fromNamespaceAndPath(
                    Pesticides.MOD_ID,
                    "pesticide_container/" + ModRegistries.PESTICIDE_TYPE.getKey(pesticideType).getPath()
            );
            ResourceKey<Recipe<?>> recipeResourceKey = ResourceKey.create(Registries.RECIPE, recipeID);

            recipes.add(new RecipeHolder<>(recipeResourceKey, recipe));
        }

        registration.addRecipes(RecipeTypes.CRAFTING, recipes);

        registration.addIngredientInfo(ModItems.ZOMBIE_BONE, Component.translatable(ModItems.ZOMBIE_BONE.getDescriptionId() + ".info"));
        registration.addIngredientInfo(ModItems.FAUCET_ANALYSER, Component.translatable(ModItems.FAUCET_ANALYSER.getDescriptionId() + ".info"));
        registration.addIngredientInfo(ModBlocks.FAUCET, Component.translatable(ModBlocks.FAUCET.getDescriptionId() + ".info"));
    }
}
