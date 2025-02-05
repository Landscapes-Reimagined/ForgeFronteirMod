package com.landscapesreimagined.forgefrontier.ModBlocks.ModBlockEntities;

import com.landscapesreimagined.forgefrontier.Config;
import com.landscapesreimagined.forgefrontier.util.MachineInternalEnergyBuffer;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EnergeticBlazeBurnerBlockEntity extends BlazeBurnerBlockEntity implements IEnergyStorage {

    protected final MachineInternalEnergyBuffer energyBuffer;
    protected LazyOptional<IEnergyStorage> lazyBuffer;



    public EnergeticBlazeBurnerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.energyBuffer = new MachineInternalEnergyBuffer(Config.ENERGETIC_BLAZE_CAPACITY.get(), Config.ENERGETIC_BLAZE_MAX_RECEIVE.get());
        this.lazyBuffer = LazyOptional.of(() -> this.energyBuffer);
    }


    //TODO: recipes and energy
    @Override
    public void tick() {
        super.tick();


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
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);

        CompoundTag energyBufferTag = compound.getCompound("EnergyBuffer");

        this.energyBuffer.readTag(energyBufferTag);
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
}
