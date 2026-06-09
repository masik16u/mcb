package com.masik.mythiccharms.mixin.charms;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.masik.mythiccharms.MythicCharms;
import com.masik.mythiccharms.component.ModDataComponentTypes;
import com.masik.mythiccharms.util.AdvancementHelper;
import com.masik.mythiccharms.util.CharmEntry;
import com.masik.mythiccharms.util.CharmInfoHelper;
import com.masik.mythiccharms.util.NbtHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class WeightlessFlowEntityMixin {

    @ModifyReturnValue(method = "hasNoGravity", at = @At("RETURN"))
    private boolean weightlessFlow(boolean original) {
        Entity entity = (Entity) (Object) this;
        if (!(entity instanceof PlayerEntity player)) return original;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "weightless_flow");
        if (entry == null) return false;

        NbtCompound comboCompound = CharmInfoHelper.getCombinationWithAbilityReverse(player, "drowned_freedom", entry);
        if (comboCompound != null && (!comboCompound.getBoolean("require_rain_or_conduit") ||
                (player.isBeingRainedOn() || player.hasStatusEffect(StatusEffects.CONDUIT_POWER)))) {
            return false;
        }

        NbtCompound nbtCompound = NbtHelper.getNbtCompound(entry.itemStack(), ModDataComponentTypes.CHARM_ABILITIES);
        NbtCompound abilityCompound = nbtCompound.getCompound("weightless_flow");

        if (!abilityCompound.getBoolean("disable_with_sneaking") || !player.isSneaking()) {
            if (player.getVelocity().y > 0) {
                AdvancementHelper.grantAdvancement(player, "mythic_charms:story/weightless_flow_jump", "impossible");
            }
            AdvancementHelper.grantAdvancement(player, "mythic_charms:story/all_charm_abilities", "weightless_flow");
            return true;
        }

        return false;
    }

}
