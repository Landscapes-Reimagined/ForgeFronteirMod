package com.landscapesreimagined.forgefrontier.ModBlocks;

import com.landscapesreimagined.forgefrontier.ForgeFrontier;
import com.simibubi.create.Create;
import com.simibubi.create.content.kinetics.mechanicalArm.AllArmInteractionPointTypes;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPoint;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class ArmInteractionStuff {

    public static final EnergeticBlazeBurnerPointType ENERGETIC_BLAZE_BURNER_POINT_TYPE = register("energetic_blaze_burner_type", EnergeticBlazeBurnerPointType::new);


    private static <T extends ArmInteractionPointType> T register(String id, Function<ResourceLocation, T> factory) {
        T type = factory.apply(ForgeFrontier.asResource(id));
        ArmInteractionPointType.register(type);
        return type;
    }

    public static void register(){}


    public static class EnergeticBlazeBurnerPointType extends ArmInteractionPointType{

        public EnergeticBlazeBurnerPointType(ResourceLocation id) {
            super(id);
        }

        @Override
        public boolean canCreatePoint(Level level, BlockPos pos, BlockState state) {
            return ModBlocks.ENERGETIC_BLAZE_BURNER_BLOCK.has(state);
        }

        @Override
        public @Nullable ArmInteractionPoint createPoint(Level level, BlockPos pos, BlockState state) {
            return new AllArmInteractionPointTypes.BlazeBurnerPoint(this, level, pos, state);
        }
    }
}
