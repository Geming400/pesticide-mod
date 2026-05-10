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
        translationBuilder.add(
                ModBlocks.FAUCET.getDescriptionId() + ".info",
                "A faucet lets you dispense pesticide on crops."
        );
        translationBuilder.add(ModBlocks.INFESTED_FARMLAND, "Infested Farmland");

        translationBuilder.add("itemGroup." + Pesticides.MOD_ID, "Pesticides");

        translationBuilder.add(ModItems.EMPTY_CONTAINER, "Empty Container");
        translationBuilder.add(ModItems.WATER_CONTAINER, "Water Container");
        translationBuilder.add(ModItems.PESTICIDE_CONTAINER, "%s Pesticide Container");
        translationBuilder.add(ModItems.PESTICIDE_CONTAINER.getDescriptionId() + ".tooltip.volume", "Contains 1b of %s");
        translationBuilder.add(ModItems.PESTICIDE_CONTAINER.getDescriptionId() + ".tooltip.growthFactor", "Has a growth factor of %s");
        translationBuilder.add(ModItems.FAUCET_ANALYSER, "Faucet analyser");
        translationBuilder.add(ModItems.FAUCET_ANALYSER.getDescriptionId() + ".onUse", "This faucet has %s mb of %s and is currently %s");
        translationBuilder.add(ModItems.FAUCET_ANALYSER.getDescriptionId() + ".onUse.empty", "This faucet is empty");
        translationBuilder.add(ModItems.FAUCET_ANALYSER.getDescriptionId() + ".onUse.state.active", "active");
        translationBuilder.add(ModItems.FAUCET_ANALYSER.getDescriptionId() + ".onUse.state.notActive", "not active");
        translationBuilder.add(
                ModItems.FAUCET_ANALYSER.getDescriptionId() + ".info",
                "A faucet analyser allows to know the amount of pesticide left in a faucet"
        );
        translationBuilder.add(ModItems.ZOMBIE_BONE, "Zombie Bone");
        translationBuilder.add(
                ModItems.ZOMBIE_BONE.getDescriptionId() + ".info",
                "Zombie Bones have a high chance of dropping from zombies. They are very useful to the creation of pesticides."
        );

        createPesticideTranslation(translationBuilder, ModPesticides.ATRAZINE, "Atrazine");
        createPesticideTranslation(translationBuilder, ModPesticides.ENDOSULFAN, "Endosulfan");
        createPesticideTranslation(translationBuilder, ModPesticides.GLYPHOSATE, "Glyphosate");
    }

    public static void createPesticideTranslation(TranslationBuilder translationBuilder, PesticideType pesticideType, String name) {
        translationBuilder.add(pesticideType.getNameTranslationKey(), name);
    }
}
