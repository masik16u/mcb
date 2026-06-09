package com.masik.mythiccharms.item;

import com.masik.mythiccharms.MythicCharms;
import com.masik.mythiccharms.component.ModDataComponentTypes;
import com.masik.mythiccharms.component.ResonanceShapeComponent;
import com.masik.mythiccharms.util.AdvancementHelper;
import com.masik.mythiccharms.util.CharmEntry;
import com.masik.mythiccharms.util.CharmInfoHelper;
import com.masik.mythiccharms.util.NbtHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PuzzleBoxItem extends Item {
    public PuzzleBoxItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack box = player.getStackInHand(hand);

        NbtCompound nbtCompound = NbtHelper.getNbtCompound(box, ModDataComponentTypes.TABLET_GRID);

        if (!isFull(box, world) || !nbtCompound.contains("puzzle_box_loot")) {
            return TypedActionResult.fail(box);
        }

        if (!world.isClient && world.getServer() != null) {
            LootTable lootTable = world.getServer().getReloadableRegistries()
                    .getLootTable(RegistryKey.of(RegistryKeys.LOOT_TABLE,
                            Identifier.of(nbtCompound.getString("puzzle_box_loot"))));
            LootContextParameterSet contextParameterSet = new LootContextParameterSet.Builder((ServerWorld) world)
                    .add(LootContextParameters.ORIGIN, player.getPos())
                    .add(LootContextParameters.THIS_ENTITY, player)
                    .build(LootContextTypes.GIFT);
            lootTable.generateLoot(contextParameterSet, itemStack -> player.dropItem(itemStack, false));

            if (!player.isCreative()) {
                box.decrement(1);
            }

            world.playSound(null, player.getBlockPos(),
                    SoundEvents.BLOCK_TUFF_BREAK,
                    SoundCategory.PLAYERS, 1f, 1.3f);

            AdvancementHelper.grantAdvancement(player, "mythic_charms:story/puzzle_box", "impossible");
        }

        return TypedActionResult.success(box, world.isClient());
    }

    public static boolean isFull(ItemStack itemStack, World world) {
        NbtCompound nbtCompound = NbtHelper.getNbtCompound(itemStack, ModDataComponentTypes.TABLET_GRID);
        List<String> matrix = Arrays.asList(nbtCompound.getString("shape").split(""));

        List<CharmEntry> pieces = CharmInfoHelper.getEquippedCharms(itemStack, world.getRegistryManager());
        for (CharmEntry entry : pieces) {
            int stackX = entry.index() % 5;
            int stackY = entry.index() / 5;

            ResonanceShapeComponent shapeComponent =
                    CharmInfoHelper.getShapeComponentOrThrow(
                            "ResonanceTableScreenHandler$getMatrix", entry.itemStack());

            List<String> shape = new ArrayList<>(Arrays.asList(shapeComponent.shape().split("")));
            int xOrigin = shapeComponent.xOrigin();
            int yOrigin = shapeComponent.yOrigin();

            for (int k = 0; k < shape.size(); k++) {
                if (!shape.get(k).equals("0")) {
                    int offsetX = (k % 5) - xOrigin;
                    int offsetY = (k / 5) - yOrigin;

                    matrix.set((stackY + offsetY) * 5 + (stackX + offsetX), "1");
                }
            }
        }

        return !matrix.contains("0");
    }
}
