package com.landscapesreimagined.forgefrontier.mixin.createDDChanges;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import uwu.lopyluna.create_dd.fluid.DDFluids;

@Mixin(DDFluids.DDFluidEvents.class)
public class RemoveLavaInteractionFluidEventsMixin {









    //null the result in DDFluids.DDFluidEvents#handlePipeFlowCollisionFallback
    @Redirect(method = "handlePipeFlowCollisionFallback", at = @At(value = "INVOKE", target = "Luwu/lopyluna/create_dd/fluid/DDFluids;getLavaInteraction(Lnet/minecraft/world/level/material/FluidState;)Lnet/minecraft/world/level/block/state/BlockState;"), remap = false)
    private static BlockState preventLavaFluidInteraction$handlePipeFlowCollisionFallback(FluidState fluidState){
        return null;
    }

    //null the result in DDFluids.DDFluidEvents#whenFluidsMeet
    @Redirect(method = "whenFluidsMeet", at = @At(value = "INVOKE", target = "Luwu/lopyluna/create_dd/fluid/DDFluids;getLavaInteraction(Lnet/minecraft/world/level/material/FluidState;)Lnet/minecraft/world/level/block/state/BlockState;"), remap = false)
    private static BlockState preventLavaFluidInteraction$whenFluidsMeet(FluidState fluidState){
        return null;
    }

    //null the result in DDFluids.DDFluidEvents#handlePipeSpillCollisionFallback
    @Redirect(method = "handlePipeSpillCollisionFallback", at = @At(value = "INVOKE", target = "Luwu/lopyluna/create_dd/fluid/DDFluids;getLavaInteraction(Lnet/minecraft/world/level/material/FluidState;)Lnet/minecraft/world/level/block/state/BlockState;"), remap = false)
    private static BlockState preventLavaFluidInteraction$handlePipeSpillCollisionFallback(FluidState fluidState){
        return null;
    }
}
