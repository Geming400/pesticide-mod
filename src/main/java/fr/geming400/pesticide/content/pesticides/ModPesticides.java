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
    public static final PesticideType AMPROPYFLOS = of(
            "ampropylfos",
            new PesticideType(
                    0.01f,
                    Set.of(
                            ModItems.WATER_CONTAINER.getDefaultInstance(),
                            ModItems.ZOMBIE_BONE.getDefaultInstance(),
                            Items.COAL.getDefaultInstance(),
                            Items.CLAY_BALL.getDefaultInstance()
                    ),
                    new MobEffectInstance(MobEffects.INSTANT_DAMAGE, 1, 0)
            )
    );

    public static final PesticideType TERPINOLENE = of(
            "terpinolene",
            new PesticideType(
                    0.45f,
                    Set.of(
                            AMPROPYFLOS.createContainer(),
                            ModItems.ZOMBIE_BONE.getDefaultInstance(),
                            Items.COAL.getDefaultInstance(),
                            Items.WHEAT_SEEDS.getDefaultInstance()
                    ),
                    new MobEffectInstance(ModEffects.FREEZING, 20*20, 0)
            )
    );

    public static final PesticideType GLYPHOSATE = of(
            "glyphosate",
            new PesticideType(
                    1.3f,
                    Set.of(
                            TERPINOLENE.createContainer(),
                            ModItems.ZOMBIE_BONE.getDefaultInstance(),
                            Items.COAL.getDefaultInstance(),
                            Items.BONE_MEAL.getDefaultInstance()
                    ),
                    new MobEffectInstance(ModEffects.DROWNING, 30*20, 0)
            )
    );

    public static final PesticideType ATRAZINE = of(
            "atrazine",
            new PesticideType(
                    1.55f,
                    Set.of(
                            GLYPHOSATE.createContainer(),
                            ModItems.ZOMBIE_BONE.getDefaultInstance(),
                            Items.COAL.getDefaultInstance(),
                            Items.GUNPOWDER.getDefaultInstance()
                    ),
                    new MobEffectInstance(MobEffects.POISON, 30*20, 2)
            )
    );

    public static final PesticideType ENDOSULFAN = of(
            "endosulfan",
            new PesticideType(
                    2.15f,
                    Set.of(
                            ATRAZINE.createContainer(),
                            ModItems.ZOMBIE_BONE.getDefaultInstance(),
                            Items.COAL.getDefaultInstance(),
                            Items.QUARTZ.getDefaultInstance()
                    ),
                    new MobEffectInstance(ModEffects.BURNING, 15*20, 2)
            )
    );

    public static final PesticideType ADIFIDOPYROPEN = of(
            "afidopyropen",
            new PesticideType(
                    6.45f,
                    Set.of(
                            ENDOSULFAN.createContainer(),
                            ModItems.ZOMBIE_BONE.getDefaultInstance(),
                            Items.COAL.getDefaultInstance(),
                            Items.DRAGON_BREATH.getDefaultInstance()
                    ),
                    new MobEffectInstance(MobEffects.WITHER, 20*20, 0)
            )
    );


    private static PesticideType of(String name, PesticideType pesticideType) {
        return Registry.register(ModRegistries.PESTICIDE_TYPE, Identifier.fromNamespaceAndPath(Pesticides.MOD_ID, name), pesticideType);
    }

    public static void initialize() {}

    private ModPesticides() {}
}
