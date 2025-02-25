package com.landscapesreimagined.forgefrontier.mixin.createDDChanges;

import com.landscapesreimagined.forgefrontier.mixin.Create.ProcessingCallbackGetter;
import com.landscapesreimagined.forgefrontier.mixinInterfaces.IndustrialProcessingTransportedItem;
import com.landscapesreimagined.forgefrontier.util.MixinUtil;
import com.landscapesreimagined.forgefrontier.util.helpers.DDToCreateProcessingCallback;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingTypeRegistry;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import uwu.lopyluna.create_dd.access.DDTransportedItemStack;
import uwu.lopyluna.create_dd.access.DDTransportedItemStackHandlerBehaviour;
import uwu.lopyluna.create_dd.block.BlockProperties.industrial_fan.Processing.DDFanProcessingTypeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

@Mixin(DDTransportedItemStackHandlerBehaviour.class)
public abstract class FixDDTransportedItemStackHandlerBehaviourMixin extends TransportedItemStackHandlerBehaviour{

    @Shadow(remap = false) private DDTransportedItemStackHandlerBehaviour.ProcessingCallback processingCallback;

    public FixDDTransportedItemStackHandlerBehaviourMixin(SmartBlockEntity be, ProcessingCallback processingCallback) {
        super(be, processingCallback);
    }

    @Inject(method = "<init>", at = @At("RETURN"), remap = false)
    private void setOurProcessingCallback(SmartBlockEntity be, TransportedItemStackHandlerBehaviour.ProcessingCallback processingCallback, CallbackInfo ci){
        this.processingCallback = new DDToCreateProcessingCallback(processingCallback);
    }
}
