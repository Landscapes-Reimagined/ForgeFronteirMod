package com.landscapesreimagined.forgefrontier;

import com.jozufozu.flywheel.api.instance.TickableInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.ISlotType;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;


@Mod.EventBusSubscriber(modid = ForgeFrontier.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec SPEC;

    public static final String CATEGORY_MACHINES = "machines";




    public static final ForgeConfigSpec.IntValue DEFAULT_MACHINE_INTERNAL_ENERGY_CAPACITY;
    public static final ForgeConfigSpec.IntValue DEFAULT_MACHINE_INTERNAL_ENERGY_INSERT;
    public static final ForgeConfigSpec.IntValue DEFAULT_MACHINE_INTERNAL_ENERGY_EXTRACT;




    public static final ForgeConfigSpec.IntValue
            ENERGETIC_BLAZE_FE_CAPACITY,
            ENERGETIC_BLAZE_MAX_FE_RECEIVE,
            ENERGETIC_BLAZE_MAX_FE_EXTRACT,
            ENERGETIC_BLAZE_INFUSE_CAPACITY;
    public static final ForgeConfigSpec.DoubleValue
            ENERGETIC_BLAZE_AE_CAPACITY,
            ENERGETIC_BLAZE_MAX_AE_RECEIVE,
            ENERGETIC_BLAZE_MAX_FE_RECEIVE_INJECT_INFUSE_MULTIPLIER;


    public static final String CATEGORY_MISC = "misc";

    public static final ForgeConfigSpec.ConfigValue<String> TICKING_CURIO_SLOTS;


    static{
        BUILDER.comment("Machine Settings").push(CATEGORY_MACHINES);

        DEFAULT_MACHINE_INTERNAL_ENERGY_CAPACITY = BUILDER.comment("Default internal requiredEnergy capacity of Forge Frontier machines").defineInRange("default_machine_internal_energy_capacity", 500000, 0, Integer.MAX_VALUE);
        DEFAULT_MACHINE_INTERNAL_ENERGY_INSERT = BUILDER.comment("Default internal requiredEnergy max insert rate of Forge Frontier machines").defineInRange("default_machine_energy_insert_rate", 4096, 0, Integer.MAX_VALUE);
        DEFAULT_MACHINE_INTERNAL_ENERGY_EXTRACT = BUILDER.comment("Default internal requiredEnergy max extract rate of Forge Frontier machines").comment("NOTE: YOU CANNOT EXTRACT ENERGY FROM MOST MACHINES!!!").defineInRange("default_machine_internal_extract_rate", 4096, 0, Integer.MAX_VALUE);

        BUILDER.comment("");

        ENERGETIC_BLAZE_FE_CAPACITY = BUILDER.comment("Default Energetic Blaze FE energy capacity").defineInRange("energetic_blaze_energy_capacity", 500000, 0, Integer.MAX_VALUE);
        ENERGETIC_BLAZE_MAX_FE_RECEIVE = BUILDER.comment("Default Energetic Blaze maximum FE receive rate").defineInRange("energetic_blaze_energy_receive", 32768/*2 ^ 15*/, 0, Integer.MAX_VALUE);
        ENERGETIC_BLAZE_MAX_FE_EXTRACT = BUILDER.comment("Defualt Energetic Blaze maximum FE extraction rate").defineInRange("energetic_blaze_energy_use", 32768, 0, Integer.MAX_VALUE);
        ENERGETIC_BLAZE_INFUSE_CAPACITY = BUILDER.comment("Default Energetic Blaze FE energy capacity when the blaze is Infusing").defineInRange("energetic_blaze_infusing_energy_capacity", 750000, ENERGETIC_BLAZE_FE_CAPACITY.getDefault(), Integer.MAX_VALUE);
        ENERGETIC_BLAZE_MAX_FE_RECEIVE_INJECT_INFUSE_MULTIPLIER = BUILDER.comment("Default multiplier for the Energetic Blaze's max FE receive and extract rat when Infusing").defineInRange("ENERGETIC_BLAZE_MAX_FE_RECEIVE_INJECT_INFUSE_MULTIPLIER".toLowerCase(), 1.5, 1, 15);

        ENERGETIC_BLAZE_AE_CAPACITY = BUILDER.comment("Default Energetic Blaze AE energy capacity").defineInRange("energetic_blaze_ae_energy_capacity", 1000000, 0, Double.MAX_VALUE);
        ENERGETIC_BLAZE_MAX_AE_RECEIVE = BUILDER.comment("Default Energetic Blaze maximum energy receive rate").defineInRange("energetic_blaze_max_ae_receive", 163840, 0, Double.MAX_VALUE);


        BUILDER.comment("Miscellaneous Settings").push(CATEGORY_MISC);

        TICKING_CURIO_SLOTS = BUILDER.comment("Curio slots to tick").comment("only modify if you know what you're doing!!!").comment("Denote different slots with a comma, like: slot1, slot2, slot3").<String>define("ticking_curio_slots", "head", Config::validateCurioSlots);



        SPEC = BUILDER.build();
    }





    private static boolean validateItemName(final Object obj)
    {
        return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(ResourceLocation.tryParse(itemName));
    }

    private static boolean validateCurioSlots(Object obj){

        if(!(obj instanceof String slotString))
            return false;

        String[] slots = slotString.split(",");

        for(String slot : slots){
            String rs = slot.strip();

            Optional<ISlotType> serverOptional = CuriosApi.getSlot(rs, false);
            Optional<ISlotType> clientOptional = CuriosApi.getSlot(rs, true);

            if(serverOptional.isEmpty() && clientOptional.isEmpty()) return false;

        }

        return true;
    }

    public static String[] getTickingCurioSlots(){
        String[] slots = Arrays.stream(TICKING_CURIO_SLOTS.get().split(",")).map(String::strip).toArray(String[]::new);

        return slots;
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {


    }
}
