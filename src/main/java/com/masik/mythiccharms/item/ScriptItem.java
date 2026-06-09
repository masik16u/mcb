package com.masik.mythiccharms.item;

import com.masik.mythiccharms.MythicCharms;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ScriptItem extends Item {
    public ScriptItem(Settings settings) {
        super(settings);
    }

    @Override
    public Text getName(ItemStack stack) {
        if (Screen.hasAltDown()) {
            return Text.translatable(stack.getTranslationKey() + ".alt").setStyle(Style.EMPTY.withFont(Identifier.of(MythicCharms.MOD_ID, "script")));
        }
        return super.getName(stack);
    }
}
