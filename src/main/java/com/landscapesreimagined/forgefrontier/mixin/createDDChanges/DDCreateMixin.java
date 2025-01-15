package com.landscapesreimagined.forgefrontier.mixin.createDDChanges;

import com.landscapesreimagined.forgefrontier.UsefulConstants;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidInteractionRegistry;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import uwu.lopyluna.create_dd.DDCreate;
import uwu.lopyluna.create_dd.fluid.DDFluids;

import java.util.ArrayList;

import static net.minecraftforge.fluids.FluidInteractionRegistry.addInteraction;

@Mixin(DDCreate.class)
public class DDCreateMixin {

    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Luwu/lopyluna/create_dd/fluid/DDFluids;registerFluidInteractions()V"), remap = false)
    private static void doNotRegisterFluidInteractions(){

        for(FluidEntry<ForgeFlowingFluid.Flowing> fluid : UsefulConstants.CreateDDFluids){
            addInteraction(ForgeMod.LAVA_TYPE.get(), new FluidInteractionRegistry.InteractionInformation(
                    fluid.get().getFluidType(),
                    fluidState -> fluidState.isSource() ? Blocks.OBSIDIAN.defaultBlockState() : Blocks.COBBLESTONE.defaultBlockState()
            ));
        }

    }
}
