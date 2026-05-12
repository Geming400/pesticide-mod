package fr.geming400.pesticide;

import fr.geming400.pesticide.content.items.ModItems;
import fr.geming400.pesticide.content.items.food.ModConsumeEffects;
import fr.geming400.pesticide.content.tags.ModItemTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.consume_effects.ConsumeEffect;

import java.util.List;
import java.util.Objects;

public final class Utils {
    public static boolean isInfectable(ItemStack itemStack) {
        if (itemStack.has(DataComponents.FOOD) && itemStack.has(DataComponents.CONSUMABLE)) {
            // noinspection DataFlowIssue
            List<ConsumeEffect> consumeEffects = itemStack.get(DataComponents.CONSUMABLE).onConsumeEffects();

            boolean hasPesticideConsumable = consumeEffects
                    .stream()
                    .map(ConsumeEffect::getClass)
                    .toList()
                    .contains(ModConsumeEffects.PesticideConsumeEffect.class);

            boolean hasPesticideContainerConsumable = consumeEffects
                    .stream()
                    .map(ConsumeEffect::getClass)
                    .toList()
                    .contains(ModConsumeEffects.PesticideContainerConsumeEffect.class);

            return !hasPesticideConsumable && !hasPesticideContainerConsumable && !itemStack.is(ModItemTags.CONTAINERS);
        }

        return false;
    }

    public static ItemStack addIsInfectedLore(ItemStack itemStack) {
        ItemLore itemLore = itemStack.has(DataComponents.LORE)
                ? Objects.requireNonNull(itemStack.get(DataComponents.LORE))
                : new ItemLore(List.of());
        Component infectedTooltip = Component.translatable(ModItems.PESTICIDE_CONTAINER.getDescriptionId() + ".infectedTooltip")
                .withStyle(ChatFormatting.DARK_GREEN);

        itemStack.set(DataComponents.LORE, itemLore.withLineAdded(infectedTooltip));
        return itemStack;
    }

    private Utils() {}
}
