package fr.geming400.pesticide.client.datagen;

import fr.geming400.pesticide.Pesticides;
import fr.geming400.pesticide.content.blocks.FaucetBlock;
import fr.geming400.pesticide.content.blocks.ModBlocks;
import fr.geming400.pesticide.content.items.ModItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public final class ModModelProvider extends FabricModelProvider {
    public static final ModelTemplate FAUCET = block("faucet_block", TextureSlot.FRONT, TextureSlot.SIDE);
    public static final ModelTemplate SINGLE_FAUCET = block("single_faucet_block", TextureSlot.FRONT, TextureSlot.SIDE);

    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(@NonNull BlockModelGenerators generator) {
        registerFaucet(
                generator,
                ModBlocks.FAUCET,
                createFaucetTextureMapping(Blocks.IRON_BLOCK, Blocks.COBBLED_DEEPSLATE)
        );
    }

    @Override
    public void generateItemModels(@NonNull ItemModelGenerators itemModelGenerator) {
        itemModelGenerator.generateFlatItem(ModItems.PESTICIDE_CONTAINER, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.EMPTY_PESTICIDE_CONTAINER, ModelTemplates.FLAT_ITEM);
    }


    private static TextureMapping createFaucetTextureMapping(Block faucetMaterial, Block pipeMaterial) {
        return new TextureMapping()
                .put(TextureSlot.FRONT, ModelLocationUtils.getModelLocation(faucetMaterial))
                .put(TextureSlot.SIDE, ModelLocationUtils.getModelLocation(pipeMaterial));
    }

    private static BlockModelDefinitionGenerator createFaucetBlockStates(FaucetBlock faucetBlock, Identifier faucetBlockID, Identifier singleFaucetBlockID) {
        MultiVariant faucetBlockModel = BlockModelGenerators.plainVariant(faucetBlockID);
        MultiVariant singleFaucetBlockModel = BlockModelGenerators.plainVariant(singleFaucetBlockID);
        return MultiVariantGenerator.dispatch(faucetBlock)
                .with(PropertyDispatch.initial(FaucetBlock.FACING, FaucetBlock.SINGLE)
                        .select(Direction.NORTH, false, faucetBlockModel.with(BlockModelGenerators.UV_LOCK))
                        .select(Direction.EAST, false, faucetBlockModel.with(BlockModelGenerators.UV_LOCK).with(BlockModelGenerators.Y_ROT_90))
                        .select(Direction.SOUTH, false, faucetBlockModel.with(BlockModelGenerators.UV_LOCK).with(BlockModelGenerators.Y_ROT_180))
                        .select(Direction.WEST, false, faucetBlockModel.with(BlockModelGenerators.UV_LOCK).with(BlockModelGenerators.Y_ROT_270))
                        .select(Direction.NORTH, true, singleFaucetBlockModel.with(BlockModelGenerators.UV_LOCK))
                        .select(Direction.EAST, true, singleFaucetBlockModel.with(BlockModelGenerators.UV_LOCK).with(BlockModelGenerators.Y_ROT_90))
                        .select(Direction.SOUTH, true, singleFaucetBlockModel.with(BlockModelGenerators.UV_LOCK).with(BlockModelGenerators.Y_ROT_180))
                        .select(Direction.WEST, true, singleFaucetBlockModel.with(BlockModelGenerators.UV_LOCK).with(BlockModelGenerators.Y_ROT_270))
                );
    }

    private static void registerFaucet(BlockModelGenerators generator, FaucetBlock faucetBlock, TextureMapping textures) {
        Identifier faucetModel = FAUCET.create(faucetBlock, textures, generator.modelOutput);
        Identifier singleFaucetModel = SINGLE_FAUCET.createWithSuffix(faucetBlock, "_single", textures, generator.modelOutput);

        generator.blockStateOutput.accept(createFaucetBlockStates(faucetBlock, faucetModel, singleFaucetModel));
        generator.registerSimpleItemModel(faucetBlock, faucetModel);
    }

    private static ModelTemplate block(String parent, TextureSlot... requiredTextureKeys) {
        return new ModelTemplate(Optional.of(Identifier.fromNamespaceAndPath(Pesticides.MOD_ID, "block/" + parent)), Optional.empty(), requiredTextureKeys);
    }

    // Helper method for creating Models with variants
    private static ModelTemplate block(String parent, String variant, TextureSlot... requiredTextureKeys) {
        return new ModelTemplate(Optional.of(Identifier.fromNamespaceAndPath(Pesticides.MOD_ID, "block/" + parent)), Optional.of(variant), requiredTextureKeys);
    }

    @Override
    @NonNull
    public String getName() {
        return "ModModelProvider";
    }
}
