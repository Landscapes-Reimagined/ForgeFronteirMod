package com.landscapesreimagined.forgefrontier.mixin.CreateTeleporters;

import com.landscapesreimagined.forgefrontier.mixinInterfaces.AccessFluidTank;
import net.createteleporters.block.entity.CustomPortalOnTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CustomPortalOnTileEntity.class)
public abstract class FluidTankAccessor implements AccessFluidTank {
    @Final
    @Shadow(remap = false) private FluidTank fluidTank;



//    protected FluidTankAccessor(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
//        super(pType, pPos, pBlockState);
//    }

//    public final FluidTank forgefronteir$fluidTank = new FluidTank()

    @Override
    public FluidTank getFluidTank() {
        return this.fluidTank;
    }

}
