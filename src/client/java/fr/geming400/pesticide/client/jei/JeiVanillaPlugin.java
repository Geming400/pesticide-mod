package fr.geming400.pesticide.client.jei;

import fr.geming400.pesticide.Pesticides;
import fr.geming400.pesticide.content.ModDataComponents;
import fr.geming400.pesticide.content.blocks.ModBlocks;
import fr.geming400.pesticide.content.items.ModItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.core.HolderLookup;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.jspecify.annotations.NonNull;

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
        registration.registerFromDataComponentTypes(ModItems.SUSPICIOUS_WHEAT, ModDataComponents.PESTICIDE_TYPE);
    }

    @Override
    public void registerRecipes(@NonNull IRecipeRegistration registration) {
        HolderLookup<Item> itemLookup = Objects.requireNonNull(Minecraft.getInstance().level).holderLookup(Registries.ITEM);

        registration.addRecipes(RecipeTypes.CRAFTING, JeiRecipes.createFoodInfectionRecipes());
        registration.addRecipes(RecipeTypes.CRAFTING, JeiRecipes.createBreadInfectionRecipe());

        this.addIngredientInfo(registration, ModItems.FAUCET_ANALYSER);
        this.addIngredientInfo(registration, ModItems.ZOMBIE_BONE);
        this.addIngredientInfo(registration, ModItems.COTTON_SWAB);
        this.addIngredientInfo(registration, ModBlocks.FAUCET);
    }

    private void addIngredientInfo(@NonNull IRecipeRegistration registration, ItemLike itemLike) {
        registration.addIngredientInfo(itemLike, Component.translatable(itemLike.asItem().getDescriptionId() + ".info"));
    }
}
