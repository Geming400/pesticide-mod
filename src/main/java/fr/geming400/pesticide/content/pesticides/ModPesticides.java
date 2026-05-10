package fr.geming400.pesticide.content.pesticides;

import fr.geming400.pesticide.Pesticides;
import fr.geming400.pesticide.content.ModRegistries;
import fr.geming400.pesticide.content.items.ModItems;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Items;

import java.util.Set;

public final class ModPesticides {
    public static final PesticideType GLYPHOSATE = of(
            "glyphosate",
            new PesticideType(1.35f, Set.of(ModItems.WATER_CONTAINER, ModItems.ZOMBIE_BONE, Items.COAL, Items.BONE_MEAL))
    );
    public static final PesticideType ATRAZINE = of(
            "atrazine",
            new PesticideType(1.65f, Set.of(ModItems.WATER_CONTAINER, ModItems.ZOMBIE_BONE, Items.COAL, Items.GUNPOWDER), new MobEffectInstance(MobEffects.POISON, 30*20, 2))
    );
    public static final PesticideType ENDOSULFAN = of(
            "endosulfan",
            new PesticideType(2.15f, Set.of(ModItems.WATER_CONTAINER, ModItems.ZOMBIE_BONE, Items.COAL, Items.QUARTZ), new MobEffectInstance(MobEffects.WITHER, 15*20, 0))
    );

    private static PesticideType of(String name, PesticideType pesticideType) {
        return Registry.register(ModRegistries.PESTICIDE_TYPE, Identifier.fromNamespaceAndPath(Pesticides.MOD_ID, name), pesticideType);
    }

    public static void initialize() {}

    private ModPesticides() {}
}
