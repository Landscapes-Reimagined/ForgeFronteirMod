package com.landscapesreimagined.forgefrontier.mixin.Create;

import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import org.openjdk.nashorn.internal.objects.annotations.Getter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ProcessingRecipeBuilder.ProcessingRecipeParams.class)
public interface ProcessingRecipeParamsFieldAccessor {

    @Accessor(remap = false)
    ResourceLocation getId();
}
