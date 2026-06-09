package com.masik.mythiccharms.mixin.charms;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.masik.mythiccharms.MythicCharms;
import com.masik.mythiccharms.component.ModDataComponentTypes;
import com.masik.mythiccharms.util.AdvancementHelper;
import com.masik.mythiccharms.util.CharmEntry;
import com.masik.mythiccharms.util.CharmInfoHelper;
import com.masik.mythiccharms.util.NbtHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class WarpedGroundLivingMixin {

    @Unique
    int jumpCount = 0;

    @Unique
    boolean releasedJump = false;

    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void warpedGroundTick(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (!(entity instanceof PlayerEntity player)) return;

        if (!entity.isOnGround() && !entity.jumping) {
            releasedJump = true;
        }
        else if (entity.isOnGround()) {
            releasedJump = false;
        }
    }

    @Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;shouldSwimInFluids()Z", shift = At.Shift.AFTER))
    private void warpedGround(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (!(entity instanceof PlayerEntity player)) return;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "warped_ground");
        if (entry == null) return;

        NbtCompound comboCompound = CharmInfoHelper.getCombinationWithAbility(player, entry, "feathered_grace");
        if (comboCompound == null) return;

        if (comboCompound.getBoolean("require_sprint") && !player.isSprinting()) {
            return;
        }

        if (!player.isOnGround()) {
            if (jumpCount > 0 && player.jumpingCooldown == 0 && player.jumping &&
                    releasedJump && player.fallDistance < comboCompound.getFloat("max_start_fall_distance")) {

                player.jump();
                player.fallDistance = 0;

                jumpCount -= 1;
                player.jumpingCooldown = 10;
            }
        }
        else {
            jumpCount = comboCompound.getInt("additional_jumps");
        }

    }

    @ModifyReturnValue(method = "getStepHeight", at = @At("RETURN"))
    private float warpedGround(float original) {
        LivingEntity entity = (LivingEntity) (Object) this;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(entity, "warped_ground");
        if (entry == null) return original;

        NbtCompound nbtCompound = NbtHelper.getNbtCompound(entry.itemStack(), ModDataComponentTypes.CHARM_ABILITIES);
        NbtCompound abilityCompound = nbtCompound.getCompound("warped_ground");

        if (abilityCompound.getBoolean("require_sprint") && !entity.isSprinting()) {
            return original;
        }

        AdvancementHelper.grantAdvancement(entity, "mythic_charms:story/all_charm_abilities", "warped_ground");
        return original + abilityCompound.getFloat("height_addend");
    }

    @ModifyReturnValue(method = "canWalkOnFluid", at = @At("RETURN"))
    private boolean warpedGround(boolean original, @Local(argsOnly = true) FluidState state) {
        LivingEntity entity = (LivingEntity) (Object) this;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(entity, "warped_ground");
        if (entry == null) return original;

        NbtCompound comboCompound = CharmInfoHelper.getCombinationWithAbility(entity, entry, "drowned_freedom");
        if (comboCompound == null) return original;

        if (comboCompound.getBoolean("require_sprint") && !entity.isSprinting()) {
            return original;
        }

        CharmEntry entryDF = CharmInfoHelper.getCharmWithAbility(entity, "drowned_freedom");
        if (entryDF == null) return original;
        NbtCompound comboCompoundDF = CharmInfoHelper.getCombinationWithAbility(entity, entryDF, "blazing_embrace");

        if (state.isIn(FluidTags.WATER) || (comboCompoundDF != null && state.isIn(FluidTags.LAVA))) {
            AdvancementHelper.grantAdvancement(entity, "mythic_charms:story/all_charm_combos", "wg_df");
            return true;
        }

        return original;
    }

}
