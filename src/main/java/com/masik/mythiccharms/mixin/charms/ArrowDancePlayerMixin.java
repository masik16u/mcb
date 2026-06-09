package com.masik.mythiccharms.mixin.charms;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.masik.mythiccharms.MythicCharms;
import com.masik.mythiccharms.accessor.ArrowDanceAccessor;
import com.masik.mythiccharms.component.ModDataComponentTypes;
import com.masik.mythiccharms.util.AdvancementHelper;
import com.masik.mythiccharms.util.CharmEntry;
import com.masik.mythiccharms.util.CharmInfoHelper;
import com.masik.mythiccharms.util.NbtHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class ArrowDancePlayerMixin implements ArrowDanceAccessor {

    @Unique
    private long lastJumpTick;

    @Override
    public long mythic_charms$getLastJumpTick() {
        return lastJumpTick;
    }

    @Inject(method = "jump", at = @At("RETURN"))
    private void arrowDance(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player.getWorld().isClient) return;

        lastJumpTick = player.getWorld().getTime();
    }

    @ModifyReturnValue(method = "canBeHitByProjectile", at = @At("RETURN"))
    private boolean arrowDance(boolean original) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player.isSpectator()) return original;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "arrow_dance");
        if (entry == null) return original;

        NbtCompound nbtCompound = NbtHelper.getNbtCompound(entry.itemStack(), ModDataComponentTypes.CHARM_ABILITIES);
        NbtCompound abilityCompound = nbtCompound.getCompound("arrow_dance");

        NbtCompound comboCompound = CharmInfoHelper.getCombinationWithAbility(player, entry, "mountain_stance");
        if (comboCompound != null) return original;

        if ((abilityCompound.getBoolean("require_jump") &&
                player.getWorld().getTime() - lastJumpTick > abilityCompound.getInt("window_time")) ||
                (abilityCompound.getBoolean("require_elevated") && player.isOnGround())) {
            return original;
        }

        AdvancementHelper.grantAdvancement(player, "mythic_charms:story/all_charm_abilities", "arrow_dance");
        return false;
    }

}
