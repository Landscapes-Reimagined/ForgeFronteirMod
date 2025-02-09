package com.landscapesreimagined.forgefrontier.client.renderer.models;

import com.jozufozu.flywheel.core.PartialModel;
import com.landscapesreimagined.forgefrontier.ForgeFrontier;
import com.simibubi.create.Create;
import net.minecraft.resources.ResourceLocation;

public class ForgeFronteirPartialModels {

    public static final PartialModel BLAZE_INERT = block("blaze_burner/blaze/inert"), BLAZE_SUPER_ACTIVE = block("blaze_burner/blaze/super_active"),
            BLAZE_GOGGLES = block("blaze_burner/goggles"), BLAZE_GOGGLES_SMALL = block("blaze_burner/goggles_small"),
            BLAZE_IDLE = block("blaze_burner/blaze/idle"), BLAZE_ACTIVE = block("blaze_burner/blaze/active"),
            BLAZE_SUPER = block("blaze_burner/blaze/super"), BLAZE_BURNER_FLAME = block("blaze_burner/flame"),
            BLAZE_BURNER_RODS = block("blaze_burner/rods_small"),
            BLAZE_BURNER_RODS_2 = block("blaze_burner/rods_large"),
            BLAZE_BURNER_SUPER_RODS = block("blaze_burner/superheated_rods_small"),
            BLAZE_BURNER_SUPER_RODS_2 = block("blaze_burner/superheated_rods_large");


    private static PartialModel block(String path) {
        return new PartialModel(new ResourceLocation(ForgeFrontier.MODID, "block/" + path));
    }

    public static void register(){}
}
