package com.landscapesreimagined.forgefrontier.mixin.Create;

import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlazeBurnerBlockEntity.class)
public interface BlazeBunerBlockEntityAccessor {

    @Invoker(value = "tickAnimation")
    void invokeTickAnimation();

}
