package com.landscapesreimagined.forgefrontier.util;

import net.mcreator.createstuffadditions.item.CustomFluidHandlerItemStack;
import net.mcreator.createstuffadditions.network.CreateSaModVariables;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class FillingFuelingTankProcedures {

    public static void fillingTank(LevelAccessor world, Player p, ItemStack jetpack){
        CompoundTag var56 = jetpack.getOrCreateTag();

        double jetpackWater = jetpack.getOrCreateTag().getDouble("tagWater");

        if(jetpackWater >= CreateSaModVariables.MapVariables.get(world).gadgetCapacity){
            return;
        }


        var56.putDouble("tagWater", jetpack.getOrCreateTag().getDouble("tagWater") + (double)1.0F);


        makeBucketSound(world, p);
    }

    public static void fuelingTank(LevelAccessor world, Player p, ItemStack jetpack){
        CompoundTag var56 = jetpack.getOrCreateTag();

        double jetpackWater = jetpack.getOrCreateTag().getDouble("tagFuel");

        if(jetpackWater >= CreateSaModVariables.MapVariables.get(world).gadgetCapacity){
            return;
        }

        var56.putDouble("tagFuel", jetpack.getOrCreateTag().getDouble("tagFuel") + (double)1.0F);


        makeBucketSound(world, p);
    }

    private static void makeBucketSound(LevelAccessor world, Player p) {
        if (world instanceof Level level) {

            if (!level.isClientSide()) {
                level.playSound(null, p.getOnPos().above(), Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.tryParse("item.bucket.empty"))), SoundSource.NEUTRAL, 0.01F, 2.0F);
            } else {
                level.playLocalSound(p.getX(), p.getY(), p.getZ(), Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.tryParse("item.bucket.empty"))), SoundSource.NEUTRAL, 0.01F, 2.0F, false);
            }
        }
    }

    public static void fillingTank(LevelAccessor world, Player p, ItemStack jetpack, ItemStack tank){
        CompoundTag var56 = jetpack.getOrCreateTag();

        double jetpackWater = jetpack.getOrCreateTag().getDouble("tagWater");

        if(jetpackWater >= CreateSaModVariables.MapVariables.get(world).gadgetCapacity){
            return;
        }


        var56.putDouble("tagWater", jetpack.getOrCreateTag().getDouble("tagWater") + (double)1.0F);


        CustomFluidHandlerItemStack.doFillTank(p, tank, -10);




        if (jetpackWater > CreateSaModVariables.MapVariables.get(world).gadgetCapacity) {
            jetpack.getOrCreateTag().putDouble("tagWater", CreateSaModVariables.MapVariables.get(world).gadgetCapacity);
        }

        makeBucketSound(world, p);
    }


    public static void fuelingTank(LevelAccessor world, Player p, ItemStack jetpack, ItemStack tank){
        CompoundTag var56 = jetpack.getOrCreateTag();

        double jetpackWater = jetpack.getOrCreateTag().getDouble("tagFuel");

        if(jetpackWater >= CreateSaModVariables.MapVariables.get(world).gadgetCapacity){
            return;
        }


        var56.putDouble("tagFuel", jetpack.getOrCreateTag().getDouble("tagFuel") + (double)1.0F);


        CustomFluidHandlerItemStack.doFillTank(p, tank, -10);




        if (jetpackWater > CreateSaModVariables.MapVariables.get(world).gadgetCapacity) {
            jetpack.getOrCreateTag().putDouble("tagFuel", CreateSaModVariables.MapVariables.get(world).gadgetCapacity);
        }

        makeBucketSound(world, p);
    }
}
