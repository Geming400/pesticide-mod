package fr.geming400.pesticide;

import fr.geming400.pesticide.content.ModAttachments;
import fr.geming400.pesticide.content.ModDataComponents;
import fr.geming400.pesticide.content.ModRegistries;
import fr.geming400.pesticide.content.blocks.ModBlocks;
import fr.geming400.pesticide.content.creativetab.ModCreativeTab;
import fr.geming400.pesticide.content.effects.ModEffects;
import fr.geming400.pesticide.content.items.ModItems;
import fr.geming400.pesticide.content.items.food.ModConsumeEffects;
import fr.geming400.pesticide.content.pesticides.ModPesticides;
import fr.geming400.pesticide.content.recipe.ModRecipes;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Pesticides implements ModInitializer {
	private static final ResourceKey<LootTable> ZOMBIE_LOOT_TABLE = EntityType.ZOMBIE.getDefaultLootTable().orElseThrow();

	public static final String MOD_ID = "pesticides";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModRegistries.initialize();
		ModDataComponents.initialize();
		ModAttachments.initialize();

		ModPesticides.initialize();

		ModBlocks.initialize();
		ModConsumeEffects.initialize();
		ModItems.initialize();
		ModEffects.initialize();
		ModRecipes.initialize();
		ModCreativeTab.initialize();

		LootTableEvents.MODIFY.register((loottableKey, builder, source, provider) -> {
			// Let's only modify built-in loot tables and leave data pack loot tables untouched by checking the source.
			// We also check that the loot table ID is equal to the ID we want.
			if (source.isBuiltin() && ZOMBIE_LOOT_TABLE.equals(loottableKey)) {
				LootPool.Builder lootPool = LootPool.lootPool()
						.setRolls(UniformGenerator.between(0.15f, 1f))
						.add(
								LootItem.lootTableItem(ModItems.ZOMBIE_BONE)
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0f, 2f)))
										.apply(EnchantedCountIncreaseFunction.lootingMultiplier(provider, UniformGenerator.between(0F, 1f)))
						);

				builder.pool(lootPool.build());
			}
		});

		LOGGER.info("Hello pesticides lover !!");
	}
}