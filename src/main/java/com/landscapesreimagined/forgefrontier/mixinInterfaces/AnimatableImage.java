package com.landscapesreimagined.forgefrontier.mixinInterfaces;

import com.google.gson.JsonElement;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public interface AnimatableImage {

    AnimationMetadataSection getMCMeta();

    Optional<Boolean> isAnimated();
}
