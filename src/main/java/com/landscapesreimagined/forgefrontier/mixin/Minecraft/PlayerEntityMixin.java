package com.landscapesreimagined.forgefrontier.mixin.Minecraft;

import com.landscapesreimagined.forgefrontier.Config;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Optional;

@SuppressWarnings("ALL")
@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity{


    protected PlayerEntityMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tickMixin(CallbackInfo ci){
        if(!Config.SPEC.isLoaded())
            return;

        String[] slots = Config.getTickingCurioSlots();

        Player p = (Player) this.self();
        Optional<ICuriosItemHandler> curiosInventory = CuriosApi.getCuriosInventory(p).resolve();

        if(curiosInventory.isPresent()){
            return;
        }


        var slotList = curiosInventory.orElseThrow().findCurios(slots);

        for(SlotResult slot : slotList){
            slot.stack().inventoryTick(p.level(), p, slot.slotContext().index(), false);
            System.out.println("Ticking " + slot.slotContext().identifier());
            slot.stack().onArmorTick(p.level(), p);
        }
    }
}
