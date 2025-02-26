package com.landscapesreimagined.forgefrontier.mixin.CreateSA;

import com.landscapesreimagined.forgefrontier.util.MixinUtil;
import net.mcreator.createstuffadditions.item.AndesiteJetpackItem;
import net.mcreator.createstuffadditions.item.NetheriteJetpackItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Optional;

@Mixin(AndesiteJetpackItem.Chestplate.class)
public class FixAndesiteJetpackMixin {


    @Redirect(method = "inventoryTick", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/Iterables;contains(Ljava/lang/Iterable;Ljava/lang/Object;)Z", remap = false))
    public boolean redirectIteratables(Iterable<? extends @Nullable Object> collection, Object iterable, ItemStack itemstack, Level world, Entity entity, int slot, boolean selected){


        return MixinUtil.isOnArmorOrCurios(collection, iterable, entity);

//        return false;
    }
}
