package com.landscapesreimagined.forgefrontier.mixin.Create;

import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.logistics.depot.DepotBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import uwu.lopyluna.create_dd.access.DDTransportedItemStackHandlerBehaviour;

import java.util.List;
import java.util.function.Function;

@Mixin(DepotBehaviour.class)
public abstract class DeoptBlockEntityCreateDDCompat extends BlockEntityBehaviour {


//    @Unique
//    private void forgeFrontier$applyToAllItems(float maxDistanceFromCentre,
//                                               Function<TransportedItemStack, TransportedItemStackHandlerBehaviour.TransportedResult> processFunction) {
//        if (this.heldItem == null)
//            return;
//        if (.5f - heldItem.beltPosition > maxDistanceFromCentre)
//            return;
//
//        boolean dirty = false;
//        TransportedItemStack transportedItemStack = heldItem;
//        ItemStack stackBefore = transportedItemStack.stack.copy();
//        TransportedItemStackHandlerBehaviour.TransportedResult result = processFunction.apply(transportedItemStack);
//        if (result == null || result.didntChangeFrom(stackBefore))
//            return;
//
//        dirty = true;
//        heldItem = null;
//        if (result.hasHeldOutput())
//            this.setCenteredHeldItem(result.getHeldOutput());
//
//        for (TransportedItemStack added : result.getOutputs()) {
//            if (this.getHeldItemStack().isEmpty()) {
//                setCenteredHeldItem(added);
//                continue;
//            }
//            ItemStack remainder = ItemHandlerHelper.insertItemStacked(this.processingOutputBuffer, added.stack, false);
//            Vec3 vec = VecHelper.getCenterOf(blockEntity.getBlockPos());
//            Containers.dropItemStack(blockEntity.getLevel(), vec.x, vec.y + .5f, vec.z, remainder);
//        }
//
//        if (dirty)
//            blockEntity.notifyUpdate();
//    }
    @Shadow(remap = false) protected abstract Vec3 getWorldPositionOf(TransportedItemStack transported);

    @Shadow(remap = false) private TransportedItemStack heldItem;

    @Shadow(remap = false) public abstract void setCenteredHeldItem(TransportedItemStack heldItem);

    @Shadow(remap = false) public abstract ItemStack getHeldItemStack();

    @Shadow(remap = false) private ItemStackHandler processingOutputBuffer;

    @Shadow(remap = false) protected abstract void applyToAllItems(float maxDistanceFromCentre, Function<TransportedItemStack, TransportedItemStackHandlerBehaviour.TransportedResult> processFunction);

    public DeoptBlockEntityCreateDDCompat(SmartBlockEntity be) {
        super(be);
    }

    @Inject(method = "addSubBehaviours", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 1), remap = false)
    public void addDDTransportedItemBehaviour(List<BlockEntityBehaviour> behaviours, CallbackInfo ci){
        behaviours.add(new DDTransportedItemStackHandlerBehaviour(this.blockEntity, this::applyToAllItems)
                .withStackPlacement((DDTransportedItemStackHandlerBehaviour.PositionGetter) this::getWorldPositionOf));
    }
}

