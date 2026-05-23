package fr.geming400.pesticide.client;

import fr.geming400.pesticide.client.datagen.*;
import fr.geming400.pesticide.client.datagen.language.ModFrFrLanguageProvider;
import fr.geming400.pesticide.client.datagen.loottables.ModBlockLoottableProvider;
import fr.geming400.pesticide.client.datagen.language.ModEnUsLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class PesticidesDataGenerator implements DataGeneratorEntrypoint {
	public static final PesticidesLanguageProvider.LanguageMap LANGUAGE_MAP = new PesticidesLanguageProvider.LanguageMap();

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModRecipeProvider::new);
		pack.addProvider(ModBlockLoottableProvider::new);
		pack.addProvider(ModAdvancementProvider::new);
		pack.addProvider(ModItemTagProvider::new);
		pack.addProvider(ModBlockTagProvider::new);

		// Languages
		pack.addProvider(ModEnUsLanguageProvider::new);
		pack.addProvider(ModFrFrLanguageProvider::new);
	}
}
