package fr.geming400.pesticide.client;

import fr.geming400.pesticide.client.datagen.loottables.ModBlockLoottableProvider;
import fr.geming400.pesticide.client.datagen.ModItemTagProvider;
import fr.geming400.pesticide.client.datagen.ModModelProvider;
import fr.geming400.pesticide.client.datagen.ModRecipeProvider;
import fr.geming400.pesticide.client.datagen.language.ModEnglishLanguageProvider;
import fr.geming400.pesticide.client.datagen.loottables.ModEntityLoottableProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class PesticidesDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModEnglishLanguageProvider::new);
		pack.addProvider(ModRecipeProvider::new);
		pack.addProvider(ModBlockLoottableProvider::new);
		pack.addProvider(ModItemTagProvider::new);
		pack.addProvider(ModEntityLoottableProvider::new);
	}
}
