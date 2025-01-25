package com.landscapesreimagined.forgefrontier.mixin.netherremastered;

import net.mcreator.netherremastered.procedures.BottomRightPieceProcedure;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.eventbus.api.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BottomRightPieceProcedure.class)
public class SealPeiceBottomRightNoCrumbleMixin {


    @Redirect(method = "onPlayerTick", at = @At(target = "Lnet/mcreator/netherremastered/procedures/BottomRightPieceProcedure;execute(Lnet/minecraftforge/eventbus/api/Event;Lnet/minecraft/world/entity/Entity;)V", value = "INVOKE"), remap = false)
    private static void iHateMCreatorCode(Event _stktoremove, Entity _player){
        return;//this is a dumb method and dear goD why do I have to do this
    }
}
