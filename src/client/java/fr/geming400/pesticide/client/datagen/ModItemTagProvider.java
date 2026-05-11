package fr.geming400.pesticide.client.datagen;

import fr.geming400.pesticide.Utils;
import fr.geming400.pesticide.content.items.ModItems;
import fr.geming400.pesticide.content.tags.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider wrapperLookup) {
        this.valueLookupBuilder(ConventionalItemTags.BONES)
                .add(ModItems.ZOMBIE_BONE);

        this.valueLookupBuilder(ModItemTags.CONTAINERS)
                .add(ModItems.EMPTY_CONTAINER)
                .add(ModItems.WATER_CONTAINER)
                .add(ModItems.PESTICIDE_CONTAINER);

        TagAppender<Item, Item> infectableFoodBuilder = this.valueLookupBuilder(ModItemTags.INFECTABLE_FOOD);
        for (Item item : BuiltInRegistries.ITEM) {
            if (
                    Utils.isInfectable(item.getDefaultInstance())
                    && item != ModItems.EMPTY_CONTAINER
                    && item != ModItems.WATER_CONTAINER
                    && item != ModItems.PESTICIDE_CONTAINER
            )
                infectableFoodBuilder.add(item);
        }
    }
}
