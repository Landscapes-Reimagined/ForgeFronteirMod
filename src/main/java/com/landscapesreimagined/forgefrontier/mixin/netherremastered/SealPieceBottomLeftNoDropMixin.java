package com.landscapesreimagined.forgefrontier.mixin.netherremastered;

import net.mcreator.netherremastered.procedures.BottomLeftPieceProcedure;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.eventbus.api.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BottomLeftPieceProcedure.class)
public class SealPieceBottomLeftNoDropMixin {

    @Redirect(method = "onEntityDeath", at = @At(value = "INVOKE", target = "Lnet/mcreator/netherremastered/procedures/BottomLeftPieceProcedure;execute(Lnet/minecraftforge/eventbus/api/Event;Lnet/minecraft/world/level/LevelAccessor;DDDLnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity;)V"), remap = false)
    private static void stopDrops(Event _plr1, LevelAccessor entityToSpawn, double _level, double event, double world, Entity x, Entity y){
        return;
    }
}
