package com.landscapesreimagined.forgefrontier.ModBlocks.ModBlockEntities;

import com.landscapesreimagined.forgefrontier.ModBlocks.EnergeticBlazeBurner;
import com.landscapesreimagined.forgefrontier.ModBlocks.ModBlocks;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import static com.landscapesreimagined.forgefrontier.ForgeFrontier.FORGE_FRONTIER_REGISTRATE;
import static com.simibubi.create.Create.REGISTRATE;

public class ModBlockEntities {

    public static final BlockEntityEntry<EnergeticBlazeBurnerBlockEntity> HEATER = FORGE_FRONTIER_REGISTRATE
            .blockEntity("energetic_blaze_heater", EnergeticBlazeBurnerBlockEntity::new)
            .validBlocks(ModBlocks.ENERGETIC_BLAZE_BURNER_BLOCK)
            .renderer(() -> BlazeBurnerRenderer::new)
            .register();
    public static void register() {};
}
