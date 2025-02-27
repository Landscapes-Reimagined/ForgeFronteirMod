package com.landscapesreimagined.forgefrontier.mixin.Create;

import com.landscapesreimagined.forgefrontier.ForgeFrontier;
import com.landscapesreimagined.forgefrontier.recipies.EnergeticMixingRecipe;
import com.landscapesreimagined.forgefrontier.recipies.ForgeFrontierRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ProcessingRecipe.class)
public abstract class ProcessingRecipeMixin<T extends Container> implements Recipe<T> {

    @Shadow(remap = false) private RecipeSerializer<?> serializer;

    @Shadow(remap = false) protected ResourceLocation id;

    @Shadow(remap = false) private IRecipeTypeInfo typeInfo;

    @Shadow(remap = false) private RecipeType<?> type;

    @Shadow(remap = false) protected abstract void validate(ResourceLocation recipeTypeId);

    @Inject(method = "<init>", at = @At("RETURN"))
    public void thing(IRecipeTypeInfo typeInfo, ProcessingRecipeBuilder.ProcessingRecipeParams params, CallbackInfo ci){

        if(((Recipe<T>)this instanceof EnergeticMixingRecipe)){
            this.typeInfo = ForgeFrontierRecipeTypes.ENERGETIC_MIXING;
            this.type = ForgeFrontierRecipeTypes.ENERGETIC_MIXING.getType();
            this.serializer = ForgeFrontierRecipeTypes.ENERGETIC_MIXING.getSerializer();
        }

    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/processing/recipe/ProcessingRecipe;validate(Lnet/minecraft/resources/ResourceLocation;)V"), remap = false)
    public void validateCorrectType(ProcessingRecipe<T> instance, ResourceLocation recipeTypeId) {
        if (this.id.equals(ForgeFrontierRecipeTypes.ENERGETIC_MIXING.id)) {
            validate(ForgeFrontierRecipeTypes.ENERGETIC_MIXING.id);
        }else{
            validate(recipeTypeId);
        }
    }


}
