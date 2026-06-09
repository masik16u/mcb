package com.masik.mythiccharms.mixin.charms;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.masik.mythiccharms.util.AdvancementHelper;
import com.masik.mythiccharms.util.CharmEntry;
import com.masik.mythiccharms.util.CharmInfoHelper;
import net.minecraft.block.Block;
import net.minecraft.block.TripwireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(Block.class)
public class EarthOrderBlockMixin {

    @ModifyReturnValue(method = "getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)Ljava/util/List;", at = @At("RETURN"))
    private static List<ItemStack> earthOrder(List<ItemStack> original, @Local(argsOnly = true) ServerWorld world, @Local(argsOnly = true) Entity entity) {
        if (!(entity instanceof PlayerEntity player)) return original;

        CharmEntry entry = CharmInfoHelper.getCharmWithAbility(player, "earth_order");
        if (entry == null) return original;

        NbtCompound comboCompound = CharmInfoHelper.getCombinationWithAbility(player, entry, "blazing_embrace");
        if (comboCompound == null ||
                (comboCompound.getBoolean("require_barehanded") && player.getMainHandStack().isDamageable())) {
            return original;
        }

        List<ItemStack> list = new ArrayList<>();

        original.forEach(itemStack -> {
            Optional<RecipeEntry<SmeltingRecipe>> recipeEntry =
                    world.getRecipeManager().getFirstMatch(RecipeType.SMELTING,
                            new SingleStackRecipeInput(itemStack), world);
            if (recipeEntry.isPresent()) {
                ItemStack result = recipeEntry.get().value().getResult(world.getRegistryManager());
                result.setCount(itemStack.getCount());
                list.add(result);
            }
            else {
                list.add(itemStack);
            }
        });

        AdvancementHelper.grantAdvancement(player, "mythic_charms:story/all_charm_combos", "eo_be");

        List<Item> items = list.stream().map(ItemStack::getItem).distinct().toList();
        if (items.contains(Items.CHARCOAL)) {
            AdvancementHelper.grantAdvancement(player, "mythic_charms:story/earth_order_blazing", "charcoal");
        }
        if (items.contains(Items.GREEN_DYE)) {
            AdvancementHelper.grantAdvancement(player, "mythic_charms:story/earth_order_blazing", "green_dye");
        }
        if (items.contains(Items.LIME_DYE)) {
            AdvancementHelper.grantAdvancement(player, "mythic_charms:story/earth_order_blazing", "lime_dye");
        }
        if (items.contains(Items.POPPED_CHORUS_FRUIT)) {
            AdvancementHelper.grantAdvancement(player, "mythic_charms:story/earth_order_blazing", "chorus");
        }

        return list;
    }



}
