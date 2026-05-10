package fr.geming400.pesticide.content.pesticides;

import fr.geming400.pesticide.Pesticides;
import fr.geming400.pesticide.content.ModRegistries;
import fr.geming400.pesticide.content.effects.ModEffects;
import fr.geming400.pesticide.content.items.ModItems;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Items;

import java.util.Set;

public final class ModPesticides {
    public static final PesticideType TERPINOLENE = of(
            "terpinolene",
            new PesticideType(0.45f, Set.of(ModItems.WATER_CONTAINER, ModItems.ZOMBIE_BONE, Items.COAL, Items.WHEAT_SEEDS), new MobEffectInstance(ModEffects.BURNING, 20*20, 0))
    );

    public static final PesticideType GLYPHOSATE = of(
            "glyphosate",
            new PesticideType(1.3f, Set.of(ModItems.WATER_CONTAINER, ModItems.ZOMBIE_BONE, Items.COAL, Items.BONE_MEAL), new MobEffectInstance(ModEffects.DROWNING, 30*20, 0))
    );

    public static final PesticideType ATRAZINE = of(
            "atrazine",
            new PesticideType(1.55f, Set.of(ModItems.WATER_CONTAINER, ModItems.ZOMBIE_BONE, Items.COAL, Items.GUNPOWDER), new MobEffectInstance(MobEffects.POISON, 30*20, 2))
    );

    public static final PesticideType ENDOSULFAN = of(
            "endosulfan",
            new PesticideType(2.15f, Set.of(ModItems.WATER_CONTAINER, ModItems.ZOMBIE_BONE, Items.COAL, Items.QUARTZ), new MobEffectInstance(MobEffects.WITHER, 15*20, 0))
    );

    public static final PesticideType ADIFIDOPYROPEN = of(
            "afidopyropen",
            new PesticideType(5.65f, Set.of(ModItems.WATER_CONTAINER, ModItems.ZOMBIE_BONE, Items.COAL, Items.SCULK), new MobEffectInstance(ModEffects.FREEZING, 20*20, 0))
    );

    public static final PesticideType AMPROPYFLOS = of(
            "ampropylfos",
            new PesticideType(Float.MAX_VALUE, Set.of(ModItems.WATER_CONTAINER, ModItems.ZOMBIE_BONE, Items.COAL, Items.BEDROCK), new MobEffectInstance(MobEffects.INSTANT_DAMAGE, 20, 255))
    );


    private static PesticideType of(String name, PesticideType pesticideType) {
        return Registry.register(ModRegistries.PESTICIDE_TYPE, Identifier.fromNamespaceAndPath(Pesticides.MOD_ID, name), pesticideType);
    }

    public static void initialize() {}

    private ModPesticides() {}
}
