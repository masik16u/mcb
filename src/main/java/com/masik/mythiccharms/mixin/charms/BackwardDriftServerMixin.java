package com.masik.mythiccharms.mixin.charms;

import com.masik.mythiccharms.component.ModDataComponentTypes;
import com.masik.mythiccharms.util.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayDeque;

@Mixin(ServerPlayerEntity.class)
public class BackwardDriftServerMixin {

    @Unique
    private final ArrayDeque<Vec3d> history = new ArrayDeque<>();

    @Inject(method = "playerTick", at = @At("RETURN"))
    private void backwardDrift(CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "backward_drift");
        if (entry == null) {
            history.clear();
            return;
        }

        NbtCompound nbtCompound = NbtHelper.getNbtCompound(entry.itemStack(), ModDataComponentTypes.CHARM_ABILITIES);
        NbtCompound abilityCompound = nbtCompound.getCompound("backward_drift");

        history.addLast(player.getPos());

        if (history.size() > abilityCompound.getInt("time_rewind")) {
            history.removeFirst();
        }
    }

    @Inject(method = "damage", at = @At("RETURN"))
    private void backwardDrift(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return;

        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "backward_drift");
        if (entry == null) return;

        NbtCompound nbtCompound = NbtHelper.getNbtCompound(entry.itemStack(), ModDataComponentTypes.CHARM_ABILITIES);
        NbtCompound abilityCompound = nbtCompound.getCompound("backward_drift");

        NbtCompound comboCompound = CharmInfoHelper.getCombinationWithAbility(player, entry, "twisted_lifeblood");
        if (comboCompound != null) {
            if (CharmMathHelper.getHealthPercentage(player) >= comboCompound.getFloat("activate_below_hp") ||
                    source.getAttacker() == null || !(source.getAttacker() instanceof LivingEntity livingEntity)) {
                return;
            }
            Vec3d playerPos = player.getPos();
            Vec3d sourcePos = livingEntity.getPos();

            if (comboCompound.getBoolean("safe_teleport")) {
                player.teleport(sourcePos.x, sourcePos.y, sourcePos.z, true);
                player.onLanding();

                livingEntity.teleport(playerPos.x, playerPos.y, playerPos.z, true);
                livingEntity.onLanding();
            }
            else {
                player.requestTeleport(sourcePos.x, sourcePos.y, sourcePos.z);
                livingEntity.requestTeleport(playerPos.x, playerPos.y, playerPos.z);
            }

            AdvancementHelper.grantAdvancement(player, "mythic_charms:story/all_charm_combos", "bd_tl");
            if (playerPos.distanceTo(sourcePos) > 50) {
                AdvancementHelper.grantAdvancement(player, "mythic_charms:story/backward_drift_twisted", "impossible");
            }
            return;
        }

        if (CharmMathHelper.getHealthPercentage(player) >= abilityCompound.getFloat("activate_below_hp")) {
            return;
        }

        Vec3d pos = history.peekFirst();
        if (pos != null) {
            if (abilityCompound.getBoolean("safe_teleport")) {
                player.teleport(pos.x, pos.y, pos.z, true);
                player.onLanding();
            }
            else {
                player.requestTeleport(pos.x, pos.y, pos.z);
            }

            AdvancementHelper.grantAdvancement(player, "mythic_charms:story/all_charm_abilities", "backward_drift");
            if (source.isOf(DamageTypes.OUT_OF_WORLD)) {
                AdvancementHelper.grantAdvancement(player, "mythic_charms:story/backward_drift_void", "impossible");
            }
        }
    }

}
