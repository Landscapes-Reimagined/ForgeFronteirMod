package com.landscapesreimagined.forgefrontier.mixin.CreateSA;

import com.landscapesreimagined.forgefrontier.util.MixinUtil;
import net.mcreator.createstuffadditions.procedures.FlyingOnKeyPressedProcedure;
import net.mcreator.createstuffadditions.procedures.FlyingOnKeyReleasedProcedure;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Optional;

@Mixin(FlyingOnKeyReleasedProcedure.class)
public class CsaFlyingKeyUpFix {


    /**
     * @author gamma_02
     * @reason FUCK mcreator
     */
    @Overwrite(remap = false)
    public static void execute(Entity entity){
        MixinUtil.overwriteJetpackKeyExecute(entity, false);
    }
}
