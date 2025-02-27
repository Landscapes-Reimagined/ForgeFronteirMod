package com.landscapesreimagined.forgefrontier.ponder;

import appeng.block.networking.ControllerBlock;
import appeng.block.networking.EnergyCellBlock;
import com.landscapesreimagined.forgefrontier.ModBlocks.EnergeticBlazeBurner;
import com.landscapesreimagined.forgefrontier.ModBlocks.ModBlockEntities.EnergeticBlazeBurnerBlockEntity;
import com.landscapesreimagined.forgefrontier.ModBlocks.ModBlocks;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import de.maxhenkel.pipez.blocks.tileentity.EnergyPipeTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;

import java.util.Arrays;

public class EnergeticBlazeBurnerPonder {

    public static void introScene(SceneBuilder scene, SceneBuildingUtil util){
        scene.title("energetic_blaze_burner_intro", "You and the Energetic Blaze Burner");
        scene.configureBasePlate(0, 0, 7);
        scene.showBasePlate();
        scene.idle(5);
        BlockPos burnerLocation = new BlockPos(3, 1, 3);
        scene.world.showSection(util.select.position(burnerLocation), Direction.DOWN);
        scene.idle(5);
        scene.overlay.showText(40).attachKeyFrame().text("This is your Energetic Blaze Burner. Right now, it's sleeping.").placeNearTarget().pointAt(util.vector.topOf(burnerLocation));
        scene.idle(50);
        scene.overlay.showText(60).attachKeyFrame().text("To wake it up, you have to give it energy, similar to giving a normal blaze burner fuel").attachKeyFrame().placeNearTarget().pointAt(util.vector.topOf(burnerLocation));
        scene.idle(70);
        BlockPos pipeOutputPos = new BlockPos(5, 1, 3);
        scene.world.showSection(util.select.fromTo(4, 1, 3, 5, 1, 4), Direction.DOWN);
        scene.world.modifyBlockEntityNBT(util.select.position(pipeOutputPos), EnergyPipeTileEntity.class, EnergeticBlazeBurnerPonder::setConnectedDirections, true);
//        scene.world.modifyBlockEntityNBT(util.select.position(pi), EnergyPipeTileEntity.class, EnergeticBlazeBurnerPonder::setConnectedDirections, true);
        scene.idle(30);
        scene.overlay.showText(80).text("Once it has energy, here from a creative generator, it will start to wake up.").attachKeyFrame().placeNearTarget().pointAt(util.vector.topOf(burnerLocation));
        scene.idle(30);
        scene.world.modifyBlockEntity(burnerLocation, EnergeticBlazeBurnerBlockEntity.class, (b) -> EnergeticBlazeBurnerPonder.setEnergyLevel(b, EnergeticBlazeBurner.EnergyLevel.SLEEPY));
        scene.world.modifyBlock(burnerLocation, (blockState -> blockState.setValue(EnergeticBlazeBurner.ENERGY_LEVEL, EnergeticBlazeBurner.EnergyLevel.SLEEPY)), false);
        scene.idle(30);
        scene.world.modifyBlockEntity(burnerLocation, EnergeticBlazeBurnerBlockEntity.class, (b) -> EnergeticBlazeBurnerPonder.setEnergyLevel(b, EnergeticBlazeBurner.EnergyLevel.CRYSTALLIZE));
        scene.world.modifyBlock(burnerLocation, (blockState -> blockState.setValue(EnergeticBlazeBurner.ENERGY_LEVEL, EnergeticBlazeBurner.EnergyLevel.CRYSTALLIZE)), false);
        scene.idle(40);
        scene.overlay.showText(80).text("This state it is in now is for recipes that say \"Crystalization\". To make it able to execute infusion recipes, we have to give it Applied Energistics power.").attachKeyFrame().placeNearTarget().pointAt(util.vector.topOf(burnerLocation));
        scene.idle(90);
        BlockPos energyCellPos = new BlockPos(1, 2, 4);
        BlockPos controllerPos = new BlockPos(1, 1, 4);
        scene.world.showSection(util.select.fromTo(2, 1, 3, 1, 1, 3), Direction.SOUTH);
        scene.idle(10);
        scene.world.showSection(util.select.fromTo(controllerPos,energyCellPos), Direction.NORTH);

        scene.world.modifyBlock(energyCellPos, (cell) -> cell.setValue(EnergyCellBlock.ENERGY_STORAGE, EnergyCellBlock.MAX_FULLNESS), false);
        scene.world.modifyBlock(controllerPos, (controller) -> controller.setValue(ControllerBlock.CONTROLLER_STATE, ControllerBlock.ControllerBlockState.online), false);
        scene.idle(10);
        scene.world.modifyBlockEntity(burnerLocation, EnergeticBlazeBurnerBlockEntity.class, (b) -> EnergeticBlazeBurnerPonder.setEnergyLevel(b, EnergeticBlazeBurner.EnergyLevel.INFUSE));
        scene.world.modifyBlock(burnerLocation, (blockState -> blockState.setValue(EnergeticBlazeBurner.ENERGY_LEVEL, EnergeticBlazeBurner.EnergyLevel.INFUSE)), false);
        scene.overlay.showText(60).text("Now that's an energetic blaze burner who's ready to do some processing!").attachKeyFrame().placeNearTarget().pointAt(util.vector.topOf(burnerLocation));
        scene.idle(70);
        scene.overlay.showText(80).text("Like the normal blaze burner, to get energetic mixing recipes going we need a mixer and basin setup.").attachKeyFrame().placeNearTarget().pointAt(util.vector.centerOf(burnerLocation.above(2)));
        scene.idle(30);
        scene.world.showSection(util.select.layersFrom(2).substract(util.select.position(energyCellPos)), Direction.DOWN);
        scene.idle(10);
        scene.world.setKineticSpeed(util.select.layersFrom(2).substract(util.select.position(energyCellPos)), 64);
        scene.idle(50);





//        scene.world.showSection(util.select.layersFrom(2).substract(util.select.position(energyCellPos)), Direction.DOWN);

    }

    private static void setEnergyLevel(EnergeticBlazeBurnerBlockEntity burner, EnergeticBlazeBurner.EnergyLevel level){
        switch(level ){
            case SLEEPING, NONE: burner.setFE(0);
            case SLEEPY: burner.setFE(10000);
            case CRYSTALLIZE: burner.setFE(500000);
            case INFUSE: {
                burner.setFE(500000);
                burner.setAE(5000000);
            }
        }
    }



    private static void setConnectedDirections(CompoundTag pipeTag){
        var extractingSides = pipeTag.getByteArray("ExtractingSides");

        if(!Arrays.equals(extractingSides, new byte[]{})){
            pipeTag.putByteArray("ExtractingSides", new byte[]{0,0,0,1,0,0});
        }
    }
}
