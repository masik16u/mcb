package com.masik.mythiccharms.block;

import com.masik.mythiccharms.screen.ResonanceTableScreenHandler;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.VaultBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ResonanceTableBlock extends Block {

    public static final MapCodec<ResonanceTableBlock> CODEC = createCodec(ResonanceTableBlock::new);
    private static final Text TITLE = Text.translatable("container.resonance_table");

    public MapCodec<? extends ResonanceTableBlock> getCodec() {
        return CODEC;
    }

    public ResonanceTableBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        player.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                ((syncId, playerInventory, player1) -> new ResonanceTableScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(world, pos))),
                TITLE
        ));
        return ActionResult.CONSUME;
    }

    @Nullable
    @Override
    protected NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedScreenHandlerFactory(
                ((syncId, playerInventory, player) -> new ResonanceTableScreenHandler(syncId, playerInventory)),
                TITLE
        );
    }
}
