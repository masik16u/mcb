package com.masik.mythiccharms.mixin.charms;

import com.llamalad7.mixinextras.sugar.Local;
import com.masik.mythiccharms.component.ModDataComponentTypes;
import com.masik.mythiccharms.util.AdvancementHelper;
import com.masik.mythiccharms.util.CharmEntry;
import com.masik.mythiccharms.util.CharmInfoHelper;
import com.masik.mythiccharms.util.NbtHelper;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.spawner.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PhantomSpawner.class)
public class NightGuardianSpawnerMixin {

    @ModifyArg(method = "spawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;clamp(III)I"), index = 0)
    private int nightGuardian(int value, @Local ServerPlayerEntity player) {
        if (player == null) return value;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "night_guardian");
        if (entry == null) return value;

        NbtCompound nbtCompound = NbtHelper.getNbtCompound(entry.itemStack(), ModDataComponentTypes.CHARM_ABILITIES);
        NbtCompound abilityCompound = nbtCompound.getCompound("night_guardian");

        if (abilityCompound.getBoolean("disable_spawn")) {
            AdvancementHelper.grantAdvancement(player, "mythic_charms:story/all_charm_abilities", "night_guardian");
            return 0;
        }

        return value;
    }

}
