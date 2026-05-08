package fr.geming400.pesticide.content.pesticides;

import fr.geming400.pesticide.Pesticides;
import fr.geming400.pesticide.content.ModRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

public final class ModPesticides {
    public static final PesticideType GLYPHOSATE = of("glyphosate", new PesticideType(1));
    public static final PesticideType ENDOSULFAN = of("endosulfan", new PesticideType(1));
    public static final PesticideType ATRAZINE = of("atrazine", new PesticideType(1));

    private static PesticideType of(String name, PesticideType pesticideType) {
        return Registry.register(ModRegistries.PESTICIDE_TYPE, Identifier.fromNamespaceAndPath(Pesticides.MOD_ID, name), pesticideType);
    }

    public static void initialize() {}

    private ModPesticides() {}
}
