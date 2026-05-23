package fr.geming400.pesticide.client.datagen.language;

import fr.geming400.pesticide.Pesticides;
import fr.geming400.pesticide.client.PesticidesDataGenerator;
import fr.geming400.pesticide.client.datagen.PesticidesLanguageProvider;
import fr.geming400.pesticide.content.blocks.ModBlocks;
import fr.geming400.pesticide.content.effects.ModEffects;
import fr.geming400.pesticide.content.items.ModItems;
import fr.geming400.pesticide.content.pesticides.ModPesticides;
import fr.geming400.pesticide.content.tags.ModBlockTags;
import fr.geming400.pesticide.content.tags.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class ModFrFrLanguageProvider extends PesticidesLanguageProvider {
    public static final TranslationUtils.PotionFormatting POTION_FORMATTING = new TranslationUtils.PotionFormatting(
            "Potion de %s",
            "Potion de %s jetable",
            "Potion de %s persistante",
            "Flèche de %s"
    );

    public ModFrFrLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, "fr_fr", registryLookup, PesticidesDataGenerator.LANGUAGE_MAP);
    }

    @Override
    public void generateTranslations(HolderLookup.@NonNull Provider holderLookup, @NonNull TranslationBuilder builder) {
        TranslationUtils tu = new TranslationUtils(builder);

        builder.add(ModEffects.BAD_FARMER.value(), "Mauvais Fermier");
        tu.createPotionTranslations(ModEffects.DROWNING, POTION_FORMATTING, "Noyade", true);
        tu.createPotionTranslations(ModEffects.FREEZING, POTION_FORMATTING, "Gel", true);
        tu.createPotionTranslations(ModEffects.BURNING, POTION_FORMATTING, "Brûlure", true);

        builder.add(ModBlocks.COPPER_FAUCET, "Robinet en cuivre");
        builder.add(ModBlocks.IRON_FAUCET, "Robinet en fer");
        builder.add(ModBlocks.DIAMOND_FAUCET, "Robinet en diamant");
        builder.add(ModBlocks.NETHERITE_FAUCET, "Robinet en netherite");
        builder.add("block.pesticides.faucet.tooltip.infectionChance", "A une chance de %s%% d'infecter de la terre labourée");
        builder.add(
                "block.pesticides.faucet.info",
                """
                        Un robinet permet de mettre des pesticides sur des plantations. En lui donnant un signal de redstone, tu peux le désactiver.
                        Il est aussi possible de faire SHIFT + CLIC pour changer entre ses différents modes : 'simple' et 'double'.
                        
                        Quand il est en mode 'simple', les pesticides sont seulement envoyés en face du robinet, dans une zone de 5x3x5.
                        Quand il est en mode 'double', sa consommation de pesticides double et il envoie des pesticides dans les deux directions, il couvre donc une zone de 5x5x5.
                        """
        );
        builder.add(ModBlocks.INFESTED_FARMLAND, "Terre labourée infectée");

        builder.add("itemGroup." + Pesticides.MOD_ID, "Pesticides");

        builder.add(ModItems.EMPTY_CONTAINER, "Bidon vide");
        builder.add(ModItems.WATER_CONTAINER, "Bidon d'eau");
        builder.add(ModItems.PESTICIDE_CONTAINER, "Bidon de %s");
        builder.add(ModItems.PESTICIDE_CONTAINER.getDescriptionId() + ".infectedTooltip", "Nourriture infectée par des pesticides (ceci n'est pas affiché sur l'objet crafté)");
        builder.add(ModItems.PESTICIDE_CONTAINER.getDescriptionId() + ".unknownType", "pesticide inconnu");
        builder.add(ModItems.PESTICIDE_CONTAINER.getDescriptionId() + ".tooltip.volume", "Contient 1b de %s");
        builder.add(ModItems.PESTICIDE_CONTAINER.getDescriptionId() + ".tooltip.general", "Contient du %s");
        builder.add(ModItems.PESTICIDE_CONTAINER.getDescriptionId() + ".tooltip.growthFactor", "Possède un facteur de croissance de %s");
        builder.add(ModItems.FAUCET_ANALYSER, "Analyseur de robinet");
        builder.add(ModItems.FAUCET_ANALYSER.getDescriptionId() + ".onUse", "Ce robinet contient %s mb de %s et est %s");
        builder.add(ModItems.FAUCET_ANALYSER.getDescriptionId() + ".onUse.empty", "Ce robinet est vide");
        builder.add(ModItems.FAUCET_ANALYSER.getDescriptionId() + ".onUse.state.active", "actif");
        builder.add(ModItems.FAUCET_ANALYSER.getDescriptionId() + ".onUse.state.notActive", "inactif");
        builder.add(
                ModItems.FAUCET_ANALYSER.getDescriptionId() + ".info",
                "Un analyseur de robinet permet de connaître le volume de pesticides contenu dans un robinet."
        );
        builder.add(ModItems.ZOMBIE_BONE, "Os de zombie");
        builder.add(
                ModItems.ZOMBIE_BONE.getDescriptionId() + ".info",
                "Les os de zombies ont une forte chance d'être lâchés par les zombies. Ils sont très utiles pour la fabrication de pesticides."
        );
        builder.add(ModItems.SUSPICIOUS_WHEAT, "Blé suspect");
        builder.add(ModItems.WOOL_ROD, "Tige de laine");
        builder.add(ModItems.COTTON_SWAB, "Coton-tige");
        builder.add(ModItems.COTTON_SWAB.getDescriptionId() + ".dirty", "Coton-tige sale");
        builder.add(ModItems.COTTON_SWAB.getDescriptionId() + ".infected", "Coton-tige infecté");
        builder.add(
                ModItems.COTTON_SWAB.getDescriptionId() + ".info",
                "Les coton-tiges sont utiles pour savoir si de la terre labourée ou une plante à été infectée par du pesticide. Le plus la terre à été infectée, le plus le coton tige à de chance de fonctionner.\nTu peut faire SHIFT+CLICK dans l'air pour le nettoyer."
        );
        builder.add(ModItems.HOT_MILK_BUCKET, "Seau de lait chaud");
        builder.add(ModItems.PLASTIC_SHEET, "Plastique");
        builder.add(ModItems.BIOMASS, "Biomasse");
        builder.add(ModItems.BIOMASS_BAG, "Sac de biomasse");
        builder.add(ModItems.SULFUR_POWDER, "Poudre de soufre");
        builder.add(ModItems.TOXIC_COMPOUND, "Composé toxique");

        builder.add(ModItems.HAZMAT_SUIT.helmet(), "Casque de protection Hazmat");
        builder.add(ModItems.HAZMAT_SUIT.chestplate(), "Plastron de protection Hazmat");
        builder.add(ModItems.HAZMAT_SUIT.leggings(), "Jambières de protection Hazmat");
        builder.add(ModItems.HAZMAT_SUIT.boots(), "Bottes de protection Hazmat");

        builder.add(ModItemTags.CONTAINERS, "Bidons");
        builder.add(ModItemTags.INFECTABLE_FOOD, "Nourriture infectable");
        builder.add(ModItemTags.PLASTIC_SHEETS, "Plastiques");
        builder.add(ModBlockTags.FAUCETS, "Robinets");

        tu.createPesticideTranslation(ModPesticides.TERPINOLENE, "Terpinolène");
        tu.createPesticideTranslation(ModPesticides.GLYPHOSATE, "Glyphosate");
        tu.createPesticideTranslation(ModPesticides.ATRAZINE, "Atrazine");
        tu.createPesticideTranslation(ModPesticides.ENDOSULFAN, "Endosulfan");
        tu.createPesticideTranslation(ModPesticides.ADIFIDOPYROPEN, "Afidopyropen");
        tu.createPesticideTranslation(ModPesticides.AMPROPYFLOS, "Ampropylfos");

        tu.advancement("core")
                .root("Pesticides", "Le début de toute entreprise de pesticides: les os de zombies !")
                .add("a_new_farmer_era", "Une nouvelle ère dans l'agriculture", "Tous hail les pesticides !")
                .add("first_pesticides" ,"Les premiers pesticides", "Et voilà comment rendre les écolos heureux !")
                .add("a_true_farmer" ,"Un vrai fermier", "Obtenir l'effet \"Mauvais fermier\"");
    }
}
