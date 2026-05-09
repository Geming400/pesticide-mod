package fr.geming400.pesticide.content.items;

import fr.geming400.pesticide.content.ModDataComponents;
import fr.geming400.pesticide.content.pesticides.ModPesticides;
import fr.geming400.pesticide.content.pesticides.PesticideType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class PesticideContainer extends Item {
    public PesticideContainer(Properties properties) {
        super(properties);
    }

    @NotNull
    public PesticideType getPesticideType(ItemStack itemStack) {
        return itemStack.getOrDefault(ModDataComponents.PESTICIDE_TYPE, ModPesticides.ATRAZINE);
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
