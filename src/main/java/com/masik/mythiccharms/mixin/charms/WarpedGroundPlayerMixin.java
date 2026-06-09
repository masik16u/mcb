package com.masik.mythiccharms.mixin.charms;

import com.masik.mythiccharms.MythicCharms;
import com.masik.mythiccharms.util.AdvancementHelper;
import com.masik.mythiccharms.util.CharmEntry;
import com.masik.mythiccharms.util.CharmInfoHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class WarpedGroundPlayerMixin {

    @Inject(method = "jump", at = @At("HEAD"))
    private void warpedGround(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "warped_ground");
        if (entry == null) return;

        NbtCompound comboCompound = CharmInfoHelper.getCombinationWithAbility(player, entry, "feathered_grace");
        if (comboCompound != null) {
            AdvancementHelper.grantAdvancement(player, "mythic_charms:story/all_charm_combos", "wg_fg");
        }
    }

}
