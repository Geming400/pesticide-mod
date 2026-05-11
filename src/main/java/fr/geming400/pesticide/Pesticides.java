package fr.geming400.pesticide;

import fr.geming400.pesticide.content.ModAttachments;
import fr.geming400.pesticide.content.ModDataComponents;
import fr.geming400.pesticide.content.ModRegistries;
import fr.geming400.pesticide.content.blocks.ModBlocks;
import fr.geming400.pesticide.content.creativetab.ModCreativeTab;
import fr.geming400.pesticide.content.effects.ModEffects;
import fr.geming400.pesticide.content.items.ModItems;
import fr.geming400.pesticide.content.items.food.ModConsumeEffects;
import fr.geming400.pesticide.content.pesticides.ModPesticides;
import fr.geming400.pesticide.content.recipe.ModRecipes;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Pesticides implements ModInitializer {
	public static final String MOD_ID = "pesticides";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModRegistries.initialize();
		ModDataComponents.initialize();
		ModAttachments.initialize();

		ModPesticides.initialize();

		ModBlocks.initialize();
		ModConsumeEffects.initialize();
		ModItems.initialize();
		ModEffects.initialize();
		ModRecipes.initialize();
		ModCreativeTab.initialize();

		LOGGER.info("Hello pesticides lover !!");
	}
}