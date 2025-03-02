package com.landscapesreimagined.forgefrontier.mixin.CreateSA;

import com.landscapesreimagined.forgefrontier.util.FillingFuelingTankProcedures;
import net.mcreator.createstuffadditions.item.CustomFluidHandlerItemStack;
import net.mcreator.createstuffadditions.procedures.SmallFuelingTankItemInInventoryTickProcedure;
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

@Mixin(SmallFuelingTankItemInInventoryTickProcedure.class)
public class CsaFuelingTankMixin {

    /**
     * @author gamma_02
     * @reason mcreator code sucks
     */
    @Overwrite(remap = false)
    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, ItemStack itemstack) {
        if(entity == null){
            return;
        }

        if(!(entity instanceof Player player))
            return;

        ItemStack Itemtank = itemstack;
        double fluidAmount;
        fluidAmount = CustomFluidHandlerItemStack.isTankEmpty(player, itemstack);
        var fuelable = ItemTags.create(ResourceLocation.parse("create_sa:fuelable"));

        if(fluidAmount <= 0){
            return;
        }

        for (int i = 0; i < 4; i++) {
            fluidAmount = CustomFluidHandlerItemStack.isTankEmpty(player, Itemtank);

            ItemStack mainhand = player.getMainHandItem();

            if(mainhand.is(fuelable) && fluidAmount > 0){
                FillingFuelingTankProcedures.fuelingTank(world, player, mainhand, Itemtank);
            }

            fluidAmount = CustomFluidHandlerItemStack.isTankEmpty(player, Itemtank);

            ItemStack offhand = player.getMainHandItem();

            if(offhand.is(fuelable) && fluidAmount > 0){
                FillingFuelingTankProcedures.fuelingTank(world, player, offhand, Itemtank);
            }

            fluidAmount = CustomFluidHandlerItemStack.isTankEmpty(player, Itemtank);

            ItemStack chestplate = player.getItemBySlot(EquipmentSlot.CHEST);

            if(chestplate.is(fuelable) && fluidAmount > 0){
                FillingFuelingTankProcedures.fuelingTank(world, player, chestplate, Itemtank);
            }

            var inventoryOptional = CuriosApi.getCuriosInventory(player);

            if(!inventoryOptional.isPresent())
                continue;

            var itemHandlerOptional = inventoryOptional.resolve();

            if(itemHandlerOptional.isEmpty())
                continue;

            var itemHandler = itemHandlerOptional.get();

            for(SlotResult jetpacks : itemHandler.findCurios((v) -> v.is(fuelable))){
                fluidAmount = CustomFluidHandlerItemStack.isTankEmpty(player, Itemtank);

                if(fluidAmount > 0)
                    FillingFuelingTankProcedures.fuelingTank(world, player, jetpacks.stack(), Itemtank);

            }


        }


    }
}
