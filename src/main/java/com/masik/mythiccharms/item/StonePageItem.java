package com.masik.mythiccharms.item;

import com.masik.mythiccharms.MythicCharms;
import com.masik.mythiccharms.component.ModDataComponentTypes;
import com.masik.mythiccharms.component.StonePageComponent;
import com.masik.mythiccharms.util.TextHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.List;

public class StonePageItem extends Item {
    public StonePageItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);

        if (stack.contains(ModDataComponentTypes.STONE_PAGE)) {
            StonePageComponent component = stack.get(ModDataComponentTypes.STONE_PAGE);
            if (component == null) return;

            String contentKey = component.content();

            if (Screen.hasAltDown()) {
                for (String part : Text.translatable(stack.getTranslationKey() + "." + contentKey + ".alt").getString().split("\\.\\.")) {
                    tooltip.add(Text.literal(part)
                            .setStyle(Style.EMPTY.withFont(Identifier.of(MythicCharms.MOD_ID, "script")))
                            .formatted(Formatting.GRAY));
                }
            }
            else {
                if (!component.hidden()) {
                    List<String> descLines = TextHelper.wrapText(
                            Text.translatable(stack.getTranslationKey() + "." + contentKey).getString(), 28);

                    for (String line : descLines) {
                        tooltip.add(Text.literal(line).formatted(Formatting.GRAY));
                    }
                }
                else {
                    List<String> descLines = TextHelper.wrapText(
                            Text.translatable("util.mythic_charms.not_advanced_tooltip").getString(), 28);

                    for (String line : descLines) {
                        tooltip.add(Text.literal(line).formatted(Formatting.GRAY));
                    }
                }
            }
        }
    }
}
