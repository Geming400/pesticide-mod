package fr.geming400.pesticide.content.items;

import fr.geming400.pesticide.Pesticides;
import fr.geming400.pesticide.content.items.food.ModConsumeEffects;
import fr.geming400.pesticide.content.items.food.ModFoodProperties;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public final class ModItems {
    public static final Item EMPTY_CONTAINER = register(
            "empty_container",
            EmptyContainer::new,
            new Item.Properties()
    );

    public static final Item WATER_CONTAINER = register(
            "water_container",
            Item::new,
            new Item.Properties()
                    .food(ModFoodProperties.PESTICIDE, ModFoodProperties.WATER_BOTTLE_LIKE_CONSUMABLE)
                    .stacksTo(8)
    );

    public static final Item PESTICIDE_CONTAINER = register(
            "pesticide_container",
            PesticideContainer::new,
            new Item.Properties()
                    .food(ModFoodProperties.PESTICIDE, ModFoodProperties.WATER_BOTTLE_LIKE_CONSUMABLE)
                    .fireResistant()
                    .craftRemainder(EMPTY_CONTAINER)
                    .usingConvertsTo(EMPTY_CONTAINER)
                    .stacksTo(8)
    );

    public static final Item FAUCET_ANALYSER = register(
            "faucet_analyser",
            FaucetAnalyser::new,
            new Item.Properties()
                    .stacksTo(1)
    );

    public static final Item ZOMBIE_BONE = register(
            "zombie_bone",
            Item::new,
            new Item.Properties()
    );

    public static void initialize() {
        ModConsumeEffects.initialize();
        ModPotions.initialize();
    }

    private static <T extends Item> T register(String name, Function<Item.Properties, T> itemFactory, Item.Properties settings) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Pesticides.MOD_ID, name));

        T item = itemFactory.apply(settings.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);

        return item;
    }

    private ModItems() {}
}
