package com.masik.mythiccharms.mixin.charms;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.masik.mythiccharms.component.ModDataComponentTypes;
import com.masik.mythiccharms.util.AdvancementHelper;
import com.masik.mythiccharms.util.CharmEntry;
import com.masik.mythiccharms.util.CharmInfoHelper;
import com.masik.mythiccharms.util.NbtHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.PoisonStatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PoisonStatusEffect.class)
public class TwistedLifebloodPoisonMixin {

    @WrapMethod(method = "applyUpdateEffect")
    private boolean twistedLifeblood(LivingEntity entity, int amplifier, Operation<Boolean> original) {
        if (entity.getWorld().isClient || !(entity instanceof PlayerEntity player)) return original.call(entity, amplifier);

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "twisted_lifeblood");
        if (entry == null) return original.call(entity, amplifier);

        NbtCompound nbtCompound = NbtHelper.getNbtCompound(entry.itemStack(), ModDataComponentTypes.CHARM_ABILITIES);
        NbtCompound abilityCompound = nbtCompound.getCompound("twisted_lifeblood");

        if (!abilityCompound.getBoolean("invert_poison_and_regen")) return original.call(entity, amplifier);

        if (player.getHealth() < player.getMaxHealth()) {
            player.heal(1.0F);
            AdvancementHelper.grantAdvancement(player, "mythic_charms:story/all_charm_abilities", "twisted_lifeblood");
        }
        return true;
    }

}
