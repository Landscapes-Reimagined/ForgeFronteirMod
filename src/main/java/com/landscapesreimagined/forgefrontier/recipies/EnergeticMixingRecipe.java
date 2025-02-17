package com.landscapesreimagined.forgefrontier.recipies;

import com.google.gson.JsonObject;
import com.landscapesreimagined.forgefrontier.ModBlocks.ModBlockEntities.EnergeticBlazeBurnerBlockEntity;
import com.landscapesreimagined.forgefrontier.mixinInterfaces.EnergeticMixingProcessingRecipeParams;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.item.SmartInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.gen.Accessor;

public class EnergeticMixingRecipe extends WorldMatchingMixingRecipe {




    protected int requiredEnergy;
    protected EnergyCondition requiredEnergyLevel;


    public EnergeticMixingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(params);
        this.requiredEnergy = ((EnergeticMixingProcessingRecipeParams) params).getEnergy();
        this.requiredEnergyLevel = ((EnergeticMixingProcessingRecipeParams) params).getRequiredEnergyLevel();
    }

    @Override
    public boolean matchesWorld(BasinBlockEntity basin) {

        Level world = basin.getLevel();
        BlockPos basinPos = basin.getBlockPos();

        if(world == null)
            return false;//until maybe needed?

        BlockEntity entityUnderBasin = world.getBlockEntity(basinPos.below());

        if(!(entityUnderBasin instanceof EnergeticBlazeBurnerBlockEntity ebb))
            return false;

        return this.requiredEnergyLevel.testEnergeticBlazeBurner(ebb.getEnergyLevelFromBlock());
    }

    @Override
    public void readAdditional(@NotNull JsonObject json) {
        super.readAdditional(json);

        this.requiredEnergy = GsonHelper.getAsInt(json, "requiredEnergy");
//                json.getAsJsonObject("requiredEnergy").getAsInt();
        this.requiredEnergyLevel = EnergyCondition.deserialize(GsonHelper.getAsString(json, "requiredEnergyLevel"));
    }

    public int getRequiredEnergy() {
        return requiredEnergy;
    }

    public EnergyCondition getRequiredEnergyLevel() {
        return requiredEnergyLevel;
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

    @Override
    public boolean matches(SmartInventory inv, @NotNull Level worldIn) {
        return false;
    }
}
