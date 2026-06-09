package com.masik.mythiccharms.mixin.charms;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.masik.mythiccharms.component.ModDataComponentTypes;
import com.masik.mythiccharms.util.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
public class EarthOrderPlayerMixin {

    @ModifyReturnValue(method = "canHarvest", at = @At("RETURN"))
    private boolean earthOrder(boolean original) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "earth_order");
        if (entry == null) return original;

        NbtCompound nbtCompound = NbtHelper.getNbtCompound(entry.itemStack(), ModDataComponentTypes.CHARM_ABILITIES);
        NbtCompound abilityCompound = nbtCompound.getCompound("earth_order");

        if (abilityCompound.getBoolean("require_barehanded") &&
                player.getMainHandStack().isDamageable()) {
            return original;
        }

        AdvancementHelper.grantAdvancement(player, "mythic_charms:story/all_charm_abilities", "earth_order");
        return true;
    }

    @ModifyReturnValue(method = "getBlockBreakingSpeed", at = @At("RETURN"))
    private float earthOrder(float original) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "earth_order");
        if (entry == null) return original;

        NbtCompound nbtCompound = NbtHelper.getNbtCompound(entry.itemStack(), ModDataComponentTypes.CHARM_ABILITIES);
        NbtCompound abilityCompound = nbtCompound.getCompound("earth_order");

        if (abilityCompound.getBoolean("require_barehanded") &&
                player.getMainHandStack().isDamageable()) {
            return original;
        }

        float speedMultiplier = abilityCompound.getFloat("mining_speed_multiplier");

        NbtCompound comboCompound = CharmInfoHelper.getCombinationWithAbility(player, entry, "battle_fury");
        if (comboCompound != null) {
            CharmEntry entryBF = CharmInfoHelper.getCharmWithAbility(player, "battle_fury");
            if (entryBF != null) {
                speedMultiplier *= CharmMathHelper.getBattleFuryMultiplier(player, entryBF);
            }
        }

        return original * speedMultiplier * (!player.isOnGround() ? 5 : 1);
    }

}
