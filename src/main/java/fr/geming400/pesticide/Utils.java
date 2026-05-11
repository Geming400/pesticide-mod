package fr.geming400.pesticide;

import fr.geming400.pesticide.content.items.food.ModConsumeEffects;
import fr.geming400.pesticide.content.tags.ModItemTags;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.consume_effects.ConsumeEffect;

public final class Utils {
    public static boolean isInfectable(ItemStack itemStack) {
        if (itemStack.has(DataComponents.FOOD) && itemStack.has(DataComponents.CONSUMABLE)) {
            // noinspection DataFlowIssue
            boolean hasPesticideConsumable = itemStack.get(DataComponents.CONSUMABLE).onConsumeEffects()
                    .stream()
                    .map(ConsumeEffect::getClass)
                    .toList()
                    .contains(ModConsumeEffects.PesticideConsumeEffect.class);

            return !hasPesticideConsumable && !itemStack.is(ModItemTags.CONTAINERS);
        }

        return false;
    }

    private Utils() {}
}
