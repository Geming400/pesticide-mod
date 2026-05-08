package fr.geming400.pesticide.content.pesticides;

import fr.geming400.pesticide.Pesticides;
import fr.geming400.pesticide.content.ModRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public final class ModPesticides {
    public static final PesticideType GLYPHOSATE = of("glyphosate", new PesticideType(1.35f));
    public static final PesticideType ATRAZINE = of("atrazine", new PesticideType(1.75f, new MobEffectInstance(MobEffects.POISON, 30*20, 2)));
    public static final PesticideType ENDOSULFAN = of("endosulfan", new PesticideType(2f, new MobEffectInstance(MobEffects.WITHER, 15*20, 0)));

    private static PesticideType of(String name, PesticideType pesticideType) {
        return Registry.register(ModRegistries.PESTICIDE_TYPE, Identifier.fromNamespaceAndPath(Pesticides.MOD_ID, name), pesticideType);
    }

    public static void initialize() {}

    private ModPesticides() {}
}
