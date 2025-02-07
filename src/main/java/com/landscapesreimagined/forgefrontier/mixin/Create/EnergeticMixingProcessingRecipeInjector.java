package com.landscapesreimagined.forgefrontier.mixin.Create;

import com.landscapesreimagined.forgefrontier.ModBlocks.EnergeticBlazeBurner;
import com.landscapesreimagined.forgefrontier.mixinInterfaces.EnergeticMixingProcessingRecipeParams;
import com.landscapesreimagined.forgefrontier.recipies.EnergyCondition;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ProcessingRecipeBuilder.ProcessingRecipeParams.class)
public class EnergeticMixingProcessingRecipeInjector implements EnergeticMixingProcessingRecipeParams {

    @Unique
    protected int energy;
    @Unique
    protected EnergyCondition requiredEnergy;

    @Unique
    @Override
    public int getEnergy() {
        return energy;
    }

    @Unique
    @Override
    public EnergyCondition getRequiredEnergyLevel() {
        return requiredEnergy;
    }

    @Unique
    @Override
    public void setEnergy(int energy) {
        this.energy = energy;
    }

    @Unique
    @Override
    public void setRequiredEnergyLevel(EnergyCondition level) {
        this.requiredEnergy = level;
    }

     @Inject(method = "<init>", at = @At("RETURN"), remap = false)
    public void initParams(ResourceLocation id, CallbackInfo ci){
        this.energy = 0;
        this.requiredEnergy = EnergyCondition.NONE;
    }

}
