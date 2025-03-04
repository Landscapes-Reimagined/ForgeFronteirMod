package com.landscapesreimagined.forgefrontier.ModBlocks;

import com.landscapesreimagined.forgefrontier.ForgeFrontier;
import com.landscapesreimagined.forgefrontier.ModItems.ModBlockItems.EnergeticBlazeBurnerBlockItem;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerInteractionBehaviour;
import com.simibubi.create.content.processing.burner.BlazeBurnerMovementBehaviour;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.simibubi.create.AllInteractionBehaviours.interactionBehaviour;
import static com.simibubi.create.AllMovementBehaviours.movementBehaviour;
import static com.landscapesreimagined.forgefrontier.ForgeFrontier.FORGE_FRONTIER_REGISTRATE;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

//this is a warning: I will use BOTH the DeferredRegister and Registrate, depending on what I want from the item/entry.
public class ModBlocks {
    public static final DeferredRegister<Block> MOD_BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ForgeFrontier.MODID);

    //This copies a LOT of stuff from com.simibubi.create.AllBlocks#BLAZE_BURNER
    @SuppressWarnings("removal")
    public static final BlockEntry<EnergeticBlazeBurner> ENERGETIC_BLAZE_BURNER_BLOCK =
            FORGE_FRONTIER_REGISTRATE.block("energetic_blaze_burner", EnergeticBlazeBurner::new)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.mapColor(MapColor.COLOR_GRAY).lightLevel(EnergeticBlazeBurner::getLight))
                    .transform(pickaxeOnly())
                    .addLayer(() -> RenderType::cutoutMipped)
                    .tag(AllTags.AllBlockTags.FAN_PROCESSING_CATALYSTS_BLASTING.tag, AllTags.AllBlockTags.FAN_PROCESSING_CATALYSTS_SMOKING.tag, AllTags.AllBlockTags.FAN_TRANSPARENT.tag, AllTags.AllBlockTags.PASSIVE_BOILER_HEATERS.tag)
                    .loot((lt, block) -> lt.add(block, EnergeticBlazeBurner.buildLootTable()))
                    .blockstate((c, p) -> p.simpleBlock(c.getEntry(), AssetLookup.partialBaseModel(c, p)))
                    .onRegister(movementBehaviour(new BlazeBurnerMovementBehaviour()))
                    .onRegister(interactionBehaviour(new BlazeBurnerInteractionBehaviour()))
                    .item(EnergeticBlazeBurnerBlockItem::withBlaze)
                    .model(AssetLookup.customBlockItemModel("blaze_burner", "block_with_blaze"))
                    .build()
                    .register();

    public static void register() {};
}
