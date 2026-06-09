package com.masik.mythiccharms.mixin.charms;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.masik.mythiccharms.component.ModDataComponentTypes;
import com.masik.mythiccharms.util.AdvancementHelper;
import com.masik.mythiccharms.util.CharmEntry;
import com.masik.mythiccharms.util.CharmInfoHelper;
import com.masik.mythiccharms.util.NbtHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Entity.class)
public class MountainStanceEntityMixin {

    @WrapMethod(method = "addVelocity(DDD)V")
    private void mountainStance(double deltaX, double deltaY, double deltaZ, Operation<Void> original) {
        Entity entity = (Entity) (Object) this;
        if (!(entity instanceof PlayerEntity player)) {
            original.call(deltaX, deltaY, deltaZ);
            return;
        }

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "mountain_stance");
        if (entry == null) {
            original.call(deltaX, deltaY, deltaZ);
            return;
        }

        NbtCompound nbtCompound = NbtHelper.getNbtCompound(entry.itemStack(), ModDataComponentTypes.CHARM_ABILITIES);
        NbtCompound abilityCompound = nbtCompound.getCompound("mountain_stance");

        if (abilityCompound.getBoolean("require_stationary") &&
                player.getMovement().lengthSquared() > 1.0E-4) {
            original.call(deltaX, deltaY, deltaZ);
            return;
        }

        if (abilityCompound.getBoolean("ignore_knockback")) {
            AdvancementHelper.grantAdvancement(player, "mythic_charms:story/all_charm_abilities", "mountain_stance");
            return;
        }

        original.call(deltaX, deltaY, deltaZ);
    }

}
