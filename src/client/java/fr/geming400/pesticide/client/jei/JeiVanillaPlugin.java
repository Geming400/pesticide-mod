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

        registration.addRecipes(RecipeTypes.CRAFTING, JeiRecipes.createPesticideContainerRecipes());
        registration.addRecipes(RecipeTypes.CRAFTING, JeiRecipes.createFoodInfectionRecipes());
        registration.addRecipes(RecipeTypes.CRAFTING, JeiRecipes.createBreadInfectionRecipe());

        registration.addIngredientInfo(ModItems.ZOMBIE_BONE, Component.translatable(ModItems.ZOMBIE_BONE.getDescriptionId() + ".info"));
        registration.addIngredientInfo(ModItems.FAUCET_ANALYSER, Component.translatable(ModItems.FAUCET_ANALYSER.getDescriptionId() + ".info"));
        registration.addIngredientInfo(ModBlocks.FAUCET, Component.translatable(ModBlocks.FAUCET.getDescriptionId() + ".info"));
    }
}
