package com.landscapesreimagined.forgefrontier.mixin.createDDChanges;

import com.landscapesreimagined.forgefrontier.mixinInterfaces.IndustrialProcessingTransportedItem;
import com.simibubi.create.api.registry.CreateBuiltInRegistries;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.kinetics.fan.processing.AllFanProcessingTypes;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessing;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingTypeRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import uwu.lopyluna.create_dd.access.DDTransportedItemStack;
import uwu.lopyluna.create_dd.block.BlockProperties.industrial_fan.Processing.DDFanProcessingTypeRegistry;
import uwu.lopyluna.create_dd.block.BlockProperties.industrial_fan.Processing.IndustrialTypeFanProcessing;
import uwu.lopyluna.create_dd.block.BlockProperties.industrial_fan.Processing.InterfaceIndustrialProcessingType;

import java.util.Objects;

@Mixin(DDTransportedItemStack.class)
public abstract class DDTransportedItemStackMixin extends TransportedItemStack implements IndustrialProcessingTransportedItem{

    @Shadow(remap = false) public InterfaceIndustrialProcessingType processedBy;


    public DDTransportedItemStackMixin(ItemStack stack) {
        super(stack);
    }

    @Inject(method = "getSimilar()Luwu/lopyluna/create_dd/access/DDTransportedItemStack;", at = @At("RETURN"), remap = false, locals = LocalCapture.CAPTURE_FAILHARD)
    public void getSimilarMixin(CallbackInfoReturnable<DDTransportedItemStack> cir, DDTransportedItemStack copy){
        ((TransportedItemStack) copy).beltPosition = ((TransportedItemStack) this).beltPosition;
        ((IndustrialProcessingTransportedItem) copy).setIndustrialProcessingType(this.getIndustrialProcessingType());
    }

    @Inject(method = "serializeNBT", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;putInt(Ljava/lang/String;I)V", ordinal = 2, remap = true), remap = false, locals = LocalCapture.CAPTURE_FAILHARD)
    public void serializeIndustrialProcessingType(CallbackInfoReturnable<CompoundTag> cir, CompoundTag nbt){

        if (processedBy != null) {
            ResourceLocation key = CreateBuiltInRegistries.FAN_PROCESSING_TYPE.getKey(((TransportedItemStack) this).processedBy);
            if (key == null)
                throw new IllegalArgumentException("Could not get id for FanProcessingType " + ((TransportedItemStack) this).processedBy + "!");

            nbt.putString("FanProcessingType", key.toString());
            nbt.putInt("FanProcessingTime", ((TransportedItemStack) this).processingTime);
        }

        ResourceLocation ddId = DDFanProcessingTypeRegistry.getId(this.processedBy);
        nbt.putString("industrialProcessedBy", (Objects.requireNonNull(ddId == null ? DDFanProcessingTypeRegistry.getId(IndustrialTypeFanProcessing.NONE) : ddId)).toString());
    }

    @Inject(method = "read", at = @At(value="RETURN"), remap = false, locals = LocalCapture.CAPTURE_FAILHARD)
    private static void readProcessedBy(CompoundTag nbt, CallbackInfoReturnable<DDTransportedItemStack> cir, DDTransportedItemStack stack){

        if (nbt.contains("FanProcessingType")) {
            ((TransportedItemStack) stack).processedBy = AllFanProcessingTypes.parseLegacy(nbt.getString("FanProcessingType"));
            ((TransportedItemStack) stack).processingTime = nbt.getInt("FanProcessingTime");
        }

        ResourceLocation DDProcessedByID = ResourceLocation.tryParse(nbt.getString("industrialProcessedBy"));

        InterfaceIndustrialProcessingType ddType = DDFanProcessingTypeRegistry.getType(DDProcessedByID);

        (stack).processedBy = ddType;
        ((IndustrialProcessingTransportedItem) stack).setIndustrialProcessingType(ddType);
    }

}
