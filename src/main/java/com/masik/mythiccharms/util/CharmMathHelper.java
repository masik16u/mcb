package com.masik.mythiccharms.util;

import com.masik.mythiccharms.component.ModDataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;

public class CharmMathHelper {

    public static float getBattleFuryMultiplier(LivingEntity livingEntity, CharmEntry entry) {

        NbtCompound nbtCompound = NbtHelper.getNbtCompound(entry.itemStack(), ModDataComponentTypes.CHARM_ABILITIES);
        NbtCompound abilityCompound = nbtCompound.getCompound("battle_fury");

        NbtCompound comboCompoundNG = CharmInfoHelper.getCombinationWithAbility(livingEntity, entry, "night_guardian");
        NbtCompound comboCompoundTL = CharmInfoHelper.getCombinationWithAbility(livingEntity, entry, "twisted_lifeblood");
        NbtCompound comboCompoundEO = CharmInfoHelper.getCombinationWithAbility(livingEntity, entry, "earth_order");

        if (comboCompoundNG != null && comboCompoundTL == null && comboCompoundEO == null) {
            AdvancementHelper.grantAdvancement(livingEntity, "mythic_charms:story/all_charm_combos", "bf_ng");

            return getFuryMultiplier(
                    getNoonPercentage(livingEntity) * 2,
                    comboCompoundNG.getFloat("vertical_scale"),
                    comboCompoundNG.getInt("curvature_exponent"),
                    comboCompoundNG.getFloat("horizontal_shift"));
        }
        else if (comboCompoundEO != null && comboCompoundTL == null && comboCompoundNG == null) {
            AdvancementHelper.grantAdvancement(livingEntity, "mythic_charms:story/all_charm_combos", "bf_eo");

            return getFuryMultiplier(
                    getCoefficientRelativeSeaLevel(livingEntity),
                    comboCompoundEO.getFloat("vertical_scale"),
                    comboCompoundEO.getInt("curvature_exponent"),
                    comboCompoundEO.getFloat("horizontal_shift"));
        }
        else if (comboCompoundTL != null && comboCompoundNG == null && comboCompoundEO == null) {
            AdvancementHelper.grantAdvancement(livingEntity, "mythic_charms:story/all_charm_combos", "bf_tl");

            return Math.min(getFuryMultiplier(
                    getHealthPercentageWithAbsorption(livingEntity),
                            comboCompoundTL.getFloat("vertical_scale"),
                            comboCompoundTL.getInt("curvature_exponent"),
                            comboCompoundTL.getFloat("horizontal_shift")),
                    comboCompoundTL.getFloat("damage_cap"));
        }
        else {
            return getFuryMultiplier(
                    getHealthPercentageWithAbsorption(livingEntity),
                    abilityCompound.getFloat("vertical_scale"),
                    abilityCompound.getInt("curvature_exponent"),
                    abilityCompound.getFloat("horizontal_shift"));
        }
    }

    public static float getFuryMultiplier(float percentage, float vertical_scale, int curvature_exponent, float horizontal_shift) {
        return (float) ((vertical_scale - 1) * Math.pow((-percentage + horizontal_shift), (2 * curvature_exponent + 1)) + 1);
    }

    public static float getHealthPercentage(LivingEntity entity) {
        return entity.getHealth() / entity.getMaxHealth();
    }

    public static float getHealthPercentageWithAbsorption(LivingEntity entity) {
        return (entity.getHealth() + entity.getAbsorptionAmount()) / entity.defaultMaxHealth;
    }

    // at noon(6000t) - 100%; at midnight(18000t) - 0%
    public static float getNoonPercentage(LivingEntity entity) {
        long time = Math.abs(entity.getWorld().getTimeOfDay() - 6000);
        return (float) ((12000 - (time - Math.max(0, time - 12000))) / 12000.0);
    }

    public static float getCoefficientRelativeSeaLevel(LivingEntity entity) {
        return (float) (entity.getPos().y / 128 + 0.5);
    }

}
