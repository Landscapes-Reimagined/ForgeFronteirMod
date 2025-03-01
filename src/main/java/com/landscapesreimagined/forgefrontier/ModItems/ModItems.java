package com.landscapesreimagined.forgefrontier.ModItems;

import com.landscapesreimagined.forgefrontier.ForgeFrontier;
import com.landscapesreimagined.forgefrontier.ModItems.ModBlockItems.EnergeticBlazeBurnerBlockItem;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockItem;
import com.simibubi.create.foundation.data.AssetLookup;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


import static com.landscapesreimagined.forgefrontier.ForgeFrontier.FORGE_FRONTIER_REGISTRATE;


public class ModItems {
    public static final DeferredRegister<Item> MOD_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ForgeFrontier.MODID);

    public static final RegistryObject<Item> ANIM_TEST = MOD_ITEMS.register("animation_test", () -> new Item(new Item.Properties()));

    public static final ItemEntry<Item> FORGE_ENERGY = FORGE_FRONTIER_REGISTRATE.item("fe_icon", Item::new).lang("Forge Energy Icon").register();
//monkey made me remove these :(
//    public static final RegistryObject<Item> NOVA_ANIM_TEST = MOD_ITEMS.register("nova_animation_test", () -> new Item(new Item.Properties()));
//    public static final RegistryObject<Item> GLOBE_ANIM_TEST = MOD_ITEMS.register("globe_animation_test", () -> new Item(new Item.Properties()));

    public static final ItemEntry<EnergeticBlazeBurnerBlockItem> EMPTY_BLAZE_BURNER =
            FORGE_FRONTIER_REGISTRATE.item("empty_energetic_blaze_burner", EnergeticBlazeBurnerBlockItem::empty)
//                    .model(AssetLookup.customBlockItemModel("blaze_burner", "block"))
//                    .model((c, p) ->{
//                        p.getExistingFile(new ResourceLocation("create:item/blaze_burner");
//                    })
                    .model((c, p) -> {
                        String path = "block";
                        for (String string : new String[]{"blaze_burner", "block"})
                            path += "/" + ("_".equals(string) ? c.getName() : string);
                        p.withExistingParent(c.getName(), p.modLoc(path));
                    })
                    .register();

    public static void register() {};
}
