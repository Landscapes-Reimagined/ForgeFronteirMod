package com.landscapesreimagined.forgefrontier.client;

import com.landscapesreimagined.forgefrontier.client.renderer.ForgeFrontierSpriteShifts;
import com.landscapesreimagined.forgefrontier.client.renderer.models.ForgeFronteirPartialModels;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
public class ForgeFrontierClient {



    public static void clientInit(final FMLClientSetupEvent event) {
        ForgeFrontierSpriteShifts.register();

        ForgeFronteirPartialModels.register();
    }

    public static void onCtorClient(IEventBus modEventBus) {
        modEventBus.addListener(ForgeFrontierClient::clientInit);
    }
}
