package com.masik.mythiccharms.mixin.charms;

import com.masik.mythiccharms.component.ModDataComponentTypes;
import com.masik.mythiccharms.util.AdvancementHelper;
import com.masik.mythiccharms.util.CharmEntry;
import com.masik.mythiccharms.util.CharmInfoHelper;
import com.masik.mythiccharms.util.NbtHelper;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ShroudedExistenceServerMixin {

    @Inject(method = "playerTick", at = @At("RETURN"))
    private void shroudedExistence(CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "shrouded_existence");
        if (entry == null) return;

        NbtCompound nbtCompound = NbtHelper.getNbtCompound(entry.itemStack(), ModDataComponentTypes.CHARM_ABILITIES);
        NbtCompound abilityCompound = nbtCompound.getCompound("shrouded_existence");

        if (abilityCompound.getBoolean("require_no_sprint") && player.isSprinting()) {
            return;
        }

        AdvancementHelper.grantAdvancement(player, "mythic_charms:story/all_charm_abilities", "shrouded_existence");
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY,
                abilityCompound.getInt("duration"), 0, false,
                abilityCompound.getBoolean("show_particles"), false));
    }

}
