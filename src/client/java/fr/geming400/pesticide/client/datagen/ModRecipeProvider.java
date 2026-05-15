package fr.geming400.pesticide.client.datagen;

import fr.geming400.pesticide.content.ModRegistries;
import fr.geming400.pesticide.content.blocks.ModBlocks;
import fr.geming400.pesticide.content.items.ModItems;
import fr.geming400.pesticide.content.pesticides.PesticideType;
import fr.geming400.pesticide.content.recipe.InfectFoodRecipe;
import fr.geming400.pesticide.content.recipe.InfectedBreadRecipe;
import fr.geming400.pesticide.content.recipe.ModRecipes;
import fr.geming400.pesticide.content.tags.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.recipe.v1.ingredient.DefaultCustomIngredients;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jspecify.annotations.NonNull;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    @NonNull
    protected RecipeProvider createRecipeProvider(HolderLookup.@NonNull Provider registryLookup, @NonNull RecipeOutput exporter) {
        return new RecipeProvider(registryLookup, exporter) {
            @Override
            public void buildRecipes() {
                HolderLookup.RegistryLookup<Item> itemLookup = registries.lookupOrThrow(Registries.ITEM);
                shaped(RecipeCategory.MISC, ModBlocks.FAUCET, 3)
                        .pattern("ddd")
                        .pattern(" i ")
                        .define('d', ConventionalItemTags.DEEPSLATE_COBBLESTONES)
                        .define('i', ConventionalItemTags.IRON_NUGGETS)
                        .group("faucet")
                        .unlockedBy(getHasName(Items.IRON_INGOT), has(ConventionalItemTags.IRON_INGOTS))
                        .unlockedBy(getHasName(Items.COBBLED_DEEPSLATE), has(ConventionalItemTags.DEEPSLATE_COBBLESTONES))
                        .save(output);

                shaped(RecipeCategory.MISC, ModItems.EMPTY_CONTAINER, 3)
                        .pattern("  g")
                        .pattern(" gg")
                        .pattern(" gg")
                        .define('g', ConventionalItemTags.GLASS_BLOCKS)
                        .group("pesticide_containers")
                        .unlockedBy(getHasName(Items.GLASS), has(ConventionalItemTags.GLASS_BLOCKS))
                        .save(output);

                shaped(RecipeCategory.TOOLS, ModItems.FAUCET_ANALYSER)
                        .pattern("  i")
                        .pattern(" s ")
                        .pattern("s  ")
                        .define('i', ConventionalItemTags.IRON_NUGGETS)
                        .define('s', ConventionalItemTags.WOODEN_RODS)
                        .group("pesticide_containers")
                        .unlockedBy(getHasName(Items.IRON_INGOT), has(ConventionalItemTags.IRON_INGOTS))
                        .unlockedBy(getHasName(Items.STICK), has(ConventionalItemTags.WOODEN_RODS))
                        .save(output);


                shaped(RecipeCategory.MISC, ModItems.WOOL_ROD, 8)
                        .pattern(" w ")
                        .pattern(" w ")
                        .define('w', ItemTags.WOOL)
                        .group("wool_rod")
                        .unlockedBy(getHasName(Items.WHITE_WOOL), has(ItemTags.WOOL))
                        .save(output, "wool_rod/wool");

                shaped(RecipeCategory.MISC, ModItems.WOOL_ROD, 2)
                        .pattern(" s ")
                        .pattern(" s ")
                        .define('s', Items.STRING)
                        .group("wool_rod")
                        .unlockedBy(getHasName(Items.WHITE_WOOL), has(ItemTags.WOOL))
                        .save(output, "wool_rod/string");

                shaped(RecipeCategory.TOOLS, ModItems.COTTON_SWAB)
                        .pattern("  w")
                        .pattern(" s ")
                        .pattern("w  ")
                        .define('w', Items.WHITE_WOOL)
                        .define('s', ModItems.WOOL_ROD)
                        .group("cotton_swab")
                        .unlockedBy(getHasName(Items.WHITE_WOOL), has(Items.WHITE_WOOL))
                        .unlockedBy(getHasName(Items.STICK), has(ConventionalItemTags.WOODEN_RODS))
                        .save(output);

                shapeless(RecipeCategory.MISC, ModItems.PLASTIC_SHEET)
                        .requires(ModItems.HOT_MILK_BUCKET)
                        .requires(Items.PAPER)
                        .requires(Items.COAL, 4)
                        .unlockedBy(getHasName(Items.COAL), has(ConventionalItemTags.COAL_ORES))
                        .save(output);

                shaped(RecipeCategory.TOOLS, ModItems.HAZMAT_SUIT.helmet())
                        .pattern("sws")
                        .pattern("s s")
                        .define('s', ModItemTags.PLASTIC_SHEETS)
                        .define('w', ConventionalItemTags.ORANGE_DYES)
                        .unlockedBy(getHasName(ModItems.PLASTIC_SHEET), has(ModItemTags.PLASTIC_SHEETS))
                        .save(output);

                shaped(RecipeCategory.TOOLS, ModItems.HAZMAT_SUIT.chestplate())
                        .pattern("s s")
                        .pattern("sws")
                        .pattern("sws")
                        .define('s', ModItemTags.PLASTIC_SHEETS)
                        .define('w', ConventionalItemTags.ORANGE_DYES)
                        .unlockedBy(getHasName(ModItems.PLASTIC_SHEET), has(ModItemTags.PLASTIC_SHEETS))
                        .save(output);

                shaped(RecipeCategory.TOOLS, ModItems.HAZMAT_SUIT.leggings())
                        .pattern("sws")
                        .pattern("s s")
                        .pattern("s s")
                        .define('s', ModItemTags.PLASTIC_SHEETS)
                        .define('w', ConventionalItemTags.ORANGE_DYES)
                        .unlockedBy(getHasName(ModItems.PLASTIC_SHEET), has(ModItemTags.PLASTIC_SHEETS))
                        .save(output);

                shaped(RecipeCategory.TOOLS, ModItems.HAZMAT_SUIT.boots())
                        .pattern("s s")
                        .pattern("sws")
                        .define('s', ModItemTags.PLASTIC_SHEETS)
                        .define('w', ConventionalItemTags.ORANGE_DYES)
                        .unlockedBy(getHasName(ModItems.PLASTIC_SHEET), has(ModItemTags.PLASTIC_SHEETS))
                        .save(output);

                SimpleCookingRecipeBuilder.smoking(
                        Ingredient.of(Items.MILK_BUCKET),
                        RecipeCategory.FOOD,
                        ModItems.HOT_MILK_BUCKET,
                        0.25f,
                        300
                )
                        .unlockedBy(getHasName(Items.MILK_BUCKET), has(ConventionalItemTags.MILK_BUCKETS))
                        .save(output);

                shapeless(RecipeCategory.MISC, ModItems.BIOMASS)
                        .requires(ItemTags.SAPLINGS)
                        .requires(ItemTags.DIRT)
                        .requires(ConventionalItemTags.SEEDS)
                        .requires(ConventionalItemTags.FLOWERS)
                        .unlockedBy("has_flowers", has(ConventionalItemTags.FLOWERS))
                        .save(output);

                shapeless(RecipeCategory.MISC, ModItems.SULFUR_POWDER)
                        .requires(Items.BLAZE_POWDER)
                        .requires(Items.CHARCOAL)
                        .unlockedBy(getHasName(Items.BLAZE_POWDER), has(Items.BLAZE_POWDER))
                        .save(output);

                shapeless(RecipeCategory.MISC, ModItems.TOXIC_COMPOUND)
                        .requires(Items.FERMENTED_SPIDER_EYE)
                        .requires(ConventionalItemTags.GUNPOWDERS)
                        .unlockedBy(getHasName(Items.GUNPOWDER), has(ConventionalItemTags.GUNPOWDERS))
                        .save(output);

                this.addPesticideContainerRecipes();

                this.addSpecialRecipe(InfectFoodRecipe::new, ModRecipes.INFECT_FOOD_RECIPE);
                this.addSpecialRecipe(InfectedBreadRecipe::new, ModRecipes.INFECTED_BREAD_RECIPE);
            }

            public void addPesticideContainerRecipes() {
                for (PesticideType pesticideType : ModRegistries.PESTICIDE_TYPE) {
                    this.createPesticideContainerRecipe(pesticideType)
                            .save(output, "pesticide_container/" + pesticideType.getID().getPath());
                }
            }

            public ShapelessRecipeBuilder createPesticideContainerRecipe(PesticideType pesticideType) {
                ShapelessRecipeBuilder recipeBuilder = shapeless(RecipeCategory.MISC, pesticideType.createContainer())
                        .group("pesticide_container");

                List<Ingredient> sortedIngredients = pesticideType.ingredients()
                        .stream()
                        .sorted(Comparator.comparingInt(item -> BuiltInRegistries.ITEM.getId(item.getItem())))
                        .map(itemstack -> {
                            if (itemstack.is(ModItems.PESTICIDE_CONTAINER)) {
                                return DefaultCustomIngredients.components(itemstack);
                            } else {
                                return Ingredient.of(itemstack.getItem());
                            }
                        })
                        .toList();

                sortedIngredients.forEach(item -> {
                    recipeBuilder.requires(item);
                    recipeBuilder.unlockedBy(getHasName(ModItems.EMPTY_CONTAINER), has(ModItemTags.CONTAINERS));
                });

                return recipeBuilder;
            }

            public void addSpecialRecipe(Function<CraftingBookCategory, Recipe<?>> customRecipeFactory, RecipeSerializer<?> serializer) {
                SpecialRecipeBuilder.special(customRecipeFactory)
                        .save(this.output, Objects.requireNonNull(BuiltInRegistries.RECIPE_SERIALIZER.getKey(serializer)).getPath());
            }
        };
    }

    @Override
    public @NonNull String getName() {
        return "Pesticides recipe provider";
    }
}
