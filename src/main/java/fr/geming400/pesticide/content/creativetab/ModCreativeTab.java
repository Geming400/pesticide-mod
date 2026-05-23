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
                List<PesticideType> sortedPesticides = ModRegistries.PESTICIDE_TYPE
                        .stream()
                        .sorted(Comparator.comparingDouble(PesticideType::growSpeedFactor))
                        .toList();

                for (PesticideType pesticideType : sortedPesticides)
                    output.accept(pesticideType.createSuspiciousWheat());

                output.accept(ModItems.ZOMBIE_BONE);
                output.accept(ModItems.WOOL_ROD);
                output.accept(ModItems.COTTON_SWAB);
                output.accept(ModItems.HOT_MILK_BUCKET);
                output.accept(ModItems.PLASTIC_SHEET);
                output.accept(ModItems.SULFUR_POWDER);
                output.accept(ModItems.TOXIC_COMPOUND);
                output.accept(ModItems.BIOMASS);
                output.accept(ModItems.BIOMASS_BAG);
                output.accept(ModItems.FILTER);

                ModItems.HAZMAT_SUIT.getAllArmorItem()
                        .forEach(output::accept);

                ModBlocks.getFaucetBlocks()
                        .forEach(output::accept);
                output.accept(ModItems.FAUCET_ANALYSER);

                output.accept(ModItems.EMPTY_CONTAINER);
                output.accept(ModItems.WATER_CONTAINER);


                for (PesticideType pesticideType : sortedPesticides)
                    output.accept(pesticideType.createContainer());
            })
            .build();

    public static void initialize() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, PESTICIDE_CREATIVE_TAB_KEY, PESTICIDE_CREATIVE_TAB);
    }

    private ModCreativeTab() {}
}
