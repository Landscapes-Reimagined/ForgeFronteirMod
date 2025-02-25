package com.landscapesreimagined.forgefrontier.util.helpers;

import com.landscapesreimagined.forgefrontier.util.MixinUtil;
import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import net.minecraft.nbt.CompoundTag;
import uwu.lopyluna.create_dd.access.DDTransportedItemStack;
import uwu.lopyluna.create_dd.access.DDTransportedItemStackHandlerBehaviour;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class DDToCreateProcessingCallback implements DDTransportedItemStackHandlerBehaviour.ProcessingCallback {
    
    TransportedItemStackHandlerBehaviour.ProcessingCallback origCallback;
    
    public DDToCreateProcessingCallback(TransportedItemStackHandlerBehaviour.ProcessingCallback origCallback){
        this.origCallback = origCallback;
    }
    
    
    
    @Override
    public void applyToAllItems(float v, Function<DDTransportedItemStack, DDTransportedItemStackHandlerBehaviour.TransportedResult> function) {
//        TransportedItemStackHandlerBehaviour.ProcessingCallback origCallback = ((ProcessingCallbackGetter)(FixDDTransportedItemStackHandlerBehaviourMixin.this)).getProcessingCallback();

        AtomicReference<DDTransportedItemStackHandlerBehaviour.TransportedResult> result = new AtomicReference<>();

        origCallback.applyToAllItems(v, (stack) -> getTransportedItemStackTransportedResultFunction(stack, function, result));

    }

    private static TransportedItemStackHandlerBehaviour.TransportedResult getTransportedItemStackTransportedResultFunction(TransportedItemStack stack, Function<DDTransportedItemStack, DDTransportedItemStackHandlerBehaviour.TransportedResult> function, AtomicReference<DDTransportedItemStackHandlerBehaviour.TransportedResult> result) {


            if (stack instanceof DDTransportedItemStack ddTransportedItemStack) {
                result.set(function.apply(ddTransportedItemStack));
                System.out.println("WAHOO!!!");

            } else {
//                        System.out.println(" NOT WAHOO!!! >:( ");


                DDTransportedItemStack stack2 = MixinUtil.fromCreateItemStack(stack);
//                        System.out.println(FanProcessingTypeRegistry.getId(stack.processedBy));
//                        System.out.println(DDFanProcessingTypeRegistry.getId(((IndustrialProcessingTransportedItem) stack).getIndustrialProcessingType()));
//
//                        System.out.println();
//                        System.out.println(DDFanProcessingTypeRegistry.getId(stack2.processedBy));


//                        stack = stack2;

                result.set(function.apply(stack2));

                MixinUtil.setFromDDItemStack(stack2, stack, true);
            }

            List<DDTransportedItemStack> outputs = result.get().doesNothing() ? new ArrayList<>() : result.get().getOutputs();

            List<TransportedItemStack> stackList = new ArrayList<>();

            for (DDTransportedItemStack iterStack : outputs) {
                stackList.add(MixinUtil.fromDDItemStack(iterStack));

            }

            if (result.get().hasHeldOutput() && !result.get().doesNothing()) {
                return TransportedItemStackHandlerBehaviour.TransportedResult.convertToAndLeaveHeld(stackList, result.get().getHeldOutput());
            } else if (!result.get().doesNothing()) {
                return TransportedItemStackHandlerBehaviour.TransportedResult.convertTo(stackList);
            }

            return null;
        }
}
