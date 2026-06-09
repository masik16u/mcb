package com.masik.mythiccharms.mixin.charms;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.masik.mythiccharms.component.ModDataComponentTypes;
import com.masik.mythiccharms.util.AdvancementHelper;
import com.masik.mythiccharms.util.CharmEntry;
import com.masik.mythiccharms.util.CharmInfoHelper;
import com.masik.mythiccharms.util.NbtHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class DrownedFreedomEntityMixin {

    @ModifyReturnValue(method = "isTouchingWater", at = @At("RETURN"))
    private boolean drownedFreedom(boolean original) {
        Entity entity = (Entity) (Object) this;
        if (!(entity instanceof PlayerEntity player)) return original;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "drowned_freedom");
        if (entry == null) return original;

        if (isWeightlessFlowCombinationActive(player, entry)) {
            AdvancementHelper.grantAdvancement(player, "mythic_charms:story/all_charm_combos", "df_wf");
            if (player.getPos().y >= 370) {
                AdvancementHelper.grantAdvancement(player, "mythic_charms:story/drowned_freedom_top", "impossible");
            }
            if (player.getEquippedStack(EquipmentSlot.FEET).getEnchantments()
                    .getLevel(player.getWorld().getRegistryManager().getOptionalWrapper(RegistryKeys.ENCHANTMENT)
                            .get().getOrThrow(Enchantments.DEPTH_STRIDER)) > 0 && player.hasStatusEffect(StatusEffects.DOLPHINS_GRACE)) {
                AdvancementHelper.grantAdvancement(player, "mythic_charms:story/drowned_freedom_speed", "impossible");
            }
            return true;
        }

        AdvancementHelper.grantAdvancement(player, "mythic_charms:story/all_charm_abilities", "drowned_freedom");
        return false;
    }

    @ModifyReturnValue(method = "isSwimming", at = @At("RETURN"))
    private boolean drownedFreedomSwim(boolean original) {
        Entity entity = (Entity) (Object) this;
        if (!(entity instanceof PlayerEntity player)) return original;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "drowned_freedom");
        if (entry == null) return original;

        if (isWeightlessFlowCombinationActive(player, entry)) {
            return true;
        }

        return false;
    }

    @WrapWithCondition(method = "checkWaterState", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;onLanding()V"))
    private boolean drownedFreedomFall(Entity instance) {
        Entity entity = (Entity) (Object) this;
        if (!(entity instanceof PlayerEntity player)) return true;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "drowned_freedom");
        if (entry == null) return true;

        NbtCompound nbtCompound = NbtHelper.getNbtCompound(entry.itemStack(), ModDataComponentTypes.CHARM_ABILITIES);
        NbtCompound abilityCompound = nbtCompound.getCompound("drowned_freedom");

        if (isWeightlessFlowCombinationActive(player, entry)) {
            return true;
        }

        if (!abilityCompound.getBoolean("take_fall_damage")) {
            return true;
        }

        return false;
    }

    @WrapWithCondition(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;onLanding()V"))
    private boolean drownedFreedomFall2(Entity instance) {
        Entity entity = (Entity) (Object) this;
        if (!(entity instanceof PlayerEntity player)) return true;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "drowned_freedom");
        if (entry == null) return true;

        NbtCompound nbtCompound = NbtHelper.getNbtCompound(entry.itemStack(), ModDataComponentTypes.CHARM_ABILITIES);
        NbtCompound abilityCompound = nbtCompound.getCompound("drowned_freedom");

        if (isWeightlessFlowCombinationActive(player, entry)) {
            return true;
        }

        if (!abilityCompound.getBoolean("take_fall_damage")) {
            return true;
        }

        return false;
    }

    @ModifyReturnValue(method = "updateMovementInFluid", at = @At("RETURN"))
    private boolean drownedFreedomMovement(boolean original, @Local(argsOnly = true) TagKey<Fluid> fluidTag) {
        Entity entity = (Entity) (Object) this;
        if (!(entity instanceof PlayerEntity player)) return original;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "drowned_freedom");
        if (entry == null) return original;

        NbtCompound comboCompound = CharmInfoHelper.getCombinationWithAbility(player, entry, "blazing_embrace");
        if (!fluidTag.equals(FluidTags.WATER) && !(fluidTag.equals(FluidTags.LAVA) && comboCompound != null)) {
            return original;
        }

        if (isWeightlessFlowCombinationActive(player, entry)) {
            return true;
        }

        if (fluidTag.equals(FluidTags.LAVA) && comboCompound != null) {
            AdvancementHelper.grantAdvancement(player, "mythic_charms:story/all_charm_combos", "df_be");
        }
        return false;
    }

    @WrapWithCondition(method = "updateMovementInFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"))
    private boolean drownedFreedomMovement(Entity instance, Vec3d velocity, @Local(argsOnly = true) TagKey<Fluid> fluidTag) {
        Entity entity = (Entity) (Object) this;
        if (!(entity instanceof PlayerEntity player)) return true;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "drowned_freedom");
        if (entry == null) return true;

        NbtCompound comboCompound = CharmInfoHelper.getCombinationWithAbility(player, entry, "blazing_embrace");
        if (!fluidTag.equals(FluidTags.WATER) && !(fluidTag.equals(FluidTags.LAVA) && comboCompound != null)) {
            return true;
        }

        if (isWeightlessFlowCombinationActive(player, entry)) {
            return true;
        }

        return false;
    }

    // ONLY For Combination
    @ModifyReturnValue(method = "isSubmergedIn", at = @At("RETURN"))
    private boolean drownedFreedomSubmerged(boolean original, @Local(argsOnly = true) TagKey<Fluid> fluidTag) {
        Entity entity = (Entity) (Object) this;
        if (!(entity instanceof PlayerEntity player)) return original;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "drowned_freedom");
        if (entry == null) return original;

        if (isWeightlessFlowCombinationActive(player, entry)) {
            return true;
        }

        return original;
    }

    @ModifyReturnValue(method = "isSubmergedInWater", at = @At("RETURN"))
    private boolean drownedFreedomSubmerged(boolean original) {
        Entity entity = (Entity) (Object) this;
        if (!(entity instanceof PlayerEntity player)) return original;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "drowned_freedom");
        if (entry == null) return original;

        if (isWeightlessFlowCombinationActive(player, entry)) {
            return true;
        }

        return original;
    }

    @ModifyReturnValue(method = "isInLava", at = @At("RETURN"))
    private boolean drownedFreedomLava(boolean original) {
        Entity entity = (Entity) (Object) this;
        if (!(entity instanceof PlayerEntity player)) return original;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "drowned_freedom");
        if (entry == null) return original;

        NbtCompound comboCompound = CharmInfoHelper.getCombinationWithAbility(player, entry, "blazing_embrace");
        if (comboCompound == null) return original;

        return false;
    }

    @Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isInLava()Z"))
    private void drownedFreedomLava(CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        if (!(entity instanceof PlayerEntity player)) return;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "drowned_freedom");
        if (entry == null) return;

        NbtCompound comboCompound = CharmInfoHelper.getCombinationWithAbility(player, entry, "blazing_embrace");
        if (comboCompound != null && player.getFluidHeight(FluidTags.LAVA) > 0 && comboCompound.getBoolean("set_on_fire")) {
            player.setOnFireFromLava();
        }
    }

    @Unique
    private static boolean isWeightlessFlowCombinationActive(LivingEntity entity, CharmEntry charmEntry) {
        NbtCompound comboCompound = CharmInfoHelper.getCombinationWithAbility(entity, charmEntry, "weightless_flow");
        if (comboCompound == null) return false;

        if (comboCompound.getBoolean("require_rain_or_conduit") &&
                (!entity.isBeingRainedOn() && !entity.hasStatusEffect(StatusEffects.CONDUIT_POWER))) {
            return false;
        }

        return true;
    }

}
