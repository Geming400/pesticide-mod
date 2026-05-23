package fr.geming400.pesticide.content.blocks;

import fr.geming400.pesticide.Pesticides;
import fr.geming400.pesticide.content.blockentities.ModBlockEntities;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.*;
import java.util.function.Function;

public final class ModBlocks {
    public static final FaucetBlock COPPER_FAUCET = register(
            "copper_faucet",
            properties -> new FaucetBlock(properties, 0.45),
            FaucetBlock.getProperties(SoundType.COPPER),
            true
    );

    public static final FaucetBlock IRON_FAUCET = register(
            "iron_faucet",
            properties -> new FaucetBlock(properties, 0.3),
            FaucetBlock.getProperties(SoundType.IRON),
            true
    );

    public static final FaucetBlock DIAMOND_FAUCET = register(
            "diamond_faucet",
            properties -> new FaucetBlock(properties, 0.05),
            FaucetBlock.getProperties(SoundType.IRON),
            true
    );

    public static final FaucetBlock NETHERITE_FAUCET = register(
            "netherite_faucet",
            properties -> new FaucetBlock(properties, 0),
            FaucetBlock.getProperties(SoundType.NETHERITE_BLOCK),
            new Item.Properties()
                    .rarity(Rarity.EPIC)
    );

    public static final InfestedFarmBlock INFESTED_FARMLAND = register(
            "infested_farmland",
            InfestedFarmBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.FARMLAND)
                    .sound(SoundType.SUSPICIOUS_GRAVEL)
                    .strength(0.5f),
            true
    );

    public static List<FaucetBlock> getFaucetBlocks() {
        return BuiltInRegistries.BLOCK
                .stream()
                .filter(block -> block instanceof FaucetBlock)
                .map(block -> (FaucetBlock) block)
                .sorted(Comparator.comparingDouble(FaucetBlock::getInfectionChance).reversed())
                .toList();
    }

    public static void initialize() {
        ModBlockEntities.initialize();
    }

    private static <T extends Block> T register(String name, Function<BlockBehaviour.Properties, T> blockFactory, BlockBehaviour.Properties settings, boolean shouldRegisterItem) {
        return register(name, blockFactory, settings, new Item.Properties(), shouldRegisterItem);
    }

    @SuppressWarnings("SameParameterValue")
    private static <T extends Block> T register(String name, Function<BlockBehaviour.Properties, T> blockFactory, BlockBehaviour.Properties blockSettings, Item.Properties itemSettings) {
        return register(name, blockFactory, blockSettings, itemSettings, true);
    }

    private static <T extends Block> T register(String name, Function<BlockBehaviour.Properties, T> blockFactory, BlockBehaviour.Properties settings, Item.Properties itemSettings, boolean shouldRegisterItem) {
        // Create a registry key for the block
        ResourceKey<Block> blockKey = keyOfBlock(name);
        // Create the block instance
        T block = blockFactory.apply(settings.setId(blockKey));

        // Sometimes, you may not want to register an item for the block.
        // Eg: if it's a technical block like `minecraft:moving_piston` or `minecraft:end_gateway`
        if (shouldRegisterItem) {
            // Items need to be registered with a different type of registry key, but the ID
            // can be the same.
            ResourceKey<Item> itemKey = keyOfItem(name);

            BlockItem blockItem = new BlockItem(block, itemSettings.setId(itemKey).useBlockDescriptionPrefix());
            Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
        }

        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }

    private static ResourceKey<Block> keyOfBlock(String name) {
        return ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Pesticides.MOD_ID, name));
    }

    private static ResourceKey<Item> keyOfItem(String name) {
        return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Pesticides.MOD_ID, name));
    }

    private ModBlocks() {}
}
