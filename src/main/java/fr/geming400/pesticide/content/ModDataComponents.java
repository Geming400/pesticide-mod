package fr.geming400.pesticide.content;

import com.mojang.serialization.Codec;
import fr.geming400.pesticide.Pesticides;
import fr.geming400.pesticide.content.pesticides.PesticideType;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;

public final class ModDataComponents {
    public static final DataComponentType<PesticideType> PESTICIDE_TYPE = create(
            "pesticide_type",
            DataComponentType.<PesticideType>builder()
                    .persistent(PesticideType.CODEC)
                    .networkSynchronized(PesticideType.STREAM_CODEC)
    );

    public static final DataComponentType<Boolean> COTTON_SWAB_USED = create(
            "cotton_swab_used",
            DataComponentType.<Boolean>builder()
                    .persistent(Codec.BOOL)
                    .networkSynchronized(ByteBufCodecs.BOOL)
    );

    private static <T> DataComponentType<T> create(String name, DataComponentType.Builder<T> builder) {
        return Registry.register(
                BuiltInRegistries.DATA_COMPONENT_TYPE,
                Identifier.fromNamespaceAndPath(Pesticides.MOD_ID, name),
                builder.build()
        );
    }

    public static void initialize() {}

    private ModDataComponents() {}
}
