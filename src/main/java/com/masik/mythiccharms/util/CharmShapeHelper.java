package com.masik.mythiccharms.util;

import com.masik.mythiccharms.component.ModDataComponentTypes;
import com.masik.mythiccharms.component.ResonanceShapeComponent;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CharmShapeHelper {

    public static void rotateShapeComponent(ItemStack itemStack) {
        if (itemStack == null) return;
        ResonanceShapeComponent shapeComponent = CharmInfoHelper.getShapeComponentOrThrow("rotateShapeComponent", itemStack);

        String shape = rotateShapeMatrix(shapeComponent.shape());
        int xOrigin = 4 - shapeComponent.yOrigin();
        int yOrigin = shapeComponent.xOrigin();

        itemStack.set(ModDataComponentTypes.RESONANCE_SHAPE, new ResonanceShapeComponent(shape, xOrigin, yOrigin, true));
    }

    public static String rotateShapeMatrix(String shape) {
        List<String> originalShape = new ArrayList<>(Arrays.asList(shape.split("")));
        List<String> rotatedShape = new ArrayList<>(Arrays.asList(new String[25]));
        for (int i = 0; i < 25; i++) rotatedShape.set(i, "0"); // initialize

        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                rotatedShape.set(
                        (4 - y) + (x * 5),
                        originalShape.get(x + y * 5)
                );
            }
        }

        return String.join("", rotatedShape);
    }

    public static String centerShape(String shape) {
        List<String> originalShape = new ArrayList<>(Arrays.asList(shape.split("")));
        List<String> movedShape = new ArrayList<>(Arrays.asList(new String[25]));
        for (int i = 0; i < 25; i++) movedShape.set(i, "0");

        int minX = 5, maxX = -1, minY = 5, maxY = -1;

        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                if (originalShape.get(y * 5 + x).equals("1")) {
                    minX = Math.min(minX, x);
                    maxX = Math.max(maxX, x);
                    minY = Math.min(minY, y);
                    maxY = Math.max(maxY, y);
                }
            }
        }

        int offsetX = (int) Math.ceil((minX + 4 - maxX) / 2.0) - minX;
        int offsetY = (minY + 4 - maxY) / 2 - minY;

        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                if (originalShape.get(y * 5 + x).equals("1")) {
                    movedShape.set((y + offsetY) * 5 + (x + offsetX), "1");
                }
            }
        }

        return String.join("", movedShape);
    }

}
