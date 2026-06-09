package com.masik.mythiccharms.mixin.charms;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
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
public class TwistedLifebloodLivingMixin {

    @ModifyReturnValue(method = "hasInvertedHealingAndHarm", at = @At("RETURN"))
    private boolean twistedLifeblood(boolean original) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (!(entity instanceof PlayerEntity player)) return original;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "twisted_lifeblood");
        if (entry == null) return original;

        NbtCompound nbtCompound = NbtHelper.getNbtCompound(entry.itemStack(), ModDataComponentTypes.CHARM_ABILITIES);
        NbtCompound abilityCompound = nbtCompound.getCompound("twisted_lifeblood");

        if (!abilityCompound.getBoolean("invert_heal_and_harm")) return original;

        AdvancementHelper.grantAdvancement(player, "mythic_charms:story/all_charm_abilities", "twisted_lifeblood");
        return true;
    }

}
