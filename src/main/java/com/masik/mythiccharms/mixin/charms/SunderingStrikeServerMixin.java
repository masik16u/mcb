package com.masik.mythiccharms.mixin.charms;

import com.masik.mythiccharms.MythicCharms;
import com.masik.mythiccharms.accessor.SunderingStrikeAccessor;
import com.masik.mythiccharms.util.AdvancementHelper;
import com.masik.mythiccharms.util.CharmEntry;
import com.masik.mythiccharms.util.CharmInfoHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class SunderingStrikeServerMixin implements SunderingStrikeAccessor {

    @Unique
    private int consecutiveCrits = 0;

    @Override
    public int mythic_charms$getConsecutiveCrits() {
        return consecutiveCrits;
    }
    @Override
    public void mythic_charms$setConsecutiveCrits(int crits) {
        consecutiveCrits = crits;
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void sunderingStrike(CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "sundering_strike");
        if (entry == null) return;

        if (player.isOnGround()) {
            consecutiveCrits = 0;
        }

        if (consecutiveCrits >= 5) {
            AdvancementHelper.grantAdvancement(player, "mythic_charms:story/sundering_strike_jump", "impossible");
        }
    }
}
