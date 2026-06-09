package com.masik.mythiccharms.mixin.charms;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.masik.mythiccharms.util.AdvancementHelper;
import com.masik.mythiccharms.util.CharmEntry;
import com.masik.mythiccharms.util.CharmInfoHelper;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemEntity.class)
public class CollectorGiftItemMixin {

    @WrapMethod(method = "damage")
    private boolean collectorGift(DamageSource source, float amount, Operation<Boolean> original) {
        ItemEntity entity = (ItemEntity) (Object) this;
        Box box = Box.from(entity.getPos()).expand(16);

        boolean isSafe = !entity.getWorld().getEntitiesByType(
                TypeFilter.instanceOf(PlayerEntity.class),
                box,
                player -> {
                    CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "collector_gift");
                    if (entry == null) return original.call(source, amount);

                    NbtCompound comboCompound = CharmInfoHelper.getCombinationWithAbility(player, entry, "nullified_impact");
                    if (comboCompound == null) return original.call(source, amount);

                    float radius = comboCompound.getFloat("radius");

                    AdvancementHelper.grantAdvancement(player, "mythic_charms:story/all_charm_combos", "cg_ni");
                    return entity.squaredDistanceTo(player) < radius * radius;
                }
        ).isEmpty();
        if (isSafe) return false;

        return original.call(source, amount);
    }

    @ModifyReturnValue(method = "isFireImmune", at = @At("RETURN"))
    private boolean collectorGift(boolean original) {
        ItemEntity entity = (ItemEntity) (Object) this;
        Box box = Box.from(entity.getPos()).expand(16);

        boolean isSafe = !entity.getWorld().getEntitiesByType(
                TypeFilter.instanceOf(PlayerEntity.class),
                box,
                player -> {
                    CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "collector_gift");
                    if (entry == null) return original;

                    NbtCompound comboCompound = CharmInfoHelper.getCombinationWithAbility(player, entry, "nullified_impact");
                    if (comboCompound == null) return original;

                    float radius = comboCompound.getFloat("radius");

                    AdvancementHelper.grantAdvancement(player, "mythic_charms:story/all_charm_combos", "cg_ni");
                    return entity.squaredDistanceTo(player) < radius * radius;
                }
        ).isEmpty();
        if (isSafe) return true;

        return original;
    }

}
