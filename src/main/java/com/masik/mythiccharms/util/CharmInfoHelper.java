package com.masik.mythiccharms.util;

import com.masik.mythiccharms.component.*;
import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.api.slot.SlotEntryReference;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class CharmInfoHelper {

    public static List<CharmEntry> getEquippedCharms(ItemStack tablet) {
        if (MinecraftClient.getInstance().getNetworkHandler() != null) {
            return getEquippedCharms(tablet, MinecraftClient.getInstance().getNetworkHandler().getRegistryManager());
        }
        return List.of();
    }

    public static List<CharmEntry> getEquippedCharms(ItemStack tablet, DynamicRegistryManager manager) {

        if (!tablet.isIn(ModTags.TABLETS)) return List.of();
        if (!tablet.contains(ModDataComponentTypes.TABLET_CONTENTS)) return List.of();

        List<CharmEntry> charms = new ArrayList<>();

        NbtCompound nbtCompound = NbtHelper.getNbtCompound(tablet, ModDataComponentTypes.TABLET_CONTENTS);

        for (int i = 0; i < 25; i++) {
            String key = "grid_slot_" + i;
            if (!nbtCompound.contains(key)) continue;

            ItemStack itemStack = ItemStack.EMPTY;
            if (manager != null) {
                itemStack = ItemStack.fromNbt(manager,
                        nbtCompound.get(key)).orElse(ItemStack.EMPTY);
            }

            if (!itemStack.isEmpty()) {
                charms.add(new CharmEntry(itemStack, i));
            }
        }

        return charms;
    }

    @Nullable
    public static CharmEntry getCharmWithAbility(List<CharmEntry> charms, String ability) {
        for (CharmEntry charm : charms) {
            if (!charm.itemStack().contains(ModDataComponentTypes.CHARM_ABILITIES)) continue;

            NbtCompound nbtCompound = NbtHelper.getNbtCompound(charm.itemStack(), ModDataComponentTypes.CHARM_ABILITIES);

            if (nbtCompound.contains(ability)) {
                return charm;
            }

        }
        return null;
    }

    @Nullable
    public static CharmEntry getCharmWithAbility(LivingEntity livingEntity, String ability) {

        if (livingEntity.accessoriesCapability() != null) {
            SlotEntryReference slot = Objects.requireNonNull(livingEntity.accessoriesCapability())
                    .getAllEquipped()
                    .stream()
                    .filter(a -> a.stack().isIn(ModTags.TABLETS))
                    .findFirst().orElse(null);

            if (slot != null) {
                return getCharmWithAbility(getEquippedCharms(slot.stack(), livingEntity.getWorld().getRegistryManager()), ability);
            }
        }

        return null;
    }

    @Nullable
    public static NbtCompound getCombinationWithAbility(LivingEntity livingEntity, CharmEntry mainCharm, String ability) {
        CharmEntry secondaryCharm = getCharmWithAbility(livingEntity, ability);
        if (secondaryCharm == null) return null;

        NbtCompound nbtCompound = getCombinationWithAbility(mainCharm.itemStack(), ability);
        if (nbtCompound == null) return null;

        if (nbtCompound.getBoolean("require_adjacent") && !shapesTouch(mainCharm, secondaryCharm)) {
            return null;
        }

        return nbtCompound;
    }

    @Nullable
    public static NbtCompound getCombinationWithAbilityReverse(LivingEntity livingEntity, String ability, CharmEntry secondaryCharm) {
        CharmEntry mainCharm = getCharmWithAbility(livingEntity, ability);
        if (mainCharm == null) return null;

        NbtCompound nbtCompound = getCombinationWithAbility(mainCharm.itemStack(), ability);
        if (nbtCompound == null) return null;

        if (nbtCompound.getBoolean("require_adjacent") && !shapesTouch(mainCharm, secondaryCharm)) {
            return null;
        }

        return nbtCompound;
    }

    @Nullable
    private static NbtCompound getCombinationWithAbility(ItemStack charm, String ability) {
        if (!charm.contains(ModDataComponentTypes.CHARM_COMBINATIONS)) return null;

        NbtCompound nbtCompound = NbtHelper.getNbtCompound(charm, ModDataComponentTypes.CHARM_COMBINATIONS);

        if (nbtCompound.contains(ability)) {
            return nbtCompound.getCompound(ability).copy();
        }

        return null;
    }

    private static boolean shapesTouch(CharmEntry a, CharmEntry b) {
        int ax = a.index() % 5;
        int ay = a.index() / 5;

        int bx = b.index() % 5;
        int by = b.index() / 5;

        if (a.itemStack() == null || b.itemStack() == null) return false;
        ResonanceShapeComponent shapeA = getShapeComponentOrThrow("CharmInfoHelper$shapesTouch", a.itemStack());
        ResonanceShapeComponent shapeB = getShapeComponentOrThrow("CharmInfoHelper$shapesTouch", b.itemStack());

        int aOffsetX = ax - shapeA.xOrigin();
        int aOffsetY = ay - shapeA.yOrigin();
        int bOffsetX = bx - shapeB.xOrigin();
        int bOffsetY = by - shapeB.yOrigin();

        ArrayList<Point> aPoints = new ArrayList<>();
        ArrayList<Point> bPoints = new ArrayList<>();

        for (int i = 0; i < 25; i++) {
            if (shapeA.shape().charAt(i) == '1') {
                aPoints.add(new Point(i % 5 + aOffsetX, i / 5 + aOffsetY));
            }
            if (shapeB.shape().charAt(i) == '1') {
                bPoints.add(new Point(i % 5 + bOffsetX, i / 5 + bOffsetY));
            }
        }

        for (Point pa : aPoints) {
            for (Point pb : bPoints) {

                int dx = Math.abs(pa.x - pb.x);
                int dy = Math.abs(pa.y - pb.y);

                if ((dx == 1 && dy == 0) || (dx == 0 && dy == 1)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static ResonanceShapeComponent getShapeComponentOrThrow(String method, ItemStack itemStack) {
        return Objects.requireNonNull(itemStack.get(ModDataComponentTypes.RESONANCE_SHAPE),
                "[Mythic Charms] ResonanceShapeComponent is missing: Charm - " +
                        itemStack.getTranslationKey() + " Method - " + method);
    }

    public static ResonanceConstraintsComponent getConstraintsComponentOrThrow(String method, ItemStack itemStack) {
        return Objects.requireNonNull(itemStack.get(ModDataComponentTypes.RESONANCE_CONSTRAINTS),
                "[Mythic Charms] ResonanceConstraintsComponent is missing: Charm - " +
                        itemStack.getTranslationKey() + " Method - " + method);
    }

    public static ResonanceColorComponent getColorComponentOrThrow(String method, ItemStack itemStack) {
//        ResonanceColorComponent colorComponent = itemStack.get(ModDataComponentTypes.RESONANCE_COLOR);
//        if (colorComponent == null) {
//            return new ResonanceColorComponent(9820250);
//        }
        return Objects.requireNonNull(itemStack.get(ModDataComponentTypes.RESONANCE_COLOR),
                "[Mythic Charms] ResonanceColorComponent is missing: Charm - " +
                        itemStack.getTranslationKey() + " Method - " + method);
    }

}
