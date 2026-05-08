package fr.geming400.pesticide.client.datagen;

import fr.geming400.pesticide.content.blocks.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class ModBlockLoottableProvider extends FabricBlockLootTableProvider {
    public ModBlockLoottableProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        this.dropSelf(ModBlocks.FAUCET);
        this.dropOther(ModBlocks.INFESTED_FARM_BLOCK, Blocks.DIRT);
    }
}
