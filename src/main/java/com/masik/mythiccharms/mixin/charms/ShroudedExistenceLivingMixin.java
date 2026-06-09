package com.masik.mythiccharms.mixin.charms;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.masik.mythiccharms.component.ModDataComponentTypes;
import com.masik.mythiccharms.util.CharmEntry;
import com.masik.mythiccharms.util.CharmInfoHelper;
import com.masik.mythiccharms.util.NbtHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class ShroudedExistenceLivingMixin {

    @ModifyReturnValue(method = "getArmorVisibility", at = @At("RETURN"))
    private float shroudedExistence(float original) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (!(entity instanceof PlayerEntity)) return original;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(entity, "shrouded_existence");
        if (entry == null) return original;

        NbtCompound nbtCompound = NbtHelper.getNbtCompound(entry.itemStack(), ModDataComponentTypes.CHARM_ABILITIES);
        NbtCompound abilityCompound = nbtCompound.getCompound("shrouded_existence");

        if (!abilityCompound.getBoolean("ignore_armor_visibility")) return original;

        if (abilityCompound.getBoolean("require_no_sprint") && entity.isSprinting()) {
            return original;
        }

        return 0;

    }

}
