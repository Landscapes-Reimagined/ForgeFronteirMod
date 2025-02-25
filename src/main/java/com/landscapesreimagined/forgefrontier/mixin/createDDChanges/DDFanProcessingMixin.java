package com.landscapesreimagined.forgefrontier.mixin.createDDChanges;

import com.landscapesreimagined.forgefrontier.mixinInterfaces.IndustrialProcessingTransportedItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import uwu.lopyluna.create_dd.access.DDTransportedItemStack;
import uwu.lopyluna.create_dd.block.BlockProperties.industrial_fan.Processing.DDFanProcessing;
import uwu.lopyluna.create_dd.block.BlockProperties.industrial_fan.Processing.InterfaceIndustrialProcessingType;

@Mixin(DDFanProcessing.class)
public class DDFanProcessingMixin {


    @Redirect(method = "applyProcessing(Luwu/lopyluna/create_dd/access/DDTransportedItemStack;Lnet/minecraft/world/level/Level;Luwu/lopyluna/create_dd/block/BlockProperties/industrial_fan/Processing/InterfaceIndustrialProcessingType;)Luwu/lopyluna/create_dd/access/DDTransportedItemStackHandlerBehaviour$TransportedResult;", at = @At(value = "FIELD", target = "Luwu/lopyluna/create_dd/access/DDTransportedItemStack;processedBy:Luwu/lopyluna/create_dd/block/BlockProperties/industrial_fan/Processing/InterfaceIndustrialProcessingType;", ordinal = 0), remap = false)
    private static InterfaceIndustrialProcessingType accessTypeRedirection(DDTransportedItemStack instance){
        return ((IndustrialProcessingTransportedItem) instance).getIndustrialProcessingType();
    }

    @Redirect(method = "applyProcessing(Luwu/lopyluna/create_dd/access/DDTransportedItemStack;Lnet/minecraft/world/level/Level;Luwu/lopyluna/create_dd/block/BlockProperties/industrial_fan/Processing/InterfaceIndustrialProcessingType;)Luwu/lopyluna/create_dd/access/DDTransportedItemStackHandlerBehaviour$TransportedResult;", at = @At(value = "FIELD", target = "Luwu/lopyluna/create_dd/access/DDTransportedItemStack;processedBy:Luwu/lopyluna/create_dd/block/BlockProperties/industrial_fan/Processing/InterfaceIndustrialProcessingType;", ordinal = 1), remap = false)
    private static void setTypeRedirection(DDTransportedItemStack instance, InterfaceIndustrialProcessingType value){
        ((IndustrialProcessingTransportedItem) instance).setIndustrialProcessingType(value);
        instance.processedBy = value;
    }
}
