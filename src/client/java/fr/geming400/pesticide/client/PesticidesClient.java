package fr.geming400.pesticide.client;

import fr.geming400.pesticide.content.ModDataComponents;
import fr.geming400.pesticide.content.items.ModItems;
import fr.geming400.pesticide.content.pesticides.PesticideType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class PesticidesClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ItemTooltipCallback.EVENT.register((stack, context, type, tooltip) -> {
			PesticideType pesticideType = stack.get(ModDataComponents.PESTICIDE_TYPE);

			if (pesticideType != null) {
				tooltip.add(Component.translatable(ModItems.PESTICIDE_CONTAINER.getDescriptionId() + ".tooltip", pesticideType.getName())
						.withStyle(ChatFormatting.DARK_GREEN));
			}
		});
	}
}