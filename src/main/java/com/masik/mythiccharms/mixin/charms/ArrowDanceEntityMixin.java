package com.masik.mythiccharms.mixin.charms;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.masik.mythiccharms.MythicCharms;
import com.masik.mythiccharms.accessor.ArrowDanceAccessor;
import com.masik.mythiccharms.util.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ProjectileDeflection;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.*;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(Entity.class)
public class ArrowDanceEntityMixin {

    @ModifyReturnValue(method = "getProjectileDeflection", at = @At("RETURN"))
    private ProjectileDeflection arrowDance(ProjectileDeflection original, @Local(argsOnly = true) ProjectileEntity projectileEntity) {
        Entity entity = (Entity) (Object) this;
        if (!(entity instanceof PlayerEntity player)) return original;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "arrow_dance");
        if (entry == null) return original;

        NbtCompound comboCompound = CharmInfoHelper.getCombinationWithAbility(player, entry, "mountain_stance");
        if (comboCompound == null) return original;

        ArrowDanceAccessor accessor = (ArrowDanceAccessor) player;
        if ((comboCompound.getBoolean("require_jump") &&
                player.getWorld().getTime() - accessor.mythic_charms$getLastJumpTick() > comboCompound.getInt("window_time")) ||
                (comboCompound.getBoolean("require_elevated") && player.isOnGround())) {
            return original;
        }

        // Challenge
        Entity owner = projectileEntity.getOwner();
        if (owner instanceof MobEntity) {

            List<EntityType<?>> entityTypes = TagHelper.getEntitiesFromTag(player.getWorld(), ModTags.ADVANCEMENT_PROJECTILES);
            for (EntityType<?> entityType : entityTypes) {

                EntityType<?> projectileEntityType = projectileEntity.getType();

                if (projectileEntityType == entityType) {
                    AdvancementHelper.grantAdvancement(player, "mythic_charms:story/arrow_dance_reflect",
                            projectileEntityType.toString().replace("entity.minecraft.", ""));
                }
            }

        }

        AdvancementHelper.grantAdvancement(player, "mythic_charms:story/all_charm_combos", "ad_ms");
        return ProjectileDeflection.SIMPLE;
    }

}
