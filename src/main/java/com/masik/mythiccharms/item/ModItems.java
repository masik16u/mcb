package com.masik.mythiccharms.item;

import com.masik.mythiccharms.MythicCharms;
import io.wispforest.accessories.api.client.AccessoriesRendererRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModItems {

    public static final Item AMETHYST_CORE = dummyItem("amethyst_core");
    public static final Item DIAMOND_CORE = dummyItem("diamond_core");

    public static final Item BLANK_RUNE = dummyItem("blank_rune");
    public static final Item ANCIENT_RUNE = runeItem("ancient");
    public static final Item BLAZE_RUNE = runeItem("blaze");
    public static final Item BREEZE_RUNE = runeItem("breeze");
    public static final Item ECHO_RUNE = runeItem("echo");
    public static final Item NAUTILUS_RUNE = runeItem("nautilus");
    public static final Item PHANTOM_RUNE = runeItem("phantom");
    public static final Item PRISMARINE_RUNE = runeItem("prismarine");
    public static final Item SHULKER_RUNE = runeItem("shulker");
    public static final Item SLIME_RUNE = runeItem("slime");

    public static final Item CHARM_BASE = dummyItem("charm_base");

    public static final Item ARROW_DANCE = charmItem("arrow_dance");
    public static final Item BACKWARD_DRIFT = charmItem("backward_drift");
    public static final Item BATTLE_FURY = charmItem("battle_fury");
    public static final Item BLAZING_EMBRACE = charmItem("blazing_embrace");
    public static final Item COLLECTOR_GIFT = charmItem("collector_gift");
    public static final Item DROWNED_FREEDOM = charmItem("drowned_freedom");
    public static final Item EARTH_ORDER = charmItem("earth_order");
    public static final Item ECHOING_WRATH = charmItem("echoing_wrath");
    public static final Item FEATHERED_GRACE = charmItem("feathered_grace");
    public static final Item MOUNTAIN_STANCE = charmItem("mountain_stance");
    public static final Item NIGHT_GUARDIAN = charmItem("night_guardian");
    public static final Item NULLIFIED_IMPACT = charmItem("nullified_impact");
    public static final Item SHROUDED_EXISTENCE = charmItem("shrouded_existence");
    public static final Item SUNDERING_STRIKE = charmItem("sundering_strike");
    public static final Item TWISTED_LIFEBLOOD = charmItem("twisted_lifeblood");
    public static final Item WARPED_GROUND = charmItem("warped_ground");
    public static final Item WEIGHTLESS_FLOW = charmItem("weightless_flow");

    public static final Item ARCANE_PROGRESS = tokenItem("arcane_progress");
    public static final Item CROOKED_STARE = tokenItem("crooked_stare");
    public static final Item DAYLIGHT_DREAMS = tokenItem("daylight_dreams");
    public static final Item DUSTY_CLOUD = tokenItem("dusty_cloud");
    public static final Item FELINE_WHIMSY = tokenItem("feline_whimsy");
    public static final Item INNER_LIGHT = tokenItem("inner_light");
    public static final Item NAMELESS_SHADOW = tokenItem("nameless_shadow");

    public static final Item DIAMOND_TABLET = tabletItem("diamond");
    public static final Item NETHERITE_TABLET = tabletItem("netherite");
    public static final Item STARFORGED_TABLET = tabletItem("starforged");

    public static final Item RESONANCE_INSCRIBER = registerItem("resonance_inscriber", new ResonanceInscriberItem(new Item.Settings().maxDamage(64)));
    public static final Item AMETHYST_BRUSH = registerItem("amethyst_brush", new AmethystBrushItem(new Item.Settings().maxDamage(96)));
    public static final Item TOTEM_OF_PRESERVATION = registerItem("totem_of_preservation", new Item(new Item.Settings().maxDamage(128)));
//    public static final Item RESONANCE_KEY = dummyItem("resonance_key");
    public static final Item PUZZLE_BOX = registerItem("puzzle_box", new PuzzleBoxItem(new Item.Settings().maxCount(1)));
    public static final Item PUZZLE_PIECE = registerItem("puzzle_piece", new PuzzlePieceItem(new Item.Settings()));
    public static final Item STONE_PAGE = registerItem("stone_page", new StonePageItem(new Item.Settings()));


    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(MythicCharms.MOD_ID, name), item);
    }

    public static void registerModItems() {
        AccessoriesRendererRegistry.registerNoRenderer(ModItems.DIAMOND_TABLET);
        AccessoriesRendererRegistry.registerNoRenderer(ModItems.NETHERITE_TABLET);
        AccessoriesRendererRegistry.registerNoRenderer(ModItems.STARFORGED_TABLET);
    }


    private static Item dummyItem(String name) {
        return registerItem(name, new Item(new Item.Settings()));
    }

    private static Item charmItem(String name) {
        return registerItem("charm_of_" + name, new CharmItem(new Item.Settings().maxCount(1)));
    }

    private static Item tokenItem(String name) {
        return registerItem("token_of_" + name, new TokenItem(new Item.Settings().maxCount(1)));
    }

    private static Item runeItem(String name) {
        return registerItem(name + "_rune", new ScriptItem(new Item.Settings()));
    }

    private static Item tabletItem(String name) {
        return registerItem(name + "_tablet", new TabletItem(new Item.Settings().maxCount(1)));
    }


    public static final List<Item> TABLETS = List.of(
            DIAMOND_TABLET,
            NETHERITE_TABLET,
            STARFORGED_TABLET
    );

    public static final List<Item> RUNES = List.of(
            ANCIENT_RUNE,
            BLAZE_RUNE,
            BREEZE_RUNE,
            ECHO_RUNE,
            NAUTILUS_RUNE,
            PHANTOM_RUNE,
            PRISMARINE_RUNE,
            SHULKER_RUNE,
            SLIME_RUNE
    );
    public static final Map<Item, Item> RUNE_TO_MATERIAL = Map.of(
            ANCIENT_RUNE, Items.NETHERITE_SCRAP,
            BLAZE_RUNE, Items.BLAZE_ROD,
            BREEZE_RUNE, Items.BREEZE_ROD,
            ECHO_RUNE, Items.ECHO_SHARD,
            NAUTILUS_RUNE, Items.NAUTILUS_SHELL,
            PHANTOM_RUNE, Items.PHANTOM_MEMBRANE,
            PRISMARINE_RUNE, Items.PRISMARINE_SHARD,
            SHULKER_RUNE, Items.SHULKER_SHELL,
            SLIME_RUNE, Items.SLIME_BALL
    );

    public static final List<Item> CHARMS = List.of(
            ARROW_DANCE,
            BACKWARD_DRIFT,
            BATTLE_FURY,
            BLAZING_EMBRACE,
            COLLECTOR_GIFT,
            DROWNED_FREEDOM,
            EARTH_ORDER,
            ECHOING_WRATH,
            FEATHERED_GRACE,
            MOUNTAIN_STANCE,
            NIGHT_GUARDIAN,
            NULLIFIED_IMPACT,
            SHROUDED_EXISTENCE,
            SUNDERING_STRIKE,
            TWISTED_LIFEBLOOD,
            WARPED_GROUND,
            WEIGHTLESS_FLOW
    );
    public static final Map<Item, List<Item>> CHARM_TO_RUNES = Util.make(new HashMap<>(), map -> {
        map.put(WARPED_GROUND, List.of(BREEZE_RUNE, PRISMARINE_RUNE));
        map.put(NIGHT_GUARDIAN, List.of(PHANTOM_RUNE, NAUTILUS_RUNE));
        map.put(COLLECTOR_GIFT, List.of(ANCIENT_RUNE, ECHO_RUNE));
        map.put(BATTLE_FURY, List.of(PRISMARINE_RUNE, BLAZE_RUNE));
        map.put(ARROW_DANCE, List.of(SLIME_RUNE, PHANTOM_RUNE));
        map.put(MOUNTAIN_STANCE, List.of(SHULKER_RUNE, NAUTILUS_RUNE));
        map.put(NULLIFIED_IMPACT, List.of(BLAZE_RUNE, NAUTILUS_RUNE));
        map.put(EARTH_ORDER, List.of(ANCIENT_RUNE, NAUTILUS_RUNE));
        map.put(BLAZING_EMBRACE, List.of(BLAZE_RUNE, SLIME_RUNE));
        map.put(SHROUDED_EXISTENCE, List.of(PHANTOM_RUNE, ECHO_RUNE, PRISMARINE_RUNE));
        map.put(SUNDERING_STRIKE, List.of(SLIME_RUNE, BREEZE_RUNE));
        map.put(DROWNED_FREEDOM, List.of(ANCIENT_RUNE, PRISMARINE_RUNE, NAUTILUS_RUNE));
        map.put(ECHOING_WRATH, List.of(ECHO_RUNE, BLAZE_RUNE));
        map.put(WEIGHTLESS_FLOW, List.of(ANCIENT_RUNE, SHULKER_RUNE, BREEZE_RUNE));
        map.put(BACKWARD_DRIFT, List.of(ANCIENT_RUNE, SHULKER_RUNE, ECHO_RUNE));
        map.put(FEATHERED_GRACE, List.of(PHANTOM_RUNE, BREEZE_RUNE, ECHO_RUNE));
        map.put(TWISTED_LIFEBLOOD, List.of(ANCIENT_RUNE, PHANTOM_RUNE, BLAZE_RUNE));
    });

    public static final List<Item> TOKENS = List.of(
            ARCANE_PROGRESS,
            CROOKED_STARE,
            DAYLIGHT_DREAMS,
            DUSTY_CLOUD,
            FELINE_WHIMSY,
            INNER_LIGHT,
            NAMELESS_SHADOW
    );

    public static final Identifier EMPTY_SLOT_RUNE_TEXTURE = Identifier.of(MythicCharms.MOD_ID, "item/empty_slot_rune");
    public static final Identifier EMPTY_SLOT_TABLET_TEXTURE = Identifier.of(MythicCharms.MOD_ID, "item/empty_slot_tablet");
    public static final Identifier EMPTY_SLOT_BRUSH_TEXTURE = Identifier.of(MythicCharms.MOD_ID, "item/empty_slot_brush");

    public static final Identifier EMPTY_SLOT_AMETHYST_CORE_TEXTURE = Identifier.of(MythicCharms.MOD_ID, "item/empty_slot_amethyst_core");
    public static final Identifier EMPTY_SLOT_ECHO_SHARD_TEXTURE = Identifier.of(MythicCharms.MOD_ID, "item/empty_slot_echo_shard");
    public static final Identifier EMPTY_SLOT_NAUTILUS_SHELL_TEXTURE = Identifier.of(MythicCharms.MOD_ID, "item/empty_slot_nautilus_shell");
    public static final Identifier EMPTY_SLOT_NETHERITE_SCRAP_TEXTURE = Identifier.of(MythicCharms.MOD_ID, "item/empty_slot_netherite_scrap");
    public static final Identifier EMPTY_SLOT_PHANTOM_MEMBRANE_TEXTURE = Identifier.of(MythicCharms.MOD_ID, "item/empty_slot_phantom_membrane");
    public static final Identifier EMPTY_SLOT_PRISMARINE_SHARD_TEXTURE = Identifier.of(MythicCharms.MOD_ID, "item/empty_slot_prismarine_shard");
    public static final Identifier EMPTY_SLOT_ROD_TEXTURE = Identifier.of(MythicCharms.MOD_ID, "item/empty_slot_rod");
    public static final Identifier EMPTY_SLOT_SHULKER_SHELL_TEXTURE = Identifier.of(MythicCharms.MOD_ID, "item/empty_slot_shulker_shell");
    public static final Identifier EMPTY_SLOT_SLIMEBALL_TEXTURE = Identifier.of(MythicCharms.MOD_ID, "item/empty_slot_slimeball");
    public static final Identifier EMPTY_SLOT_NETHER_STAR_TEXTURE = Identifier.of(MythicCharms.MOD_ID, "item/empty_slot_nether_star");

    public static final List<Identifier> EMPTY_SLOT_ADDITION_TEXTURES = List.of(
            EMPTY_SLOT_AMETHYST_CORE_TEXTURE,
            EMPTY_SLOT_ECHO_SHARD_TEXTURE,
            EMPTY_SLOT_NAUTILUS_SHELL_TEXTURE,
            EMPTY_SLOT_NETHERITE_SCRAP_TEXTURE,
            EMPTY_SLOT_PHANTOM_MEMBRANE_TEXTURE,
            EMPTY_SLOT_PRISMARINE_SHARD_TEXTURE,
            EMPTY_SLOT_ROD_TEXTURE,
            EMPTY_SLOT_SHULKER_SHELL_TEXTURE,
            EMPTY_SLOT_SLIMEBALL_TEXTURE,
            EMPTY_SLOT_NETHER_STAR_TEXTURE
    );

    public static final Item CARVING_TEMPLATE = registerItem("carving_template",
            new CarvingTemplateItem(
                    Text.translatable("item.mythic_charms.carving_template.applies_to").formatted(Formatting.BLUE),
                    Text.translatable("item.mythic_charms.carving_template.ingredients").formatted(Formatting.BLUE),
                    Text.translatable("item.mythic_charms.resonance_carving").formatted(Formatting.GRAY),
                    Text.translatable("item.mythic_charms.carving_template.base_slot_description"),
                    Text.translatable("item.mythic_charms.carving_template.additions_slot_description"),
                    List.of(EMPTY_SLOT_RUNE_TEXTURE, EMPTY_SLOT_TABLET_TEXTURE, EMPTY_SLOT_BRUSH_TEXTURE),
                    EMPTY_SLOT_ADDITION_TEXTURES
            ));

}
