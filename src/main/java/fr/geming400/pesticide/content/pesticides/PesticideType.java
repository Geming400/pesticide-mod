package fr.geming400.pesticide.content.pesticides;

import com.mojang.serialization.Codec;
import fr.geming400.pesticide.content.ModRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

public record PesticideType(float growSpeedFactor) {
    public static final Codec<PesticideType> CODEC = ModRegistries.createRegistryCodec(ModRegistries.PESTICIDE_TYPE);

    public static final StreamCodec<RegistryFriendlyByteBuf, PesticideType> PACKET_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, PesticideType::growSpeedFactor,
            PesticideType::new
    );

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
