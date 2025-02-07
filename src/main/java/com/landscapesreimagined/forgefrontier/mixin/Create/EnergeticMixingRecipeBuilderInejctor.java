package com.landscapesreimagined.forgefrontier.mixin.Create;

import com.landscapesreimagined.forgefrontier.mixinInterfaces.EnergeticMixingProcessingRecipeParams;
import com.landscapesreimagined.forgefrontier.mixinInterfaces.EnergeticMixingRecipeBuilder;
import com.landscapesreimagined.forgefrontier.recipies.EnergyCondition;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ProcessingRecipeBuilder.class)
public class EnergeticMixingRecipeBuilderInejctor<T extends ProcessingRecipe<?>> implements EnergeticMixingRecipeBuilder<T> {

    @Shadow(remap = false) protected ProcessingRecipeBuilder.ProcessingRecipeParams params;

    @Override
    @Unique
    public EnergeticMixingRecipeBuilder<T> withEnergy(int energy) {
        ((EnergeticMixingProcessingRecipeParams) this.params).setEnergy(energy);
        return this;
    }

    @Override
    @Unique
    public EnergeticMixingRecipeBuilder<T> requiresEnergyLevel(EnergyCondition condition) {
        ((EnergeticMixingProcessingRecipeParams) this.params).setRequiredEnergyLevel(condition);
        return this;
    }
}
