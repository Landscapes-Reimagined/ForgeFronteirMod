package com.landscapesreimagined.forgefrontier.mixin.Create;

import com.landscapesreimagined.forgefrontier.recipies.EnergeticMixingRecipe;
import com.landscapesreimagined.forgefrontier.recipies.WorldMatchingMixingRecipe;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BasinRecipe.class)
public abstract class BasinRecipeMixin extends ProcessingRecipe<SmartInventory> {

    public BasinRecipeMixin(IRecipeTypeInfo typeInfo, ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(typeInfo, params);
    }

    @Inject(method = "apply(Lcom/simibubi/create/content/processing/basin/BasinBlockEntity;Lnet/minecraft/world/item/crafting/Recipe;Z)Z", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/processing/basin/BasinBlockEntity;getHeatLevelOf(Lnet/minecraft/world/level/block/state/BlockState;)Lcom/simibubi/create/content/processing/burner/BlazeBurnerBlock$HeatLevel;"), cancellable = true, remap = false)
    private static void hrm(BasinBlockEntity basin, Recipe<?> recipe, boolean test, CallbackInfoReturnable<Boolean> cir) {
        boolean isWorldMatchingMixingRecipe = recipe instanceof WorldMatchingMixingRecipe;

        if (!isWorldMatchingMixingRecipe) {
            return;
        }

        WorldMatchingMixingRecipe modRecipe = (WorldMatchingMixingRecipe) recipe;

        if (!modRecipe.matchesWorld(basin)) {
            cir.setReturnValue(false);
            cir.cancel();
        }

    }

    @Redirect(method = "apply(Lcom/simibubi/create/content/processing/basin/BasinBlockEntity;Lnet/minecraft/world/item/crafting/Recipe;Z)Z", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/items/IItemHandler;extractItem(IIZ)Lnet/minecraft/world/item/ItemStack;", remap = true), remap = false)
    private static ItemStack extractRightAmount(IItemHandler availableItems, int slot, int amount, boolean simulate, BasinBlockEntity basin, Recipe<?> recipe) {
        if (recipe instanceof EnergeticMixingRecipe energeticMixingRecipe) {
            var stack = availableItems.getStackInSlot(slot);
            Integer result = energeticMixingRecipe.getItemAmount(stack.getItem());

            if (result != null) {
                return availableItems.extractItem(slot, result, simulate);
            }
        }

        return availableItems.extractItem(slot, amount, simulate);
    }


    @Redirect(method = "apply(Lcom/simibubi/create/content/processing/basin/BasinBlockEntity;Lnet/minecraft/world/item/crafting/Recipe;Z)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/Ingredient;test(Lnet/minecraft/world/item/ItemStack;)Z"), remap = false)
    private static boolean testAgainstRightAmount(Ingredient instance, ItemStack itemStack, BasinBlockEntity basin, Recipe<?> recipe){
        if (recipe instanceof EnergeticMixingRecipe energeticMixingRecipe) {
            var item = itemStack.getItem();
            Integer result = energeticMixingRecipe.getItemAmount(item);

            if (result != null) {
                return instance.test(itemStack) && itemStack.getCount() >= result;
            }
        }

        return instance.test(itemStack);
    }


}
