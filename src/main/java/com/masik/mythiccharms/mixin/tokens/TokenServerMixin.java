package com.masik.mythiccharms.mixin.tokens;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.masik.mythiccharms.util.CharmEntry;
import com.masik.mythiccharms.util.CharmInfoHelper;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class TokenServerMixin {

    @ModifyExpressionValue(method = "trySleep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isDay()Z"))
    private boolean daylightDreams(boolean original) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "daylight_dreams");
        if (entry == null) return original;

        return false;
    }

    @Inject(method = "playerTick", at = @At("RETURN"))
    private void innerLight(CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "inner_light");
        if (entry == null) return;

        player.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING,
                4, 0, false,
                false, false));
    }

}
