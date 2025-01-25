package com.landscapesreimagined.forgefrontier.mixin.netherremastered;

import net.mcreator.netherremastered.procedures.TopLeftPieceProcedure;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.eventbus.api.Event;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TopLeftPieceProcedure.class)
public class SealPeiceTopLeftNoCrumbleMixin {


    @Redirect(method = "onPlayerTick", at = @At(value = "INVOKE", target = "Lnet/mcreator/netherremastered/procedures/TopLeftPieceProcedure;execute(Lnet/minecraftforge/eventbus/api/Event;Lnet/minecraft/world/entity/Entity;)V"), remap = false)
    private static void iHateMCreatorCode(Event _stktoremove, Entity _player){
        return;//this is a dumb method and dear goD why do I have to do this
    }

}
