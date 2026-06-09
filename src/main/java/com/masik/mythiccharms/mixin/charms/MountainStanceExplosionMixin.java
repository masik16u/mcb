package com.masik.mythiccharms.mixin.charms;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.masik.mythiccharms.MythicCharms;
import com.masik.mythiccharms.component.ModDataComponentTypes;
import com.masik.mythiccharms.util.AdvancementHelper;
import com.masik.mythiccharms.util.CharmEntry;
import com.masik.mythiccharms.util.CharmInfoHelper;
import com.masik.mythiccharms.util.NbtHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

@Mixin(Explosion.class)
public class MountainStanceExplosionMixin {

    @WrapWithCondition(method = "collectBlocksAndDamageEntities",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"))
    private boolean mountainStance(Entity instance, Vec3d velocity) {
        if (!(instance instanceof PlayerEntity player)) return true;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "mountain_stance");
        if (entry == null) return true;

        NbtCompound nbtCompound = NbtHelper.getNbtCompound(entry.itemStack(), ModDataComponentTypes.CHARM_ABILITIES);
        NbtCompound abilityCompound = nbtCompound.getCompound("mountain_stance");

        if (abilityCompound.getBoolean("require_stationary") &&
                player.getMovement().lengthSquared() > 1.0E-4) {
            return true;
        }

        if (!abilityCompound.getBoolean("ignore_explosion")) {
            return true;
        }

        AdvancementHelper.grantAdvancement(player, "mythic_charms:story/all_charm_abilities", "mountain_stance");
        CharmEntry entryNI = CharmInfoHelper.getCharmWithAbility(player, "nullified_impact");
        if (entryNI != null) {
            AdvancementHelper.grantAdvancement(player, "mythic_charms:story/nullified_impact_mountain", "impossible");
        }
        return false;
    }

    @WrapWithCondition(method = "collectBlocksAndDamageEntities",
            at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
    private boolean mountainStance(Map<?, ?> instance, Object playerObject, Object vec3d) {
        if (!(playerObject instanceof PlayerEntity player)) return true;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "mountain_stance");
        if (entry == null) return true;

        NbtCompound nbtCompound = NbtHelper.getNbtCompound(entry.itemStack(), ModDataComponentTypes.CHARM_ABILITIES);
        NbtCompound abilityCompound = nbtCompound.getCompound("mountain_stance");

        if (abilityCompound.getBoolean("require_stationary") &&
                player.getMovement().lengthSquared() > 1.0E-4) {
            return true;
        }

        if (!abilityCompound.getBoolean("ignore_explosion")) {
            return true;
        }

        return false;
    }

}
