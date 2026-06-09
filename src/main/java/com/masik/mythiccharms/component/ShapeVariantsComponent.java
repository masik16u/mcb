package com.masik.mythiccharms.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

import java.util.List;

public record ShapeVariantsComponent(List<ResonanceShapeComponent> variants, boolean generated) {
    public static final Codec<ShapeVariantsComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResonanceShapeComponent.CODEC.listOf().fieldOf("variants").forGetter(ShapeVariantsComponent::variants),
            Codec.BOOL.optionalFieldOf("generated", false).forGetter(ShapeVariantsComponent::generated)
    ).apply(instance, ShapeVariantsComponent::new));
//    public static final Codec<ShapeVariantsComponent> CODEC =
//            ResonanceShapeComponent.CODEC.listOf().xmap(
//                    ShapeVariantsComponent::new,
//                    ShapeVariantsComponent::variants
//            );

    public static final PacketCodec<ByteBuf, ShapeVariantsComponent> PACKET_CODEC = PacketCodec.tuple(
            ResonanceShapeComponent.PACKET_CODEC.collect(PacketCodecs.toList()), ShapeVariantsComponent::variants,
            PacketCodecs.BOOL, ShapeVariantsComponent::generated,
            ShapeVariantsComponent::new
    );
//    public static final PacketCodec<ByteBuf, ShapeVariantsComponent> PACKET_CODEC =
//            ResonanceShapeComponent.PACKET_CODEC.collect(PacketCodecs.toList()).xmap(
//                    ShapeVariantsComponent::new,
//                    ShapeVariantsComponent::variants
//            );
}

