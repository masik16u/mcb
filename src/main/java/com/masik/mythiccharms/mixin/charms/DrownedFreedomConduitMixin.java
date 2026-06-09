package com.masik.mythiccharms.mixin.charms;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.masik.mythiccharms.component.ModDataComponentTypes;
import com.masik.mythiccharms.util.CharmEntry;
import com.masik.mythiccharms.util.CharmInfoHelper;
import com.masik.mythiccharms.util.NbtHelper;
import net.minecraft.block.entity.ConduitBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.FluidTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ConduitBlockEntity.class)
public class DrownedFreedomConduitMixin {

    @WrapOperation(method = "givePlayersEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isTouchingWaterOrRain()Z"))
    private static boolean drownedFreedom(PlayerEntity instance, Operation<Boolean> original) {
        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(instance, "drowned_freedom");
        if (entry == null) return original.call(instance);

        NbtCompound nbtCompound = NbtHelper.getNbtCompound(entry.itemStack(), ModDataComponentTypes.CHARM_ABILITIES);
        NbtCompound abilityCompound = nbtCompound.getCompound("drowned_freedom");

        if (!abilityCompound.getBoolean("activate_conduit")) {
            return original.call(instance);
        }

        return instance.isSubmergedIn(FluidTags.WATER);
    }

}
