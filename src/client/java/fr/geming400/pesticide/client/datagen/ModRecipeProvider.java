package fr.geming400.pesticide.client.datagen;

import fr.geming400.pesticide.content.blocks.ModBlocks;
import fr.geming400.pesticide.content.items.ModItems;
import fr.geming400.pesticide.content.pesticides.PesticideType;
import fr.geming400.pesticide.content.recipe.PesticideContainerRecipe;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

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
                
                SpecialRecipeBuilder.special(PesticideContainerRecipe::new)
                        .save(this.output, "pesticide_container");
            }

            public ShapelessRecipeBuilder createPesticide(PesticideType pesticideType) {
                return shapeless(RecipeCategory.MISC, pesticideType.createContainer())
                        .group("pesticides");
            }
        };
    }

    @Override
    public @NonNull String getName() {
        return "Pesticides recipe provider";
    }
}
