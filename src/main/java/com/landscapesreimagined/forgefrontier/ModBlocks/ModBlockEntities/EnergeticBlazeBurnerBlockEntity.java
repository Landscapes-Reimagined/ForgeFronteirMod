package com.landscapesreimagined.forgefrontier.ModBlocks.ModBlockEntities;

import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
import appeng.api.config.PowerUnits;
import appeng.api.networking.GridHelper;
import appeng.api.networking.IManagedGridNode;
import appeng.api.networking.energy.IAEPowerStorage;
import appeng.api.orientation.BlockOrientation;
import appeng.api.util.AECableType;
import appeng.blockentity.powersink.IExternalPowerSink;
import appeng.me.helpers.BlockEntityNodeListener;
import appeng.me.helpers.IGridConnectedBlockEntity;
import com.landscapesreimagined.forgefrontier.Config;
import com.landscapesreimagined.forgefrontier.ModBlocks.EnergeticBlazeBurner;
import com.landscapesreimagined.forgefrontier.ModBlocks.ModBlocks;
import com.landscapesreimagined.forgefrontier.mixin.Create.CurrentBasinRecipeAccessor;
import com.landscapesreimagined.forgefrontier.recipies.EnergeticMixingRecipe;
import com.landscapesreimagined.forgefrontier.util.AE2InternalEnergyBuffer;
import com.landscapesreimagined.forgefrontier.util.MachineInternalEnergyBuffer;
import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlockEntity;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;
import com.simibubi.create.foundation.utility.Pair;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
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

public class EnergeticBlazeBurnerBlockEntity extends BlazeBurnerBlockEntity implements IEnergyStorage, IExternalPowerSink, IGridConnectedBlockEntity {

    //FE power buffer
    protected final MachineInternalEnergyBuffer energyBuffer;
    protected LazyOptional<IEnergyStorage> lazyBuffer;

    //AE2 power buffer
    protected final AE2InternalEnergyBuffer internalAEBuffer;

    //AE2 grid stuff
    private final IManagedGridNode mainNode = createMainNode()
            .setVisualRepresentation(ModBlocks.ENERGETIC_BLAZE_BURNER_BLOCK.asStack())
            .addService(IAEPowerStorage.class, this)
            .setIdlePowerUsage(20)
            .setInWorldNode(true)
            .setTagName("proxy");

    //recipe stuff
    @Nullable
    protected EnergeticMixingRecipe currentRecipe = null;
    protected boolean lastTickHadNoRecipie = true;
    protected double usedPower = 0;

    @Nullable
    protected Pair<Integer, String> currentMixer = null;

    public EnergeticBlazeBurnerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.energyBuffer = new MachineInternalEnergyBuffer(Config.ENERGETIC_BLAZE_FE_CAPACITY.get(), Config.ENERGETIC_BLAZE_MAX_FE_RECEIVE.get(), Config.ENERGETIC_BLAZE_MAX_FE_EXTRACT.get());
        this.lazyBuffer = LazyOptional.of(() -> this.energyBuffer);
        this.internalAEBuffer = new AE2InternalEnergyBuffer(Config.ENERGETIC_BLAZE_AE_CAPACITY.get(), Config.ENERGETIC_BLAZE_MAX_AE_RECEIVE.get(), 0);
    }

    protected IManagedGridNode createMainNode() {
        return GridHelper.createManagedNode(this, BlockEntityNodeListener.INSTANCE);
    }


    //TODO: recipes and requiredEnergy
    @Override
    public void tick() {
        super.tick();

        //do side-agnostic processes


        if(level == null){
            return;
        }

        if(level.isClientSide){
            //do client side things

            //play a sound?
            //Do rendering stuff?
            //idk

            return;
        }

        //do server-side things


        //Fill internal buffer from our AE network
        var grid = this.getMainNode().getGrid();

        if(grid != null && !this.internalAEBuffer.isFull()) {
            double powerTest = grid.getEnergyService().extractAEPower(this.internalAEBuffer.getMaxInsert(), Actionable.SIMULATE, PowerMultiplier.ONE);


            double remainder = this.internalAEBuffer.injectAEPower(powerTest, Actionable.SIMULATE);

            if(remainder >= 0){

                double realPower = grid.getEnergyService().extractAEPower(powerTest - remainder, Actionable.MODULATE, PowerMultiplier.ONE);

                double realLeftover = this.internalAEBuffer.injectAEPower(realPower, Actionable.MODULATE);

                grid.getEnergyService().injectPower(realLeftover, Actionable.MODULATE);

            }


        }



        boolean stateChanged = false;
        boolean stateChangedUp = false;

        EnergeticBlazeBurner.EnergyLevel oldEnergyLevel = level.getBlockState(this.worldPosition).getValue(EnergeticBlazeBurner.ENERGY_LEVEL);


        if(this.internalAEBuffer.getAECurrentPower() > 0 && this.energyBuffer.energy >= EnergeticBlazeBurner.EnergyLevel.INFUSE.getMinFE()){

            if(oldEnergyLevel != EnergeticBlazeBurner.EnergyLevel.INFUSE) {
                stateChangedUp = stateChanged = true;

                this.level.setBlock(this.worldPosition, this.level.getBlockState(this.getBlockPos()).setValue(EnergeticBlazeBurner.ENERGY_LEVEL, EnergeticBlazeBurner.EnergyLevel.INFUSE), 0b10);
            }

        }else if(this.energyBuffer.energy > EnergeticBlazeBurner.EnergyLevel.INFUSE.getMinFE() / 2){

            if( oldEnergyLevel != EnergeticBlazeBurner.EnergyLevel.CRYSTALLIZE) {

                stateChanged = true;

                if (oldEnergyLevel != EnergeticBlazeBurner.EnergyLevel.INFUSE)
                    stateChangedUp = true;

                this.level.setBlock(this.worldPosition, this.level.getBlockState(this.getBlockPos()).setValue(EnergeticBlazeBurner.ENERGY_LEVEL, EnergeticBlazeBurner.EnergyLevel.CRYSTALLIZE), 0b10);
            }

        }else if(this.internalAEBuffer.getAECurrentPower() <= 0 && this.energyBuffer.energy > EnergeticBlazeBurner.EnergyLevel.CRYSTALLIZE.getMinFE()){

            if(oldEnergyLevel != EnergeticBlazeBurner.EnergyLevel.SLEEPY) {
                stateChanged = true;

                if (oldEnergyLevel.ordinal() > EnergeticBlazeBurner.EnergyLevel.SLEEPY.ordinal())
                    stateChangedUp = true;

                this.level.setBlock(this.worldPosition, this.level.getBlockState(this.getBlockPos()).setValue(EnergeticBlazeBurner.ENERGY_LEVEL, EnergeticBlazeBurner.EnergyLevel.SLEEPY), 0b10);
            }
        }else if(this.internalAEBuffer.getAECurrentPower() <= 0 && this.energyBuffer.getEnergyStored() == 0){

            if(oldEnergyLevel != EnergeticBlazeBurner.EnergyLevel.SLEEPING) {


                this.level.setBlock(this.worldPosition, this.level.getBlockState(this.getBlockPos()).setValue(EnergeticBlazeBurner.ENERGY_LEVEL, EnergeticBlazeBurner.EnergyLevel.SLEEPING), 0b10);
            }
        }


        //set our energy capacity based on the energy level
        EnergeticBlazeBurner.EnergyLevel currentEnergylevel = this.getEnergyLevelFromBlock();
        if(currentEnergylevel == EnergeticBlazeBurner.EnergyLevel.INFUSE){
            this.energyBuffer.setCapacity(Config.ENERGETIC_BLAZE_INFUSE_CAPACITY.get());
            this.energyBuffer.setInsertExtract(
                (int) (Config.ENERGETIC_BLAZE_MAX_FE_RECEIVE.get() * Config.ENERGETIC_BLAZE_MAX_FE_RECEIVE_INJECT_INFUSE_MULTIPLIER.get()),
                (int) (Config.ENERGETIC_BLAZE_MAX_FE_EXTRACT.get() * Config.ENERGETIC_BLAZE_MAX_FE_RECEIVE_INJECT_INFUSE_MULTIPLIER.get())
            );

        }else{
            this.energyBuffer.setCapacity(Config.ENERGETIC_BLAZE_FE_CAPACITY.get());
            this.energyBuffer.setInsertExtract(Config.ENERGETIC_BLAZE_MAX_FE_RECEIVE.get(), Config.ENERGETIC_BLAZE_MAX_FE_EXTRACT.get());
            this.energyBuffer.cullEnergy();
        }



        //set the recipe so we can do Power Stuff:tm:
        BlockPos position = this.worldPosition;

        BlockEntity above = level.getBlockEntity(position.above());

        MechanicalMixerBlockEntity mixer = null;

        if(above instanceof BasinBlockEntity basin){
            mixer = this.doBasinTesting(basin);
        }

        boolean keepDoingRecipe = true;

        if(this.currentRecipe != null && mixer != null && mixer.running && mixer.processingTicks > 0){

            var energyCondition = this.currentRecipe.getRequiredEnergyLevel();

            if(lastTickHadNoRecipie){
                this.usedPower = 0;
                this.lastTickHadNoRecipie = false;
            }



            double energyLeft = this.currentRecipe.getRequiredEnergy() - ( this.usedPower);

            int processTime = this.currentRecipe.getProcessingDuration();
            int mixerTicks = Mth.clamp(Mth.log2((int) (512 / Math.abs(mixer.getSpeed()))) * Mth.ceil((processTime == 0 ? 1 : processTime / 100f)  * 15) + 1, 1, 512); //Math.max(totalProcessTime / Math.abs(mixer.getSpeed() * 20), 1);

            double energyPerTick = energyLeft / mixer.processingTicks;

            double totalEnergyPerTick = this.currentRecipe.getRequiredEnergy() / ((double) mixerTicks);

            double FEMultiplier = 1;

            if(energyCondition.testEnergeticBlazeBurner(EnergeticBlazeBurner.EnergyLevel.INFUSE)){
                FEMultiplier = 0.5;
            }

            int FEperTick = (int) Math.ceil(energyPerTick * FEMultiplier);

            int usableEnergyAmount = this.energyBuffer.internalExtractEnergy(FEperTick, true, MachineInternalEnergyBuffer.ExtractionSource.INTERNAL);

            //we can't extract that amount from the buffer and the recipe has just started
            if(usableEnergyAmount < FEperTick && energyLeft == this.currentRecipe.getRequiredEnergy()){

                int countingProcessingTicks = mixer.processingTicks <= 0 ? 1 : mixer.processingTicks;

                double tickScale = this.energyBuffer.getExtractionsToExtract(totalEnergyPerTick * FEMultiplier, countingProcessingTicks);

                mixer.processingTicks = Mth.ceil(mixer.processingTicks * tickScale);

                energyPerTick = energyLeft / mixer.processingTicks;

                FEperTick = (int) Math.ceil(energyPerTick * FEMultiplier);

                usableEnergyAmount = this.energyBuffer.internalExtractEnergy(FEperTick, false, MachineInternalEnergyBuffer.ExtractionSource.INTERNAL);

            }else{
                this.energyBuffer.internalExtractEnergy(FEperTick, false, MachineInternalEnergyBuffer.ExtractionSource.INTERNAL);
            }

            double usedAEEnergy = 0;
            if(energyCondition.testEnergeticBlazeBurner(EnergeticBlazeBurner.EnergyLevel.INFUSE)){
                usedAEEnergy = this.internalAEBuffer.usePower(energyPerTick, Actionable.SIMULATE, PowerMultiplier.ONE);

                if(usedAEEnergy > 0 && usedAEEnergy < energyPerTick && energyLeft == this.currentRecipe.getRequiredEnergy()){
                    int countingProcessingTicks = mixer.processingTicks <= 0 ? 1 : mixer.processingTicks;

                    double tickScale = getExtractionsToExtract(usedAEEnergy, totalEnergyPerTick, countingProcessingTicks);

                    mixer.processingTicks = Mth.ceil(countingProcessingTicks * tickScale);

                    energyPerTick = energyLeft / mixer.processingTicks;

                    usedAEEnergy = this.internalAEBuffer.usePower(energyPerTick, Actionable.MODULATE, PowerMultiplier.ONE);


                }else{
                    this.internalAEBuffer.usePower(energyPerTick, Actionable.MODULATE, PowerMultiplier.ONE);
                }
            }

            this.usedPower += energyPerTick;

            if(this.usedPower == this.currentRecipe.getRequiredEnergy() || mixer.processingTicks == 0 || !keepDoingRecipe){
                this.resetRecipe();
            }
//            doProcessingAndEnergy(totalRecipeEnergy, totalProcessTime, keepDoingRecipe, mixer);




        }else if(currentRecipe == null){
            this.resetRecipe();
        }

    }

    public void resetRecipe(){
        this.currentRecipe = null;
        this.usedPower = 0;
    }


    private void doProcessingAndEnergy(double totalRecipeEnergy, int totalProcessTime, boolean keepDoingRecipe, MechanicalMixerBlockEntity mixer) {


        if(totalRecipeEnergy == 0){
            this.resetRecipe();
            return;
        }



        double realRecipeSpeed = Mth.clamp(Mth.log2((int) (512 / Math.abs(mixer.getSpeed()))) * Mth.ceil(mixer.processingTicks * 15) + 1, 1, 512); //Math.max(totalProcessTime / Math.abs(mixer.getSpeed() * 20), 1);
        double energyPerTick = totalRecipeEnergy / realRecipeSpeed;




        double FEMultiplier = 1;

        assert currentRecipe != null;
        if(currentRecipe.getRequiredEnergyLevel().testEnergeticBlazeBurner(EnergeticBlazeBurner.EnergyLevel.INFUSE)){
            FEMultiplier = 0.5;
        }


        int FEperTick = (int) (energyPerTick * FEMultiplier) ;



        int usedEnergy = this.energyBuffer.internalExtractEnergy( FEperTick, true, MachineInternalEnergyBuffer.ExtractionSource.INTERNAL);



        if(usedEnergy < FEperTick){
//            keepDoingRecipe = false;
            //repeat
//            mixer.processingTicks++;
//            this.doProcessingAndEnergy(totalRecipeEnergy, mixer.processingTicks, keepDoingRecipe, mixer);


            int nonZeroProcessingTicks = (mixer.processingTicks <= 0 ? 1 : mixer.processingTicks);

            double ticksToExtractFE = this.energyBuffer.getExtractionsToExtract(energyPerTick * FEMultiplier, nonZeroProcessingTicks);

            mixer.processingTicks = Mth.ceil(ticksToExtractFE * nonZeroProcessingTicks);

            realRecipeSpeed = Mth.clamp((Mth.log2((int) (512 / Math.abs(mixer.getSpeed())))) * Mth.ceil(mixer.processingTicks * 15) + 1, 1, 512);
            energyPerTick = totalRecipeEnergy / realRecipeSpeed;

            FEperTick = (int) (energyPerTick);

            usedEnergy = this.energyBuffer.internalExtractEnergy( (int) (FEperTick * FEMultiplier), false, MachineInternalEnergyBuffer.ExtractionSource.INTERNAL);



        }else if(usedEnergy < 0) {
            keepDoingRecipe = false;
        }else{
            this.energyBuffer.internalExtractEnergy(FEperTick, false, MachineInternalEnergyBuffer.ExtractionSource.INTERNAL);
        }

        double usedAEEnergy = Double.MIN_VALUE;
        if(this.currentRecipe.getRequiredEnergyLevel().testEnergeticBlazeBurner(EnergeticBlazeBurner.EnergyLevel.INFUSE)){

            usedAEEnergy = this.internalAEBuffer.usePower(energyPerTick, Actionable.SIMULATE, PowerMultiplier.ONE);

            if(usedAEEnergy >= 0 && usedAEEnergy < energyPerTick){
                int nonZeroProcessingTicks = (mixer.processingTicks >= 0 ? 1 : mixer.processingTicks);

                double ticksToExtractAE = getExtractionsToExtract(usedAEEnergy, energyPerTick, nonZeroProcessingTicks);

                mixer.processingTicks = Mth.ceil(ticksToExtractAE * nonZeroProcessingTicks);

                realRecipeSpeed = Mth.clamp((Mth.log2((int) (512 / Math.abs(mixer.getSpeed())))) * Mth.ceil(mixer.processingTicks * 15) + 1, 1, 512);
                energyPerTick = totalRecipeEnergy / realRecipeSpeed;

                usedAEEnergy = this.internalAEBuffer.usePower(energyPerTick, Actionable.MODULATE, PowerMultiplier.ONE);



            }else if(usedAEEnergy < 0){
                keepDoingRecipe = false;
            }else{
                this.internalAEBuffer.usePower(energyPerTick, Actionable.MODULATE, PowerMultiplier.ONE);
            }


        }

        this.usedPower += energyPerTick;


        if(!keepDoingRecipe){
            mixer.running = false;
            this.resetRecipe();
        }
    }

    private static double getExtractionsToExtract(double extract, double amount, int extractionsOtherwise){
        double extracted = Math.min(extract, amount);

        double test = ( amount) / ( extracted);

        return extractionsOtherwise > test ? 1 : test;
    }

    @Nullable
    private MechanicalMixerBlockEntity doBasinTesting(BasinBlockEntity basin) {

        Level world = this.level;

        BlockPos basinPos = basin.getBlockPos();

        BlockPos mixerPos = basinPos.above(2);

        //Never call in a null world!
        assert world != null;
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
        this.mainNode.saveToNBT(compound);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);

        CompoundTag energyBufferTag = compound.getCompound("EnergyBuffer");
        CompoundTag AEBufferTag = compound.getCompound("AEEnergyBuffer");

        this.energyBuffer.readTag(energyBufferTag);
        this.internalAEBuffer.readTag(AEBufferTag);
        this.mainNode.loadFromNBT(compound);
    }

    @Override
    public AECableType getCableConnectionType(Direction dir) {
        return AECableType.SMART;
    }

    @Override
    public void clearRemoved() {
        super.clearRemoved();
        scheduleInit(); // Required for onReady to be called
    }

    @Override
    public void remove() {
        super.remove();
        this.getMainNode().destroy();
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        this.getMainNode().destroy();
    }

    protected void scheduleInit() {
        GridHelper.onFirstTick(this, EnergeticBlazeBurnerBlockEntity::onReady);
    }


    public void onReady() {

        this.getMainNode().create(getLevel(),this.getBlockPos());
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return this.energyBuffer.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return this.energyBuffer.extractEnergy(maxExtract, simulate);
    }

    public void setFE(int FE){
        this.energyBuffer.energy = FE;
    }

    public void setAE(double AE){
        this.internalAEBuffer.AEPower = AE;
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

    @Override
    public double injectExternalPower(PowerUnits externalUnit, double amount, Actionable mode) {
        return PowerUnits.AE.convertTo(externalUnit, this.internalAEBuffer.injectAEPower(externalUnit.convertTo(PowerUnits.AE, amount), mode));
    }

    @Override
    public double getExternalPowerDemand(PowerUnits externalUnit, double maxPowerRequired) {
        return PowerUnits.AE.convertTo(externalUnit,
                Math.max(0.0, this.getCapacityLeft()));
    }

    protected double getCapacityLeft() {
        return this.internalAEBuffer.getAEMaxPower() - this.internalAEBuffer.getAECurrentPower();
    }

    @Override
    public double injectAEPower(double amt, Actionable mode) {
        return this.internalAEBuffer.injectAEPower(amt, mode);
    }

    @Override
    public double getAEMaxPower() {
        return this.internalAEBuffer.getAEMaxPower();
    }

    @Override
    public double getAECurrentPower() {
        return this.internalAEBuffer.getAECurrentPower();
    }

    @Override
    public boolean isAEPublicPowerStorage() {
        return this.internalAEBuffer.isAEPublicPowerStorage();
    }

    @Override
    public AccessRestriction getPowerFlow() {
        return this.internalAEBuffer.getPowerFlow();
    }

    @Override
    public double extractAEPower(double amt, Actionable mode, PowerMultiplier usePowerMultiplier) {
        return this.internalAEBuffer.extractAEPower(amt, mode, usePowerMultiplier);
    }

    @Override
    public IManagedGridNode getMainNode() {
        return this.mainNode;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setBlockState(BlockState state) {
        var previousOrientation = BlockOrientation.get(getBlockState());

        super.setBlockState(state);

        // This method is called when the blockstate of an existing block-entity is changed
        // We use this to detect a change to rotation
        var newOrientation = BlockOrientation.get(getBlockState());
        if (previousOrientation != newOrientation) {
            onOrientationChanged(newOrientation);
        }
    }

    protected void onOrientationChanged(BlockOrientation orientation) {

        onGridConnectableSidesChanged();
    }

    /**
     * Call when the return value {@link IGridConnectedBlockEntity#getGridConnectableSides(BlockOrientation)} has
     * changed, to update the grid nodes exposed sides.
     */
    protected final void onGridConnectableSidesChanged() {
        getMainNode().setExposedOnSides(getGridConnectableSides(getOrientation()));
    }

    public final BlockOrientation getOrientation() {
        return BlockOrientation.get(getBlockState());
    }

    @Override
    public void saveChanges() {

    }
}
