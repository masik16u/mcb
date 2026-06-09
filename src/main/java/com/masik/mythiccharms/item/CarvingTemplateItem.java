package com.masik.mythiccharms.item;

import com.masik.mythiccharms.MythicCharms;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SmithingTemplateItem;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.resource.featuretoggle.FeatureFlag;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class CarvingTemplateItem extends SmithingTemplateItem {
    public CarvingTemplateItem(Text appliesToText, Text ingredientsText, Text titleText, Text baseSlotDescriptionText, Text additionsSlotDescriptionText, List<Identifier> emptyBaseSlotTextures, List<Identifier> emptyAdditionsSlotTextures, FeatureFlag... requiredFeatures) {
        super(appliesToText, ingredientsText, titleText, baseSlotDescriptionText, additionsSlotDescriptionText, emptyBaseSlotTextures, emptyAdditionsSlotTextures, requiredFeatures);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        if (Screen.hasAltDown()) {
            tooltip.add(Text.translatable("item.mythic_charms.carving_template.alt").setStyle(Style.EMPTY.withFont(Identifier.of(MythicCharms.MOD_ID, "script"))));
        }
        else {
            super.appendTooltip(stack, context, tooltip, type);
        }
    }
}
