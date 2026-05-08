package fr.geming400.pesticide.content.effects;

import fr.geming400.pesticide.Pesticides;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;

public final class ModEffects {
    public static final Holder<MobEffect> BAD_FARMER = create("bad_farmer", new BadFarmerEffect());

    private static Holder<MobEffect> create(String name, MobEffect mobEffect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Identifier.fromNamespaceAndPath(Pesticides.MOD_ID, name), mobEffect);
    }

    public static void initialize() {}

    private ModEffects() {}
}
