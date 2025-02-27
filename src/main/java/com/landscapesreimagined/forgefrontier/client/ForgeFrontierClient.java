package com.landscapesreimagined.forgefrontier.client;

import appeng.items.tools.fluix.FluixSpadeItem;
import com.landscapesreimagined.forgefrontier.ModBlocks.ModBlocks;
import com.landscapesreimagined.forgefrontier.client.renderer.ForgeFrontierSpriteShifts;
import com.landscapesreimagined.forgefrontier.client.renderer.models.ForgeFronteirPartialModels;
import com.landscapesreimagined.forgefrontier.ponder.ForgeFrontierPonders;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.infrastructure.ponder.AllPonderTags;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
public class ForgeFrontierClient {



    public static void clientInit(final FMLClientSetupEvent event) {
        ForgeFrontierSpriteShifts.register();

        ForgeFronteirPartialModels.register();

        PonderRegistry.TAGS.forTag(AllPonderTags.RECENTLY_UPDATED)
                .add(ModBlocks.ENERGETIC_BLAZE_BURNER_BLOCK);
        ForgeFrontierPonders.register();


    }

    public static void onCtorClient(IEventBus modEventBus) {
        modEventBus.addListener(ForgeFrontierClient::clientInit);
    }
}
