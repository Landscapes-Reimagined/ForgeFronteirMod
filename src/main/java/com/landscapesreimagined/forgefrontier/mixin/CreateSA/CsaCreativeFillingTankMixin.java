package com.landscapesreimagined.forgefrontier.mixin.CreateSA;

import com.landscapesreimagined.forgefrontier.util.FillingFuelingTankProcedures;
import net.mcreator.createstuffadditions.item.CustomFluidHandlerItemStack;
import net.mcreator.createstuffadditions.procedures.CreativeFillingTankItemInInventoryTickProcedure;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

@Mixin(CreativeFillingTankItemInInventoryTickProcedure.class)
public class CsaCreativeFillingTankMixin {

    /**
     * @author gamma_02
     * @reason mcreator code sucks
     */
    @Overwrite(remap = false)
    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {


        if(entity == null){
            return;
        }

        if(!(entity instanceof Player player))
            return;

        var fillable = ItemTags.create(ResourceLocation.parse("create_sa:fillable"));
        var fuelable = ItemTags.create(ResourceLocation.parse("create_sa:fuelable"));


        for (int i = 0; i < 4; i++) {

            ItemStack mainhand = player.getMainHandItem();

            if(mainhand.is(fillable) ){
                FillingFuelingTankProcedures.fillingTank(world, player, mainhand);
            }
            if(mainhand.is(fuelable)){
                FillingFuelingTankProcedures.fuelingTank(world, player, mainhand);
            }

            ItemStack offhand = player.getMainHandItem();

            if(offhand.is(fillable) ){
                FillingFuelingTankProcedures.fillingTank(world, player, offhand);
            }
            if(offhand.is(fuelable)){
                FillingFuelingTankProcedures.fuelingTank(world, player, offhand);
            }


            ItemStack chestplate = player.getItemBySlot(EquipmentSlot.CHEST);

            if(chestplate.is(fillable)){
                FillingFuelingTankProcedures.fillingTank(world, player, chestplate);
            }
            if(chestplate.is(fuelable)){
                FillingFuelingTankProcedures.fuelingTank(world, player, chestplate);
            }


            var inventoryOptional = CuriosApi.getCuriosInventory(player);

            if(!inventoryOptional.isPresent())
                continue;

            var itemHandlerOptional = inventoryOptional.resolve();

            if(itemHandlerOptional.isEmpty())
                continue;

            var itemHandler = itemHandlerOptional.get();

            for(SlotResult jetpacks : itemHandler.findCurios((v) -> v.is(fillable))){
                FillingFuelingTankProcedures.fillingTank(world, player, jetpacks.stack());

            }

            for(SlotResult jetpacks : itemHandler.findCurios((v) -> v.is(fuelable))){
                FillingFuelingTankProcedures.fuelingTank(world, player, jetpacks.stack());

            }


        }


    }
}
