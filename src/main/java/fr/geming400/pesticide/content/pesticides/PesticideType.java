package fr.geming400.pesticide.content.pesticides;

import com.mojang.serialization.Codec;
import fr.geming400.pesticide.Pesticides;
import fr.geming400.pesticide.content.ModDataComponents;
import fr.geming400.pesticide.content.ModRegistries;
import fr.geming400.pesticide.content.items.ModItems;
import fr.geming400.pesticide.content.items.PesticideContainer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import org.jspecify.annotations.NonNull;

import java.util.*;

public record PesticideType(float growSpeedFactor, Set<Item> ingredients, MobEffectInstance... effects) implements ItemLike {
    public static final Codec<PesticideType> CODEC = ModRegistries.createRegistryCodec(ModRegistries.PESTICIDE_TYPE);

    public static final StreamCodec<RegistryFriendlyByteBuf, PesticideType> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC, PesticideType::getID,
            PesticideType::fromID
    );


    public Identifier getID() {
        return Objects.requireNonNull(ModRegistries.PESTICIDE_TYPE.getKey(this));
    }

    public String getNameTranslationKey() {
        return this.getID().toLanguageKey() + ".name";
    }

    public Component getName() {
        return Component.translatable(this.getNameTranslationKey());
    }

    /**
     * Creates the {@linkplain PesticideContainer container item} linked to this {@link PesticideType}
     * @return the {@linkplain PesticideContainer container item} linked to this {@link PesticideType}
     */
    public ItemStack createContainer() {
        return PesticideContainer.createItemStack(this);
    }

    public ItemStack createSuspiciousWheat() {
        ItemStack itemStack = new ItemStack(ModItems.SUSPICIOUS_WHEAT);
        itemStack.set(ModDataComponents.PESTICIDE_TYPE, this);

        return itemStack;
    }

    public boolean ingredientMatches(Collection<ItemStack> items) {
        return new HashSet<>(
                items
                        .stream()
                        .map(ItemStack::getItem)
                        .toList()
        ).containsAll(ingredients);
    }

    public RecipeHolder<CraftingRecipe> createJeiRecipe() {
        CraftingRecipe recipe = new ShapelessRecipe(
                "pesticide_container",
                CraftingBookCategory.MISC,
                this.createContainer(),
                this.ingredients()
                        .stream()
                        .sorted(Comparator.comparingInt(BuiltInRegistries.ITEM::getId))
                        .map(Ingredient::of)
                        .toList()
        );

        Identifier recipeID = Identifier.fromNamespaceAndPath(
                Pesticides.MOD_ID,
                "pesticide_container/" + this.getID().getPath()
        );
        ResourceKey<Recipe<?>> recipeResourceKey = ResourceKey.create(Registries.RECIPE, recipeID);

        return new RecipeHolder<>(recipeResourceKey, recipe);
    }

    @Override
    @NonNull
    public Item asItem() {
        return this.createContainer().getItem();
    }

    public static PesticideType fromID(Identifier id) {
        return Objects.requireNonNull(ModRegistries.PESTICIDE_TYPE.getValue(id));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getID());
    }
}
