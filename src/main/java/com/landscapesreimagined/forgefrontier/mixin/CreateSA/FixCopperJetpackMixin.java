package com.landscapesreimagined.forgefrontier.mixin.CreateSA;

import com.landscapesreimagined.forgefrontier.util.MixinUtil;
import net.mcreator.createstuffadditions.item.CopperJetpackItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CopperJetpackItem.Chestplate.class)
public class FixCopperJetpackMixin {

    @Redirect(method = "inventoryTick", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/Iterables;contains(Ljava/lang/Iterable;Ljava/lang/Object;)Z", remap = false))
    public boolean redirectIteratables(Iterable<? extends @Nullable Object> collection, Object iterable, ItemStack itemstack, Level world, Entity entity, int slot, boolean selected){
        return MixinUtil.isOnArmorOrCurios(collection, iterable, entity);
    }

}
