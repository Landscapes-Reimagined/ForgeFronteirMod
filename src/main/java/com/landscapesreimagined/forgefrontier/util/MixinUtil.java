package com.landscapesreimagined.forgefrontier.util;

import com.landscapesreimagined.forgefrontier.mixinInterfaces.IndustrialProcessingTransportedItem;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import uwu.lopyluna.create_dd.access.DDTransportedItemStack;

public class MixinUtil {

    public static TransportedItemStack fromDDItemStack(DDTransportedItemStack fromStack){
        TransportedItemStack resultStack = new TransportedItemStack(fromStack.stack);

        return setFromDDItemStack(fromStack, resultStack, true);

    }
    public static TransportedItemStack setFromDDItemStack(DDTransportedItemStack fromStack, TransportedItemStack resultStack){

        return setFromDDItemStack(fromStack, resultStack, false);
    }


    public static TransportedItemStack setFromDDItemStack(DDTransportedItemStack fromStack, TransportedItemStack resultStack, boolean useDDTime){

    resultStack.angle = ((TransportedItemStack) fromStack).angle;
    resultStack.insertedFrom = ((TransportedItemStack) fromStack).insertedFrom;
    resultStack.beltPosition = ((TransportedItemStack) fromStack).beltPosition;
    resultStack.insertedAt = ((TransportedItemStack) fromStack).insertedAt;
    resultStack.locked = ((TransportedItemStack) fromStack).locked;
    resultStack.lockedExternally = ((TransportedItemStack) fromStack).lockedExternally;
    resultStack.prevBeltPosition = ((TransportedItemStack) fromStack).prevBeltPosition;
    resultStack.prevSideOffset = ((TransportedItemStack) fromStack).prevSideOffset;
    resultStack.processingTime = (fromStack).processingTime;
    resultStack.sideOffset = ((TransportedItemStack) fromStack).sideOffset;
    resultStack.processedBy = ((TransportedItemStack) fromStack).processedBy;
    ((IndustrialProcessingTransportedItem) resultStack).setIndustrialProcessingType(((IndustrialProcessingTransportedItem)fromStack).getIndustrialProcessingType());


    return resultStack;

    }

    public static DDTransportedItemStack fromCreateItemStack(TransportedItemStack resultStack){
        DDTransportedItemStack fromStack = new DDTransportedItemStack(resultStack.stack);

        ((TransportedItemStack) fromStack).angle = resultStack.angle;
        (fromStack).angle = resultStack.angle;
        ((TransportedItemStack) fromStack).insertedFrom = resultStack.insertedFrom;
        (fromStack).insertedFrom = resultStack.insertedFrom;
        ((TransportedItemStack) fromStack).beltPosition = resultStack.beltPosition;
        ((TransportedItemStack) fromStack).insertedAt = resultStack.insertedAt;
        ( fromStack).insertedAt = resultStack.insertedAt;
        ((TransportedItemStack) fromStack).locked = resultStack.locked;
        (fromStack).locked = resultStack.locked;

        ((TransportedItemStack) fromStack).lockedExternally = resultStack.lockedExternally;
        ( fromStack).lockedExternally = resultStack.lockedExternally;
        ((TransportedItemStack) fromStack).prevBeltPosition = resultStack.prevBeltPosition;
        (fromStack).prevBeltPosition = resultStack.prevBeltPosition;

        ((TransportedItemStack) fromStack).prevSideOffset = resultStack.prevSideOffset;
        (fromStack).prevSideOffset = resultStack.prevSideOffset;

        ((TransportedItemStack) fromStack).processingTime = resultStack.processingTime;
        (fromStack).processingTime = resultStack.processingTime;

        ((TransportedItemStack) fromStack).sideOffset = resultStack.sideOffset;
        (fromStack).sideOffset = resultStack.sideOffset;

        ((TransportedItemStack) fromStack).processedBy = resultStack.processedBy;
        fromStack.processedBy = ((IndustrialProcessingTransportedItem) resultStack).getIndustrialProcessingType();

        ((IndustrialProcessingTransportedItem) fromStack).setIndustrialProcessingType(((IndustrialProcessingTransportedItem) resultStack).getIndustrialProcessingType());


        return fromStack;

    }

}
