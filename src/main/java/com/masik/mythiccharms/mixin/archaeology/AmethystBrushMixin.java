package com.masik.mythiccharms.mixin.archaeology;

import com.llamalad7.mixinextras.sugar.Local;
import com.masik.mythiccharms.item.ModItems;
import net.minecraft.block.entity.BrushableBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(BrushableBlockEntity.class)
public class AmethystBrushMixin {

    @ModifyConstant(method = "brush", constant = @Constant(intValue = 10))
    private int amethystBrushCount(int original, @Local(argsOnly = true) PlayerEntity player) {
        if (player.getMainHandStack().isOf(ModItems.AMETHYST_BRUSH)) {
            return 5;
        }

        return original;
    }

}
