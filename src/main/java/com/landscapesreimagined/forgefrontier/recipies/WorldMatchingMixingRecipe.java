package com.landscapesreimagined.forgefrontier.recipies;

import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;

public abstract class WorldMatchingMixingRecipe extends MixingRecipe {
    public WorldMatchingMixingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(params);
    }

    public abstract boolean matchesWorld(BasinBlockEntity basin);


}
