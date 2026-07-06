package com.masik.mythiccharms.mixin.tokens;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.masik.mythiccharms.MythicCharms;
import com.masik.mythiccharms.util.CharmEntry;
import com.masik.mythiccharms.util.CharmInfoHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class TokenPlayerMixin {

    @ModifyReturnValue(method = "getHurtSound", at = @At("RETURN"))
    private SoundEvent felineWhimsyHurt(SoundEvent original) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "feline_whimsy");
        if (entry == null) return original;

        return SoundEvents.ENTITY_CAT_HURT;
    }

    @ModifyReturnValue(method = "getDeathSound", at = @At("RETURN"))
    private SoundEvent felineWhimsyDeath(SoundEvent original) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "feline_whimsy");
        if (entry == null) return original;

        return SoundEvents.ENTITY_CAT_HURT;
    }

    @ModifyReturnValue(method = "getName", at = @At("RETURN"))
    private Text namelessShadow(Text original) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "nameless_shadow");
        if (entry == null) return original;

        return Text.empty().append(original).setStyle(Style.EMPTY.withFont(Identifier.of(MythicCharms.MOD_ID, "ascii_script")));
    }

    @Inject(method = "jump", at = @At("RETURN"))
    private void dustyCloud(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
//        if (player.getWorld().isClient) return;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "dusty_cloud");
        if (entry == null) return;

        player.getWorld().addParticle(ParticleTypes.CLOUD, player.getX() + (Math.random() - 0.5) * 0.3,
                player.getY() + 0.05,
                player.getZ() + (Math.random() - 0.5) * 0.3,
                (Math.random() - 0.5) * 0.05,
                0.02,
                (Math.random() - 0.5) * 0.05);
    }

    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isDay()Z"))
    private boolean daylightDreams(boolean original) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "daylight_dreams");
        if (entry == null) return original;

        if (player.getWorld().isDay() && !player.getWorld().isClient) {
            player.sleepTimer = 0;
        }
        return false;
    }

    @ModifyReturnValue(method = "getMaxRelativeHeadRotation", at = @At("RETURN"))
    private float crookedStare(float original) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "crooked_stare");
        if (entry == null) return original;

        return 160F;
    }

    @Inject(method = "addExperienceLevels", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"))
    private void arcaneProgress(int levels, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player.getWorld().isClient) return;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "arcane_progress");
        if (entry == null) return;

        player.getWorld().playSound(null,
                player.getX(), player.getY(), player.getZ(),
                SoundEvents.ENTITY_EVOKER_PREPARE_WOLOLO,
                player.getSoundCategory(), 10F, 1.0F);
    }

}
