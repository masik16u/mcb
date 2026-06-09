package com.masik.mythiccharms.mixin.client;

import com.masik.mythiccharms.component.ModDataComponentTypes;
import com.masik.mythiccharms.component.ResonanceColorComponent;
import com.masik.mythiccharms.component.ResonanceShapeComponent;
import com.masik.mythiccharms.util.CharmEntry;
import com.masik.mythiccharms.util.CharmInfoHelper;
import com.masik.mythiccharms.util.ModTags;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ItemRenderer.class)
public class TabletRendererMixin {

    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V"))
    private void drawTabletContents(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {

        if (!stack.isIn(ModTags.TABLETS)) return;

        List<CharmEntry> entries = CharmInfoHelper.getEquippedCharms(stack);
        if (entries.isEmpty()) return;

        matrices.push();

        matrices.scale(1F, -1F, 1F);

        MatrixStack.Entry entry = matrices.peek();
        VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getGui());

        for (CharmEntry charm : entries) {

            ResonanceShapeComponent shapeComponent =
                    charm.itemStack().get(ModDataComponentTypes.RESONANCE_SHAPE);
            ResonanceColorComponent colorComponent =
                    charm.itemStack().get(ModDataComponentTypes.RESONANCE_COLOR);

            if (colorComponent == null || shapeComponent == null) {
                continue;
            }

//            ResonanceShapeComponent shapeComponent =
//                    CharmInfoHelper.getShapeComponentOrThrow("TabletRendererMixin$drawTabletContents", charm.itemStack());
//            ResonanceColorComponent colorComponent =
//                    CharmInfoHelper.getColorComponentOrThrow("TabletRendererMixin$drawTabletContents", charm.itemStack());

            int tabletIndex = charm.index();
            // Charm item coordinates in the tablet grid
            int absoluteX = tabletIndex % 5;
            int absoluteY = tabletIndex / 5;

            // Offset from charm's shape grid to tablet grid
            int offsetX = absoluteX - shapeComponent.xOrigin();
            int offsetY = absoluteY - shapeComponent.yOrigin();

            int charmColor = colorComponent.rgb() | 0xFF000000;

            for (int i = 0; i < 25; i++) {
                if (shapeComponent.shape().charAt(i) != '1') continue;

                // Tile coordinates in the charm's shape grid
                int tileX = i % 5;
                int tileY = i / 5;

                // Coordinates in the tablet grid
                int tileAbsoluteX = offsetX + tileX;
                int tileAbsoluteY = offsetY + tileY;

                if (tileAbsoluteX < 0 || tileAbsoluteX >= 5 || tileAbsoluteY < 0 || tileAbsoluteY >= 5) continue;

                int pixelX = tileAbsoluteX + 6;
                int pixelY = tileAbsoluteY + 5;

                drawQuad(buffer, entry, pixelX, pixelY - 16, charmColor, light);
            }
        }

        matrices.pop();

    }

    @Unique
    private static void drawQuad(VertexConsumer buffer, MatrixStack.Entry entry, float x, float y, int color, int light) {
        float x1 = x / 16f;
        float y1 = y / 16f;
        float x2 = (x + 1) / 16f;
        float y2 = (y + 1) / 16f;
        float z = 0.5313f;

        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;

        buffer.vertex(entry, x1, y2, z).color(r, g, b, 1).light(light);
        buffer.vertex(entry, x2, y2, z).color(r, g, b, 1).light(light);
        buffer.vertex(entry, x2, y1, z).color(r, g, b, 1).light(light);
        buffer.vertex(entry, x1, y1, z).color(r, g, b, 1).light(light);
    }

}
