package com.landscapesreimagined.forgefrontier.ModBlocks.ModBlockEntities;

import com.landscapesreimagined.forgefrontier.ModBlocks.ModBlocks;
import com.landscapesreimagined.forgefrontier.client.renderer.blockentities.EnergeticBlazeBurnerRenderer;
import com.landscapesreimagined.forgefrontier.client.renderer.blockentities.EnergeticBlazeBurnerVisual;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import static com.landscapesreimagined.forgefrontier.ForgeFrontier.FORGE_FRONTIER_REGISTRATE;

public class ModBlockEntities {

    public static final BlockEntityEntry<EnergeticBlazeBurnerBlockEntity> HEATER = FORGE_FRONTIER_REGISTRATE
            .blockEntity("energetic_blaze_heater", EnergeticBlazeBurnerBlockEntity::new)
            .visual(() -> EnergeticBlazeBurnerVisual::new, false)
            .validBlocks(ModBlocks.ENERGETIC_BLAZE_BURNER_BLOCK)
            .renderer(() -> EnergeticBlazeBurnerRenderer::new)
            .register();
    public static void register() {};
}
