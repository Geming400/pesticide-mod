package fr.geming400.pesticide.content.pesticides;

import com.mojang.serialization.Codec;
import fr.geming400.pesticide.content.ModRegistries;
import fr.geming400.pesticide.content.items.PesticideContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public record PesticideType(float growSpeedFactor, Set<Item> ingredients, MobEffectInstance... effects) {
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

    public ItemStack createContainer() {
        return PesticideContainer.createItemStack(this);
    }

    public boolean ingredientMatches(Collection<ItemStack> items) {
        return new HashSet<>(
                items
                        .stream()
                        .map(ItemStack::getItem)
                        .toList()
        ).containsAll(ingredients);
    }
}
