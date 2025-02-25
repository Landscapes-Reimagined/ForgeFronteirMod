package com.landscapesreimagined.forgefrontier.mixin.Create;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import uwu.lopyluna.create_dd.access.DDTransportedItemStackHandlerBehaviour;

import java.util.List;
import java.util.function.Function;

@Mixin(BeltBlockEntity.class)
public abstract class BeltBlockEntityCreateDDCompat extends KineticBlockEntity {

    public BeltBlockEntityCreateDDCompat(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }


    @Shadow(remap = false) protected abstract Vec3 getWorldPositionOf(TransportedItemStack transported);


    @Shadow(remap = false) protected abstract void applyToAllItems(float maxDistanceFromCenter, Function<TransportedItemStack, TransportedItemStackHandlerBehaviour.TransportedResult> processFunction);

    @Inject(method = "addBehaviours", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 1), remap = false)
    public void addDDTransportedItemBehaviour(List<BlockEntityBehaviour> behaviours, CallbackInfo ci){
        behaviours.add(new DDTransportedItemStackHandlerBehaviour(this, this::applyToAllItems)
                .withStackPlacement((DDTransportedItemStackHandlerBehaviour.PositionGetter) this::getWorldPositionOf));
    }
}
