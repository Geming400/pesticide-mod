package fr.geming400.pesticide.content.tags;

import fr.geming400.pesticide.Pesticides;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class ModBlockTags {
    public static final TagKey<Block> FAUCETS = create("faucets");

    private static TagKey<Block> create(String name) {
        return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Pesticides.MOD_ID, name));
    }

    private static TagKey<Block> createConventional(String name) {
        return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", name));
    }

    private ModBlockTags() {}
}
