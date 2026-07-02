package com.masik.mythiccharms.item;

import com.masik.mythiccharms.component.ModDataComponentTypes;
import com.masik.mythiccharms.component.ResonanceColorComponent;
import com.masik.mythiccharms.component.ResonanceConstraintsComponent;
import com.masik.mythiccharms.component.ResonanceShapeComponent;
import com.masik.mythiccharms.util.AdvancementHelper;
import com.masik.mythiccharms.util.CharmEntry;
import com.masik.mythiccharms.util.CharmInfoHelper;
import com.masik.mythiccharms.util.NbtHelper;
import io.wispforest.accessories.api.AccessoryItem;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabletItem extends AccessoryItem {
    public TabletItem(Settings properties) {
        super(properties);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        List<CharmEntry> charmEntries = CharmInfoHelper.getEquippedCharms(stack);
        for (CharmEntry entry : charmEntries) {
            tooltip.add(Text.literal("-")
                    .append(Text.translatable(entry.itemStack().getTranslationKey()))
                    .formatted(Formatting.GRAY));
        }

        super.appendTooltip(stack, context, tooltip, type);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(itemStack, world, entity, slot, selected);

        if (itemStack.contains(ModDataComponentTypes.TABLET_GRID)) {
            NbtComponent gridComponent = itemStack.get(ModDataComponentTypes.TABLET_GRID);
            if (gridComponent != null && !gridComponent.contains("rebuilt")) {
                NbtCompound compound = gridComponent.copyNbt();
                compound.put("rebuilt", NbtByte.of(true));
                NbtComponent newCombinationsComponent = NbtComponent.of(compound);
                itemStack.set(ModDataComponentTypes.TABLET_GRID, newCombinationsComponent);
            }
        }
    }
}
