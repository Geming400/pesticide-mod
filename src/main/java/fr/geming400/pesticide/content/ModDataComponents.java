package fr.geming400.pesticide.content;

import fr.geming400.pesticide.Pesticides;
import fr.geming400.pesticide.content.pesticides.PesticideType;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public final class ModDataComponents {
    public static final DataComponentType<PesticideType> PESTICIDE_TYPE = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(Pesticides.MOD_ID, "pesticide_type"),
            DataComponentType.<PesticideType>builder()
                    .persistent(PesticideType.CODEC)
                    .build()
    );

    public static void initialize() {}

    private ModDataComponents() {}
}
