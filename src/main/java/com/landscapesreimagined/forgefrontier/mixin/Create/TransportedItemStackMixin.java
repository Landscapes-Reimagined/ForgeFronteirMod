package com.landscapesreimagined.forgefrontier.mixin.Create;

import com.landscapesreimagined.forgefrontier.mixinInterfaces.IndustrialProcessingTransportedItem;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.kinetics.fan.processing.AllFanProcessingTypes;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingTypeRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import uwu.lopyluna.create_dd.block.BlockProperties.industrial_fan.Processing.DDFanProcessingTypeRegistry;
import uwu.lopyluna.create_dd.block.BlockProperties.industrial_fan.Processing.IndustrialTypeFanProcessing;
import uwu.lopyluna.create_dd.block.BlockProperties.industrial_fan.Processing.InterfaceIndustrialProcessingType;

import java.util.Objects;

@Mixin(TransportedItemStack.class)
public class TransportedItemStackMixin implements IndustrialProcessingTransportedItem {

    @Shadow(remap = false) public FanProcessingType processedBy;

    @Unique
    public InterfaceIndustrialProcessingType forgefrontier$industrialProcessingType = null;


    @Override
    @Nullable
    @Unique
    public InterfaceIndustrialProcessingType getIndustrialProcessingType() {
        return this.forgefrontier$industrialProcessingType;
    }

    @Override
    @Unique
    public void setIndustrialProcessingType(@Nullable InterfaceIndustrialProcessingType newType) {
        this.forgefrontier$industrialProcessingType = newType;
    }

    @Inject(method = "getSimilar", at = @At("RETURN"), remap = false, locals = LocalCapture.CAPTURE_FAILHARD)
    public void getSimilarMixin(CallbackInfoReturnable<TransportedItemStack> cir, TransportedItemStack copy){
        ((IndustrialProcessingTransportedItem) copy).setIndustrialProcessingType(this.getIndustrialProcessingType());
    }

    @Inject(method = "serializeNBT", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;putInt(Ljava/lang/String;I)V", ordinal = 2, remap = true), remap = false, locals = LocalCapture.CAPTURE_FAILHARD)
    public void serializeIndustrialProcessingType(CallbackInfoReturnable<CompoundTag> cir, CompoundTag nbt){
        ResourceLocation id = FanProcessingTypeRegistry.getId(this.processedBy);
        nbt.putString("processedBy", (Objects.requireNonNull(id == null ? FanProcessingTypeRegistry.getId(AllFanProcessingTypes.NONE) : id)).toString());
        ResourceLocation ddId = DDFanProcessingTypeRegistry.getId(this.forgefrontier$industrialProcessingType);
        nbt.putString("industrialProcessedBy", (Objects.requireNonNull(ddId == null ? DDFanProcessingTypeRegistry.getId(IndustrialTypeFanProcessing.NONE) : ddId)).toString());
    }

    @Inject(method = "read", at = @At(value="RETURN"), remap = false, locals = LocalCapture.CAPTURE_FAILHARD)
    private static void readProcessedBy(CompoundTag nbt, CallbackInfoReturnable<TransportedItemStack> cir, TransportedItemStack stack){
        ResourceLocation processedByID = new ResourceLocation(nbt.getString("processedBy"));
        ResourceLocation DDProcessedByID = new ResourceLocation(nbt.getString("industrialProcessedBy"));

        FanProcessingType cType = FanProcessingTypeRegistry.getType(processedByID);
        InterfaceIndustrialProcessingType ddType = DDFanProcessingTypeRegistry.getType(DDProcessedByID);

        stack.processedBy = cType;
        ((IndustrialProcessingTransportedItem) stack).setIndustrialProcessingType(ddType);
    }


}
