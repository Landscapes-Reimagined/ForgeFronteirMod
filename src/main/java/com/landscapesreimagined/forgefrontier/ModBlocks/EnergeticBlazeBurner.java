package com.landscapesreimagined.forgefrontier.ModBlocks;

import appeng.api.orientation.IOrientableBlock;
import appeng.api.orientation.IOrientationStrategy;
import appeng.api.orientation.OrientationStrategies;
import com.landscapesreimagined.forgefrontier.ModBlocks.ModBlockEntities.EnergeticBlazeBurnerBlockEntity;
import com.landscapesreimagined.forgefrontier.ModBlocks.ModBlockEntities.ModBlockEntities;
import com.landscapesreimagined.forgefrontier.ModItems.ModBlockItems.EnergeticBlazeBurnerBlockItem;
import com.landscapesreimagined.forgefrontier.ModItems.ModItems;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.util.FakePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnergeticBlazeBurner extends BlazeBurnerBlock implements IOrientableBlock {

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

    @Override
    public @NotNull InteractionResult use(BlockState state, @NotNull Level world, @NotNull BlockPos pos, Player player, InteractionHand hand,
                                          @NotNull BlockHitResult blockRayTraceResult) {
        ItemStack heldItem = player.getItemInHand(hand);
        HeatLevel heat = state.getValue(HEAT_LEVEL);

        if (AllItems.GOGGLES.isIn(heldItem) && heat != HeatLevel.NONE)
            return onBlockEntityUse(world, pos, bbte -> {
                if(!(bbte instanceof EnergeticBlazeBurnerBlockEntity ebbbe))
                    return InteractionResult.FAIL;
                if (ebbbe.hasGoggles())
                    return InteractionResult.PASS;
                ebbbe.setGoggles(true);
                bbte.notifyUpdate();
                return InteractionResult.SUCCESS;
            });

        if (heldItem.isEmpty() && heat != HeatLevel.NONE)
            return onBlockEntityUse(world, pos, bbte -> {
                if(!(bbte instanceof EnergeticBlazeBurnerBlockEntity ebbbe))
                    return InteractionResult.FAIL;
                if (!ebbbe.hasGoggles())
                    return InteractionResult.PASS;
                ebbbe.setGoggles(false);
                bbte.notifyUpdate();
                return InteractionResult.SUCCESS;
            });

//        if (heat == HeatLevel.NONE) {
//            if (heldItem.getItem() instanceof FlintAndSteelItem) {
//                world.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F,
//                        world.random.nextFloat() * 0.4F + 0.8F);
//                if (world.isClientSide)
//                    return InteractionResult.SUCCESS;
//                heldItem.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
//                world.setBlockAndUpdate(pos, AllBlocks.LIT_BLAZE_BURNER.getDefaultState());
//                return InteractionResult.SUCCESS;
//            }
//            return InteractionResult.PASS;
//        }

        boolean doNotConsume = player.isCreative();
        boolean forceOverflow = !(player instanceof FakePlayer);

        InteractionResultHolder<ItemStack> res =
                tryInsert(state, world, pos, heldItem, doNotConsume, forceOverflow, false);
        ItemStack leftover = res.getObject();
        if (!world.isClientSide && !doNotConsume && !leftover.isEmpty()) {
            if (heldItem.isEmpty()) {
                player.setItemInHand(hand, leftover);
            } else if (!player.getInventory()
                    .add(leftover)) {
                player.drop(leftover, false);
            }
        }

        return res.getResult() == InteractionResult.SUCCESS ? InteractionResult.SUCCESS : InteractionResult.PASS;
    }

    public static EnergyLevel getEnergyLevelOf(BlockState blockState) {
        return blockState.hasProperty(ENERGY_LEVEL) ? blockState.getValue(ENERGY_LEVEL)
                : EnergyLevel.NONE;
    }

    @Override
    public IOrientationStrategy getOrientationStrategy() {
        return OrientationStrategies.horizontalFacing();
    }

    public static LootTable.@NotNull Builder buildLootTable() {
        LootItemCondition.Builder survivesExplosion = ExplosionCondition.survivesExplosion();
        BlazeBurnerBlock block = ModBlocks.ENERGETIC_BLAZE_BURNER_BLOCK.get();
        LootTable.Builder builder = LootTable.lootTable();
        LootPool.Builder poolBuilder = LootPool.lootPool();

        for (HeatLevel level : HeatLevel.values()) {
            ItemLike drop = level == HeatLevel.NONE ? ModItems.EMPTY_BLAZE_BURNER.get() : ModBlocks.ENERGETIC_BLAZE_BURNER_BLOCK.get();
            poolBuilder.add(LootItem.lootTableItem(drop)
                    .when(survivesExplosion)
                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                            .setProperties(StatePropertiesPredicate.Builder.properties()
                                    .hasProperty(HEAT_LEVEL, level))));
        }

        builder.withPool(poolBuilder.setRolls(ConstantValue.exactly(1)));
        return builder;
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

        public boolean isAtLeast(EnergyLevel energyLevel) {
            return this.ordinal() >= energyLevel.ordinal();
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
