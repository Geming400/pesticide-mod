package fr.geming400.pesticide;

import fr.geming400.pesticide.content.items.food.ModConsumeEffects;
import fr.geming400.pesticide.content.tags.ModItemTags;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.consume_effects.ConsumeEffect;

import java.util.List;

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

    private Utils() {}
}
