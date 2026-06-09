package com.masik.mythiccharms;

import com.masik.mythiccharms.block.ModBlocks;
import com.masik.mythiccharms.component.ModDataComponentTypes;
import com.masik.mythiccharms.item.ModItemGroups;
import com.masik.mythiccharms.item.ModItems;
import com.masik.mythiccharms.screen.ModScreenHandlers;
import com.masik.mythiccharms.screen.ResonanceTableScreen;
import io.wispforest.accessories.api.client.AccessoriesRendererRegistry;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MythicCharms implements ModInitializer {
	public static final String MOD_ID = "mythic_charms";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		HandledScreens.register(ModScreenHandlers.RESONANCE_TABLE, ResonanceTableScreen::new);

		ModDataComponentTypes.registerModDataComponentTypes();
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModItemGroups.registerItemGroups();

		ModelPredicateProviderRegistry.register(
				ModItems.STONE_PAGE,
				Identifier.of(MOD_ID, "stone_page_texture"),
				(stack, world, entity, seed) -> {
					Integer value = stack.get(ModDataComponentTypes.STONE_PAGE_TEXTURE);
					return switch (value) {
                        case 1 -> 0.1F;
						case 2 -> 0.2F;
						case 3 -> 0.3F;
                        case null, default -> 0F;
                    };
				}
		);
	}


}