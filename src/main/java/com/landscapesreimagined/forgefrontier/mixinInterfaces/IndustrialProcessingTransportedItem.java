package com.landscapesreimagined.forgefrontier.mixinInterfaces;

import org.checkerframework.checker.nullness.qual.Nullable;
import uwu.lopyluna.create_dd.block.BlockProperties.industrial_fan.Processing.InterfaceIndustrialProcessingType;

public interface IndustrialProcessingTransportedItem {

    @Nullable
    InterfaceIndustrialProcessingType getIndustrialProcessingType();
    void setIndustrialProcessingType(@Nullable InterfaceIndustrialProcessingType newType);
}
