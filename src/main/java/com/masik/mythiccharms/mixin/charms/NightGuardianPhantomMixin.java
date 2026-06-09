package com.masik.mythiccharms.mixin.charms;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.masik.mythiccharms.component.ModDataComponentTypes;
import com.masik.mythiccharms.util.AdvancementHelper;
import com.masik.mythiccharms.util.CharmEntry;
import com.masik.mythiccharms.util.CharmInfoHelper;
import com.masik.mythiccharms.util.NbtHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PhantomEntity.class)
public class NightGuardianPhantomMixin {

    @ModifyReturnValue(method = "canTarget", at = @At("RETURN"))
    private boolean nightGuardian(boolean original) {
        PhantomEntity phantomEntity = (PhantomEntity) (Object) this;

        LivingEntity livingEntity = phantomEntity.getTarget();
        if (!(livingEntity instanceof PlayerEntity player)) return original;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "night_guardian");
        if (entry == null) return original;

        NbtCompound nbtCompound = NbtHelper.getNbtCompound(entry.itemStack(), ModDataComponentTypes.CHARM_ABILITIES);
        NbtCompound abilityCompound = nbtCompound.getCompound("night_guardian");

        if (abilityCompound.getBoolean("disable_target")) {
            AdvancementHelper.grantAdvancement(player, "mythic_charms:story/all_charm_abilities", "night_guardian");
            return false;
        }

        return original;
    }

}
