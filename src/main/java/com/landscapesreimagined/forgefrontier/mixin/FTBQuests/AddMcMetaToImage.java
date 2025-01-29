package com.landscapesreimagined.forgefrontier.mixin.FTBQuests;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.landscapesreimagined.forgefrontier.mixinInterfaces.AnimatableImage;
import dev.ftb.mods.ftblibrary.icon.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSectionSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Inject;

import java.io.IOException;
import java.util.Optional;

@Mixin(AtlasSpriteIcon.class)
public abstract class AddMcMetaToImage implements AnimatableImage {

    public boolean isAnimated = false;

    @Shadow(remap = false) public abstract ResourceLocation getResourceLocation();

    @Shadow(remap = false) @Final private ResourceLocation id;

    @Override
    public AnimationMetadataSection getMCMeta() {
        var animated = isAnimated();
        if(animated.isEmpty()){
            return null;
        }

        if(!animated.get()){
            return null;
        }


//        String id = this.texture.toString();
//
//        ResourceLocation mcmetaId = new ResourceLocation(id + (id.endsWith(".png") ? ".mcmeta" : ".png.mcmeta"));
        Optional<AnimationMetadataSection> section = Optional.empty();

        try {
            section = forgeFronteir$getAnimationMetadataSection();

        }catch(Exception e){
            return null;
        }

        return section.orElse(null);


    }

    @Override
    public Optional<Boolean> isAnimated() {
        try {
            Optional<AnimationMetadataSection> section = forgeFronteir$getAnimationMetadataSection();
            return Optional.of(section.isPresent());


        }catch(Exception e){
            return Optional.empty();
        }



    }


    @Unique
    private @NotNull Optional<AnimationMetadataSection> forgeFronteir$getAnimationMetadataSection() throws IOException {
        Resource rsrc = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(this.id.getNamespace(), "textures/" + this.id.getPath() + ".png")).orElseThrow();


        Optional<AnimationMetadataSection> section = rsrc.metadata().getSection(AnimationMetadataSection.SERIALIZER);
        return section;
    }
}
