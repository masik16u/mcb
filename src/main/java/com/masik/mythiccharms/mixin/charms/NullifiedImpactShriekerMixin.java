package com.masik.mythiccharms.mixin.charms;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.masik.mythiccharms.util.AdvancementHelper;
import com.masik.mythiccharms.util.CharmEntry;
import com.masik.mythiccharms.util.CharmInfoHelper;
import net.minecraft.block.entity.SculkShriekerWarningManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SculkShriekerWarningManager.class)
public class NullifiedImpactShriekerMixin {

    @WrapWithCondition(method = "warnNearbyPlayers", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/SculkShriekerWarningManager;increaseWarningLevel()V"))
    private static boolean nullifiedImpact(SculkShriekerWarningManager instance, @Local(argsOnly = true) ServerPlayerEntity player, @Local(argsOnly = true) BlockPos pos) {
        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "nullified_impact");
        if (entry == null) return true;

        NbtCompound comboCompound = CharmInfoHelper.getCombinationWithAbility(player, entry, "shrouded_existence");
        if (comboCompound == null) return true;

        float radius = comboCompound.getFloat("radius");

        if (comboCompound.getBoolean("require_no_sprint") && player.isSprinting() ||
                player.squaredDistanceTo(pos.toCenterPos()) > radius * radius) {
            return true;
        }

        AdvancementHelper.grantAdvancement(player, "mythic_charms:story/all_charm_combos", "ni_se");
        return false;
    }

}
