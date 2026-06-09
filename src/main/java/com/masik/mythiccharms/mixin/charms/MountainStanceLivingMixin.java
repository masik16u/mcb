package com.masik.mythiccharms.mixin.charms;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.masik.mythiccharms.component.ModDataComponentTypes;
import com.masik.mythiccharms.util.AdvancementHelper;
import com.masik.mythiccharms.util.CharmEntry;
import com.masik.mythiccharms.util.CharmInfoHelper;
import com.masik.mythiccharms.util.NbtHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class MountainStanceLivingMixin {

    @WrapMethod(method = "takeKnockback")
    private void mountainStance(double strength, double x, double z, Operation<Void> original) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (!(entity instanceof PlayerEntity)) return;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(entity, "mountain_stance");
        if (entry == null) {
            original.call(strength, x, z);
            return;
        }

        NbtCompound nbtCompound = NbtHelper.getNbtCompound(entry.itemStack(), ModDataComponentTypes.CHARM_ABILITIES);
        NbtCompound abilityCompound = nbtCompound.getCompound("mountain_stance");

        if (abilityCompound.getBoolean("require_stationary") &&
                entity.getMovement().lengthSquared() > 1.0E-4) {
            original.call(strength, x, z);
            return;
        }

        if (abilityCompound.getBoolean("ignore_knockback")) {
            AdvancementHelper.grantAdvancement(entity, "mythic_charms:story/all_charm_abilities", "mountain_stance");
            return;
        }

        original.call(strength, x, z);
    }

    @ModifyReturnValue(method = "isPushable", at = @At("RETURN"))
    private boolean mountainStance(boolean original) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (!(entity instanceof PlayerEntity)) return original;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(entity, "mountain_stance");
        if (entry == null) return original;

        NbtCompound nbtCompound = NbtHelper.getNbtCompound(entry.itemStack(), ModDataComponentTypes.CHARM_ABILITIES);
        NbtCompound abilityCompound = nbtCompound.getCompound("mountain_stance");

        if (abilityCompound.getBoolean("require_stationary") &&
                entity.getMovement().lengthSquared() > 1.0E-4) {
            return original;
        }

        if (abilityCompound.getBoolean("ignore_pushing")) {
            AdvancementHelper.grantAdvancement(entity, "mythic_charms:story/all_charm_abilities", "mountain_stance");
            return false;
        }

        return original;
    }

}
