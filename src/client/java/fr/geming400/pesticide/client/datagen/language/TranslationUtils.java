package fr.geming400.pesticide.client.datagen.language;

import fr.geming400.pesticide.Pesticides;
import fr.geming400.pesticide.client.datagen.ModAdvancementProvider;
import fr.geming400.pesticide.content.pesticides.PesticideType;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;

public final class TranslationUtils {
    private final FabricLanguageProvider.TranslationBuilder translationBuilder;

    public TranslationUtils(FabricLanguageProvider.TranslationBuilder translationBuilder) {
        this.translationBuilder = translationBuilder;
    }

    public void createPesticideTranslation(PesticideType pesticideType, String name) {
        this.translationBuilder.add(pesticideType.getNameTranslationKey(), name);
    }

    public void createPotionTranslations(
            Holder<MobEffect> effect,
            PotionPrefix potionPrefix,
            String name
    ) {
        this.translationBuilder.add(effect.value(), name);

        String potionID = Identifier.parse(effect.getRegisteredName()).getPath();
        this.translationBuilder.add(
                "item.minecraft.potion.effect.%s".formatted(potionID),
                "%s %s".formatted(potionPrefix.potion, name)
        );
        this.translationBuilder.add(
                "item.minecraft.splash_potion.effect.%s".formatted(potionID),
                "%s %s".formatted(potionPrefix.splashPotion, name)
        );
        this.translationBuilder.add(
                "item.minecraft.lingering_potion.effect.%s".formatted(potionID),
                "%s %s".formatted(potionPrefix.lingeringPotion, name)
        );
        this.translationBuilder.add(
                "item.minecraft.tipped_arrow.effect.%s".formatted(potionID),
                "%s %s".formatted(potionPrefix.tippedArrow, name)
        );
    }

    public AdvancementTranslationBuilder advancement(String id) {
        return new AdvancementTranslationBuilder(this.translationBuilder, id);
    }

    public static abstract class BaseBuilder {
        protected final FabricLanguageProvider.TranslationBuilder translationBuilder;

        public BaseBuilder(FabricLanguageProvider.TranslationBuilder translationBuilder) {
            this.translationBuilder = translationBuilder;
        }
    }

    public static class AdvancementTranslationBuilder extends BaseBuilder {
        private final String id;

        public AdvancementTranslationBuilder(FabricLanguageProvider.TranslationBuilder translationBuilder, String id) {
            super(translationBuilder);
            this.id = id;
        }

        public AdvancementTranslationBuilder add(String path, String name, String description) {
            String rootID = "advancements.%s.%s.%s.".formatted(Pesticides.MOD_ID, this.id, path);
            this.translationBuilder.add(rootID + "title", name);
            this.translationBuilder.add(rootID + "description", description);

            return this;
        }

        /// Adds the {@code root} translation
        public AdvancementTranslationBuilder root(String name, String description) {
            return this.add(ModAdvancementProvider.AdvancementBuilder.ROOT, name, description);
        }
    }

    public record PotionPrefix(
            String potion,
            String splashPotion,
            String lingeringPotion,
            String tippedArrow
    ) {}
}
