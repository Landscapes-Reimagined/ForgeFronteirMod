package com.landscapesreimagined.forgefrontier.mixin.JEI;

import com.landscapesreimagined.forgefrontier.ModItems.ModItems;
import com.landscapesreimagined.forgefrontier.compat.jei.IconRecipeSlot;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotRichTooltipCallback;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.common.gui.elements.OffsetDrawable;
import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.core.util.Pair;
import mezz.jei.library.gui.ingredients.ICycler;
import mezz.jei.library.gui.ingredients.RecipeSlot;
import mezz.jei.library.gui.ingredients.RendererOverrides;
import mezz.jei.library.gui.recipes.layout.builder.RecipeSlotBuilder;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Mixin(RecipeSlotBuilder.class)
public abstract class BuildIconRecipeSlotMixin {

    @Shadow(remap = false) @Final private RecipeIngredientRole role;

    @Shadow(remap = false) private ImmutableRect2i rect;

    @Shadow(remap = false) @Final private List<IRecipeSlotRichTooltipCallback> tooltipCallbacks;

    @Shadow(remap = false) private @Nullable OffsetDrawable background;

    @Shadow(remap = false) private @Nullable IDrawable overlay;

    @Shadow(remap = false) private @Nullable String slotName;

    @Shadow(remap = false) private @Nullable RendererOverrides rendererOverrides;

    @Shadow(remap = false) @Final private int slotIndex;

    @Inject(method = "build(Ljava/util/Set;Lmezz/jei/library/gui/ingredients/ICycler;)Lmezz/jei/core/util/Pair;",
            at = @At(value = "INVOKE", target = "Lmezz/jei/library/gui/ingredients/RecipeSlot;<init>(Lmezz/jei/api/recipe/RecipeIngredientRole;Lmezz/jei/common/util/ImmutableRect2i;Lmezz/jei/library/gui/ingredients/ICycler;Ljava/util/List;Ljava/util/List;Ljava/util/List;Lmezz/jei/common/gui/elements/OffsetDrawable;Lmezz/jei/api/gui/drawable/IDrawable;Ljava/lang/String;Lmezz/jei/library/gui/ingredients/RendererOverrides;)V", shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILSOFT,
            cancellable = true,
            remap = false)
    public void redirectBuildIconRecipeSlot(Set<Integer> focusMatches, ICycler cycler, CallbackInfoReturnable<Pair<Integer, IRecipeSlotDrawable>> cir, List<Optional<ITypedIngredient<?>>> allIngredients, List<Optional<ITypedIngredient<?>>> focusedIngredients){
        RecipeSlot ourRecipeSlot = new IconRecipeSlot(
                this.role,
                this.rect,
                cycler,
                this.tooltipCallbacks,
                allIngredients,
                focusedIngredients,
                this.background,
                this.overlay,
                this.slotName,
                this.rendererOverrides
        );



        if(allIngredients.stream().anyMatch(BuildIconRecipeSlotMixin::forgefrontier$agh)){

            cir.setReturnValue(new Pair<>(this.slotIndex, ourRecipeSlot));
        }
    }

    @Unique
    private static boolean forgefrontier$agh(Optional<ITypedIngredient<?>> ingredientOptional){
        if (ingredientOptional.isEmpty())
            return false;

        ITypedIngredient<?> ingredient = ingredientOptional.get();
        Optional<ItemStack> stackOptional = ingredient.getItemStack();

        if(stackOptional.isEmpty())
            return false;

        ItemStack stack = stackOptional.get();

        return stack.is(ModItems.APPLIED_ENERGISTICS_ENERGY.get()) || stack.is(ModItems.FORGE_ENERGY.get());
    }
}
