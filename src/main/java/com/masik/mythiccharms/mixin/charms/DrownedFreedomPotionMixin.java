package com.masik.mythiccharms.mixin.charms;

import com.llamalad7.mixinextras.sugar.Local;
import com.masik.mythiccharms.util.AdvancementHelper;
import com.masik.mythiccharms.util.CharmEntry;
import com.masik.mythiccharms.util.CharmInfoHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PotionEntity.class)
public class DrownedFreedomPotionMixin {

    @Inject(method = "applyWater", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;extinguishWithSound()V"))
    private void drownedFreedom(CallbackInfo ci, @Local LivingEntity livingEntity) {
        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(livingEntity, "drowned_freedom");
        if (entry != null) {
            AdvancementHelper.grantAdvancement(livingEntity, "mythic_charms:story/drowned_freedom_extinguish", "impossible");
        }
    }

}
