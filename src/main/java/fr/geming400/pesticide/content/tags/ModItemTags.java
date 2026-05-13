package fr.geming400.pesticide.content.tags;

import fr.geming400.pesticide.Pesticides;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class ModItemTags {
    public static final TagKey<Item> CONTAINERS = create("containers");
    public static final TagKey<Item> INFECTABLE_FOOD = create("infectable_foods");
    public static final TagKey<Item> PLASTIC_SHEETS = createConventional("plates/plastic");

    private static TagKey<Item> create(String name) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Pesticides.MOD_ID, name));
    }

    private static TagKey<Item> createConventional(String name) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", name));
    }

    private ModItemTags() {}
}
