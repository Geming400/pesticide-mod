package fr.geming400.pesticide.content.creativetab;

import fr.geming400.pesticide.Pesticides;
import fr.geming400.pesticide.content.ModRegistries;
import fr.geming400.pesticide.content.blocks.ModBlocks;
import fr.geming400.pesticide.content.items.ModItems;
import fr.geming400.pesticide.content.pesticides.PesticideType;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.Comparator;
import java.util.List;

public final class ModCreativeTab {
    private static final ResourceKey<CreativeModeTab> PESTICIDE_CREATIVE_TAB_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(), Identifier.fromNamespaceAndPath(Pesticides.MOD_ID, "creative_tab")
    );

    public static final CreativeModeTab PESTICIDE_CREATIVE_TAB = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModItems.PESTICIDE_CONTAINER))
            .title(Component.translatable("itemGroup." + Pesticides.MOD_ID))
            .displayItems((params, output) -> {
                output.accept(ModItems.ZOMBIE_BONE);

                output.accept(ModBlocks.FAUCET);
                output.accept(ModItems.FAUCET_ANALYSER);

                output.accept(ModItems.EMPTY_CONTAINER);
                output.accept(ModItems.WATER_CONTAINER);

                List<PesticideType> sortedPesticides = ModRegistries.PESTICIDE_TYPE
                        .stream()
                        .sorted(Comparator.comparingDouble(PesticideType::growSpeedFactor))
                        .toList();
                for (PesticideType pesticideType : sortedPesticides)
                    output.accept(pesticideType.createContainer());
            })
            .build();

    public static void initialize() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, PESTICIDE_CREATIVE_TAB_KEY, PESTICIDE_CREATIVE_TAB);
    }

    private ModCreativeTab() {}
}
