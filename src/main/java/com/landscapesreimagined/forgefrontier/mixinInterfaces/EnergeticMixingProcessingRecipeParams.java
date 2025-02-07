package com.landscapesreimagined.forgefrontier.mixinInterfaces;

import com.landscapesreimagined.forgefrontier.ModBlocks.EnergeticBlazeBurner;
import com.landscapesreimagined.forgefrontier.recipies.EnergyCondition;

public interface EnergeticMixingProcessingRecipeParams {

    int getEnergy();
    EnergyCondition getRequiredEnergyLevel();

    void setEnergy(int energy);
    void setRequiredEnergyLevel(EnergyCondition level);
}
