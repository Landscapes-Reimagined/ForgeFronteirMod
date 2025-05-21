package com.landscapesreimagined.forgefrontier.ponder;

import com.landscapesreimagined.forgefrontier.ForgeFrontier;
import com.landscapesreimagined.forgefrontier.ModBlocks.ModBlocks;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class ForgeFrontierPonders {



    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        HELPER.addStoryBoard(ModBlocks.ENERGETIC_BLAZE_BURNER_BLOCK, "energize_main", EnergeticBlazeBurnerPonder::introScene);
        HELPER.addStoryBoard(ModBlocks.ENERGETIC_BLAZE_BURNER_BLOCK, "energize_main", EnergeticBlazeBurnerPonder::mixerScene);
        HELPER.addStoryBoard(ModBlocks.ENERGETIC_BLAZE_BURNER_BLOCK, "energize_main", EnergeticBlazeBurnerPonder::energyUseScene);

    }
}
