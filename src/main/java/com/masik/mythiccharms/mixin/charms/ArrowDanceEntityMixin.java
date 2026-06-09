package com.masik.mythiccharms.mixin.charms;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.masik.mythiccharms.MythicCharms;
import com.masik.mythiccharms.accessor.ArrowDanceAccessor;
import com.masik.mythiccharms.util.AdvancementHelper;
import com.masik.mythiccharms.util.CharmEntry;
import com.masik.mythiccharms.util.CharmInfoHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ProjectileDeflection;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.*;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

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
            if (projectileEntity instanceof ArrowEntity) {
                AdvancementHelper.grantAdvancement(player, "mythic_charms:story/arrow_dance_reflect", "arrow");
            }
            else if (projectileEntity instanceof FireballEntity) {
                AdvancementHelper.grantAdvancement(player, "mythic_charms:story/arrow_dance_reflect", "fireball");
            }
            else if (projectileEntity instanceof SmallFireballEntity) {
                AdvancementHelper.grantAdvancement(player, "mythic_charms:story/arrow_dance_reflect", "small_fireball");
            }
            else if (projectileEntity instanceof DragonFireballEntity) {
                AdvancementHelper.grantAdvancement(player, "mythic_charms:story/arrow_dance_reflect", "dragon_fireball");
            }
            else if (projectileEntity instanceof LlamaSpitEntity) {
                AdvancementHelper.grantAdvancement(player, "mythic_charms:story/arrow_dance_reflect", "llama_spit");
            }
            else if (projectileEntity instanceof ShulkerBulletEntity) {
                AdvancementHelper.grantAdvancement(player, "mythic_charms:story/arrow_dance_reflect", "shulker_bullet");
            }
            else if (projectileEntity instanceof SnowballEntity) {
                AdvancementHelper.grantAdvancement(player, "mythic_charms:story/arrow_dance_reflect", "snowball");
            }
            else if (projectileEntity instanceof PotionEntity) {
                AdvancementHelper.grantAdvancement(player, "mythic_charms:story/arrow_dance_reflect", "potion");
            }
            else if (projectileEntity instanceof TridentEntity) {
                AdvancementHelper.grantAdvancement(player, "mythic_charms:story/arrow_dance_reflect", "trident");
            }
            else if (projectileEntity instanceof BreezeWindChargeEntity) {
                AdvancementHelper.grantAdvancement(player, "mythic_charms:story/arrow_dance_reflect", "wind_charge");
            }
            else if (projectileEntity instanceof WitherSkullEntity) {
                AdvancementHelper.grantAdvancement(player, "mythic_charms:story/arrow_dance_reflect", "wither_skull");
            }
        }

        AdvancementHelper.grantAdvancement(player, "mythic_charms:story/all_charm_combos", "ad_ms");
        return ProjectileDeflection.SIMPLE;
    }

}
