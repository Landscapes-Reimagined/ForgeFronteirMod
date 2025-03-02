package com.landscapesreimagined.forgefrontier.recipies;

import com.landscapesreimagined.forgefrontier.ForgeFrontier;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public enum ForgeFrontierRecipeTypes implements IRecipeTypeInfo {
    ENERGETIC_MIXING(EnergeticMixingRecipe::new, ForgeFrontier.asResource("energetic_mixing"));

    public final ResourceLocation id;
    private final RegistryObject<RecipeSerializer<?>> serializerObject;
    @Nullable
    private final RegistryObject<RecipeType<?>> typeObject;
    private final Supplier<RecipeType<?>> type;

    ForgeFrontierRecipeTypes(Supplier<RecipeSerializer<?>> serializerSupplier, ResourceLocation id) {
        this.id = id;
        serializerObject = Registers.SERIALIZER.register(id.getPath(), serializerSupplier);
        typeObject = Registers.TYPE.register(id.getPath(), () -> RecipeType.simple(id));
        type = typeObject;
    }

    ForgeFrontierRecipeTypes(ProcessingRecipeBuilder.ProcessingRecipeFactory<?> processingFactory, ResourceLocation id) {
        this(() -> new ProcessingRecipeSerializer<>(processingFactory), id);
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public <T extends RecipeSerializer<?>> T getSerializer() {
        return (T) serializerObject.get();
    }

    @Override
    public <T extends RecipeType<?>> T getType() {
        return (T) type.get();
    }

    private static class Registers{
        public static DeferredRegister<RecipeSerializer<?>> SERIALIZER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ForgeFrontier.MODID);
        public static DeferredRegister<RecipeType<?>> TYPE = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, ForgeFrontier.MODID);

    }

    public static void register(IEventBus modEventBus){
        Registers.SERIALIZER.register(modEventBus);
        Registers.TYPE.register(modEventBus);
    }
}
