package com.landscapesreimagined.forgefrontier.compat.jei;

import appeng.core.definitions.AEBlocks;
import com.landscapesreimagined.forgefrontier.ForgeFrontier;
import com.landscapesreimagined.forgefrontier.ModBlocks.EnergeticBlazeBurner;
import com.landscapesreimagined.forgefrontier.ModBlocks.ModBlocks;
import com.landscapesreimagined.forgefrontier.ModItems.ModItems;
import com.landscapesreimagined.forgefrontier.client.renderer.GUI.ForgeFrontierTextures;
import com.landscapesreimagined.forgefrontier.recipies.EnergeticMixingRecipe;
import com.landscapesreimagined.forgefrontier.recipies.EnergyCondition;
import com.mrh0.createaddition.index.CAItems;
import com.simibubi.create.AllItems;
import com.simibubi.create.compat.jei.category.BasinCategory;
import com.simibubi.create.compat.jei.category.animations.AnimatedMixer;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.Pair;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.mutable.MutableInt;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@ParametersAreNonnullByDefault
public class EnergeticMixingCategory extends BasinCategory {

    private final AnimatedMixer mixer = new AnimatedMixer();
    private final AnimatedEnergeticBlazeBurner energizer = new AnimatedEnergeticBlazeBurner();


    public static EnergeticMixingCategory standard(Info<BasinRecipe> info) {
        return new EnergeticMixingCategory(info);
    }

    protected EnergeticMixingCategory(Info<BasinRecipe> info) {
        super(info, true);
    }

    @SuppressWarnings("removal")
    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, BasinRecipe basinRecipe, IFocusGroup focuses) {
        List<Pair<Ingredient, MutableInt>> condensedIngredients = ItemHelper.condenseIngredients(basinRecipe.getIngredients());

        if(!(basinRecipe instanceof EnergeticMixingRecipe recipe))
            return;

        HashMap<Item, Integer> itemAmountMap = recipe.getItemCountMap();

        if(itemAmountMap == null){
            return;
        }

        int size = condensedIngredients.size() + recipe.getFluidIngredients().size();
        int xOffset = size < 3 ? (3 - size) * 19 / 2 : 0;
        int i = 0;

        for (Pair<Ingredient, MutableInt> pair : condensedIngredients) {
            List<ItemStack> stacks = new ArrayList<>();
            for (ItemStack itemStack : pair.getFirst().getItems()) {
                ItemStack copy = itemStack.copy();
                copy.setCount(pair.getSecond().getValue() * itemAmountMap.getOrDefault(itemStack.getItem(), 1));
                stacks.add(copy);
            }

            builder
                    .addSlot(RecipeIngredientRole.INPUT, 17 + xOffset + (i % 3) * 19, 51 - (i / 3) * 19)
                    .setBackground(getRenderedSlot(), -1, -1)
                    .addItemStacks(stacks);
            i++;
        }
        for (FluidIngredient fluidIngredient : recipe.getFluidIngredients()) {
            builder
                    .addSlot(RecipeIngredientRole.INPUT, 17 + xOffset + (i % 3) * 19, 51 - (i / 3) * 19)
                    .setBackground(getRenderedSlot(), -1, -1)
                    .addIngredients(ForgeTypes.FLUID_STACK, withImprovedVisibility(fluidIngredient.getMatchingFluidStacks()))
                    .addTooltipCallback(addFluidTooltip(fluidIngredient.getRequiredAmount()));
            i++;
        }

        size = recipe.getRollableResults().size() + recipe.getFluidResults().size();
        i = 0;

        for (ProcessingOutput result : recipe.getRollableResults()) {
            int xPosition = 142 - (size % 2 != 0 && i == size - 1 ? 0 : i % 2 == 0 ? 10 : -9);
            int yPosition = -19 * (i / 2) + 51;

            builder
                    .addSlot(RecipeIngredientRole.OUTPUT, xPosition, yPosition)
                    .setBackground(getRenderedSlot(result), -1, -1)
                    .addItemStack(result.getStack())
                    .addTooltipCallback(addStochasticTooltip(result));
            i++;
        }

        for (FluidStack fluidResult : recipe.getFluidResults()) {
            int xPosition = 142 - (size % 2 != 0 && i == size - 1 ? 0 : i % 2 == 0 ? 10 : -9);
            int yPosition = -19 * (i / 2) + 51;

            builder
                    .addSlot(RecipeIngredientRole.OUTPUT, xPosition, yPosition)
                    .setBackground(getRenderedSlot(), -1, -1)
                    .addIngredient(ForgeTypes.FLUID_STACK, withImprovedVisibility(fluidResult))
                    .addTooltipCallback(addFluidTooltip(fluidResult.getAmount()));
            i++;
        }



        HeatCondition requiredHeat = recipe.getRequiredHeat();
        EnergyCondition energyCondition = recipe.getRequiredEnergyLevel();
        if (!energyCondition.testEnergeticBlazeBurner(EnergeticBlazeBurner.EnergyLevel.NONE) && !requiredHeat.testBlazeBurner(BlazeBurnerBlock.HeatLevel.NONE)) {
            builder
                    .addSlot(RecipeIngredientRole.RENDER_ONLY, 134, 81)
                    .addItemStack(ModBlocks.ENERGETIC_BLAZE_BURNER_BLOCK.asStack());
        }

        if (!requiredHeat.testBlazeBurner(BlazeBurnerBlock.HeatLevel.KINDLED)) {
            builder
                    .addSlot(RecipeIngredientRole.CATALYST, 153, 81)
                    .addItemStack(AllItems.BLAZE_CAKE.asStack());
        }

        if(energyCondition == EnergyCondition.CRYSTALLIZE || energyCondition == EnergyCondition.INFUSE){
            double feMult = energyCondition == EnergyCondition.INFUSE ? 0.5d : 1;
            builder
                .addSlot(RecipeIngredientRole.RENDER_ONLY, 134, 106)

                .addRichTooltipCallback((slot, tooltip) -> {
                    tooltip.add(Component.translatable("recipe.forgefrontier.fe_use_key").withStyle(Style.EMPTY.withColor(0xFCD720)));
                    tooltip.add(Component.translatable("recipe.forgefrontier.energy_amount").append(Integer.toString((int) (recipe.getRequiredEnergy() * feMult))).withStyle(ChatFormatting.BLUE));
                })
                .addItemStack(ModItems.FORGE_ENERGY.asStack());



        }

        if(energyCondition == EnergyCondition.INFUSE){
            builder
                    .addSlot(RecipeIngredientRole.RENDER_ONLY, 153, 106)
                    .addRichTooltipCallback((slot, tooltip) -> {
                        tooltip.add(Component.translatable("recipe.forgefrontier.ae_use_key").withStyle(Style.EMPTY.withColor(0xC295F0)));
                        tooltip.add(Component.translatable("recipe.forgefrontier.energy_amount").append(Integer.toString(recipe.getRequiredEnergy())).withStyle(ChatFormatting.BLUE));
                    })
                    .addItemStack(ModItems.APPLIED_ENERGISTICS_ENERGY.asStack());
        }
    }

    @Override
    public int getHeight() {
        return getBackground().getHeight() + 24;
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, BasinRecipe recipe, IFocusGroup focuses) {
        super.createRecipeExtras(builder, recipe, focuses);
//        builder.addSlottedWidget();
    }

    @Override
    public void draw(BasinRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        super.draw(recipe, iRecipeSlotsView, graphics, mouseX, mouseY);

        if(!(recipe instanceof EnergeticMixingRecipe energeticRecipe)){
            return;
        }

        HeatCondition requiredHeat = energeticRecipe.getRequiredHeat();
        EnergyCondition requiredEnergy = energeticRecipe.getRequiredEnergyLevel();

        boolean noHeat = requiredHeat == HeatCondition.NONE;
        boolean noEnergy = requiredEnergy == EnergyCondition.NONE;

        AllGuiTextures heatBar = noHeat ? AllGuiTextures.JEI_NO_HEAT_BAR : AllGuiTextures.JEI_HEAT_BAR;
        heatBar.render(graphics, 4, 80);
        graphics.drawString(Minecraft.getInstance().font, Lang.translateDirect(requiredHeat.getTranslationKey()), 9,
                86, requiredHeat.getColor(), false);

        ForgeFrontierTextures energyBar = noEnergy ? ForgeFrontierTextures.JEI_NO_ENERGY : ForgeFrontierTextures.JEI_ENERGY;
        energyBar.render(graphics, 4, 105);
        graphics.drawString(Minecraft.getInstance().font, Component.translatable(ForgeFrontier.MODID + "." + requiredEnergy.getTranslationKey()), 9,
                105+6, requiredEnergy.getColor(), false);


        if (!noEnergy)
            energizer.withHeatAndEnergy(requiredHeat.visualizeAsBlazeBurner(), requiredEnergy.visiualizeAsEnergeticBlazeBurner())
                    .draw(graphics, getBackground().getWidth() / 2 + 3, 55);
        mixer.draw(graphics, getBackground().getWidth() / 2 + 3, 34);
    }

}