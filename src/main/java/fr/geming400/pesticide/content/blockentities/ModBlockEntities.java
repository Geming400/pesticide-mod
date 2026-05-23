package fr.geming400.pesticide.content.blockentities;

import fr.geming400.pesticide.Pesticides;
import fr.geming400.pesticide.content.blocks.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class ModBlockEntities {
    public static final BlockEntityType<FaucetBlockEntity> FAUCET_BLOCK_ENTITY = register(
            "faucet", FaucetBlockEntity::new,
            ModBlocks.COPPER_FAUCET,
            ModBlocks.IRON_FAUCET,
            ModBlocks.DIAMOND_FAUCET,
            ModBlocks.NETHERITE_FAUCET
    );
    public static final BlockEntityType<InfestedFarmlandBlockEntity> INFESTED_FARMLAND_BLOCK_ENTITY = register(
            "infested_farmland_entity", InfestedFarmlandBlockEntity::new,
            ModBlocks.INFESTED_FARMLAND
    );

    public static void initialize() {}

    private static <T extends BlockEntity> BlockEntityType<T> register(
            String name,
            FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
            Block... blocks
    ) {
        Identifier id = Identifier.fromNamespaceAndPath(Pesticides.MOD_ID, name);
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
    }

    private ModBlockEntities() {}
}
