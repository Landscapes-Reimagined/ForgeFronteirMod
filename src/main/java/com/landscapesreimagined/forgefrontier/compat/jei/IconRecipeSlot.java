package com.landscapesreimagined.forgefrontier.compat.jei;

import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotRichTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.common.gui.elements.OffsetDrawable;
import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.library.gui.ingredients.ICycler;
import mezz.jei.library.gui.ingredients.RecipeSlot;
import mezz.jei.library.gui.ingredients.RendererOverrides;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class IconRecipeSlot extends RecipeSlot {
    List<IRecipeSlotRichTooltipCallback> tooltipCallbacks;

    public IconRecipeSlot(RecipeIngredientRole role, ImmutableRect2i rect, ICycler cycler, List<IRecipeSlotRichTooltipCallback> tooltipCallbacks, List<Optional<ITypedIngredient<?>>> allIngredients, @Nullable List<Optional<ITypedIngredient<?>>> focusedIngredients, @Nullable OffsetDrawable background, @Nullable IDrawable overlay, @Nullable String slotName, @Nullable RendererOverrides rendererOverrides) {
        super(role, rect, cycler, tooltipCallbacks, allIngredients, focusedIngredients, background, overlay, slotName, rendererOverrides);
        this.tooltipCallbacks = tooltipCallbacks;
    }


    @Override
    public void getTooltip(ITooltipBuilder tooltipBuilder) {
        for(IRecipeSlotRichTooltipCallback tooltip : tooltipCallbacks){
            tooltip.onRichTooltip(this, tooltipBuilder);
        }
    }
}
