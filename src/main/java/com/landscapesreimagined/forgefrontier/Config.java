package com.landscapesreimagined.forgefrontier;

import com.landscapesreimagined.forgefrontier.util.MachineInternalEnergyBuffer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;


@Mod.EventBusSubscriber(modid = ForgeFrontier.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    static ForgeConfigSpec SPEC;

    public static final String CATEGORY_MACHINES = "machines";




    public static final ForgeConfigSpec.IntValue DEFAULT_MACHINE_INTERNAL_ENERGY_CAPACITY;
    public static final ForgeConfigSpec.IntValue DEFAULT_MACHINE_INTERNAL_ENERGY_INSERT;
    public static final ForgeConfigSpec.IntValue DEFAULT_MACHINE_INTERNAL_ENERGY_EXTRACT;

    public static final ForgeConfigSpec.IntValue ENERGETIC_BLAZE_CAPACITY;
    public static final ForgeConfigSpec.IntValue ENERGETIC_BLAZE_MAX_RECEIVE;

    static{
        BUILDER.comment("Machine Settings").push(CATEGORY_MACHINES);

        DEFAULT_MACHINE_INTERNAL_ENERGY_CAPACITY = BUILDER.comment("Default internal energy capacity of Forge Frontier machines").defineInRange("default_machine_internal_energy_capacity", 500000, 0, Integer.MAX_VALUE);
        DEFAULT_MACHINE_INTERNAL_ENERGY_INSERT = BUILDER.comment("Default internal energy max insert rate of Forge Frontier machines").defineInRange("default_machine_energy_insert_rate", 4096, 0, Integer.MAX_VALUE);
        DEFAULT_MACHINE_INTERNAL_ENERGY_EXTRACT = BUILDER.comment("Default internal energy max extract rate of Forge Frontier machines").comment("NOTE: YOU CANNOT EXTRACT ENERGY FROM MOST MACHINES!!!").defineInRange("default_machine_internal_capacity", 4096, 0, Integer.MAX_VALUE);

        BUILDER.comment("");

        ENERGETIC_BLAZE_CAPACITY = BUILDER.comment("Default Energetic Blaze energy capacity").defineInRange("energetic_blaze_energy_capacity", 500000, 0, Integer.MAX_VALUE);
        ENERGETIC_BLAZE_MAX_RECEIVE = BUILDER.comment("Default Energetic Blaze maximum energy receive rate").defineInRange("energetic_blaze_energy_receive", 32768/*2 ^ 15*/, 0, Integer.MAX_VALUE);


        SPEC = BUILDER.build();
    }





    private static boolean validateItemName(final Object obj)
    {
        return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {


    }
}
