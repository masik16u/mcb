package com.masik.mythiccharms.mixin.charms;

import com.masik.mythiccharms.component.ModDataComponentTypes;
import com.masik.mythiccharms.util.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mixin(PlayerEntity.class)
public class EchoingWrathPlayerMixin {

    @Inject(method = "damage", at = @At("RETURN"))
    private void echoingWrath(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player.getWorld().isClient) return;

        if (source.getAttacker() == null || !source.getAttacker().isLiving()) {
            return;
        }

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "echoing_wrath");
        if (entry == null) return;

        NbtCompound nbtCompound = NbtHelper.getNbtCompound(entry.itemStack(), ModDataComponentTypes.CHARM_ABILITIES);
        NbtCompound abilityCompound = nbtCompound.getCompound("echoing_wrath");

        // Get all variables necessary before loop
        float radius = abilityCompound.getFloat("radius");
        float radiusSquared = radius * radius;
        float damageMultiplier = abilityCompound.getFloat("damage_multiplier");
        float velocityMultiplier = abilityCompound.getFloat("velocity_multiplier");
        float verticalAddend = abilityCompound.getFloat("vertical_addend");

        DamageSource damageSource = new DamageSource(player.getWorld().getRegistryManager()
                .getWrapperOrThrow(RegistryKeys.DAMAGE_TYPE).getOrThrow(DamageTypes.THORNS), player);

        // Sundering Strike
        NbtCompound comboCompoundSS = CharmInfoHelper.getCombinationWithAbility(player, entry, "sundering_strike");
        if (comboCompoundSS != null) {
            damageMultiplier *= comboCompoundSS.getFloat("damage_multiplier");
            velocityMultiplier *= comboCompoundSS.getFloat("velocity_multiplier");
            verticalAddend *= comboCompoundSS.getFloat("vertical_multiplier");

            AdvancementHelper.grantAdvancement(player, "mythic_charms:story/all_charm_combos", "ew_ss");
        }

        // Battle Fury
        NbtCompound comboCompoundBF = CharmInfoHelper.getCombinationWithAbility(player, entry, "battle_fury");
        if (comboCompoundBF != null) {
            CharmEntry entryBF = CharmInfoHelper.getCharmWithAbility(player, "battle_fury");
            if (entryBF != null) {
                damageMultiplier *= CharmMathHelper.getBattleFuryMultiplier(player, entryBF);
            }
            velocityMultiplier *= comboCompoundBF.getFloat("velocity_multiplier");
            verticalAddend *= comboCompoundBF.getFloat("vertical_multiplier");

            AdvancementHelper.grantAdvancement(player, "mythic_charms:story/all_charm_combos", "ew_bf");
        }

        // Blazing Embrace
        int fireTicks = 0;
        NbtCompound comboCompoundBE = CharmInfoHelper.getCombinationWithAbility(player, entry, "blazing_embrace");
        if (comboCompoundBE != null) {
            damageMultiplier *= comboCompoundBE.getFloat("damage_multiplier");
            fireTicks = comboCompoundBE.getInt("fire_ticks");

            AdvancementHelper.grantAdvancement(player, "mythic_charms:story/all_charm_combos", "ew_be");
        }

        float finalDamageMultiplier = damageMultiplier;
        float finalVelocityMultiplier = velocityMultiplier;
        float finalVerticalAddend = verticalAddend;
        int finalFireTicks = fireTicks;

        Box box = player.getBoundingBox().expand(radius);
        List<LivingEntity> livingEntities = player.getWorld().getEntitiesByType(
                TypeFilter.instanceOf(LivingEntity.class),
                box, entity -> entity.squaredDistanceTo(player) < radiusSquared);
        livingEntities.forEach(entity -> {
            if (!entity.equals(player)) {
                entity.damage(damageSource, amount * finalDamageMultiplier);
                entity.addVelocity(entity.getPos().subtract(player.getPos())
                        .multiply(finalVelocityMultiplier)
                        .add(0, finalVerticalAddend, 0));
                entity.velocityModified = true;
            }
            if (finalFireTicks > 0) {
                entity.setOnFireForTicks(finalFireTicks);
            }
        });

        AdvancementHelper.grantAdvancement(player, "mythic_charms:story/all_charm_abilities", "echoing_wrath");

        // Challenge
        if (containsAllTypes(livingEntities, player.getWorld())) {
            AdvancementHelper.grantAdvancement(player, "mythic_charms:story/echoing_wrath_blazing", "impossible");
        }
    }

    @Unique
    private static boolean containsAllTypes(List<LivingEntity> entities, World world) {
        Set<EntityType<?>> presentTypes = entities.stream()
                .map(Entity::getType)
                .collect(Collectors.toSet());

        List<EntityType<?>> entityTypes = TagHelper.getEntitiesFromTag(world, ModTags.ADVANCEMENT_DROP_COOKED_FOOD);
        return presentTypes.containsAll(entityTypes);
    }

}
