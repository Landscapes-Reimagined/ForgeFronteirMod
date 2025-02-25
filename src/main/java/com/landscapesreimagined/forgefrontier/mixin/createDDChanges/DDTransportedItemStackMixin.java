package com.landscapesreimagined.forgefrontier.mixin.createDDChanges;

import com.landscapesreimagined.forgefrontier.mixinInterfaces.IndustrialProcessingTransportedItem;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.kinetics.fan.processing.AllFanProcessingTypes;
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
        ((IndustrialProcessingTransportedItem) copy).setIndustrialProcessingType(this.getIndustrialProcessingType());
    }

    @Inject(method = "serializeNBT", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;putInt(Ljava/lang/String;I)V", ordinal = 2, remap = true), remap = false, locals = LocalCapture.CAPTURE_FAILHARD)
    public void serializeIndustrialProcessingType(CallbackInfoReturnable<CompoundTag> cir, CompoundTag nbt){
        ResourceLocation id = FanProcessingTypeRegistry.getId(((TransportedItemStack)this).processedBy);
        nbt.putString("processedBy", (Objects.requireNonNull(id == null ? FanProcessingTypeRegistry.getId(AllFanProcessingTypes.NONE) : id)).toString());
        ResourceLocation ddId = DDFanProcessingTypeRegistry.getId(this.getIndustrialProcessingType());
        ResourceLocation dd2Id = DDFanProcessingTypeRegistry.getId(this.processedBy);
        nbt.putString("industrialProcessedBy", (Objects.requireNonNull(ddId == null ? DDFanProcessingTypeRegistry.getId(IndustrialTypeFanProcessing.NONE) : ddId)).toString());
    }

    @Inject(method = "read", at = @At(value="RETURN"), remap = false, locals = LocalCapture.CAPTURE_FAILHARD)
    private static void readProcessedBy(CompoundTag nbt, CallbackInfoReturnable<DDTransportedItemStack> cir, DDTransportedItemStack stack){
        ResourceLocation processedByID = new ResourceLocation(nbt.getString("processedBy"));
        ResourceLocation DDProcessedByID = new ResourceLocation(nbt.getString("industrialProcessedBy"));

        FanProcessingType cType = FanProcessingTypeRegistry.getType(processedByID);
        InterfaceIndustrialProcessingType ddType = DDFanProcessingTypeRegistry.getType(DDProcessedByID);

        ((TransportedItemStack) stack).processedBy = cType;
        (stack).processedBy = ddType;
        ((IndustrialProcessingTransportedItem) stack).setIndustrialProcessingType(ddType);
    }

}
