package com.landscapesreimagined.forgefrontier.ModItems;

import com.landscapesreimagined.forgefrontier.ForgeFrontier;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> MOD_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ForgeFrontier.MODID);

    public static final RegistryObject<Item> ANIM_TEST = MOD_ITEMS.register("animation_test", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> NOVA_ANIM_TEST = MOD_ITEMS.register("nova_animation_test", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GLOBE_ANIM_TEST = MOD_ITEMS.register("globe_animation_test", () -> new Item(new Item.Properties()));
}
