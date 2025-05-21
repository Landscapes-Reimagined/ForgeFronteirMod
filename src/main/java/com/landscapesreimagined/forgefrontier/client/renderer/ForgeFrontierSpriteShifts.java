package com.landscapesreimagined.forgefrontier.client.renderer;

import com.landscapesreimagined.forgefrontier.ForgeFrontier;
import net.createmod.catnip.render.SpriteShiftEntry;
import net.createmod.catnip.render.SpriteShifter;

public class ForgeFrontierSpriteShifts {

    public static final SpriteShiftEntry
            BURNER_FLAME = SpriteShifter.get(
                    ForgeFrontier.asResource( "block/blaze_burner_flame"), 
                ForgeFrontier.asResource("block/blaze_burner_flame_scroll")),
            SUPER_BURNER_FLAME = SpriteShifter.get(ForgeFrontier.asResource("block/blaze_burner_flame"), ForgeFrontier.asResource("block/blaze_burner_flame_superheated_scroll"));





    public static void register(){}

}
