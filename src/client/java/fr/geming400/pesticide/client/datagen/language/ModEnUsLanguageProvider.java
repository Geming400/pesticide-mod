package fr.geming400.pesticide.client.datagen.language;

import fr.geming400.pesticide.Pesticides;
import fr.geming400.pesticide.content.blocks.ModBlocks;
import fr.geming400.pesticide.content.effects.ModEffects;
import fr.geming400.pesticide.content.items.ModItems;
import fr.geming400.pesticide.content.pesticides.ModPesticides;
import fr.geming400.pesticide.content.tags.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class ModEnUsLanguageProvider extends FabricLanguageProvider {
    public static final TranslationUtils.PotionFormatting POTION_FORMATTING = new TranslationUtils.PotionFormatting(
            "Potion of %s",
            "Splash Potion of %s",
            "Lingering Potion of %s",
            "Arrow of %s"
    );

    public ModEnUsLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.@NonNull Provider holderLookup, @NonNull TranslationBuilder builder) {
        TranslationUtils tu = new TranslationUtils(builder);

        builder.add(ModEffects.BAD_FARMER.value(), "Bad Farmer");
        tu.createPotionTranslations(ModEffects.DROWNING, POTION_FORMATTING, "Drowning");
        tu.createPotionTranslations(ModEffects.FREEZING, POTION_FORMATTING, "Freezing");
        tu.createPotionTranslations(ModEffects.BURNING, POTION_FORMATTING, "Burning");

        builder.add(ModBlocks.FAUCET, "Faucet");
        builder.add(
                ModBlocks.FAUCET.getDescriptionId() + ".info",
                """
                        A faucet lets you dispense pesticide on crops. By providing it a redstone signal you can disable it.
                        You can also SHIFT+CLICK on it to toggle cycle between its 2 modes: 'single' and 'double'.
                        
                        When in single mode, pesticides only get fired in front of the faucet and it has a radius of 5x3x5
                        When in double mode, its pesticide consumption doubles and also fires pesticide in both sides and it has a radius of 5x5x5
                        """
        );
        builder.add(ModBlocks.INFESTED_FARMLAND, "Infested Farmland");

        builder.add("itemGroup." + Pesticides.MOD_ID, "Pesticides");

        builder.add(ModItems.EMPTY_CONTAINER, "Empty Container");
        builder.add(ModItems.WATER_CONTAINER, "Water Container");
        builder.add(ModItems.PESTICIDE_CONTAINER, "%s Container");
        builder.add(ModItems.PESTICIDE_CONTAINER.getDescriptionId() + ".infectedTooltip", "Infected with pesticide (this doesn't normally show up)");
        builder.add(ModItems.PESTICIDE_CONTAINER.getDescriptionId() + ".unknownType", "Unknown");
        builder.add(ModItems.PESTICIDE_CONTAINER.getDescriptionId() + ".tooltip.volume", "Contains 1b of %s");
        builder.add(ModItems.PESTICIDE_CONTAINER.getDescriptionId() + ".tooltip.general", "Contains %s");
        builder.add(ModItems.PESTICIDE_CONTAINER.getDescriptionId() + ".tooltip.growthFactor", "Has a growth factor of %s");
        builder.add(ModItems.FAUCET_ANALYSER, "Faucet Analyser");
        builder.add(ModItems.FAUCET_ANALYSER.getDescriptionId() + ".onUse", "This faucet has %s mb of %s and is currently %s");
        builder.add(ModItems.FAUCET_ANALYSER.getDescriptionId() + ".onUse.empty", "This faucet is empty");
        builder.add(ModItems.FAUCET_ANALYSER.getDescriptionId() + ".onUse.state.active", "active");
        builder.add(ModItems.FAUCET_ANALYSER.getDescriptionId() + ".onUse.state.notActive", "not active");
        builder.add(
                ModItems.FAUCET_ANALYSER.getDescriptionId() + ".info",
                "A faucet analyser allows to know the volume of pesticide left in a faucet"
        );
        builder.add(ModItems.ZOMBIE_BONE, "Zombie Bone");
        builder.add(
                ModItems.ZOMBIE_BONE.getDescriptionId() + ".info",
                "Zombie Bones have a high chance of dropping from zombies. They are very useful to the creation of pesticides."
        );
        builder.add(ModItems.SUSPICIOUS_WHEAT, "Suspicious Wheat");
        builder.add(ModItems.WOOL_ROD, "Wool Rod");
        builder.add(ModItems.COTTON_SWAB, "Cotton Swab");
        builder.add(ModItems.COTTON_SWAB.getDescriptionId() + ".dirty", "Dirty Cotton Swab");
        builder.add(ModItems.COTTON_SWAB.getDescriptionId() + ".infected", "Infected Cotton Swab");
        builder.add(
                ModItems.COTTON_SWAB.getDescriptionId() + ".info",
                "Cotton swabs are used to find out if a farmland or crop has been infected.\nThe more the land has been infected the more likely it is to work."
        );

        builder.add(ModItemTags.CONTAINERS, "Containers");
        builder.add(ModItemTags.INFECTABLE_FOOD, "Infectable Food");

        tu.createPesticideTranslation(ModPesticides.TERPINOLENE, "Terpinolene");
        tu.createPesticideTranslation(ModPesticides.GLYPHOSATE, "Glyphosate");
        tu.createPesticideTranslation(ModPesticides.ATRAZINE, "Atrazine");
        tu.createPesticideTranslation(ModPesticides.ENDOSULFAN, "Endosulfan");
        tu.createPesticideTranslation(ModPesticides.ADIFIDOPYROPEN, "Afidopyropen");
        tu.createPesticideTranslation(ModPesticides.AMPROPYFLOS, "Ampropylfos");

        tu.advancement("core")
                .root("Pesticides", "The start of every pesticide industry, zombie bones !")
                .add("a_new_farmer_era", "A new farmer era", "All hail pesticides !")
                .add("first_pesticides" ,"First pesticides", "And that's how you make ecologists happy !")
                .add("a_true_farmer" ,"A true farmer", "Get the \"bad farmer\" effect");
    }
}
