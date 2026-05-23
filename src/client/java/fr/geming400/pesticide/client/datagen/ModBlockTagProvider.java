package fr.geming400.pesticide.client.datagen;

import fr.geming400.pesticide.content.blocks.ModBlocks;
import fr.geming400.pesticide.content.tags.ModBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider wrapperLookup) {
        this.valueLookupBuilder(ModBlockTags.FAUCETS)
                .add(ModBlocks.getFaucetBlocks().toArray(new Block[]{}));
    }
}
