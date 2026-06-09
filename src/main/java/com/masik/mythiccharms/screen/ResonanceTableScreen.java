package com.masik.mythiccharms.screen;

import com.masik.mythiccharms.MythicCharms;
import com.masik.mythiccharms.component.ModDataComponentTypes;
import com.masik.mythiccharms.component.ResonanceColorComponent;
import com.masik.mythiccharms.component.ResonanceShapeComponent;
import com.masik.mythiccharms.inventory.ResonanceGridSlot;
import com.masik.mythiccharms.util.CharmInfoHelper;
import com.masik.mythiccharms.util.ModTags;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ResonanceTableScreen extends HandledScreen<ResonanceTableScreenHandler> {

    private static final Identifier TEXTURE = Identifier.of(MythicCharms.MOD_ID, "textures/gui/resonance_table.png");
    private static final Identifier DISABLED_SLOT_TEXTURE = Identifier.ofVanilla("container/crafter/disabled_slot");

    public ResonanceTableScreen(ResonanceTableScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 176;
        this.backgroundHeight = 204;
    }

    @Override
    protected void drawSlot(DrawContext context, Slot slot) {
        if (slot instanceof ResonanceGridSlot resonanceGridSlot) {
            if (!this.handler.isSlotEnabled(slot.id)) {
                this.drawDisabledSlot(context, resonanceGridSlot);
                return;
            }
            else {
                if (!resonanceGridSlot.inventory.getStack(resonanceGridSlot.id - 1).isEmpty()) {
                    drawItemShape(context, resonanceGridSlot.x + 8, resonanceGridSlot.y + 8, resonanceGridSlot.inventory.getStack(resonanceGridSlot.id - 1));
                }
            }
        }
        super.drawSlot(context, slot);
    }

    private void drawDisabledSlot(DrawContext context, ResonanceGridSlot slot) {
        context.drawGuiTexture(DISABLED_SLOT_TEXTURE, slot.x - 1, slot.y - 1, 18, 18);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(TEXTURE, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        context.drawText(this.textRenderer, this.title, this.titleX, this.titleY, 4210752, false);
        context.drawText(this.textRenderer, this.playerInventoryTitle, this.playerInventoryTitleX, this.playerInventoryTitleY + 38, 4210752, false);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);

        ItemStack carried = this.handler.getCursorStack();
        if (!carried.isEmpty()) {
            drawItemShape(context, mouseX, mouseY, carried);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 1 && this.client != null && this.client.interactionManager != null) {
            ItemStack stackCarried = this.handler.getCursorStack();
            if (!stackCarried.isEmpty() && stackCarried.isIn(ModTags.CHARMS)) {
                this.client.interactionManager.clickButton(this.handler.syncId, 1);
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType) {
        if (button == 1 && actionType == SlotActionType.PICKUP) {
            ItemStack stackCarried = this.handler.getCursorStack();
            if (!stackCarried.isEmpty() && stackCarried.isIn(ModTags.CHARMS)) {
                return;
            }
        }
        super.onMouseClick(slot, slotId, button, actionType);
    }

    private void drawItemShape(DrawContext context, int mouseX, int mouseY, ItemStack itemStack) {
        if (itemStack == null) return;

        if (itemStack.contains(ModDataComponentTypes.RESONANCE_SHAPE)) {

            ResonanceShapeComponent shapeComponent =
                    CharmInfoHelper.getShapeComponentOrThrow("ResonanceTableScreen$drawItemShape", itemStack);
            ResonanceColorComponent colorComponent =
                    CharmInfoHelper.getColorComponentOrThrow("ResonanceTableScreen$drawItemShape", itemStack);

            List<String> shape = new ArrayList<>(Arrays.asList(shapeComponent.shape().split("")));
            int xOrigin = shapeComponent.xOrigin();
            int yOrigin = shapeComponent.yOrigin();

            for (int i = 0; i < shape.size(); i++) {
                if (!shape.get(i).equals("0")) {
                    int currentX = i % 5;
                    int currentY = i / 5;

                    int slotX = mouseX - 9 + (currentX - xOrigin) * 18;
                    int slotY = mouseY - 9 + (currentY - yOrigin) * 18;

                    // Divide 32bit integer into 0-1 rgb values
                    int color = colorComponent.rgb();
                    float r = ((color >> 16) & 0xFF) / 255.0f;
                    float g = ((color >> 8) & 0xFF) / 255.0f;
                    float b = (color & 0xFF) / 255.0f;

                    float alpha = 0.7f;

                    context.setShaderColor(r, g, b, alpha);
                    context.drawTexture(
                            Identifier.of(MythicCharms.MOD_ID, "textures/gui/disabled_slot.png"),
                            slotX, slotY,
                            0, 0, 18, 18, 18, 18);
                    context.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f); // reset
                }
            }
        }

    }
}
