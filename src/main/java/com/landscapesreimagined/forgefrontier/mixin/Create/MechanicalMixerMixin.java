package com.landscapesreimagined.forgefrontier.mixin.Create;

import com.landscapesreimagined.forgefrontier.recipies.ForgeFronteirRecipieTypes;
import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlockEntity;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Optional;

@Mixin(MechanicalMixerBlockEntity.class)
public abstract class MechanicalMixerMixin extends BasinOperatingBlockEntity {


    public MechanicalMixerMixin(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

//    @Inject(method = "getMatchingRecipes", at = @At(value = "RETURN", ordinal = 4), remap = false, locals = LocalCapture.CAPTURE_FAILSOFT)
//    public void injectMatchingRecipes(CallbackInfoReturnable<List<Recipe<?>>> cir, List<Recipe<?>> matchingRecipes){
//
//        Optional<BasinBlockEntity> basin = getBasin();
//        if (!basin.isPresent())
//            return;
//
//        BasinBlockEntity basinBlockEntity = basin.get();
//        if (basin.isEmpty())
//            return;
//
//        IItemHandler availableItems = basinBlockEntity
//                .getCapability(ForgeCapabilities.ITEM_HANDLER)
//                .orElse(null);
//        if (availableItems == null)
//            return;
//
//
//    }
    @Inject(method = "matchStaticFilters", at = @At("RETURN"), remap = false, cancellable = true)
    public <C extends Container> void injectMatchStaticRecipes(Recipe<C> r, CallbackInfoReturnable<Boolean> cir){

        cir.setReturnValue(cir.getReturnValue() || r.getType() == ForgeFronteirRecipieTypes.ENERGETIC_MIXING.getType());

    }

}
