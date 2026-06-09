package com.masik.mythiccharms.mixin.charms;

import com.masik.mythiccharms.MythicCharms;
import com.masik.mythiccharms.component.ModDataComponentTypes;
import com.masik.mythiccharms.util.AdvancementHelper;
import com.masik.mythiccharms.util.CharmEntry;
import com.masik.mythiccharms.util.CharmInfoHelper;
import com.masik.mythiccharms.util.NbtHelper;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class FeatheredGraceServerMixin {

    @Unique
    private int ticksInAir = 0;

    @Inject(method = "playerTick", at = @At("RETURN"))
    private void featheredGrace(CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

        if (!player.isOnGround() && !player.isClimbing() && !player.isInsideWaterOrBubbleColumn() &&
                !player.isFallFlying() && player.getVelocity().y < 0) {
            ticksInAir += 1;
        }
        else {
            ticksInAir = 0;
        }

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "feathered_grace");
        if (entry == null) return;

        NbtCompound nbtCompound = NbtHelper.getNbtCompound(entry.itemStack(), ModDataComponentTypes.CHARM_ABILITIES);
        NbtCompound abilityCompound = nbtCompound.getCompound("feathered_grace");

        if (ticksInAir > abilityCompound.getInt("start_tick") &&
                ticksInAir < abilityCompound.getInt("end_tick") &&
                !player.isSneaking() && player.fallDistance > player.getSafeFallDistance()) {

            AdvancementHelper.grantAdvancement(player, "mythic_charms:story/all_charm_abilities", "feathered_grace");
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING,
                    abilityCompound.getInt("duration"), 0, false,
                    false, false));
        }
    }

}
