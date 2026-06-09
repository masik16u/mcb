package com.masik.mythiccharms.mixin.charms;

import com.masik.mythiccharms.MythicCharms;
import com.masik.mythiccharms.accessor.SunderingStrikeAccessor;
import com.masik.mythiccharms.component.ModDataComponentTypes;
import com.masik.mythiccharms.util.AdvancementHelper;
import com.masik.mythiccharms.util.CharmEntry;
import com.masik.mythiccharms.util.CharmInfoHelper;
import com.masik.mythiccharms.util.NbtHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class SunderingStrikeLivingMixin {

    @Inject(method = "damage", at = @At("RETURN"))
    private void sunderingStrike(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        Entity entity = source.getAttacker();

        if (!(entity instanceof PlayerEntity player)) return;
        if (player.getWorld().isClient) return;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "sundering_strike");
        if (entry == null) return;

        NbtCompound nbtCompound = NbtHelper.getNbtCompound(entry.itemStack(), ModDataComponentTypes.CHARM_ABILITIES);
        NbtCompound abilityCompound = nbtCompound.getCompound("sundering_strike");

        boolean isAttackCrit = player.fallDistance > 0.0F && !player.isOnGround() &&
                !player.isClimbing() && !player.isTouchingWater() &&
                !player.hasStatusEffect(StatusEffects.BLINDNESS) && !player.hasVehicle() &&
                !player.isSprinting();

        if (isAttackCrit || !abilityCompound.getBoolean("require_crit")) {
            SunderingStrikeAccessor accessor = (SunderingStrikeAccessor) player;
            accessor.mythic_charms$setConsecutiveCrits(accessor.mythic_charms$getConsecutiveCrits() + 1);

            Vec3d knockbackVector = player.getRotationVector()
                    .negate().normalize().multiply(abilityCompound.getFloat("knockback_multiplier"));

            NbtCompound comboCompound = CharmInfoHelper.getCombinationWithAbility(player, entry, "mountain_stance");
            if (comboCompound != null) {
                player.setVelocity(knockbackVector.negate());
                AdvancementHelper.grantAdvancement(player, "mythic_charms:story/all_charm_combos", "ss_ms");
            }
            else {
                player.setVelocity(knockbackVector);
            }
            player.velocityModified = true;

            AdvancementHelper.grantAdvancement(player, "mythic_charms:story/all_charm_abilities", "sundering_strike");
        }
    }

}
