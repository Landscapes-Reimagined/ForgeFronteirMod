
//Large portions of the in this class is taken directly from Simibubi's Create mod, and therefore licensed under the MIT licence:
/**
 * MIT License
 *
 * Copyright (c) 2019 simibubi
 *
 * Copyright (c) 2025 gamma_02
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */


package com.landscapesreimagined.forgefrontier.client.renderer.blockentities;

import com.ibm.icu.text.MessagePattern;
import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.landscapesreimagined.forgefrontier.ModBlocks.EnergeticBlazeBurner;
import com.landscapesreimagined.forgefrontier.ModBlocks.ModBlockEntities.EnergeticBlazeBurnerBlockEntity;
import com.landscapesreimagined.forgefrontier.client.renderer.ForgeFrontierSpriteShifts;
import com.landscapesreimagined.forgefrontier.client.renderer.models.ForgeFronteirPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.render.ContraptionMatrices;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.block.render.SpriteShiftEntry;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

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
        renderShared(ms, null, bufferSource, level,
                blockState, EnergeticBlazeBurner.getHeatLevelOf(blockState), EnergeticBlazeBurner.getEnergyLevelOf(blockState),
            animation, horizontalAngle, canDrawFlame, drawGoggles, drawHat, hashCode);
    }

    public static void renderInContraption(MovementContext context, VirtualRenderWorld renderWorld,
                                           ContraptionMatrices matrices, MultiBufferSource bufferSource, LerpedFloat headAngle, boolean conductor) {
        BlockState state = context.state;
        BlazeBurnerBlock.HeatLevel heatLevel = BlazeBurnerBlock.getHeatLevelOf(state);
        EnergeticBlazeBurner.EnergyLevel energyLevel = EnergeticBlazeBurner.getEnergyLevelOf(state);
        if (heatLevel == BlazeBurnerBlock.HeatLevel.NONE)
            return;

        if (!heatLevel.isAtLeast(BlazeBurnerBlock.HeatLevel.FADING)) {
            heatLevel = BlazeBurnerBlock.HeatLevel.FADING;
        }

        Level level = context.world;
        float horizontalAngle = AngleHelper.rad(headAngle.getValue(AnimationTickHolder.getPartialTicks(level)));
        boolean drawGoggles = context.blockEntityData.contains("Goggles");
        boolean drawHat = conductor || context.blockEntityData.contains("TrainHat");
        int hashCode = context.hashCode();

        renderShared(matrices.getViewProjection(), matrices.getModel(), bufferSource,
                level, state, heatLevel, energyLevel, 0, horizontalAngle,
                false, drawGoggles, drawHat, hashCode);
    }

    private static void renderShared(PoseStack ms, @Nullable PoseStack modelTransform, MultiBufferSource bufferSource,
                                     Level level, BlockState blockState, BlazeBurnerBlock.HeatLevel heatLevel, EnergeticBlazeBurner.EnergyLevel energyLevel, float animation, float horizontalAngle,
                                     boolean canDrawFlame, boolean drawGoggles, boolean drawHat, int hashCode) {

        boolean blockAbove = animation > 0.125f;
        float time = AnimationTickHolder.getRenderTime(level);
        float renderTick = time + (hashCode % 13) * 16f;
        float offsetMult = (heatLevel.isAtLeast(BlazeBurnerBlock.HeatLevel.FADING) || energyLevel.isAtLeast(EnergeticBlazeBurner.EnergyLevel.SLEEPY)) ? 64 : 16;
        float offset = Mth.sin((float) ((renderTick / 16f) % (2 * Math.PI))) / offsetMult;
        float offset1 = Mth.sin((float) ((renderTick / 16f + Math.PI) % (2 * Math.PI))) / offsetMult;
        float offset2 = Mth.sin((float) ((renderTick / 16f + Math.PI / 2) % (2 * Math.PI))) / offsetMult;
        float headY = offset - (animation * .75f);

        VertexConsumer solid = bufferSource.getBuffer(RenderType.solid());
        VertexConsumer cutout = bufferSource.getBuffer(RenderType.cutoutMipped());

        ms.pushPose();

        if (canDrawFlame && blockAbove) {
            SpriteShiftEntry spriteShift =
                    heatLevel == BlazeBurnerBlock.HeatLevel.SEETHING ? ForgeFrontierSpriteShifts.SUPER_BURNER_FLAME : ForgeFrontierSpriteShifts.BURNER_FLAME;

            float spriteWidth = spriteShift.getTarget()
                    .getU1()
                    - spriteShift.getTarget()
                    .getU0();

            float spriteHeight = spriteShift.getTarget()
                    .getV1()
                    - spriteShift.getTarget()
                    .getV0();

            float speed = 1 / 32f + 1 / 64f * heatLevel.ordinal();

            double vScroll = speed * time;
            vScroll = vScroll - Math.floor(vScroll);
            vScroll = vScroll * spriteHeight / 2;

            double uScroll = speed * time / 2;
            uScroll = uScroll - Math.floor(uScroll);
            uScroll = uScroll * spriteWidth / 2;

            SuperByteBuffer flameBuffer = CachedBufferer.partial(ForgeFronteirPartialModels.BLAZE_BURNER_FLAME, blockState);
            if (modelTransform != null)
                flameBuffer.transform(modelTransform);
            flameBuffer.shiftUVScrolling(spriteShift, (float) uScroll, (float) vScroll);
            draw(flameBuffer, horizontalAngle, ms, cutout);
        }

        PartialModel blazeModel = getPartialModel(heatLevel, energyLevel, blockAbove);

        if(partialBlazeModelNeedsUnderLayer(blazeModel)){
            PartialModel underInfuseModel = blockAbove ? ForgeFronteirPartialModels.ENERGETIC_BLAZE_INFUSE_ACTIVE_ON : ForgeFronteirPartialModels.ENERGETIC_BLAZE_INFUSE_ON;
            SuperByteBuffer blazeBuffer = CachedBufferer.partial(underInfuseModel, blockState);
            if (modelTransform != null)
                blazeBuffer.transform(modelTransform);
            blazeBuffer.translate(0, headY, 0);
            draw(blazeBuffer, horizontalAngle, ms, solid);

        }

        SuperByteBuffer blazeBuffer = CachedBufferer.partial(blazeModel, blockState);
        if (modelTransform != null)
            blazeBuffer.transform(modelTransform);
        blazeBuffer.translate(0, headY, 0);
        draw(blazeBuffer, horizontalAngle, ms, solid);



        if (drawGoggles) {
            PartialModel gogglesModel = isSmallBlaze(blazeModel)
                    ? ForgeFronteirPartialModels.BLAZE_GOGGLES_SMALL : ForgeFronteirPartialModels.BLAZE_GOGGLES;

            SuperByteBuffer gogglesBuffer = CachedBufferer.partial(gogglesModel, blockState);
            if (modelTransform != null)
                gogglesBuffer.transform(modelTransform);
            gogglesBuffer.translate(0, headY + 8 / 16f, 0);
            draw(gogglesBuffer, horizontalAngle, ms, solid);
        }

        if (drawHat) {
            SuperByteBuffer hatBuffer = CachedBufferer.partial(AllPartialModels.TRAIN_HAT, blockState);
            if (modelTransform != null)
                hatBuffer.transform(modelTransform);
            hatBuffer.translate(0, headY, 0);
            if (isSmallBlaze(blazeModel)) {
                hatBuffer.translateY(0.5f)
                        .centre()
                        .scale(0.75f)
                        .unCentre();
            } else {
                hatBuffer.translateY(0.75f);
            }
            hatBuffer
                    .rotateCentered(Direction.UP, horizontalAngle + Mth.PI)
                    .translate(0.5f, 0, 0.5f)
                    .light(LightTexture.FULL_BRIGHT)
                    .renderInto(ms, solid);
        }

        if (heatLevel.isAtLeast(BlazeBurnerBlock.HeatLevel.FADING)) {
            PartialModel rodsModel = heatLevel == BlazeBurnerBlock.HeatLevel.SEETHING ? ForgeFronteirPartialModels.BLAZE_BURNER_SUPER_RODS
                    : ForgeFronteirPartialModels.BLAZE_BURNER_RODS;
            PartialModel rodsModel2 = heatLevel == BlazeBurnerBlock.HeatLevel.SEETHING ? ForgeFronteirPartialModels.BLAZE_BURNER_SUPER_RODS_2
                    : ForgeFronteirPartialModels.BLAZE_BURNER_RODS_2;

            SuperByteBuffer rodsBuffer = CachedBufferer.partial(rodsModel, blockState);
            if (modelTransform != null)
                rodsBuffer.transform(modelTransform);
            rodsBuffer.translate(0, offset1 + animation + .125f, 0)
                    .light(LightTexture.FULL_BRIGHT)
                    .renderInto(ms, solid);

            SuperByteBuffer rodsBuffer2 = CachedBufferer.partial(rodsModel2, blockState);
            if (modelTransform != null)
                rodsBuffer2.transform(modelTransform);
            rodsBuffer2.translate(0, offset2 + animation - 3 / 16f, 0)
                    .light(LightTexture.FULL_BRIGHT)
                    .renderInto(ms, solid);
        }

        ms.popPose();
    }

    private static boolean isSmallBlaze(PartialModel blazeModel) {
        return blazeModel == ForgeFronteirPartialModels.BLAZE_INERT || blazeModel == ForgeFronteirPartialModels.ENERGETIC_BLAZE_SLEEPING;
    }

    private static PartialModel getPartialModel(BlazeBurnerBlock.HeatLevel heatLevel, EnergeticBlazeBurner.EnergyLevel energyLevel, boolean blockAbove) {
        PartialModel blazeModel;
        if (energyLevel.isAtLeast(EnergeticBlazeBurner.EnergyLevel.INFUSE)) {

            blazeModel = blockAbove
                ? switch (heatLevel){
                    case NONE -> ForgeFronteirPartialModels.ENERGETIC_BLAZE_SLEEPING;
                    case SMOULDERING -> ForgeFronteirPartialModels.ENERGETIC_BLAZE_INFUSE_OFF;
                    case FADING, KINDLED -> ForgeFronteirPartialModels.ENERGETIC_BLAZE_INFUSE_ACTIVE_HEATED;
                    case SEETHING -> ForgeFronteirPartialModels.ENERGETIC_BLAZE_INFUSE_ACTIVE_SUPERHEATED;
                }
                : switch (heatLevel){
                    case NONE -> ForgeFronteirPartialModels.ENERGETIC_BLAZE_SLEEPING;
                    case SMOULDERING -> ForgeFronteirPartialModels.ENERGETIC_BLAZE_INFUSE_OFF;
                    case FADING, KINDLED -> ForgeFronteirPartialModels.ENERGETIC_BLAZE_INFUSE_HEATED;
                    case SEETHING -> ForgeFronteirPartialModels.ENERGETIC_BLAZE_INFUSE_SUPERHEATED;
                };


        } else if (energyLevel.isAtLeast(EnergeticBlazeBurner.EnergyLevel.SLEEPY)) {
            blazeModel = blockAbove && energyLevel.isAtLeast(EnergeticBlazeBurner.EnergyLevel.CRYSTALLIZE) ? ForgeFronteirPartialModels.ENERGETIC_BLAZE_CRYSTALLIZE
                    : (energyLevel.isAtLeast(EnergeticBlazeBurner.EnergyLevel.CRYSTALLIZE) ? ForgeFronteirPartialModels.ENERGETIC_BLAZE_CRYSTALLIZE_EYES_OPEN : ForgeFronteirPartialModels.ENERGETIC_BLAZE_SLEEPY);
        } else {
            blazeModel = ForgeFronteirPartialModels.ENERGETIC_BLAZE_SLEEPING;
        }
        return blazeModel;
    }

    private static final PartialModel[] UNDER_LAYER_REQUIRED = {
            ForgeFronteirPartialModels.ENERGETIC_BLAZE_INFUSE_HEATED,
            ForgeFronteirPartialModels.ENERGETIC_BLAZE_INFUSE_SUPERHEATED,
            ForgeFronteirPartialModels.ENERGETIC_BLAZE_INFUSE_ACTIVE_HEATED,
            ForgeFronteirPartialModels.ENERGETIC_BLAZE_INFUSE_ACTIVE_SUPERHEATED
    };

    private static boolean partialBlazeModelNeedsUnderLayer(PartialModel model){
        for(var p : UNDER_LAYER_REQUIRED){
            if(p == model){
                return true;
            }
        }
        return false;
    }

    private static void draw(SuperByteBuffer buffer, float horizontalAngle, PoseStack ms, VertexConsumer vc) {
        buffer.rotateCentered(Direction.UP, horizontalAngle)
                .light(LightTexture.FULL_BRIGHT)
                .renderInto(ms, vc);
    }
}
