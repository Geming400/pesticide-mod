package fr.geming400.pesticide.content.items;

import fr.geming400.pesticide.content.ModDataComponents;
import fr.geming400.pesticide.content.pesticides.PesticideType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

public class PesticideContainer extends Item {
    public PesticideContainer(Properties properties) {
        super(properties);
    }

    public PesticideType getPesticideType(ItemStack itemStack) {
        return Objects.requireNonNull(itemStack.get(ModDataComponents.PESTICIDE_TYPE));
    }

    @Override
    @NonNull
    public Component getName(@NonNull ItemStack itemStack) {
        return Component.translatable(this.descriptionId, this.getPesticideType(itemStack).getName());
    }

    public static ItemStack createItemStack(PesticideType pesticideType) {
        ItemStack itemStack = new ItemStack(ModItems.PESTICIDE_CONTAINER);
        itemStack.set(ModDataComponents.PESTICIDE_TYPE, pesticideType);

        return itemStack;
    }
}
