package fr.geming400.pesticide.client.datagen;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public abstract class PesticidesLanguageProvider extends FabricLanguageProvider {
    private final LanguageMap languageMap;
    private final CompletableFuture<HolderLookup.Provider> registryLookup;
    private final String languageCode;

    public PesticidesLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup, LanguageMap languageMap) {
        this(dataOutput, "en_us", registryLookup, languageMap);
    }

    public PesticidesLanguageProvider(FabricDataOutput dataOutput, String languageCode, CompletableFuture<HolderLookup.Provider> registryLookup, LanguageMap languageMap) {
        super(dataOutput, languageCode, registryLookup);
        this.languageCode = languageCode;
        this.registryLookup = registryLookup;
        this.languageMap = languageMap;

        this.languageMap.addLanguage(languageCode);
    }

    @Override
    @NotNull
    public CompletableFuture<?> run(@NonNull CachedOutput writer) {
        TreeMap<String, String> translationEntries = new TreeMap<>();

        return this.registryLookup.thenCompose(lookup -> {
            generateTranslations(lookup, (key, value) -> {
                Objects.requireNonNull(key);
                Objects.requireNonNull(value);

                if (translationEntries.containsKey(key)) {
                    throw new RuntimeException("Existing translation key found - " + key + " - Duplicate will be ignored.");
                }

                translationEntries.put(key, value);
            });

            JsonObject langEntryJson = new JsonObject();

            for (Map.Entry<String, String> entry : translationEntries.entrySet()) {
                langEntryJson.addProperty(entry.getKey(), entry.getValue());
                this.languageMap.addTranslation(this.languageCode, entry.getKey(), entry.getValue());
            }

            return DataProvider.saveStable(writer, langEntryJson, getLangFilePath(this.languageCode));
        });
    }

    public static class LanguageMap extends HashMap<String, Map<String, String>> {
        public void addLanguage(String languageID) {
            this.putIfAbsent(languageID, new HashMap<>());
        }

        public void addTranslation(String languageID, String key, String value) {
            this.addLanguage(languageID);
            this.get(languageID).put(key, value);
        }

        public Map<String, String> getTranslations(String languageID) {
            return Objects.requireNonNull(this.get(languageID));
        }
    }
}
