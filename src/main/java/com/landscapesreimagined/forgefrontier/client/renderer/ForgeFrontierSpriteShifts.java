package com.landscapesreimagined.forgefrontier.client.renderer;

import com.landscapesreimagined.forgefrontier.ForgeFrontier;
import com.simibubi.create.foundation.block.render.SpriteShiftEntry;
import com.simibubi.create.foundation.block.render.SpriteShifter;
import net.minecraft.resources.ResourceLocation;

public class ForgeFrontierSpriteShifts {

    public static final SpriteShiftEntry
            BURNER_FLAME = SpriteShifter.get(new ResourceLocation(ForgeFrontier.MODID, "block/blaze_burner_flame"), new ResourceLocation(ForgeFrontier.MODID, "block/blaze_burner_flame_scroll")),
            SUPER_BURNER_FLAME = SpriteShifter.get(new ResourceLocation(ForgeFrontier.MODID,"block/blaze_burner_flame"), new ResourceLocation(ForgeFrontier.MODID, "block/blaze_burner_flame_superheated_scroll"));





    public static void register(){}

}
