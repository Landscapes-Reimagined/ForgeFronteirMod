package com.landscapesreimagined.forgefrontier.mixin.createDDChanges;

import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.fan.EncasedFanBlockEntity;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessing;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import uwu.lopyluna.create_dd.access.DDTransportedItemStackHandlerBehaviour;
import uwu.lopyluna.create_dd.block.BlockProperties.industrial_fan.IndustrialAirCurrent;
import uwu.lopyluna.create_dd.block.BlockProperties.industrial_fan.IndustrialAirCurrentSource;
import uwu.lopyluna.create_dd.block.BlockProperties.industrial_fan.IndustrialFanBlockEntity;
import uwu.lopyluna.create_dd.block.BlockProperties.industrial_fan.Processing.DDFanProcessing;
import uwu.lopyluna.create_dd.block.BlockProperties.industrial_fan.Processing.InterfaceIndustrialProcessingType;

import java.util.List;

@Mixin(IndustrialAirCurrent.class)
public class IndustrialAirCurrentMixin {


    @Shadow(remap = false) protected List<Pair<DDTransportedItemStackHandlerBehaviour, InterfaceIndustrialProcessingType>> affectedItemHandlers;

    @Shadow(remap = false) @Final public IndustrialAirCurrentSource source;

    /**
     * @author gamma_02
     * @reason weird-ass dd code be like:
     */
    @Overwrite(remap = false)
    public void tickAffectedHandlers() {
        for (Pair<DDTransportedItemStackHandlerBehaviour, InterfaceIndustrialProcessingType> pair : this.affectedItemHandlers) {
            DDTransportedItemStackHandlerBehaviour handler = pair.getKey();
            Level world = handler.getWorld();
            InterfaceIndustrialProcessingType processingType = pair.getRight();

            handler.DDhandleProcessingOnAllItems(transported -> {
                if (world.isClientSide) {
                    processingType.spawnProcessingParticles(world, handler.getWorldPositionOf(transported));
                    return DDTransportedItemStackHandlerBehaviour.TransportedResult.doNothing();
                }
                DDTransportedItemStackHandlerBehaviour.TransportedResult applyProcessing = DDFanProcessing.applyProcessing(transported, world, processingType);
                if (!applyProcessing.doesNothing() && source instanceof IndustrialFanBlockEntity fan)
                    fan.award(AllAdvancements.FAN_PROCESSING);
                return applyProcessing;
            });
        }
    }
}
