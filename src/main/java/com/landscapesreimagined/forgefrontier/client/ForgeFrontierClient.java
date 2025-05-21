package com.landscapesreimagined.forgefrontier.client;

import appeng.items.tools.fluix.FluixSpadeItem;
import com.landscapesreimagined.forgefrontier.ModBlocks.ModBlocks;
import com.landscapesreimagined.forgefrontier.client.renderer.ForgeFrontierSpriteShifts;
import com.landscapesreimagined.forgefrontier.client.renderer.models.ForgeFronteirPartialModels;
import com.landscapesreimagined.forgefrontier.ponder.ForgeFrontierPonderPlugin;
import com.landscapesreimagined.forgefrontier.ponder.ForgeFrontierPonders;
import com.simibubi.create.foundation.ponder.CreatePonderPlugin;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
public class ForgeFrontierClient {



    public static void clientInit(final FMLClientSetupEvent event) {
        ForgeFrontierSpriteShifts.register();

        ForgeFronteirPartialModels.register();

        PonderIndex.addPlugin(new ForgeFrontierPonderPlugin());


    }

    public static void onCtorClient(IEventBus modEventBus) {
        modEventBus.addListener(ForgeFrontierClient::clientInit);
    }
}
