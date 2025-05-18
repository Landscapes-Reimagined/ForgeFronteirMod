package com.landscapesreimagined.forgefrontier.mixin.Minecraft;

import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable, net.minecraftforge.common.extensions.IForgeLivingEntity {
    public LivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
//
//    @Inject(method = "calculateFallDamage", at = @At("HEAD"), cancellable = true)
//    public void noFallDamageWithFlightRing(float pFallDistance, float pDamageMultiplier, CallbackInfoReturnable<Integer> cir){
//        LazyOptional<ICuriosItemHandler> maybeCuriosInventory = CuriosApi.getCuriosInventory((this).self());
//
//        if(maybeCuriosInventory.isPresent()){
//            var resolved = maybeCuriosInventory.resolve();
//            if(resolved.isPresent()){
//                var handler = resolved.get();
//
//                if(handler.isEquipped(BalancedFlight.ASCENDED_FLIGHT_RING.get())){
//                    cir.setReturnValue(0);
//                }
//            }
//        }
//
//
//    }
}
