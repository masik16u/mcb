package com.masik.mythiccharms.mixin.charms;

import com.masik.mythiccharms.component.ModDataComponentTypes;
import com.masik.mythiccharms.util.AdvancementHelper;
import com.masik.mythiccharms.util.CharmEntry;
import com.masik.mythiccharms.util.CharmInfoHelper;
import com.masik.mythiccharms.util.NbtHelper;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class BlazingEmbracePlayerMixin {

    @Inject(method = "setFireTicks", at = @At("RETURN"))
    private void blazingEmbrace(int fireTicks, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player.getWorld().isClient) return;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "blazing_embrace");
        if (entry == null) return;

        NbtCompound nbtCompound = NbtHelper.getNbtCompound(entry.itemStack(), ModDataComponentTypes.CHARM_ABILITIES);
        NbtCompound abilityCompound = nbtCompound.getCompound("blazing_embrace");

        if (abilityCompound.getBoolean("require_extinguished") && player.isOnFire()) {
            return;
        }

        int duration = abilityCompound.getInt("duration");

        NbtCompound comboCompound = CharmInfoHelper.getCombinationWithAbility(player, entry, "drowned_freedom");
        if (comboCompound != null) {
            duration += comboCompound.getInt("duration_addend");
        }

        AdvancementHelper.grantAdvancement(player, "mythic_charms:story/all_charm_abilities", "blazing_embrace");
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE,
                duration, 0, false,
                false, true));
    }

}
