package fr.geming400.pesticide.content.items;

import fr.geming400.pesticide.Pesticides;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public final class ModItems {
    public static final Item PESTICIDE_CONTAINER = register(
            "pesticide_container",
            PesticideContainer::new,
            new Item.Properties()
    );
    public static final Item EMPTY_PESTICIDE_CONTAINER = register(
            "empty_pesticide_container",
            PesticideContainer::new,
            new Item.Properties()
    );

    public static void initialize() {}

    private static <T extends Item> T register(String name, Function<Item.Properties, T> itemFactory, Item.Properties settings) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Pesticides.MOD_ID, name));

        T item = itemFactory.apply(settings.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);

        return item;
    }

    private ModItems() {}
}
