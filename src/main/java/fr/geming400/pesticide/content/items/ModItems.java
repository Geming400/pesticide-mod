package fr.geming400.pesticide.content.items;

import fr.geming400.pesticide.Pesticides;
import fr.geming400.pesticide.content.armor.HazmatArmorMaterial;
import fr.geming400.pesticide.content.items.food.ModFoodProperties;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.List;
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
                    .usingConvertsTo(EMPTY_CONTAINER)
                    .stacksTo(8)
    );

    public static final Item PESTICIDE_CONTAINER = register(
            "pesticide_container",
            PesticideContainer::new,
            new Item.Properties()
                    .food(ModFoodProperties.PESTICIDE, ModFoodProperties.PESTICIBLE_CONTAINER_CONSUMABLE)
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

    public static final Item COTTON_SWAB = register(
            "cotton_swab",
            CottonSwab::new,
            new Item.Properties()
                    .durability(8)
    );

    public static final Item ZOMBIE_BONE = register("zombie_bone");

    public static final Item WOOL_ROD = register(
            "wool_rod",
            Item::new,
            new Item.Properties()
    );

    public static final Item SUSPICIOUS_WHEAT = register("suspicious_wheat");

    public static final Item HOT_MILK_BUCKET = register(
            "hot_milk_bucket",
            Item::new,
            new Item.Properties()
                    .stacksTo(1)
                    .craftRemainder(Items.BUCKET)
                    .usingConvertsTo(Items.BUCKET)
                    .food(ModFoodProperties.HOT_MILK_BUCKET, Consumables.defaultDrink().build())
    );

    public static final Item PLASTIC_SHEET = register("plastic_sheet");

    public static final Item BIOMASS = register("biomass");

    public static final Item SULFUR_POWDER = register("sulfur_powder");

    public static final Item TOXIC_COMPOUND = register("toxic_compound");

    public static final ArmorItems HAZMAT_SUIT = ArmorItems.of("hazmat", HazmatArmorMaterial.INSTANCE, HazmatArmorMaterial.BASE_DURABILITY);

    public static void initialize() {
        ModPotions.initialize();
    }

    private static Item register(String name) {
        return register(name, Item::new, new Item.Properties());
    }

    private static <T extends Item> T register(String name, Function<Item.Properties, T> itemFactory, Item.Properties settings) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Pesticides.MOD_ID, name));

        T item = itemFactory.apply(settings.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);

        return item;
    }

    public record ArmorItems(Item helmet, Item chestplate, Item leggings, Item boots) {
        @Unmodifiable
        public Collection<Item> getAllArmorItem() {
            return List.of(this.helmet, this.chestplate, this.leggings, this.boots);
        }

        public boolean isWearing(LivingEntity entity) {
            return entity.getItemBySlot(EquipmentSlot.HEAD).is(this.helmet())
                    && entity.getItemBySlot(EquipmentSlot.CHEST).is(this.chestplate())
                    && entity.getItemBySlot(EquipmentSlot.LEGS).is(this.leggings())
                    && entity.getItemBySlot(EquipmentSlot.FEET).is(this.boots());
        }

        private static ArmorItems of(String name, ArmorMaterial armorMaterial, int durability) {
            Item helmet = register(
                    name + "_helmet",
                    Item::new,
                    new Item.Properties()
                            .humanoidArmor(armorMaterial, ArmorType.HELMET)
                            .durability(ArmorType.HELMET.getDurability(durability))
            );

            Item chestplate = register(
                    name + "_chestplate",
                    Item::new,
                    new Item.Properties()
                            .humanoidArmor(armorMaterial, ArmorType.CHESTPLATE)
                            .durability(ArmorType.CHESTPLATE.getDurability(durability))
            );

            Item leggings = register(
                    name + "_leggings",
                    Item::new,
                    new Item.Properties()
                            .humanoidArmor(armorMaterial, ArmorType.LEGGINGS)
                            .durability(ArmorType.LEGGINGS.getDurability(durability))
            );

            Item boots = register(
                    name + "_boots",
                    Item::new,
                    new Item.Properties()
                            .humanoidArmor(armorMaterial, ArmorType.BOOTS)
                            .durability(ArmorType.BOOTS.getDurability(durability))
            );

            return new ArmorItems(helmet, chestplate, leggings, boots);
        }
    }

    private ModItems() {}
}
