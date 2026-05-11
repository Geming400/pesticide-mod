package fr.geming400.pesticide.client.datagen;

import com.google.common.base.MoreObjects;
import fr.geming400.pesticide.Pesticides;
import fr.geming400.pesticide.content.blocks.ModBlocks;
import fr.geming400.pesticide.content.effects.ModEffects;
import fr.geming400.pesticide.content.items.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.criterion.EffectsChangedTrigger;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MobEffectsPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public class ModAdvancementProvider extends FabricAdvancementProvider {
    public ModAdvancementProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    @SuppressWarnings("ExtractMethodRecommender")
    public void generateAdvancement(HolderLookup.Provider registryLookup, @NonNull Consumer<AdvancementHolder> consumer) {
        HolderLookup.RegistryLookup<Item> itemLookup = registryLookup.lookupOrThrow(Registries.ITEM);

        AdvancementBuilder advBuilder = new AdvancementBuilder(consumer, "core");
        advBuilder.root(builder -> builder.display(
                ModItems.EMPTY_CONTAINER,
                Identifier.withDefaultNamespace("gui/advancements/backgrounds/stone"),
                AdvancementType.TASK,
                true,
                true,
                false
        ).addCriterion("got_zombie_bone", InventoryChangeTrigger.TriggerInstance.hasItems(
                ItemPredicate.Builder.item().of(itemLookup, ModItems.ZOMBIE_BONE)))
        );

        advBuilder.add(
                "a_new_farmer_era",
                builder -> builder.display(
                    ModBlocks.FAUCET,
                    Identifier.withDefaultNamespace("gui/advancements/backgrounds/stone"),
                    AdvancementType.TASK,
                    true,
                    true,
                    false
                        )
                        .parentAsRoot()
                        .addCriterion("got_faucet", InventoryChangeTrigger.TriggerInstance.hasItems(
                                ItemPredicate.Builder.item().of(itemLookup, ModBlocks.FAUCET)))
        );

        advBuilder.add(
                "first_pesticides",
                builder -> builder.display(
                        ModItems.PESTICIDE_CONTAINER,
                        Identifier.withDefaultNamespace("gui/advancements/backgrounds/stone"),
                        AdvancementType.GOAL,
                        true,
                        true,
                        false
                        )
                        .parentAsLast()
                        .addCriterion("got_pesticides", InventoryChangeTrigger.TriggerInstance.hasItems(
                            ItemPredicate.Builder.item().of(itemLookup, ModItems.PESTICIDE_CONTAINER))
                    )
        );

        advBuilder.add(
                "a_true_farmer",
                builder -> builder.display(
                                Items.DIAMOND_HOE,
                                Identifier.withDefaultNamespace("gui/advancements/backgrounds/stone"),
                                AdvancementType.GOAL,
                                true,
                                true,
                                true
                        )
                        .parentAsLast()
                        .addCriterion("got_bad_farmer_effect", EffectsChangedTrigger.TriggerInstance.hasEffects(
                                MobEffectsPredicate.Builder.effects().and(ModEffects.BAD_FARMER))
                        )
        );
    }


    public static class AdvancementBuilder {
        public static final String ROOT = "root";

        private final Consumer<AdvancementHolder> consumer;
        private final String name;
        private final List<AdvancementHolder> advancementEntries;
        private AdvancementHolder root = null;
        @Nullable
        private AdvancementHolder previousEntry = null;

        public AdvancementBuilder(Consumer<AdvancementHolder> AdvancementHolderConsumer, String name) {
            this.consumer = AdvancementHolderConsumer;
            this.name = name;
            this.advancementEntries = new ArrayList<>();
        }

        private String formatName(String subAdvancement) {
            return "%s/%s".formatted(this.name, subAdvancement);
        }

        public AdvancementHolder add(Identifier id, Function<Builder, Advancement.Builder> advancementBuilderFactory) {
            if (this.root == null && !id.getPath().equals(this.formatName(ROOT)))
                throw new RuntimeException("Cannot add an advancement until a root advancement was added");

            if (!id.getPath().split("/", 2)[0].equals(this.name))
                throw new RuntimeException("Given advancement id %s isn't in the category %s".formatted(id, this.name));
            if (
                    this.advancementEntries
                            .stream()
                            .anyMatch((entry -> entry.id().equals(id)))
            )
                throw new RuntimeException("Given advancement id %s was already registered !".formatted(id));

            AdvancementHolder AdvancementHolder = advancementBuilderFactory.apply(new Builder(this, id)).build(id);
            this.consumer.accept(AdvancementHolder);
            this.previousEntry = AdvancementHolder;
            this.advancementEntries.add(AdvancementHolder);

            return AdvancementHolder;
        }
        public AdvancementHolder add(String id, Function<Builder, Advancement.Builder> advancementBuilderFactory) {
            return this.add(Identifier.fromNamespaceAndPath(Pesticides.MOD_ID, this.formatName(id)), advancementBuilderFactory);
        }

        /// Adds the root advancement under the <code>{@value #ROOT}</code> id
        @SuppressWarnings("UnusedReturnValue")
        public AdvancementHolder root(Function<Builder, Advancement.Builder> advancementBuilderFactory) {
            this.root = this.add("root", advancementBuilderFactory);
            return this.root;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("name", name)
                    .toString();
        }

        public static class Builder extends Advancement.Builder {
            private final AdvancementBuilder advancementBuilder;
            private final Identifier id;

            private Builder(AdvancementBuilder advancementBuilder, Identifier id) {
                this.advancementBuilder = advancementBuilder;
                this.id = id;
            }

            /**
             * Sets this {@link AdvancementBuilder}'s parent as the last registered {@link AdvancementHolder}
             * @return {@code this} for chaining
             */
            public Builder parentAsLast() {
                Objects.requireNonNull(this.advancementBuilder.previousEntry, "The previous entry is null. The root entry cannot have a parent !");
                this.parent(this.advancementBuilder.previousEntry);

                return this;
            }
            /**
             * Sets this {@link AdvancementBuilder}'s parent as the {@linkplain Identifier ID} of another {@link AdvancementHolder} (<b>of the same advancement category</b>)
             * @return {@code this} for chaining
             */
            public Builder parentID(String id) {
                Optional<AdvancementHolder> entry = this.advancementBuilder.advancementEntries
                        .stream()
                        .filter(advEntry -> advEntry.id().equals(
                                Identifier.fromNamespaceAndPath(this.id.getNamespace(), this.advancementBuilder.formatName(id))
                        ))
                        .findFirst();
                if (entry.isEmpty())
                    throw new RuntimeException("Tried finding entry %s in %s but failed because it wasn't registered".formatted(id, this.advancementBuilder));

                this.parent(entry.get());

                return this;
            }

            /**
             * Sets this {@link AdvancementBuilder}'s parent as the {@linkplain AdvancementBuilder#ROOT root} of advancement
             * @return {@code this} for chaining
             */
            public Builder parentAsRoot() {
                return this.parentID(AdvancementBuilder.ROOT);
            }

            /**
             * Sets this {@link AdvancementBuilder} display
             * @param icon the icon of the advancement
             * @param background the background of the advancement
             * @param frame the frame of advancement
             * @param showToast if an "Advancement Unlocked" toast should get shown upon completion
             * @param announceToChat if when completed, a message should get broadcasted on the chat to inform about the player's completion
             * @param hidden if the advancement should be hidden in the advancement tab
             * @return {@code this} for chaining
             * @implNote this automatically infers the {@linkplain Component#translatable(String) translation keys} of the description and title of this advancement
             * @see #display(ItemStack, Component, Component, Identifier, AdvancementType, boolean, boolean, boolean)
             */
            public Builder display(
                    ItemLike icon,
                    @Nullable Identifier background,
                    AdvancementType frame,
                    boolean showToast,
                    boolean announceToChat,
                    boolean hidden
            ) {
                String rootID = "advancements.%s.%s.".formatted(this.id.getNamespace(), this.id.getPath().replace("/", "."));
                super.display(
                        icon,
                        Component.translatable(rootID + "title"),
                        Component.translatable(rootID + "description"),
                        background,
                        frame,
                        showToast,
                        announceToChat,
                        hidden
                );

                return this;
            }
        }
    }
}
