package com.landscapesreimagined.forgefrontier.ModBlocks.ModBlockEntities;

import com.landscapesreimagined.forgefrontier.Config;
import com.landscapesreimagined.forgefrontier.ModBlocks.EnergeticBlazeBurner;
import com.landscapesreimagined.forgefrontier.mixin.Create.CurrentBasinRecipeAccessor;
import com.landscapesreimagined.forgefrontier.recipies.EnergeticMixingRecipe;
import com.landscapesreimagined.forgefrontier.util.AE2InternalEnergyBuffer;
import com.landscapesreimagined.forgefrontier.util.MachineInternalEnergyBuffer;
import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlockEntity;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

public class EnergeticBlazeBurnerBlockEntity extends BlazeBurnerBlockEntity implements IEnergyStorage {

    protected final MachineInternalEnergyBuffer energyBuffer;
    protected LazyOptional<IEnergyStorage> lazyBuffer;

    protected final AE2InternalEnergyBuffer internalAEBuffer;
    @Nullable
    private EnergeticMixingRecipe currentRecipe = null;

    public EnergeticBlazeBurnerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.energyBuffer = new MachineInternalEnergyBuffer(Config.ENERGETIC_BLAZE_FE_CAPACITY.get(), Config.ENERGETIC_BLAZE_MAX_FE_RECEIVE.get());
        this.lazyBuffer = LazyOptional.of(() -> this.energyBuffer);
        this.internalAEBuffer = new AE2InternalEnergyBuffer(Config.ENERGETIC_BLAZE_AE_CAPACITY.get(), Config.ENERGETIC_BLAZE_MAX_AE_RECEIVE.get(), 0);
    }


    //TODO: recipes and requiredEnergy
    @Override
    public void tick() {
        super.tick();

        //do side-agnostic processes


        if(level == null){
            return;
        }

        boolean stateChanged = false;
        boolean stateChangedUp = false;

        if(this.internalAEBuffer.getAECurrentPower() > 0 && this.energyBuffer.energy >= EnergeticBlazeBurner.EnergyLevel.INFUSE.getMinFE()){

            stateChanged = level.getBlockState(this.worldPosition).getValue(EnergeticBlazeBurner.ENERGY_LEVEL) != EnergeticBlazeBurner.EnergyLevel.INFUSE;
            if(stateChanged) stateChangedUp = true;

            this.level.setBlock(this.worldPosition, this.level.getBlockState(this.getBlockPos()).setValue(EnergeticBlazeBurner.ENERGY_LEVEL, EnergeticBlazeBurner.EnergyLevel.INFUSE), 0b10);

        }else if(this.internalAEBuffer.getAECurrentPower() <= 0 && this.energyBuffer.energy > EnergeticBlazeBurner.EnergyLevel.INFUSE.getMinFE() / 2){

            EnergeticBlazeBurner.EnergyLevel oldEnergyLevel = level.getBlockState(this.worldPosition).getValue(EnergeticBlazeBurner.ENERGY_LEVEL);

            stateChanged = level.getBlockState(this.worldPosition).getValue(EnergeticBlazeBurner.ENERGY_LEVEL) != EnergeticBlazeBurner.EnergyLevel.CRYSTALLIZE;

            if(stateChanged && oldEnergyLevel != EnergeticBlazeBurner.EnergyLevel.CRYSTALLIZE) stateChangedUp = true;

            this.level.setBlock(this.worldPosition, this.level.getBlockState(this.getBlockPos()).setValue(EnergeticBlazeBurner.ENERGY_LEVEL, EnergeticBlazeBurner.EnergyLevel.CRYSTALLIZE), 0b10);

        }else if(this.internalAEBuffer.getAECurrentPower() <= 0 && this.energyBuffer.energy > EnergeticBlazeBurner.EnergyLevel.CRYSTALLIZE.getMinFE()){

            EnergeticBlazeBurner.EnergyLevel oldEnergyLevel = level.getBlockState(this.worldPosition).getValue(EnergeticBlazeBurner.ENERGY_LEVEL);

            stateChanged = level.getBlockState(this.worldPosition).getValue(EnergeticBlazeBurner.ENERGY_LEVEL) != EnergeticBlazeBurner.EnergyLevel.CRYSTALLIZE;

            if(stateChanged && oldEnergyLevel.ordinal() > EnergeticBlazeBurner.EnergyLevel.SLEEPY.ordinal()) stateChangedUp = true;

            this.level.setBlock(this.worldPosition, this.level.getBlockState(this.getBlockPos()).setValue(EnergeticBlazeBurner.ENERGY_LEVEL, EnergeticBlazeBurner.EnergyLevel.SLEEPY), 0b10);

        }else if(this.internalAEBuffer.getAECurrentPower() <= 0 && this.energyBuffer.getEnergyStored() == 0){


            this.level.setBlock(this.worldPosition, this.level.getBlockState(this.getBlockPos()).setValue(EnergeticBlazeBurner.ENERGY_LEVEL, EnergeticBlazeBurner.EnergyLevel.SLEEPING), 0b10);

        }


        if(level.isClientSide){
            //do client side things

            return;
        }

        //do server-side things


        //set the recipe so we can do Power Stuff:tm:
        BlockPos position = this.worldPosition;

        BlockEntity above = level.getBlockEntity(position.above());

        MechanicalMixerBlockEntity potentialMixer = null;

        if(above instanceof BasinBlockEntity basin){
            potentialMixer = this.doBasinTesting(basin);
        }

        if(this.currentRecipe != null && potentialMixer != null){

            int totalRecipeEnergy = this.currentRecipe.getRequiredEnergy();
            int totalProcessTime = this.currentRecipe.getProcessingDuration();
            double realRecipeSpeed = totalProcessTime / (potentialMixer.getSpeed() * 20);

            double FEperTick = totalRecipeEnergy / realRecipeSpeed;

            this.energyBuffer.internalExtractEnergy((int) FEperTick, false, MachineInternalEnergyBuffer.ExtractionSource.INTERNAL);

            if(this.currentRecipe.getRequiredEnergyLevel().testEnergeticBlazeBurner(EnergeticBlazeBurner.EnergyLevel.INFUSE)){
                //
//                this.internalAEBuffer.extractAEPower()
            }

        }










    }

    @Nullable
    private MechanicalMixerBlockEntity doBasinTesting(BasinBlockEntity basin) {

        Level world = this.level;

        BlockPos basinPos = basin.getBlockPos();

        BlockPos mixerPos = basinPos.above(2);

        if(!(world.getBlockEntity(mixerPos) instanceof MechanicalMixerBlockEntity mixer)) return null;

        Recipe<?> recipe = ((CurrentBasinRecipeAccessor) mixer).getCurrentRecipe();

        if(recipe instanceof EnergeticMixingRecipe energeticMixingRecipe){
            this.currentRecipe = energeticMixingRecipe;
        }
        return mixer;

    }

//    public EnergeticBlazeBurner.EnergyLevel getEnergyLevel(){
//
//    }


    public LerpedFloat getHeadAnimation(){
        return this.headAnimation;
    }

    public LerpedFloat getHeadAngle(){
        return this.headAngle;
    }

    public boolean hasGoggles(){
        return this.goggles;
    }

    public boolean hasHat(){
        return this.hat;
    }


    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        if(cap == ForgeCapabilities.ENERGY){

            if(!sideValidForEnergy(side)){
                return super.getCapability(cap, side);
            }

            return this.lazyBuffer.cast();
        }

        return super.getCapability(cap, side);
    }


    public boolean sideValidForEnergy(Direction side){

        if(side == Direction.UP){
            return false;
        }

        return true;
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("EnergyBuffer", this.energyBuffer.writeToTag());
        compound.put("AEEnergyBuffer", this.internalAEBuffer.writeToTag());
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);

        CompoundTag energyBufferTag = compound.getCompound("EnergyBuffer");
        CompoundTag AEBufferTag = compound.getCompound("AEEnergyBuffer");

        this.energyBuffer.readTag(energyBufferTag);
        this.internalAEBuffer.readTag(AEBufferTag);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return this.energyBuffer.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return this.energyBuffer.extractEnergy(maxExtract, simulate);
    }

    @Override
    public int getEnergyStored() {
        return this.energyBuffer.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        return this.energyBuffer.getMaxEnergyStored();
    }

    @Override
    public boolean canExtract() {
        return this.energyBuffer.canExtract();
    }

    @Override
    public boolean canReceive() {
        return this.energyBuffer.canReceive();
    }

    public EnergeticBlazeBurner.EnergyLevel getEnergyLevelFromBlock() {
        return EnergeticBlazeBurner.getEnergyLevelOf(this.getBlockState());
    }
}
