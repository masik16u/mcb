package com.masik.mythiccharms.screen;

import com.masik.mythiccharms.MythicCharms;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {

    public static final ScreenHandlerType<ResonanceTableScreenHandler> RESONANCE_TABLE =
            registerScreenHandler("resonance_table", ResonanceTableScreenHandler::new);

    public static <T extends ScreenHandler> ScreenHandlerType<T> registerScreenHandler(String id, ScreenHandlerType.Factory<T> factory) {
        return Registry.register(Registries.SCREEN_HANDLER,
                Identifier.of(MythicCharms.MOD_ID, id),
                new ScreenHandlerType<>(factory, FeatureFlags.VANILLA_FEATURES));
    }

    public static void registerScreenHandlers() {

    }
}
