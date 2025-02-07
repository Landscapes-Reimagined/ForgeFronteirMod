package com.landscapesreimagined.forgefrontier.ModBlocks;

import com.landscapesreimagined.forgefrontier.ModBlocks.ModBlockEntities.ModBlockEntities;
import com.landscapesreimagined.forgefrontier.ModItems.ModBlockItems.EnergeticBlazeBurnerBlockItem;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnergeticBlazeBurner extends BlazeBurnerBlock {

    public static final EnumProperty<EnergyLevel> ENERGY_LEVEL = EnumProperty.create("energy_level", EnergyLevel.class);


    public EnergeticBlazeBurner(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(BlazeBurnerBlock.HEAT_LEVEL, HeatLevel.NONE).setValue(ENERGY_LEVEL, EnergyLevel.NONE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ENERGY_LEVEL);
    }

    @Override
    public @NotNull BlockEntityType<? extends BlazeBurnerBlockEntity> getBlockEntityType() {
        return ModBlockEntities.HEATER.get();
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        if (state.getValue(HEAT_LEVEL) == HeatLevel.NONE)
            return null;
        return super.newBlockEntity(pos, state);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        ItemStack stack = context.getItemInHand();
        Item item = stack.getItem();
        BlockState defaultState = defaultBlockState();

        if (!(item instanceof EnergeticBlazeBurnerBlockItem blockItem))
            return defaultState;

        boolean hasCaptured = blockItem.hasCapturedBlaze();

        HeatLevel initialHeat =
                hasCaptured ? HeatLevel.SMOULDERING : HeatLevel.NONE;
        EnergyLevel energyLevel =
                hasCaptured ? EnergyLevel.SLEEPING : EnergyLevel.NONE;

        return defaultState.setValue(HEAT_LEVEL, initialHeat)
                .setValue(FACING, context.getHorizontalDirection()
                        .getOpposite())
                .setValue(ENERGY_LEVEL, energyLevel);
    }

    public static EnergyLevel getEnergyLevelOf(BlockState blockState) {
        return blockState.hasProperty(ENERGY_LEVEL) ? blockState.getValue(ENERGY_LEVEL)
                : EnergyLevel.NONE;
    }


    /**
     * This enum represents the state of the blaze. <br>Each entry is a direct paralell to one of {@link HeatLevel}'s entries.<br>
     * What they mean:
     *<br>
     *  1. {@link EnergyLevel#NONE}: See {@link HeatLevel#NONE}. This is when there is no blaze in the burner. <br>
     *  2. {@link EnergyLevel#SLEEPING}: See {@link HeatLevel#SMOULDERING}. This is when there is no fuel. <br>
     *  3. {@link EnergyLevel#SLEEPY}: See {@link HeatLevel#FADING}. This is when the fuel in the burner is low.<br>
     *  4. {@link EnergyLevel#CRYSTALLIZE}: See {@link HeatLevel#KINDLED}. Blaze burner is burning normally.<br>
     *  5. {@link EnergyLevel#INFUSE}: See {@link HeatLevel#SEETHING}. Blaze burner is superheated
     *
     * @see HeatLevel
     */
    public enum EnergyLevel implements StringRepresentable {
        NONE(-1, HeatLevel.NONE),//this should never happen, all recipes should give the filled version of the block.
        SLEEPING(-1, HeatLevel.SMOULDERING),
        SLEEPY(-1, HeatLevel.FADING),
        CRYSTALLIZE(0, HeatLevel.KINDLED),
        INFUSE(500000, HeatLevel.SEETHING);


        private final int minFE;
        EnergyLevel(int FE, HeatLevel level){
            this.minFE = FE;
        }

        @Override
        public @NotNull String getSerializedName() {
            return switch (this){
                case NONE -> "unpowered";
                case SLEEPING -> "sleeping";
                case SLEEPY -> "sleepy";
                case CRYSTALLIZE -> "crystallize";
                case INFUSE -> "infuse";
            };
        }

        public int getMinFE() {
            return minFE;
        }
    }
}
