package com.landscapesreimagined.forgefrontier.ponder;

import com.landscapesreimagined.forgefrontier.ForgeFrontier;
import com.landscapesreimagined.forgefrontier.ModBlocks.ModBlocks;
import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;

public class ForgeFrontierPonders {

    static final PonderRegistrationHelper HELPER = new PonderRegistrationHelper(ForgeFrontier.MODID);

    public static void register(){

        HELPER.addStoryBoard(ModBlocks.ENERGETIC_BLAZE_BURNER_BLOCK, "energize_main", EnergeticBlazeBurnerPonder::introScene);
        HELPER.addStoryBoard(ModBlocks.ENERGETIC_BLAZE_BURNER_BLOCK, "energize_main", EnergeticBlazeBurnerPonder::mixerScene);
        HELPER.addStoryBoard(ModBlocks.ENERGETIC_BLAZE_BURNER_BLOCK, "energize_main", EnergeticBlazeBurnerPonder::energyUseScene);
    }


}
