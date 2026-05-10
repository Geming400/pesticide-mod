package fr.geming400.pesticide.client.datagen.language;

import fr.geming400.pesticide.Pesticides;
import fr.geming400.pesticide.content.blocks.ModBlocks;
import fr.geming400.pesticide.content.effects.ModEffects;
import fr.geming400.pesticide.content.items.ModItems;
import fr.geming400.pesticide.content.pesticides.ModPesticides;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class ModEnUsLanguageProvider extends FabricLanguageProvider {
    public static final TranslationUtils.PotionPrefix POTION_PREFIX = new TranslationUtils.PotionPrefix(
            "Potion of",
            "Splash Potion of",
            "Lingering Potion of",
            "Arrow of"
    );

    public ModEnUsLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.@NonNull Provider holderLookup, @NonNull TranslationBuilder builder) {
        TranslationUtils tu = new TranslationUtils(builder);

        builder.add(ModEffects.BAD_FARMER.value(), "Bad Farmer");
        tu.createPotionTranslations(ModEffects.DROWNING, POTION_PREFIX, "Drowning");
        tu.createPotionTranslations(ModEffects.FREEZING, POTION_PREFIX, "Freezing");
        tu.createPotionTranslations(ModEffects.BURNING, POTION_PREFIX, "Burning");

        builder.add(ModBlocks.FAUCET, "Faucet");
        builder.add(
                ModBlocks.FAUCET.getDescriptionId() + ".info",
                "A faucet lets you dispense pesticide on crops. By providing it a redstone signal you can disable it."
        );
        builder.add(ModBlocks.INFESTED_FARMLAND, "Infested Farmland");

        builder.add("itemGroup." + Pesticides.MOD_ID, "Pesticides");

        builder.add(ModItems.EMPTY_CONTAINER, "Empty Container");
        builder.add(ModItems.WATER_CONTAINER, "Water Container");
        builder.add(ModItems.PESTICIDE_CONTAINER, "%s Pesticide Container");
        builder.add(ModItems.PESTICIDE_CONTAINER.getDescriptionId() + ".tooltip.volume", "Contains 1b of %s");
        builder.add(ModItems.PESTICIDE_CONTAINER.getDescriptionId() + ".tooltip.growthFactor", "Has a growth factor of %s");
        builder.add(ModItems.FAUCET_ANALYSER, "Faucet Analyser");
        builder.add(ModItems.FAUCET_ANALYSER.getDescriptionId() + ".onUse", "This faucet has %s mb of %s and is currently %s");
        builder.add(ModItems.FAUCET_ANALYSER.getDescriptionId() + ".onUse.empty", "This faucet is empty");
        builder.add(ModItems.FAUCET_ANALYSER.getDescriptionId() + ".onUse.state.active", "active");
        builder.add(ModItems.FAUCET_ANALYSER.getDescriptionId() + ".onUse.state.notActive", "not active");
        builder.add(
                ModItems.FAUCET_ANALYSER.getDescriptionId() + ".info",
                "A faucet analyser allows to know the amount of pesticide left in a faucet"
        );
        builder.add(ModItems.ZOMBIE_BONE, "Zombie Bone");
        builder.add(
                ModItems.ZOMBIE_BONE.getDescriptionId() + ".info",
                "Zombie Bones have a high chance of dropping from zombies. They are very useful to the creation of pesticides."
        );

        tu.createPesticideTranslation(ModPesticides.TERPINOLENE, "Terpinolene");
        tu.createPesticideTranslation(ModPesticides.GLYPHOSATE, "Glyphosate");
        tu.createPesticideTranslation(ModPesticides.ATRAZINE, "Atrazine");
        tu.createPesticideTranslation(ModPesticides.ENDOSULFAN, "Endosulfan");
        tu.createPesticideTranslation(ModPesticides.ADIFIDOPYROPEN, "Afidopyropen");
        tu.createPesticideTranslation(ModPesticides.AMPROPYFLOS, "Ampropylfos");
    }
}
