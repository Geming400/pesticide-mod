package fr.geming400.pesticide.content;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Lifecycle;
import fr.geming400.pesticide.Pesticides;
import fr.geming400.pesticide.content.pesticides.PesticideType;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import java.util.function.Function;

public final class ModRegistries {
    public static final ResourceKey<Registry<PesticideType>> PESTICIDE_TYPE_KEY = createResourceKey("pesticide_types");
    public static final Registry<PesticideType> PESTICIDE_TYPE = create(PESTICIDE_TYPE_KEY);

    private static <T> Registry<T> create(ResourceKey<? extends Registry<T>> resourceKey) {
        return new MappedRegistry<>(resourceKey, Lifecycle.stable(), false);
    }
    private static <T> ResourceKey<Registry<T>> createResourceKey(String name) {
        return ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(Pesticides.MOD_ID, name));
    }

    public static <T> DataResult<T> validateRegistry(Identifier id, Registry<T> registry) {
        T obj = registry.getValue(id);
        return obj == null ? DataResult.error(() -> "Not a valid registry entry: " + id) : DataResult.success(obj);
    }
    public static <T> Function<Identifier, DataResult<T>> createRegistryValidator(Registry<T> registry) {
        return identifier -> validateRegistry(identifier, registry);
    }

    public static <T> Codec<T> createRegistryCodec(Registry<T> registry, Function<T, Identifier> idGetter) {
        return Identifier.CODEC.comapFlatMap(ModRegistries.createRegistryValidator(registry), idGetter).stable();
    }
    public static <T> Codec<T> createRegistryCodec(Registry<T> registry) {
        return createRegistryCodec(registry, registry::getKey);
    }

    public static void initialize() {}

    private ModRegistries() {}
}
