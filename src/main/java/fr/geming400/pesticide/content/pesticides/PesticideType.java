package fr.geming400.pesticide.content.pesticides;

import com.mojang.serialization.Codec;
import fr.geming400.pesticide.content.ModRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;

public record PesticideType(float growSpeedFactor, MobEffectInstance... effects) {
    public static final Codec<PesticideType> CODEC = ModRegistries.createRegistryCodec(ModRegistries.PESTICIDE_TYPE);

    public Identifier getID() {
        return ModRegistries.PESTICIDE_TYPE.getKey(this);
    }

    public String getNameTranslationKey() {
        return this.getID().toLanguageKey() + ".name";
    }
    public Component getName() {
        return Component.translatable(this.getNameTranslationKey());
    }
}
