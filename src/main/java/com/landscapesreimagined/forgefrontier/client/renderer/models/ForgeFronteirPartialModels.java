package com.landscapesreimagined.forgefrontier.client.renderer.models;


import com.landscapesreimagined.forgefrontier.ForgeFrontier;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.resources.ResourceLocation;

public class ForgeFronteirPartialModels {

    //Blaze Burner Partial Models
    public static final PartialModel
            BLAZE_INERT = block("blaze_burner/blaze/inert"),
            BLAZE_SUPER_ACTIVE = block("blaze_burner/blaze/super_active"),
            BLAZE_GOGGLES = block("blaze_burner/goggles"),
            BLAZE_GOGGLES_SMALL = block("blaze_burner/goggles_small"),
            BLAZE_IDLE = block("blaze_burner/blaze/idle"),
            BLAZE_ACTIVE = block("blaze_burner/blaze/active"),
            BLAZE_SUPER = block("blaze_burner/blaze/super"),
            BLAZE_BURNER_FLAME = block("blaze_burner/flame"),
            BLAZE_BURNER_RODS = block("blaze_burner/rods_small"),
            BLAZE_BURNER_RODS_2 = block("blaze_burner/rods_large"),
            BLAZE_BURNER_SUPER_RODS = block("blaze_burner/superheated_rods_small"),
            BLAZE_BURNER_SUPER_RODS_2 = block("blaze_burner/superheated_rods_large");

    //Energetic Blaze Burner Partial Models
    public static final PartialModel
            ENERGETIC_BLAZE_SLEEPING = block("blaze_burner/blaze/sleeping"),
            ENERGETIC_BLAZE_SLEEPY = block("blaze_burner/blaze/sleepy"),
            ENERGETIC_BLAZE_CRYSTALLIZE = block("blaze_burner/blaze/crystallize"),
            ENERGETIC_BLAZE_CRYSTALLIZE_EYES_OPEN = block("blaze_burner/blaze/crystallize_eyes_open"),
            ENERGETIC_BLAZE_INFUSE_OFF = block("blaze_burner/blaze/infuse_open"),
            ENERGETIC_BLAZE_INFUSE_ON = block("blaze_burner/blaze/infuse_open_on"),
            ENERGETIC_BLAZE_INFUSE_HEATED = block("blaze_burner/blaze/infuse_open_error"),
            ENERGETIC_BLAZE_INFUSE_SUPERHEATED = block("blaze_burner/blaze/infuse_open_on_lights"),
            ENERGETIC_BLAZE_INFUSE_ACTIVE_ON = block("blaze_burner/blaze/infuse_close_on"),
            ENERGETIC_BLAZE_INFUSE_ACTIVE_HEATED = block("blaze_burner/blaze/infuse_close_error"),
            ENERGETIC_BLAZE_INFUSE_ACTIVE_SUPERHEATED = block("blaze_burner/blaze/infuse_close_on_lights");


    private static PartialModel block(String path) {
        return PartialModel.of(ForgeFrontier.asResource("block/" + path));
    }

    public static void register(){}
}
