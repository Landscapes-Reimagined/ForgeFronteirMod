package com.landscapesreimagined.forgefrontier.mixin.Create;

import com.landscapesreimagined.forgefrontier.mixinInterfaces.ListenToRecipeFinish;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(BasinOperatingBlockEntity.class)
public class BasinOperatingBlockMixin implements ListenToRecipeFinish {

    @Unique
    public int forgeFrontier$nextId = 0;
    @Unique
    public Int2ObjectArrayMap<Consumer<BasinOperatingBlockEntity>> forgeFrontier$listenerMap = new Int2ObjectArrayMap<>();


    @Inject(method = "applyBasinRecipe", at = @At("HEAD"), remap = false)
    public void notifyBlazeBurners(CallbackInfo ci){
        forgeFrontier$listenerMap.forEach(((integer, entityConsumer) -> entityConsumer.accept(BasinOperatingBlockEntity.class.cast(this))));
    }

    @Override
    @Unique
    public int subscribeToRecipeFinish(Consumer<BasinOperatingBlockEntity> entityConsumer) {
        forgeFrontier$listenerMap.put(forgeFrontier$nextId, entityConsumer);
        forgeFrontier$nextId += 1;
        return forgeFrontier$nextId - 1;
    }

    @Override
    @Unique
    public void unsubscribeToRecipeFinish(int id) {
        forgeFrontier$listenerMap.remove(id);
    }
}
