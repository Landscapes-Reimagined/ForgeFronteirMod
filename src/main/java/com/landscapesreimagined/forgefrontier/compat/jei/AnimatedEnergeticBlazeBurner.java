package com.landscapesreimagined.forgefrontier.compat.jei;

import com.landscapesreimagined.forgefrontier.ForgeFrontier;
import com.landscapesreimagined.forgefrontier.ModBlocks.EnergeticBlazeBurner;
import com.landscapesreimagined.forgefrontier.ModBlocks.ModBlocks;
import com.landscapesreimagined.forgefrontier.client.renderer.ForgeFrontierSpriteShifts;
import com.landscapesreimagined.forgefrontier.client.renderer.models.ForgeFronteirPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.compat.jei.category.animations.AnimatedBlazeBurner;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import mezz.jei.api.gui.drawable.IDrawable;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SpriteShiftEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

public class AnimatedEnergeticBlazeBurner extends AnimatedKinetics {

    private BlazeBurnerBlock.HeatLevel heatLevel;
    private EnergeticBlazeBurner.EnergyLevel energyLevel;

    public AnimatedEnergeticBlazeBurner withHeat(BlazeBurnerBlock.HeatLevel heatLevel) {
        this.heatLevel = heatLevel;
        return this;
    }

    public AnimatedEnergeticBlazeBurner withEnergy(EnergeticBlazeBurner.EnergyLevel energyLevel) {
        this.energyLevel = energyLevel;
        return this;
    }

    public AnimatedEnergeticBlazeBurner withHeatAndEnergy(BlazeBurnerBlock.HeatLevel heatLevel, EnergeticBlazeBurner.EnergyLevel energyLevel){
        this.energyLevel = energyLevel;
        this.heatLevel = heatLevel;
        return this;
    }



    @Override
    public void draw(@NotNull GuiGraphics graphics, int xOffset, int yOffset) {
        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate(xOffset, yOffset, 200);
        matrixStack.mulPose(Axis.XP.rotationDegrees(-15.5f));
        matrixStack.mulPose(Axis.YP.rotationDegrees(22.5f));
        int scale = 23;

        float offset = (Mth.sin(AnimationTickHolder.getRenderTime() / 16f) + 0.5f) / 16f;

        blockElement(ModBlocks.ENERGETIC_BLAZE_BURNER_BLOCK.getDefaultState()).atLocal(0, 1.65, 0)
                .scale(scale)
                .render(graphics);

        PartialModel blaze = getPartialModel(heatLevel, energyLevel, true);
        PartialModel rods2 = heatLevel == BlazeBurnerBlock.HeatLevel.SEETHING ? ForgeFronteirPartialModels.BLAZE_BURNER_SUPER_RODS_2
                : ForgeFronteirPartialModels.BLAZE_BURNER_RODS_2;

        if(partialBlazeModelNeedsUnderLayer(blaze)){
            PartialModel underInfuseModel = ForgeFronteirPartialModels.ENERGETIC_BLAZE_INFUSE_ACTIVE_ON;

            blockElement(underInfuseModel).atLocal(1, 1.8, 1)
                    .rotate(0, 180, 0)
                    .scale(scale)
                    .render(graphics);
//            if (modelTransform != null)
//                blazeBuffer.transform(modelTransform);
//            blazeBuffer.translate(0, headY, 0);
//            draw(blazeBuffer, horizontalAngle, ms, solid);

        }

        blockElement(blaze).atLocal(1, 1.8, 1)
                .rotate(0, 180, 0)
                .scale(scale)
                .render(graphics);


        blockElement(rods2).atLocal(1, 1.7 + offset, 1)
                .rotate(0, 180, 0)
                .scale(scale)
                .render(graphics);

        matrixStack.scale(scale, -scale, scale);
        matrixStack.translate(0, -1.8, 0);

        SpriteShiftEntry spriteShift =
                heatLevel == BlazeBurnerBlock.HeatLevel.SEETHING ? AllSpriteShifts.SUPER_BURNER_FLAME : AllSpriteShifts.BURNER_FLAME;

        float spriteWidth = spriteShift.getTarget()
                .getU1()
                - spriteShift.getTarget()
                .getU0();

        float spriteHeight = spriteShift.getTarget()
                .getV1()
                - spriteShift.getTarget()
                .getV0();

        float time = AnimationTickHolder.getRenderTime(Minecraft.getInstance().level);
        float speed = 1 / 32f + 1 / 64f * heatLevel.ordinal();

        double vScroll = speed * time;
        vScroll = vScroll - Math.floor(vScroll);
        vScroll = vScroll * spriteHeight / 2;

        double uScroll = speed * time / 2;
        uScroll = uScroll - Math.floor(uScroll);
        uScroll = uScroll * spriteWidth / 2;

        Minecraft mc = Minecraft.getInstance();
        MultiBufferSource.BufferSource buffer = mc.renderBuffers()
                .bufferSource();
        VertexConsumer vb = buffer.getBuffer(RenderType.cutoutMipped());
        CachedBuffers.partial(AllPartialModels.BLAZE_BURNER_FLAME, Blocks.AIR.defaultBlockState())
                .shiftUVScrolling(spriteShift, (float) uScroll, (float) vScroll)
                .light(LightTexture.FULL_BRIGHT)
                .renderInto(matrixStack, vb);
        matrixStack.popPose();
    }


    private static boolean isSmallBlaze(PartialModel blazeModel) {
        return blazeModel == ForgeFronteirPartialModels.BLAZE_INERT || blazeModel == ForgeFronteirPartialModels.ENERGETIC_BLAZE_SLEEPING;
    }

    private static PartialModel getPartialModel(BlazeBurnerBlock.HeatLevel heatLevel, EnergeticBlazeBurner.EnergyLevel energyLevel, boolean active) {
        PartialModel blazeModel;
        if (energyLevel.isAtLeast(EnergeticBlazeBurner.EnergyLevel.INFUSE)) {

            blazeModel = active
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
            blazeModel = active && energyLevel.isAtLeast(EnergeticBlazeBurner.EnergyLevel.CRYSTALLIZE)
                    ? ForgeFronteirPartialModels.ENERGETIC_BLAZE_CRYSTALLIZE
                    : (energyLevel.isAtLeast(EnergeticBlazeBurner.EnergyLevel.CRYSTALLIZE)
                        ? ForgeFronteirPartialModels.ENERGETIC_BLAZE_CRYSTALLIZE_EYES_OPEN
                        : ForgeFronteirPartialModels.ENERGETIC_BLAZE_SLEEPY
                    );
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

}
