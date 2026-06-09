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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class NullifiedImpactEntityMixin {

    @ModifyReturnValue(method = "canExplosionDestroyBlock", at = @At("RETURN"))
    private boolean nullifiedImpact(boolean original) {

        Entity entity = (Entity) (Object) this;
        Box box = Box.from(entity.getPos()).expand(16);

        boolean isSafe = !entity.getWorld().getEntitiesByType(
                TypeFilter.instanceOf(PlayerEntity.class),
                box,
                player -> {
                    CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "nullified_impact");
                    if (entry == null) return false;

                    NbtCompound nbtCompound = NbtHelper.getNbtCompound(entry.itemStack(), ModDataComponentTypes.CHARM_ABILITIES);
                    NbtCompound abilityCompound = nbtCompound.getCompound("nullified_impact");

                    float radius = abilityCompound.getFloat("radius");

                    AdvancementHelper.grantAdvancement(player, "mythic_charms:story/all_charm_abilities", "nullified_impact");
                    return entity.squaredDistanceTo(player) < radius * radius;
                }
        ).isEmpty();
        if (isSafe) return false;

        return original;
    }

}
