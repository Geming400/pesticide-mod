package fr.geming400.pesticide.client;

import fr.geming400.pesticide.Pesticides;
import fr.geming400.pesticide.client.datagen.*;
import fr.geming400.pesticide.client.datagen.language.ModFrFrLanguageProvider;
import fr.geming400.pesticide.client.datagen.loottables.ModBlockLoottableProvider;
import fr.geming400.pesticide.client.datagen.language.ModEnUsLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class PesticidesDataGenerator implements DataGeneratorEntrypoint {
	public static final PesticidesLanguageProvider.LanguageMap LANGUAGE_MAP = new PesticidesLanguageProvider.LanguageMap();

	private static final List<String> LANGUAGE_PROVIDERS = new ArrayList<>();

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
		FabricDataGenerator.Pack pack = dataGenerator.createPack();
		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModRecipeProvider::new);
		pack.addProvider(ModBlockLoottableProvider::new);
		pack.addProvider(ModAdvancementProvider::new);
		pack.addProvider(ModItemTagProvider::new);
		pack.addProvider(ModBlockTagProvider::new);

		// Languages
		addLanguageProvider(pack, ModEnUsLanguageProvider::new);
		addLanguageProvider(pack, ModFrFrLanguageProvider::new);
	}

	private static void addLanguageProvider(FabricDataGenerator.Pack pack, FabricDataGenerator.Pack.RegistryDependentFactory<? extends PesticidesLanguageProvider> languageProvider) {
		LANGUAGE_PROVIDERS.add(pack.addProvider(languageProvider).getLanguageCode());
	}

	public static void checkForTranslations(String languageID) {
		if (!LANGUAGE_PROVIDERS.contains("en_us"))
			throw new RuntimeException("Cannot check translations since there are no en_us translations");

		if (languageID.equals("en_us"))
			return;

		AtomicBoolean hasUnsyncedTranslations = new AtomicBoolean(false);

		Set<String> baseTranslationKeys = LANGUAGE_MAP.getTranslations("en_us").keySet();
		Set<String> languageTranslationKeys = LANGUAGE_MAP.getTranslations(languageID).keySet();

		Pesticides.LOGGER.info("Checking translations for {}", languageID);
		for (String translationKey : baseTranslationKeys) {
			if (!languageTranslationKeys.contains(translationKey)) {
				Pesticides.LOGGER.error("Language {} has not a translation key for '{}'", languageID, translationKey);
				hasUnsyncedTranslations.set(true);
			}
		}

		if (hasUnsyncedTranslations.get())
			throw new RuntimeException("Some languages have some untranslated strings. Check your logs for more info");
	}

	public static void checkForAllTranslations() {
		if (!LANGUAGE_PROVIDERS.contains("en_us"))
			throw new RuntimeException("Cannot check translations since there are no en_us translations");

		AtomicBoolean hasUnsyncedTranslations = new AtomicBoolean(false);

		Set<String> baseTranslationKeys = LANGUAGE_MAP.getTranslations("en_us").keySet();
		LANGUAGE_MAP.forEach((languageID, translationsMap) -> {
			Pesticides.LOGGER.info("Checking translations for {}", languageID);
			for (String translationKey : translationsMap.keySet()) {
				if (!baseTranslationKeys.contains(translationKey)) {
					Pesticides.LOGGER.warn("Language {} has not a translation key for {}", languageID, translationKey);
					hasUnsyncedTranslations.set(true);
				}
			}
		});

		if (hasUnsyncedTranslations.get())
			throw new RuntimeException("Some languages have some untranslated strings. Check your logs for more info");
	}
}
