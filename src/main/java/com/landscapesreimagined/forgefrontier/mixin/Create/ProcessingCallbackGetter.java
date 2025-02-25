package com.landscapesreimagined.forgefrontier.mixin.Create;

import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TransportedItemStackHandlerBehaviour.class)
public interface ProcessingCallbackGetter {

    @Accessor(remap = false)
    TransportedItemStackHandlerBehaviour.ProcessingCallback getProcessingCallback();
}
