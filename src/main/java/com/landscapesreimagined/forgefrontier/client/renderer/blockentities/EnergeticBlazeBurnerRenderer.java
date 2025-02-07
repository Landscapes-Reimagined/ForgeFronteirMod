package com.landscapesreimagined.forgefrontier.client.renderer.blockentities;

import com.landscapesreimagined.forgefrontier.ModBlocks.EnergeticBlazeBurner;
import com.landscapesreimagined.forgefrontier.ModBlocks.ModBlockEntities.EnergeticBlazeBurnerBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.utility.AngleHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class EnergeticBlazeBurnerRenderer extends SafeBlockEntityRenderer<EnergeticBlazeBurnerBlockEntity> {

    public EnergeticBlazeBurnerRenderer(BlockEntityRendererProvider.Context context) {}



    @Override
    protected void renderSafe(EnergeticBlazeBurnerBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        BlazeBurnerBlock.HeatLevel heatLevel = be.getHeatLevelFromBlock();
        EnergeticBlazeBurner.EnergyLevel energyLevel = be.getEnergyLevelFromBlock();

        if(heatLevel == BlazeBurnerBlock.HeatLevel.NONE || energyLevel == EnergeticBlazeBurner.EnergyLevel.NONE){
            return;
        }

        //constants
        Level level = be.getLevel();
        BlockState blockState = be.getBlockState();
        float animation = be.getHeadAnimation().getValue(partialTicks) * .175f;
        float horizontalAngle = AngleHelper.rad(be.getHeadAngle().getValue(partialTicks));
        boolean canDrawFlame = heatLevel.isAtLeast(BlazeBurnerBlock.HeatLevel.FADING);
        boolean drawGoggles = be.hasGoggles();
        boolean drawHat = be.hasHat();
        int hashCode = be.hashCode();

        //Render
    }
}
