package fr.geming400.pesticide.client.datagen;

import fr.geming400.pesticide.content.blocks.ModBlocks;
import fr.geming400.pesticide.content.items.ModItems;
import fr.geming400.pesticide.content.pesticides.PesticideType;
import fr.geming400.pesticide.content.recipe.InfectFoodRecipe;
import fr.geming400.pesticide.content.recipe.InfectedBreadRecipe;
import fr.geming400.pesticide.content.recipe.ModRecipes;
import fr.geming400.pesticide.content.recipe.PesticideContainerRecipe;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jspecify.annotations.NonNull;

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
                        .unlockedBy(getHasName(Items.IRON_NUGGET), has(ConventionalItemTags.IRON_NUGGETS))
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
                        .unlockedBy(getHasName(Items.IRON_NUGGET), has(ConventionalItemTags.IRON_NUGGETS))
                        .unlockedBy(getHasName(Items.STICK), has(ConventionalItemTags.WOODEN_RODS))
                        .save(output);

                SpecialRecipeBuilder.special(PesticideContainerRecipe::new)
                        .save(this.output, "pesticide_container");

                this.addSpecialRecipe(PesticideContainerRecipe::new, ModRecipes.PESTICIDE_CONTAINER_RECIPE);
                this.addSpecialRecipe(InfectFoodRecipe::new, ModRecipes.INFECT_FOOD_RECIPE);
                this.addSpecialRecipe(InfectedBreadRecipe::new, ModRecipes.INFECTED_BREAD_RECIPE);
            }

            public ShapelessRecipeBuilder createPesticide(PesticideType pesticideType) {
                return shapeless(RecipeCategory.MISC, pesticideType.createContainer())
                        .group("pesticides");
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
