package com.landscapesreimagined.forgefrontier;

import com.landscapesreimagined.forgefrontier.ModBlocks.ModBlockEntities.ModBlockEntities;
import com.landscapesreimagined.forgefrontier.ModBlocks.ModBlocks;
import com.landscapesreimagined.forgefrontier.ModItems.ModItems;
import com.landscapesreimagined.forgefrontier.client.ForgeFrontierClient;
import com.landscapesreimagined.forgefrontier.recipies.ForgeFrontierRecipeTypes;
import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ForgeFrontier.MODID)
public class ForgeFrontier {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "forgefrontier";
    public static final Logger LOGGER = LogUtils.getLogger();

    private static final DeferredRegister<CreativeModeTab> TAB_DEFERRED_REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<CreativeModeTab> MOD_TAB =
            TAB_DEFERRED_REGISTER.register("forge_frontier_tab",
                    () -> CreativeModeTab.builder()
                            .title(Component.translatable("itemGroup.forgefrontier.forge_frontier_tab"))
                            .icon(() -> ModBlocks.ENERGETIC_BLAZE_BURNER_BLOCK.asItem().getDefaultInstance())
                            .displayItems((itemDisplayParameters, output) -> {
                                output.accept(ModItems.EMPTY_BLAZE_BURNER);
//                                output.accept(ModItems.GLOBE_ANIM_TEST.get());
                                output.accept(ModItems.ANIM_TEST.get());
//                                output.accept(ModItems.NOVA_ANIM_TEST.get());
                                output.accept(ModBlocks.ENERGETIC_BLAZE_BURNER_BLOCK.asItem());
                            })
                            .build());

    public static final CreateRegistrate FORGE_FRONTIER_REGISTRATE = CreateRegistrate.create(MODID);





    public ForgeFrontier() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();



        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

//        modEventBus.addListener(ForgeFrontier::onPlayerTick);

        ModItems.MOD_ITEMS.register(modEventBus);

        TAB_DEFERRED_REGISTER.register(modEventBus);

        FORGE_FRONTIER_REGISTRATE.registerEventListeners(modEventBus);

        ModItems.register();
        ModBlocks.register();
        ModBlockEntities.register();

        ForgeFrontierRecipeTypes.register(modEventBus);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ForgeFrontierClient.onCtorClient(modEventBus));




        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }


    public static ResourceLocation asResource(String name) {
        return new ResourceLocation(MODID, name);
    }
}
