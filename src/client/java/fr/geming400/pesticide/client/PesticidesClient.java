package fr.geming400.pesticide.client;

import fr.geming400.pesticide.content.ModDataComponents;
import fr.geming400.pesticide.content.blockentities.InfestedFarmlandBlockEntity;
import fr.geming400.pesticide.content.blocks.ModBlocks;
import fr.geming400.pesticide.content.items.ModItems;
import fr.geming400.pesticide.content.pesticides.PesticideType;
import fr.geming400.pesticide.content.tags.ModItemTags;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Range;

import java.awt.*;
import java.text.DecimalFormat;

public class PesticidesClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ItemTooltipCallback.EVENT.register((stack, context, type, tooltip) -> {
			PesticideType pesticideType = stack.get(ModDataComponents.PESTICIDE_TYPE);

			if (pesticideType != null) {
				if (stack.is(ModItemTags.CONTAINERS)) {
					ChatFormatting tooltipColor = pesticideType.growSpeedFactor() > 1
							? ChatFormatting.DARK_GREEN
							: ChatFormatting.DARK_RED;

					tooltip.add(Component.translatable(ModItems.PESTICIDE_CONTAINER.getDescriptionId() + ".tooltip.volume", pesticideType.getName())
							.withStyle(tooltipColor));

					DecimalFormat df = new DecimalFormat("#.00");
					tooltip.add(Component.translatableEscape(ModItems.PESTICIDE_CONTAINER.getDescriptionId() + ".tooltip.growthFactor", df.format(pesticideType.growSpeedFactor()))
							.withStyle(ChatFormatting.DARK_GRAY));
				} else {
					tooltip.add(Component.translatable(ModItems.PESTICIDE_CONTAINER.getDescriptionId() + ".tooltip.general", pesticideType.getName())
							.withStyle(ChatFormatting.DARK_GREEN));
				}
			}
		});

		ColorProviderRegistry.BLOCK.register(
				(blockState, level, pos, i) -> {
					if (pos != null && level != null) {
						BlockEntity blockEntity = level.getBlockEntity(pos);
						if (blockEntity instanceof InfestedFarmlandBlockEntity infestedFarmland) {
							// If we apply a white color, then multiplying the block's color
							// by this will change nothing
							// aka we keep the original block color
							int startColor = 0xFFFFFF;
							int endColor = 0x75C5FF;
							return lerpColor(startColor, endColor, infestedFarmland.getInfectionProgress());
						}
					}

					// Should never happen I think
					return 0xFFFFFF;
				},
				ModBlocks.INFESTED_FARMLAND
		);
	}

	private static int lerpColor(int start, int end, @Range(from = 0, to = 1) double progress) {
		Color startColor = new Color(start);
		Color endColor = new Color(end);

		int r = (int) (startColor.getRed() * (1 - progress) + endColor.getRed() * progress);
		int g = (int) (startColor.getGreen() * (1 - progress) + endColor.getGreen() * progress);
		int b = (int) (startColor.getBlue() * (1 - progress) + endColor.getBlue() * progress);

		return new Color(r, g, b).getRGB();
	}
}