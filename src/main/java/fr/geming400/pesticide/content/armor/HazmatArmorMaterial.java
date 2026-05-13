package fr.geming400.pesticide.content.armor;

import fr.geming400.pesticide.Pesticides;
import fr.geming400.pesticide.content.tags.ModItemTags;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

import java.util.Map;

public final class HazmatArmorMaterial {
    public static final int BASE_DURABILITY = 15;
    public static final ResourceKey<EquipmentAsset> HAZMAT_ARMOR_MATERIAL_KEY = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(Pesticides.MOD_ID, "hazmat")
    );

    public static final ArmorMaterial INSTANCE = new ArmorMaterial(
            BASE_DURABILITY,
            Map.of(
                    ArmorType.HELMET, 3,
                    ArmorType.CHESTPLATE, 8,
                    ArmorType.LEGGINGS, 6,
                    ArmorType.BOOTS, 3
            ),
            1,
            SoundEvents.ARMOR_EQUIP_IRON,
            0.0f,
            0.0f,
            ModItemTags.PLASTIC_SHEETS,
            HAZMAT_ARMOR_MATERIAL_KEY
    );
}
