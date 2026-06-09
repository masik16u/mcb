package com.masik.mythiccharms.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record ResonanceShapeComponent(String shape, int xOrigin, int yOrigin) {
    public static final Codec<ResonanceShapeComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("shape").forGetter(ResonanceShapeComponent::shape),
            Codec.INT.fieldOf("x_origin").forGetter(ResonanceShapeComponent::xOrigin),
            Codec.INT.fieldOf("y_origin").forGetter(ResonanceShapeComponent::yOrigin)
    ).apply(instance, ResonanceShapeComponent::new));

    public static final PacketCodec<ByteBuf, ResonanceShapeComponent> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, ResonanceShapeComponent::shape,
            PacketCodecs.INTEGER, ResonanceShapeComponent::xOrigin,
            PacketCodecs.INTEGER, ResonanceShapeComponent::yOrigin,
            ResonanceShapeComponent::new
    );
}
