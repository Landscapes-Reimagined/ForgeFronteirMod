package com.landscapesreimagined.forgefrontier.recipies;

import com.google.gson.JsonObject;
import com.landscapesreimagined.forgefrontier.mixinInterfaces.EnergeticMixingProcessingRecipeParams;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;

public class EnergeticMixingRecipe extends MixingRecipe {

    protected int requiredEnergy;
    protected EnergyCondition requiredEnergyLevel;


    public EnergeticMixingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(params);
        this.requiredEnergy = ((EnergeticMixingProcessingRecipeParams) params).getEnergy();
        this.requiredEnergyLevel = ((EnergeticMixingProcessingRecipeParams) params).getRequiredEnergyLevel();
    }

    @Override
    public void readAdditional(@NotNull JsonObject json) {
        super.readAdditional(json);

        this.requiredEnergy = GsonHelper.getAsInt(json, "requiredEnergy");
//                json.getAsJsonObject("requiredEnergy").getAsInt();
        this.requiredEnergyLevel = EnergyCondition.deserialize(GsonHelper.getAsString(json, "requiredEnergyLevel"));
    }

    @Override
    public void readAdditional(@NotNull FriendlyByteBuf buffer) {
        super.readAdditional(buffer);

        this.requiredEnergy = buffer.readInt();
        this.requiredEnergyLevel = buffer.readEnum(EnergyCondition.class);

    }

    @Override
    public void writeAdditional(@NotNull JsonObject json) {
        super.writeAdditional(json);
        json.addProperty("requiredEnergy", this.requiredEnergy);
        json.addProperty("requiredEnergyLevel", this.requiredEnergyLevel.serialize());
    }

    @Override
    public void writeAdditional(@NotNull FriendlyByteBuf buffer) {
        super.writeAdditional(buffer);
        buffer.writeInt(this.requiredEnergy);
        buffer.writeEnum(this.requiredEnergyLevel);
    }
}
