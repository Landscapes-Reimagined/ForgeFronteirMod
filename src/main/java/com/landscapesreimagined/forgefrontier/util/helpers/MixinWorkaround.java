package com.landscapesreimagined.forgefrontier.util.helpers;

import com.landscapesreimagined.forgefrontier.ModBlocks.ModBlockEntities.EnergeticBlazeBurnerBlockEntity;
import com.landscapesreimagined.forgefrontier.mixin.Create.CurrentBasinRecipeAccessor;
import com.landscapesreimagined.forgefrontier.recipies.EnergeticMixingRecipe;
import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import org.checkerframework.checker.nullness.qual.Nullable;

public class MixinWorkaround {

    public static @Nullable MechanicalMixerBlockEntity getMechanicalMixerBlockEntity(EnergeticBlazeBurnerBlockEntity instance, Level world, BlockPos mixerPos) {
        if(!(world.getBlockEntity(mixerPos) instanceof MechanicalMixerBlockEntity mixer)) return null;

        Recipe<?> recipe = null;

        recipe = ((CurrentBasinRecipeAccessor) mixer).getCurrentRecipe();

        if(recipe instanceof EnergeticMixingRecipe energeticMixingRecipe){
            instance.setCurrentRecipe(energeticMixingRecipe);
        }


        return mixer;
    }
}
