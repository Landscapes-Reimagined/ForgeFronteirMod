package com.landscapesreimagined.forgefrontier.mixin.FTBQuests;

import com.landscapesreimagined.forgefrontier.ForgeFrontier;
import com.landscapesreimagined.forgefrontier.mixinInterfaces.AnimatableImage;
import com.mojang.logging.LogUtils;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.math.PixelBuffer;
import dev.ftb.mods.ftbquests.FTBQuests;
import dev.ftb.mods.ftbquests.net.EditObjectMessage;
import dev.ftb.mods.ftbquests.quest.Chapter;
import dev.ftb.mods.ftbquests.quest.ChapterImage;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChapterImage.class)
public abstract class TextureAdderFix {

    @Shadow(remap = false) private boolean needAspectRecalc;

    @Shadow(remap = false) private Icon image;

    @Shadow(remap = false) private double aspectRatio;

    @Shadow(remap = false) private double height;

    @Shadow(remap = false) private double width;

    @Shadow(remap = false) private Chapter chapter;

    /**
     * @author gamma_02
     * @reason its so bad I want to cry
     */
    @Overwrite(remap = false)
    public boolean isAspectRatioOff() {

//        if(!(this.image instanceof AnimatableImage img) || !img.isAnimated().orElse(false)) {
//            return this.image.hasPixelBuffer() && !Mth.equal(this.getAspectRatio(), this.width / this.height);
//        }
//
//        AnimationMetadataSection animationMetadata = img.getMCMeta();
//
//        if(animationMetadata == null) {
//            return this.image.hasPixelBuffer() && !Mth.equal(this.getAspectRatio(), this.width / this.height);
//        }
//
//        return !Mth.equal(this.getAspectRatio(), (double) animationMetadata.frameWidth / animationMetadata.frameHeight);
        return true;





    }

    /**
     * @author gamma_02
     * @reason this code is so bad an AI can write better code
     */
    @Overwrite(remap = false)
    public void fixupAspectRatio(boolean adjustWidth) {


        if (!(this.image instanceof AnimatableImage img ) || !img.isAnimated().orElse(false) ) {


            if (adjustWidth) {
                this.width = this.height * this.getAspectRatio();
            } else {
                this.height = this.width / this.getAspectRatio();
            }

            (new EditObjectMessage(this.chapter)).sendToServer();

            return;


        }

        AnimationMetadataSection animationMetadata = img.getMCMeta();

        if(this.width >= animationMetadata.frameWidth || this.height >= animationMetadata.frameHeight) {
            if(adjustWidth){
                this.width = 1 * this.getAspectRatio();
            }else{
                this.height = 1 / this.getAspectRatio();
            }
        }else {
            if (adjustWidth) {
                this.width = this.height * this.getAspectRatio();
            } else {
                this.height = this.width / this.getAspectRatio();
            }
        }

        (new EditObjectMessage(this.chapter)).sendToServer();

        return;


    }


    /**
     * @author gamma_02
     * @reason this code is worse than your mom
     */
    @Overwrite(remap = false)
    private double getAspectRatio() {
//        System.out.println(this.image.getJson());

        if( this.image instanceof AnimatableImage img) {

            if(!img.isAnimated().orElse(false)) {
                forgefrontier$recalcBad();
            }else{
                AnimationMetadataSection animationMetadata = img.getMCMeta();

                if(animationMetadata == null) {
                    forgefrontier$recalcBad();
                    this.needAspectRecalc = false;
                    return this.aspectRatio;
                }



                this.aspectRatio = (double) animationMetadata.frameWidth / (double) animationMetadata.frameHeight;

            }
        }else{
            forgefrontier$recalcBad();
        }

        return this.aspectRatio;
    }

    @Unique
    private void forgefrontier$recalcBad() {
        PixelBuffer buffer = this.image.createPixelBuffer();
        if (buffer != null) {
            this.aspectRatio = (double) buffer.getWidth() / (double) buffer.getHeight();
        } else {
            this.aspectRatio = 1.0F;
        }
    }

}
