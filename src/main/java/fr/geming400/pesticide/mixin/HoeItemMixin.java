package fr.geming400.pesticide.mixin;

import fr.geming400.pesticide.content.effects.ModEffects;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HoeItem.class)
abstract class HoeItemMixin {
    @Inject(at = @At(value = "INVOKE", target = "Ljava/util/function/Predicate;test(Ljava/lang/Object;)Z"), method = "useOn", cancellable = true)
    private void useOn(UseOnContext ctx, CallbackInfoReturnable<InteractionResult> cir) {
        Player player = ctx.getPlayer();
        if (player != null && player.hasEffect(ModEffects.BAD_FARMER)) {
            RandomSource random = player.getRandom();
            if (random.nextInt(0, 10) != 0) {
                ctx.getLevel().playSound(player, ctx.getClickedPos(), SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                ctx.getItemInHand().hurtAndBreak(random.nextInt(1, 3), player, ctx.getHand().asEquipmentSlot());

                cir.setReturnValue(InteractionResult.FAIL);
            }
        }
    }
}
