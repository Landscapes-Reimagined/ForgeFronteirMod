package com.landscapesreimagined.forgefrontier.mixinInterfaces;

import com.landscapesreimagined.forgefrontier.recipies.EnergyCondition;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;

public interface EnergeticMixingRecipeBuilder<T extends ProcessingRecipe<?>> {

    EnergeticMixingRecipeBuilder<T> withEnergy(int energy);
    EnergeticMixingRecipeBuilder<T> requiresEnergyLevel(EnergyCondition condition);
}
