package fr.geming400.pesticide.content.items;

import fr.geming400.pesticide.content.ModDataComponents;
import fr.geming400.pesticide.content.pesticides.PesticideType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class PesticideContainer extends Item {
    public PesticideContainer(Properties properties) {
        super(properties);
    }

    public PesticideType getPesticideType(ItemStack itemStack) {
        return Objects.requireNonNull(itemStack.get(ModDataComponents.PESTICIDE_TYPE));
    }
}
