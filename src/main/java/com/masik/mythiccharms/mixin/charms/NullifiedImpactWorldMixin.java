package com.masik.mythiccharms.mixin.charms;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.masik.mythiccharms.component.ModDataComponentTypes;
import com.masik.mythiccharms.util.AdvancementHelper;
import com.masik.mythiccharms.util.CharmEntry;
import com.masik.mythiccharms.util.CharmInfoHelper;
import com.masik.mythiccharms.util.NbtHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.List;

@Mixin(World.class)
public class NullifiedImpactWorldMixin {

    @ModifyArgs(method = "createExplosion(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lnet/minecraft/world/explosion/ExplosionBehavior;DDDFZLnet/minecraft/world/World$ExplosionSourceType;ZLnet/minecraft/particle/ParticleEffect;Lnet/minecraft/particle/ParticleEffect;Lnet/minecraft/registry/entry/RegistryEntry;)Lnet/minecraft/world/explosion/Explosion;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/explosion/Explosion;<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lnet/minecraft/world/explosion/ExplosionBehavior;DDDFZLnet/minecraft/world/explosion/Explosion$DestructionType;Lnet/minecraft/particle/ParticleEffect;Lnet/minecraft/particle/ParticleEffect;Lnet/minecraft/registry/entry/RegistryEntry;)V"))
    private void nullifiedImpact2(Args args) {

        Entity entity = args.get(1);
        Box box = Box.from(entity.getPos()).expand(16);

        List<PlayerEntity> players =
                entity.getWorld()
                        .getEntitiesByType(
                                TypeFilter.instanceOf(PlayerEntity.class),
                                box,
                                player -> true
                        );

        for (PlayerEntity player : players) {
            CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "nullified_impact");
            if (entry == null) continue;

            NbtCompound nbtCompound = NbtHelper.getNbtCompound(entry.itemStack(), ModDataComponentTypes.CHARM_ABILITIES);
            NbtCompound abilityCompound = nbtCompound.getCompound("nullified_impact");

            float radius = abilityCompound.getFloat("radius");
            if (entity.squaredDistanceTo(player) < radius * radius) {
                AdvancementHelper.grantAdvancement(player, "mythic_charms:story/all_charm_abilities", "nullified_impact");
                args.set(9, Explosion.DestructionType.KEEP);
            }
        }

    }

}
