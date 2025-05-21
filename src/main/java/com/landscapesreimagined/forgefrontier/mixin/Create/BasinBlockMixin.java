package com.landscapesreimagined.forgefrontier.mixin.Create;

import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BasinBlockEntity.class)
public class BasinBlockMixin {


    @Shadow(remap = false) public BasinInventory inputInventory;

    //prevfixme: will break in Create 6
    //resolved: or not
//    @Inject(method = "<init>", at = @At(value = "RETURN"), remap = false)
//    public void redirectThing(BlockEntityType type, BlockPos pos, BlockState state, CallbackInfo ci){
//        this.inputInventory.withMaxStackSize(64);
//    }
}
