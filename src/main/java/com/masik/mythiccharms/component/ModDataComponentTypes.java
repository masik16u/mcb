package com.masik.mythiccharms.component;

import com.masik.mythiccharms.MythicCharms;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

import java.util.function.UnaryOperator;

public class ModDataComponentTypes {

    public static final ComponentType<NbtComponent> TABLET_CONTENTS = register("tablet_contents",
            (builder) -> builder.codec(NbtComponent.CODEC));
    public static final ComponentType<NbtComponent> TABLET_GRID = register("tablet_grid",
            (builder) -> builder.codec(NbtComponent.CODEC));
    public static final ComponentType<ShapeVariantsComponent> SHAPE_VARIANTS = register("shape_variants",
            (builder) -> builder.codec(ShapeVariantsComponent.CODEC).packetCodec(ShapeVariantsComponent.PACKET_CODEC));
    public static final ComponentType<ResonanceShapeComponent> RESONANCE_SHAPE = register("resonance_shape",
            (builder) -> builder.codec(ResonanceShapeComponent.CODEC).packetCodec(ResonanceShapeComponent.PACKET_CODEC));
    public static final ComponentType<ResonanceConstraintsComponent> RESONANCE_CONSTRAINTS = register("resonance_constraints",
            (builder) -> builder.codec(ResonanceConstraintsComponent.CODEC).packetCodec(ResonanceConstraintsComponent.PACKET_CODEC));
    public static final ComponentType<ResonanceColorComponent> RESONANCE_COLOR = register("color",
            (builder) -> builder.codec(ResonanceColorComponent.CODEC).packetCodec(ResonanceColorComponent.PACKET_CODEC));
    public static final ComponentType<NbtComponent> CHARM_ABILITIES = register("charm_abilities",
            (builder) -> builder.codec(NbtComponent.CODEC));
    public static final ComponentType<NbtComponent> CHARM_COMBINATIONS = register("charm_combinations",
            (builder) -> builder.codec(NbtComponent.CODEC));
    public static final ComponentType<StonePageComponent> STONE_PAGE = register("stone_page",
            (builder) -> builder.codec(StonePageComponent.CODEC).packetCodec(StonePageComponent.PACKET_CODEC));
    public static final ComponentType<Integer> STONE_PAGE_TEXTURE = register("stone_page_texture",
            (builder) -> builder.codec(Codecs.POSITIVE_INT).packetCodec(PacketCodecs.VAR_INT));

    private static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {

        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(MythicCharms.MOD_ID, id),
                builderOperator.apply(ComponentType.builder()).build());

    }

    public static void registerModDataComponentTypes() {

    }
}
