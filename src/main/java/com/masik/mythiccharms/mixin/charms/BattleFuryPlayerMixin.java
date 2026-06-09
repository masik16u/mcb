package com.masik.mythiccharms.mixin.charms;

import com.llamalad7.mixinextras.sugar.Local;
import com.masik.mythiccharms.MythicCharms;
import com.masik.mythiccharms.util.AdvancementHelper;
import com.masik.mythiccharms.util.CharmEntry;
import com.masik.mythiccharms.util.CharmInfoHelper;
import com.masik.mythiccharms.util.CharmMathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PlayerEntity.class)
public class BattleFuryPlayerMixin {

    @ModifyArg(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"), index = 1)
    private float battleFury(float amount, @Local(argsOnly = true) Entity target) {
        if (!target.isLiving()) return amount;

        PlayerEntity player = (PlayerEntity) (Object) this;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "battle_fury");
        if (entry == null) return amount;

        // TL Challenge
        NbtCompound comboCompound = CharmInfoHelper.getCombinationWithAbility(player, entry, "twisted_lifeblood");
        StatusEffectInstance statusEffectInstance = player.getStatusEffect(StatusEffects.ABSORPTION);
        if (comboCompound != null && statusEffectInstance != null && statusEffectInstance.getAmplifier() == 3) {
            AdvancementHelper.grantAdvancement(player, "mythic_charms:story/battle_fury_twisted", "impossible");
        }

        float finalAmount = amount * CharmMathHelper.getBattleFuryMultiplier(player, entry);
        if (finalAmount > 100) {
            AdvancementHelper.grantAdvancement(player, "mythic_charms:story/battle_fury_overkill", "impossible");
        }
        else if (finalAmount < 0) {
            AdvancementHelper.grantAdvancement(player, "mythic_charms:story/battle_fury_negative", "impossible");
        }

        AdvancementHelper.grantAdvancement(player, "mythic_charms:story/all_charm_abilities", "battle_fury");
        return finalAmount;
    }

}
