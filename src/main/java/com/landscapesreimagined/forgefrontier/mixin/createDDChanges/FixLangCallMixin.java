package com.landscapesreimagined.forgefrontier.mixin.createDDChanges;

import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "uwu/lopyluna/create_dd/jei/DDcreateJEI$CategoryBuilder")
public class FixLangCallMixin {


    @Redirect(
            method = "build",
            at = @At(value = "INVOKE", target = "Lcom/simibubi/create/foundation/utility/Lang;translateDirect(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/network/chat/MutableComponent;"),
            remap = false
    )
    public MutableComponent redirectLangCall(String s, Object[] objects){

        return CreateLang.translateDirect(s);
    }
}
