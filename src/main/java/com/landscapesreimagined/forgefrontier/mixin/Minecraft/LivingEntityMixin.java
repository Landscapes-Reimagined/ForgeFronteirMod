package com.landscapesreimagined.forgefrontier.mixin.Minecraft;

import com.vice.balancedflight.BalancedFlight;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeLivingEntity;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable, net.minecraftforge.common.extensions.IForgeLivingEntity {


    public LivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "calculateFallDamage", at = @At("HEAD"), cancellable = true)
    public void noFallDamageWithFlightRing(float pFallDistance, float pDamageMultiplier, CallbackInfoReturnable<Integer> cir){
        LazyOptional<ICuriosItemHandler> maybeCuriosInventory = CuriosApi.getCuriosInventory((this).self());

        if(maybeCuriosInventory.isPresent()){
            var resolved = maybeCuriosInventory.resolve();
            if(resolved.isPresent()){
                var handler = resolved.get();

                if(handler.isEquipped(BalancedFlight.ASCENDED_FLIGHT_RING.get())){
                    cir.setReturnValue(0);
                }
            }
        }


    }
}
