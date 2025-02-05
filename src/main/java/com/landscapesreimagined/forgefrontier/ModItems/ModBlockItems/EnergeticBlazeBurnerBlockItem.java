//Large portions of the in this class is taken directly from Simibubi's Create mod, and therefore licensed under the MIT licence:
/**
 * MIT License
 *
 * Copyright (c) 2019 simibubi
 *
 * Copyright (c) 2025 gamma_02
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */


package com.landscapesreimagined.forgefrontier.ModItems.ModBlockItems;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.ParametersAreNonnullByDefault;

import com.landscapesreimagined.forgefrontier.ModBlocks.ModBlocks;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllTags.AllEntityTags;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import com.simibubi.create.foundation.utility.VecHelper;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.random.WeightedEntry.Wrapper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.phys.Vec3;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class EnergeticBlazeBurnerBlockItem extends BlockItem {

    private final boolean capturedBlaze;

    public static EnergeticBlazeBurnerBlockItem empty(Properties properties) {
        return new EnergeticBlazeBurnerBlockItem(ModBlocks.ENERGETIC_BLAZE_BURNER_BLOCK.get(), properties, false);
    }

    public static EnergeticBlazeBurnerBlockItem withBlaze(Block block, Properties properties) {
        return new EnergeticBlazeBurnerBlockItem(block, properties, true);
    }

    @Override
    public void registerBlocks(Map<Block, Item> p_195946_1_, Item p_195946_2_) {
        if (!hasCapturedBlaze())
            return;
        super.registerBlocks(p_195946_1_, p_195946_2_);
    }

    private EnergeticBlazeBurnerBlockItem(Block block, Properties properties, boolean capturedBlaze) {
        super(block, properties);
        this.capturedBlaze = capturedBlaze;
    }

//    @Override
//    public String getDescriptionId() {
//        return hasCapturedBlaze() ? super.getDescriptionId() : "item.create." + RegisteredObjects.getKeyOrThrow(this).getPath();
//    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (hasCapturedBlaze())
            return super.useOn(context);

        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockEntity be = world.getBlockEntity(pos);
        Player player = context.getPlayer();

        if (!(be instanceof SpawnerBlockEntity))
            return super.useOn(context);

        BaseSpawner spawner = ((SpawnerBlockEntity) be).getSpawner();

        List<SpawnData> possibleSpawns = spawner.spawnPotentials.unwrap()
                .stream()
                .map(Wrapper::getData)
                .toList();

        if (possibleSpawns.isEmpty()) {
            possibleSpawns = new ArrayList<>();
            possibleSpawns.add(spawner.nextSpawnData);
        }

        for (SpawnData e : possibleSpawns) {
            Optional<EntityType<?>> optionalEntity = EntityType.by(e.entityToSpawn());
            if (optionalEntity.isEmpty() || !AllEntityTags.BLAZE_BURNER_CAPTURABLE.matches(optionalEntity.get()))
                continue;

            spawnCaptureEffects(world, VecHelper.getCenterOf(pos));
            if (world.isClientSide || player == null)
                return InteractionResult.SUCCESS;

            giveBurnerItemTo(player, context.getItemInHand(), context.getHand());
            return InteractionResult.SUCCESS;
        }

        return super.useOn(context);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack heldItem, Player player, LivingEntity entity,
                                                  InteractionHand hand) {
        if (hasCapturedBlaze())
            return InteractionResult.PASS;
        if (!AllEntityTags.BLAZE_BURNER_CAPTURABLE.matches(entity))
            return InteractionResult.PASS;

        Level world = player.level();
        spawnCaptureEffects(world, entity.position());
        if (world.isClientSide)
            return InteractionResult.FAIL;

        giveBurnerItemTo(player, heldItem, hand);
        entity.discard();
        return InteractionResult.FAIL;
    }

    protected void giveBurnerItemTo(Player player, ItemStack heldItem, InteractionHand hand) {
        ItemStack filled  = ModBlocks.ENERGETIC_BLAZE_BURNER_BLOCK.asStack();
        if (!player.isCreative())
            heldItem.shrink(1);
        if (heldItem.isEmpty()) {
            player.setItemInHand(hand, filled);
            return;
        }
        player.getInventory()
                .placeItemBackInInventory(filled);
    }

    private void spawnCaptureEffects(Level world, Vec3 vec) {
        if (world.isClientSide) {
            for (int i = 0; i < 40; i++) {
                Vec3 motion = VecHelper.offsetRandomly(Vec3.ZERO, world.random, .125f);
                world.addParticle(ParticleTypes.FLAME, vec.x, vec.y, vec.z, motion.x, motion.y, motion.z);
                Vec3 circle = motion.multiply(1, 0, 1)
                        .normalize()
                        .scale(.5f);
                world.addParticle(ParticleTypes.SMOKE, circle.x, vec.y, circle.z, 0, -0.125, 0);
            }
            return;
        }

        BlockPos soundPos = BlockPos.containing(vec);
        world.playSound(null, soundPos, SoundEvents.BLAZE_HURT, SoundSource.HOSTILE, .25f, .75f);
        world.playSound(null, soundPos, SoundEvents.FIRE_EXTINGUISH, SoundSource.HOSTILE, .5f, .75f);
    }

    public boolean hasCapturedBlaze() {
        return capturedBlaze;
    }

}
