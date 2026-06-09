package com.masik.mythiccharms.mixin.archaeology;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.masik.mythiccharms.item.ModItems;
import com.masik.mythiccharms.util.AdvancementHelper;
import com.masik.mythiccharms.util.ModTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
public class TotemPlayerMixin {

    @ModifyReturnValue(method = "canHarvest", at = @At("RETURN"))
    private boolean totemOfPreservation(boolean original, @Local(argsOnly = true) BlockState state) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        ItemStack offhand = player.getOffHandStack();
        if (!player.isSneaking() && offhand.isOf(ModItems.TOTEM_OF_PRESERVATION) && state.isIn(ModTags.SUSPICIOUS_BLOCKS)) {
            offhand.damage(1, player, EquipmentSlot.OFFHAND);

            AdvancementHelper.grantAdvancement(player, "mythic_charms:story/totem_of_preservation", "impossible");
            return false;
        }
        return original;
    }

    @ModifyReturnValue(method = "getBlockBreakingSpeed", at = @At("RETURN"))
    private float totemOfPreservation(float original, @Local(argsOnly = true) BlockState state) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        ItemStack offhand = player.getOffHandStack();
        if (!player.isSneaking() && offhand.isOf(ModItems.TOTEM_OF_PRESERVATION) && state.isIn(ModTags.SUSPICIOUS_BLOCKS)) {
            return original * 0.03F;
        }
        return original;
    }

}
