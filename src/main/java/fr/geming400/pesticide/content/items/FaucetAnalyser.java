package fr.geming400.pesticide.content.items;

import fr.geming400.pesticide.content.blockentities.FaucetBlockEntity;
import fr.geming400.pesticide.content.blocks.FaucetBlock;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;

import java.text.DecimalFormat;
import java.util.Objects;

public class FaucetAnalyser extends Item {
    public FaucetAnalyser(Properties properties) {
        super(properties);
    }

    @Override
    @NonNull
    public InteractionResult useOn(@NonNull UseOnContext useOnContext) {
        if (useOnContext.getPlayer() == null)
            return InteractionResult.PASS;

        BlockEntity blockEntity = useOnContext.getLevel().getBlockEntity(useOnContext.getClickedPos());
        if (blockEntity instanceof FaucetBlockEntity faucetBlockEntity) {
            BlockState faucetBlockState = useOnContext.getLevel().getBlockState(useOnContext.getClickedPos());

            if (useOnContext.getLevel().isClientSide()) {
                if (faucetBlockEntity.isActive()) {
                    DecimalFormat df = new DecimalFormat("0");
                    useOnContext.getPlayer().displayClientMessage(
                            Component.translatable(
                                    "item.pesticides.faucet_analyser.onUse",
                                    df.format(faucetBlockEntity.getMbLeft()),
                                    Objects.requireNonNull(faucetBlockEntity.getPesticideType()).getName(),
                                    faucetBlockState.getValue(FaucetBlock.ENABLED)
                                            ? Component.translatable("item.pesticides.faucet_analyser.onUse.state.active")
                                            : Component.translatable("item.pesticides.faucet_analyser.onUse.state.notActive"),
                                    faucetBlockEntity.hasFilter()
                                            ? Component.translatable("item.pesticides.faucet_analyser.onUse.state.hasFilter")
                                            : Component.translatable("item.pesticides.faucet_analyser.onUse.state.hasNoFilter")
                            ),
                            false
                    );
                } else {
                    useOnContext.getPlayer().displayClientMessage(
                            Component.translatable("item.pesticides.faucet_analyser.onUse.empty"),
                            false
                    );
                }
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}
