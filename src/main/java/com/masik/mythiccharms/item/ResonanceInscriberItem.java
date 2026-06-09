package com.masik.mythiccharms.item;

import com.masik.mythiccharms.MythicCharms;
import com.masik.mythiccharms.component.ModDataComponentTypes;
import com.masik.mythiccharms.component.ResonanceConstraintsComponent;
import com.masik.mythiccharms.component.ResonanceShapeComponent;
import com.masik.mythiccharms.util.AdvancementHelper;
import com.masik.mythiccharms.util.CharmInfoHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ResonanceInscriberItem extends Item {
    public ResonanceInscriberItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasRecipeRemainder() {
        return true;
    }

    @Override
    public ItemStack getRecipeRemainder(ItemStack itemStack) {
        ItemStack copy = itemStack.copy();
        int damage = itemStack.getDamage() + 1;

        if (damage >= itemStack.getMaxDamage()) {
            return ItemStack.EMPTY;
        }
        else {
            copy.setDamage(copy.getDamage() + 1);
            return copy;
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack inscriberStack = player.getStackInHand(hand);

        if (hand != Hand.MAIN_HAND) {
            return TypedActionResult.pass(inscriberStack);
        }

        ItemStack offhand = player.getOffHandStack();
        if (!(offhand.getItem() instanceof CharmItem)) {
            return TypedActionResult.pass(inscriberStack);
        }

        ResonanceConstraintsComponent constraintsComponent =
                CharmInfoHelper.getConstraintsComponentOrThrow(
                        "ResonanceInscriberItem$use", offhand);
        if (!constraintsComponent.isQuickReforgeable()) {
            return TypedActionResult.pass(inscriberStack);
        }

        int materialSlot = findRequiredMaterial(player, constraintsComponent);
        if (materialSlot == -1) {
            return TypedActionResult.pass(inscriberStack);
        }

        if (!world.isClient) {
            ItemStack core = player.getInventory().getStack(materialSlot);
            core.decrementUnlessCreative(1, player);

            player.getMainHandStack().damage(1, player, EquipmentSlot.MAINHAND);

            ResonanceShapeComponent newShapeComponent = CharmItem.newRandomShape(offhand, world, true);
            offhand.set(ModDataComponentTypes.RESONANCE_SHAPE, newShapeComponent);

            world.playSound(null, player.getBlockPos(),
                    SoundEvents.BLOCK_GRINDSTONE_USE,
                    SoundCategory.PLAYERS, 1f, 1.3f);

            player.sendMessage(offhand.getName(), true);

            AdvancementHelper.grantAdvancement(player, "mythic_charms:story/charm_quick_reshape", "impossible");
        }

        return TypedActionResult.success(inscriberStack, world.isClient());
    }

    private int findRequiredMaterial(PlayerEntity player, ResonanceConstraintsComponent constraintsComponent) {
        Identifier id = Identifier.tryParse(constraintsComponent.requiredMaterialTag());
        if (id == null) return -1;

        TagKey<Item> tag = TagKey.of(RegistryKeys.ITEM, id);
        PlayerInventory inventory = player.getInventory();
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.getStack(i).isIn(tag)) {
                return i;
            }
        }
        return -1;
    }
}
