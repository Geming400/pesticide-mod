package fr.geming400.pesticide.client.datagen.language;

import fr.geming400.pesticide.Pesticides;
import fr.geming400.pesticide.content.blocks.ModBlocks;
import fr.geming400.pesticide.content.effects.ModEffects;
import fr.geming400.pesticide.content.items.ModItems;
import fr.geming400.pesticide.content.pesticides.ModPesticides;
import fr.geming400.pesticide.content.pesticides.PesticideType;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class ModEnglishLanguageProvider extends FabricLanguageProvider {
    public ModEnglishLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.@NonNull Provider holderLookup, @NonNull TranslationBuilder translationBuilder) {
        translationBuilder.add(ModEffects.BAD_FARMER.value(), "Bad Farmer");

        translationBuilder.add(ModBlocks.FAUCET, "Faucet");
        translationBuilder.add(ModBlocks.INFESTED_FARMLAND, "Infested Farmland");

        translationBuilder.add("itemGroup." + Pesticides.MOD_ID, "Pesticides");

        translationBuilder.add(ModItems.EMPTY_CONTAINER, "Empty Container");
        translationBuilder.add(ModItems.WATER_CONTAINER, "Water Container");
        translationBuilder.add(ModItems.PESTICIDE_CONTAINER, "%s Pesticide Container");
        translationBuilder.add(ModItems.PESTICIDE_CONTAINER.getDescriptionId() + ".tooltip.volume", "Contains 1b of %s");
        translationBuilder.add(ModItems.PESTICIDE_CONTAINER.getDescriptionId() + ".tooltip.growthFactor", "Has a growth factor of %s");
        translationBuilder.add(ModItems.ZOMBIE_BONE, "Zombie Bone");

        createPesticideTranslation(translationBuilder, ModPesticides.ATRAZINE, "Atrazine");
        createPesticideTranslation(translationBuilder, ModPesticides.ENDOSULFAN, "Endosulfan");
        createPesticideTranslation(translationBuilder, ModPesticides.GLYPHOSATE, "Glyphosate");
    }

    public static void createPesticideTranslation(TranslationBuilder translationBuilder, PesticideType pesticideType, String name) {
        translationBuilder.add(pesticideType.getNameTranslationKey(), name);
    }
}
