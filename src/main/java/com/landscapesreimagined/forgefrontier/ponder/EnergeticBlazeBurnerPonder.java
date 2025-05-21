package com.landscapesreimagined.forgefrontier.ponder;

import appeng.block.networking.ControllerBlock;
import appeng.block.networking.EnergyCellBlock;
import appeng.core.definitions.AEItems;
import com.google.common.collect.ImmutableList;
import com.landscapesreimagined.forgefrontier.ModBlocks.EnergeticBlazeBurner;
import com.landscapesreimagined.forgefrontier.ModBlocks.ModBlockEntities.EnergeticBlazeBurnerBlockEntity;
import com.landscapesreimagined.forgefrontier.ModItems.ModItems;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlockEntity;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import de.maxhenkel.pipez.blocks.PipeBlock;
import de.maxhenkel.pipez.blocks.tileentity.EnergyPipeTileEntity;
import net.createmod.catnip.data.IntAttached;
import net.createmod.catnip.math.Pointing;
import net.createmod.catnip.nbt.NBTHelper;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.createmod.ponder.foundation.element.InputWindowElement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;

public class EnergeticBlazeBurnerPonder {

    public static void introScene(SceneBuilder builder, SceneBuildingUtil util){
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);

        scene.title("energetic_blaze_burner_intro", "You and the Energetic Blaze Burner");
        scene.configureBasePlate(0, 0, 7);
        scene.showBasePlate();
        scene.idle(5);
        BlockPos burnerLocation = new BlockPos(3, 1, 3);
        scene.world().showSection(util.select().position(burnerLocation), Direction.DOWN);
        scene.idle(5);
        scene.overlay().showText(40).attachKeyFrame().text("This is your Energetic Blaze Burner. Right now, it's sleeping.").placeNearTarget().pointAt(util.vector().topOf(burnerLocation));
        scene.idle(50);
        scene.overlay().showText(60).attachKeyFrame().text("To wake it up, you have to give it energy, similar to giving a normal blaze burner fuel").attachKeyFrame().placeNearTarget().pointAt(util.vector().topOf(burnerLocation));
        scene.idle(70);
        BlockPos pipeOutputPos = new BlockPos(5, 1, 3);
        scene.world().showSection(util.select().fromTo(4, 1, 3, 5, 1, 4), Direction.DOWN);
        scene.world().modifyBlockEntityNBT(util.select().position(pipeOutputPos), EnergyPipeTileEntity.class, EnergeticBlazeBurnerPonder::setConnectedDirections, true);
        scene.world().modifyBlock(pipeOutputPos, (pipeState) -> pipeState.setValue(PipeBlock.SOUTH, true), false);
        scene.world().modifyBlockEntity(pipeOutputPos, EnergyPipeTileEntity.class, (pipe) -> pipe.load(setConnectedDirections(pipe.serializeNBT())));

//        scene.world().modifyBlockEntityNBT(util.select().position(pi), EnergyPipeTileEntity.class, EnergeticBlazeBurnerPonder::setConnectedDirections, true);
        scene.idle(30);
        scene.overlay().showText(80).text("Once it has energy, here from a creative generator, it will start to wake up.").attachKeyFrame().placeNearTarget().pointAt(util.vector().topOf(burnerLocation));
        scene.idle(30);
        scene.world().modifyBlockEntity(burnerLocation, EnergeticBlazeBurnerBlockEntity.class, (b) -> EnergeticBlazeBurnerPonder.setEnergyLevel(b, EnergeticBlazeBurner.EnergyLevel.SLEEPY));
        scene.world().modifyBlock(burnerLocation, (blockState -> blockState.setValue(EnergeticBlazeBurner.ENERGY_LEVEL, EnergeticBlazeBurner.EnergyLevel.SLEEPY)), false);
        scene.idle(30);
        scene.world().modifyBlockEntity(burnerLocation, EnergeticBlazeBurnerBlockEntity.class, (b) -> EnergeticBlazeBurnerPonder.setEnergyLevel(b, EnergeticBlazeBurner.EnergyLevel.CRYSTALLIZE));
        scene.world().modifyBlock(burnerLocation, (blockState -> blockState.setValue(EnergeticBlazeBurner.ENERGY_LEVEL, EnergeticBlazeBurner.EnergyLevel.CRYSTALLIZE)), false);
        scene.idle(40);
        scene.overlay().showText(80).text("This state it is in now is for recipes that say \"Crystalization\". To make it able to execute infusion recipes, we have to give it Applied Energistics power.").attachKeyFrame().placeNearTarget().pointAt(util.vector().topOf(burnerLocation));
        scene.idle(90);
        BlockPos energyCellPos = new BlockPos(1, 2, 4);
        BlockPos controllerPos = new BlockPos(1, 1, 4);
        scene.world().showSection(util.select().fromTo(2, 1, 3, 1, 1, 3), Direction.SOUTH);
        scene.idle(10);
        scene.world().showSection(util.select().fromTo(controllerPos,energyCellPos), Direction.NORTH);

        scene.world().modifyBlock(energyCellPos, (cell) -> cell.setValue(EnergyCellBlock.ENERGY_STORAGE, EnergyCellBlock.MAX_FULLNESS), false);
        scene.world().modifyBlock(controllerPos, (controller) -> controller.setValue(ControllerBlock.CONTROLLER_STATE, ControllerBlock.ControllerBlockState.online), false);
        scene.idle(10);
        scene.world().modifyBlockEntity(burnerLocation, EnergeticBlazeBurnerBlockEntity.class, (b) -> EnergeticBlazeBurnerPonder.setEnergyLevel(b, EnergeticBlazeBurner.EnergyLevel.INFUSE));
        scene.world().modifyBlock(burnerLocation, (blockState -> blockState.setValue(EnergeticBlazeBurner.ENERGY_LEVEL, EnergeticBlazeBurner.EnergyLevel.INFUSE)), false);
        scene.idle(20);
        scene.overlay().showText(80).text("Like the normal blaze burner, to get energetic mixing recipes going we need a mixer and basin setup. Proceed to the next scene to see how it works.").attachKeyFrame().placeNearTarget().pointAt(util.vector().centerOf(burnerLocation.above(2)));
        scene.idle(10);
        Selection Kinetics = util.select().fromTo(3,4,3, 4, 4, 3);
        BlockPos basin = burnerLocation.above();
        scene.world().modifyBlockEntity(basin, BasinBlockEntity.class, (b) -> b.onWrenched(Direction.NORTH));

        scene.world().showSection(Kinetics, Direction.NORTH);
        scene.world().showSection(util.select().position(basin), Direction.SOUTH);
        scene.idle(1);
        scene.world().setKineticSpeed(util.select().layersFrom(2).substract(util.select().position(energyCellPos)), 64);
        scene.idle(90);

//        scene.world().showSection(util.select().layersFrom(2).substract(util.select().position(energyCellPos)), Direction.DOWN);

    }

    public static void mixerScene(SceneBuilder builder, SceneBuildingUtil util){
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);

        scene.title("energetic_mixing", "Energetic Mixing");

        scene.configureBasePlate(0, 0, 7);
        scene.showBasePlate();
        scene.idle(5);
        BlockPos energyCellPos = new BlockPos(1, 2, 4);
        BlockPos controllerPos = new BlockPos(1, 1, 4);



        BlockPos burnerLocation = new BlockPos(3, 1, 3);

        BlockPos basin = burnerLocation.above();
        Vec3 topOfBasin = util.vector().topOf(basin);
        Selection Kinetics = util.select().fromTo(3,4,3, 4, 4, 3);
        BlockPos mixerLocation = basin.above(2);

        BlockPos pipeOutputPos = new BlockPos(5, 1, 3);
        Selection energySel = util.select().fromTo(4, 1, 3, 5, 1, 4);
        scene.world().showSection(util.select().position(burnerLocation), Direction.DOWN);
        scene.idle(2);
        scene.world().showSection(energySel, Direction.WEST);
        scene.world().modifyBlockEntityNBT(util.select().position(pipeOutputPos), EnergyPipeTileEntity.class, EnergeticBlazeBurnerPonder::setConnectedDirections, true);
//        scene.world().modifyBlock(pipeOutputPos, (pipeState) -> pipeState.setValue(PipeBlock.SOUTH, true), false);
        scene.world().modifyBlockEntity(pipeOutputPos, EnergyPipeTileEntity.class, (pipe) -> pipe.setExtracting(Direction.SOUTH, true));
        scene.world().modifyBlockEntity(burnerLocation, EnergeticBlazeBurnerBlockEntity.class, (b) -> EnergeticBlazeBurnerPonder.setEnergyLevel(b, EnergeticBlazeBurner.EnergyLevel.CRYSTALLIZE));
        scene.world().modifyBlock(burnerLocation, (blockState -> blockState.setValue(EnergeticBlazeBurner.ENERGY_LEVEL, EnergeticBlazeBurner.EnergyLevel.CRYSTALLIZE)), false);

        scene.idle(5);

        scene.world().showSection(Kinetics, Direction.NORTH);
        scene.world().showSection(util.select().position(basin), Direction.SOUTH);
        scene.world().modifyBlockEntity(basin, BasinBlockEntity.class, (b) -> b.onWrenched(Direction.NORTH));

        scene.idle(1);
        scene.world().setKineticSpeed(util.select().layersFrom(2).substract(util.select().position(energyCellPos)), 64);
        scene.idle(5);
        scene.overlay().showText(40).text("The Energetic Blaze Burner is a replacement for AdvancedAE's Reaction Chamber").attachKeyFrame().placeNearTarget().pointAt(util.vector().topOf(burnerLocation));
        scene.idle(50);
        scene.overlay().showText(60).text("Most recipes require the Energetic Blaze Burner to be heated. This works just like a normal Blaze Burner").attachKeyFrame().placeNearTarget().pointAt(util.vector().topOf(burnerLocation));
        scene.idle(70);
        scene.world().hideSection(Kinetics, Direction.SOUTH);
        scene.world().hideSection(util.select().position(basin), Direction.NORTH);
//        scene.world().setBlock();
        scene.idle(20);
        scene.overlay().showControls(util.vector().topOf(burnerLocation), Pointing.DOWN,  15).rightClick()
                .withItem(new ItemStack(Items.OAK_PLANKS));
        scene.idle(7);
        scene.world().modifyBlock(burnerLocation, s -> s.setValue(BlazeBurnerBlock.HEAT_LEVEL, BlazeBurnerBlock.HeatLevel.KINDLED), false);
        scene.idle(20);

        scene.overlay().showText(70)
                .attachKeyFrame()
                .text("The Energetic Blaze has to be fed with flammable items")
                .pointAt(util.vector().blockSurface(burnerLocation, Direction.WEST))
                .placeNearTarget();
        scene.idle(80);

        scene.idle(20);
        scene.overlay().showControls(util.vector().topOf(burnerLocation), Pointing.DOWN, 30)
                .rightClick()
                .withItem(AllItems.BLAZE_CAKE.asStack());
        scene.idle(7);
        scene.world().modifyBlock(burnerLocation, s -> s.setValue(BlazeBurnerBlock.HEAT_LEVEL, BlazeBurnerBlock.HeatLevel.SEETHING), false);
        scene.idle(20);

        scene.overlay().showText(80)
                .attachKeyFrame()
                .colored(PonderPalette.MEDIUM)
                .text("With a Blaze Cake or Biodiesel, the Burner can reach an even stronger level of heat")
                .pointAt(util.vector().blockSurface(burnerLocation, Direction.WEST))
                .placeNearTarget();
        scene.idle(90);

        scene.overlay().showText(100)
                .attachKeyFrame()
                .text("The feeding process can be automated using Deployers or Mechanical Arms, and modpack version 2.3.0 will allow for fluid input")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(burnerLocation, Direction.UP));
        scene.idle(110);

        scene.world().showSection(Kinetics, Direction.NORTH);
        scene.world().showSection(util.select().position(basin), Direction.SOUTH);
        scene.idle(1);
        scene.world().setKineticSpeed(Kinetics, 64);
        scene.idle(5);
        scene.overlay().showText(60).text("This processing is just like any other mechanical mixer processing").pointAt(topOfBasin).placeNearTarget().attachKeyFrame();
        scene.idle(70);

        ItemStack certusQuartz = AEItems.CERTUS_QUARTZ_CRYSTAL_CHARGED.stack();
        ItemStack quartz = Items.QUARTZ.getDefaultInstance();
        ItemStack redstone = Items.REDSTONE.getDefaultInstance();
        redstone.setCount(16);
        quartz.setCount(16);
        certusQuartz.setCount(16);

        ItemStack fluixQuartz = AEItems.FLUIX_CRYSTAL.stack(16);

        ItemStack water = Items.WATER_BUCKET.getDefaultInstance();
        ItemStack fe = ModItems.FORGE_ENERGY.asStack();



        scene.overlay().showControls(topOfBasin, Pointing.LEFT, 30).withItem(certusQuartz);
        scene.overlay().showControls(topOfBasin, Pointing.RIGHT, 30).withItem(quartz);
        scene.overlay().showControls(topOfBasin.add(0, 0.5, 0), Pointing.DOWN, 30).withItem(redstone);
        scene.idle(40);
        scene.overlay().showText(60).text("However, they use FE as well").attachKeyFrame().placeNearTarget().pointAt(Vec3.atBottomCenterOf(basin));
        scene.idle(70);
        scene.overlay().showControls(topOfBasin, Pointing.LEFT, 30).withItem(water);
        scene.overlay().showControls(topOfBasin, Pointing.RIGHT, 30).withItem(fe);
        scene.idle(30);

        Class<MechanicalMixerBlockEntity> type = MechanicalMixerBlockEntity.class;
        scene.world().modifyBlockEntity(mixerLocation, type, pte -> pte.startProcessingBasin());
        scene.world().createItemOnBeltLike(basin, Direction.UP, certusQuartz);
        scene.world().createItemOnBeltLike(basin, Direction.UP, quartz);
        scene.world().createItemOnBeltLike(basin, Direction.UP, redstone);
        scene.idle(40);
        scene.world().modifyBlockEntity(basin, BasinBlockEntity.class, (b) -> b.onWrenched(Direction.NORTH));
        scene.world().showSection(util.select().fromTo(burnerLocation.north(), new BlockPos(7, 1, 0)), Direction.SOUTH);
        scene.world().setKineticSpeed(util.select().fromTo(burnerLocation.north(), new BlockPos(7, 1, 0)), -48);
        scene.idle(40);

        scene.world().modifyBlockEntityNBT(util.select().position(basin), BasinBlockEntity.class, nbt -> {
            nbt.put("VisualizedItems",
                    NBTHelper.writeCompoundList(ImmutableList.of(IntAttached.with(1, fluixQuartz)), ia -> ia.getValue()
                            .serializeNBT()));
        });
        scene.world().modifyBlockEntity(basin, BasinBlockEntity.class, (b) -> b.inputInventory.clearContent());

        scene.idle(4);
        scene.world().createItemOnBelt(util.grid().at(3, 1, 2), Direction.UP, fluixQuartz);
        scene.idle(35);
        scene.world().modifyBlockEntity(basin, BasinBlockEntity.class, (b) -> b.onWrenched(Direction.NORTH));
        scene.world().hideSection(util.select().fromTo(burnerLocation.north(), new BlockPos(7, 1, 0)), Direction.NORTH);
        scene.idle(10);

        scene.overlay().showText(60)
                .text("The next scene will cover the energy use mechanics")
                .pointAt(util.vector().topOf(burnerLocation))
                .placeNearTarget()
                .attachKeyFrame();

        scene.idle(70);


    }

    public static void energyUseScene(SceneBuilder builder, SceneBuildingUtil util){
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);


        scene.title("energetic_energy_use", "Energetic Blaze's Energy Use");

        scene.configureBasePlate(0, 0, 7);
        scene.showBasePlate();
        scene.idle(5);
        BlockPos energyCellPos = new BlockPos(1, 2, 4);
        BlockPos controllerPos = new BlockPos(1, 1, 4);



        BlockPos burnerLocation = new BlockPos(3, 1, 3);

        BlockPos basin = burnerLocation.above();
        Vec3 topOfBasin = util.vector().topOf(basin);
        Selection Kinetics = util.select().fromTo(3,4,3, 4, 4, 3);
        BlockPos mixerLocation = basin.above(2);

        BlockPos pipeOutputPos = new BlockPos(5, 1, 3);
        Selection energySel = util.select().fromTo(4, 1, 3, 5, 1, 4);

        scene.world().showSection(util.select().position(burnerLocation), Direction.DOWN);
        scene.idle(5);

        scene.world().showSection(energySel, Direction.WEST);
        scene.world().showSection(util.select().fromTo(2, 1, 3, 1, 1, 3).add(util.select().fromTo(controllerPos,energyCellPos)), Direction.EAST);
        scene.idle(5);
        scene.world().modifyBlock(energyCellPos, (cell) -> cell.setValue(EnergyCellBlock.ENERGY_STORAGE, EnergyCellBlock.MAX_FULLNESS), false);
        scene.world().modifyBlock(controllerPos, (controller) -> controller.setValue(ControllerBlock.CONTROLLER_STATE, ControllerBlock.ControllerBlockState.online), false);
        scene.idle(5);

        scene.world().modifyBlock(burnerLocation, (state) -> state.setValue(EnergeticBlazeBurner.ENERGY_LEVEL, EnergeticBlazeBurner.EnergyLevel.INFUSE), false);
        scene.idle(10);

        scene.overlay().showText(60).text("Energetic Blaze Burners use Forge Energy and AE to complete a recipe")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector().topOf(burnerLocation));
        scene.idle(70);
        scene.overlay().showText(80).text("They have an internal energy buffer of 500,000 FE and 1,000,000 AE")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector().topOf(burnerLocation));
        scene.idle(90);
        scene.idle(20);
        scene.overlay().showControls(util.vector().topOf(burnerLocation), Pointing.DOWN, 30).rightClick()
                .withItem(AllItems.BLAZE_CAKE.asStack());
        scene.idle(7);
        scene.world().modifyBlock(burnerLocation, s -> s.setValue(BlazeBurnerBlock.HEAT_LEVEL, BlazeBurnerBlock.HeatLevel.SEETHING), false);
        scene.idle(33);
        scene.overlay().showText(90).text("Which increases to 1,500,000 AE if the Energetic Blaze Burner is superheated, and 750,000 FE if the Energetic Blaze is Infusing")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector().topOf(burnerLocation));
        scene.idle(100);
        scene.world().setKineticSpeed(Kinetics, 32);
        scene.world().showSection(Kinetics, Direction.NORTH);
        scene.world().showSection(util.select().position(basin), Direction.SOUTH);
        scene.world().modifyBlockEntity(basin, BasinBlockEntity.class, (b) -> b.onWrenched(Direction.NORTH));
        scene.idle(10);
        scene.overlay().showText(70)
                .text("The energy they use per tick is determined by the speed of the mixer")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector().centerOf(mixerLocation));

        scene.idle(80);


        scene.world().modifyBlockEntity(
                mixerLocation,
                MechanicalMixerBlockEntity.class,
                MechanicalMixerBlockEntity::startProcessingBasin
        );

        scene.overlay().showText(70)
                .text("However, the total §o§namount§r of energy used is determined by the recipe you are making")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(util.vector().centerOf(basin));
        scene.idle(80);

        scene.overlay().showText(80)
                .text("For example, a Crystallization recipe with a mixer rotating at 32 RPM and a total energy use of 50,000 FE will use 820 FE per tick")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(util.vector().centerOf(basin));
        scene.idle(90);

        scene.overlay().showText(60)
                .text("When executing an Infusion recipe, the amount of FE used is halved")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(util.vector().centerOf(controllerPos));
        scene.idle(70);

        scene.overlay().showText(100)
                .text("Therefor, an Energetic Infusion recipe with a mixer rotating at 128 RPM and a total energy use of 500,000 AE will use 16,129.032 AE per tick, and 8065 FE per tick")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(util.vector().centerOf(basin));
        scene.idle(110);





//        scene.world().showSection(, Direction.NORTH);

    }








    private static void setEnergyLevel(EnergeticBlazeBurnerBlockEntity burner, EnergeticBlazeBurner.EnergyLevel level){
        switch(level ){
            case SLEEPING, NONE: burner.setFE(0);
            case SLEEPY: burner.setFE(10000);
            case CRYSTALLIZE: burner.setFE(500000);
            case INFUSE: {
                burner.setFE(500000);
//                burner.setAE(5000000);
            }
        }
    }



    private static CompoundTag setConnectedDirections(CompoundTag pipeTag){
        var extractingSides = pipeTag.getByteArray("ExtractingSides");

        if(!Arrays.equals(extractingSides, new byte[]{})){
            pipeTag.putByteArray("ExtractingSides", new byte[]{0,0,0,1,0,0});
        }

        return pipeTag;
    }
}
