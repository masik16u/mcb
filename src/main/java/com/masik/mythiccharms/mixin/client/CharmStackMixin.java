package com.masik.mythiccharms.mixin.client;

import com.masik.mythiccharms.MythicCharms;
import com.masik.mythiccharms.component.ModDataComponentTypes;
import com.masik.mythiccharms.component.ResonanceShapeComponent;
import com.masik.mythiccharms.component.ShapeVariantsComponent;
import com.masik.mythiccharms.item.TokenItem;
import com.masik.mythiccharms.util.CharmInfoHelper;
import com.masik.mythiccharms.util.CharmShapeHelper;
import com.masik.mythiccharms.util.ModTags;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class CharmStackMixin {

    @Inject(method = "getName", at = @At("RETURN"), cancellable = true)
    private void addShapeToCharmName(CallbackInfoReturnable<Text> cir) {
        ItemStack itemStack = (ItemStack) (Object) this;
        ShapeVariantsComponent variantsComponent = itemStack.get(ModDataComponentTypes.SHAPE_VARIANTS);
        if (itemStack.isIn(ModTags.CHARMS) &&
                (variantsComponent == null || variantsComponent.generated()) &&
                !(itemStack.getItem() instanceof TokenItem) && itemStack.contains(ModDataComponentTypes.RESONANCE_SHAPE)) {

            ResonanceShapeComponent shapeComponent =
                    CharmInfoHelper.getShapeComponentOrThrow("CharmStackMixin$addShapeToCharmName", itemStack);

            String shape = CharmShapeHelper.centerShape(shapeComponent.shape());
            StringBuilder builder = new StringBuilder();
            builder.append((char) (0xE101));
            for (int i = 0; i < shape.length(); i++) {
                char c = shape.charAt(i);
                if (c == '1') {
                    if (i != 0) {
                        builder.append((char) (0xE100));
                    }
                    builder.append((char) (0xE000 + i));
                }
            }
            builder.append((char) (0xE101));

            Text shapeText = Text.literal(builder.toString()).setStyle(Style.EMPTY.withFont(Identifier.of(MythicCharms.MOD_ID, "charm_shape")));
            cir.setReturnValue(Text.empty().append(shape.charAt(0) != '1' ? Text.literal(" ") : Text.literal(""))
                    .append(shapeText).append(cir.getReturnValue()));
        }
    }

}
