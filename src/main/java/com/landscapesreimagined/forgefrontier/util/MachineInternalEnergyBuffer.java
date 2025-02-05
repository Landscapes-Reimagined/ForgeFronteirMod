package com.landscapesreimagined.forgefrontier.util;

import com.landscapesreimagined.forgefrontier.Config;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.StringRepresentable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;

public class MachineInternalEnergyBuffer implements IEnergyStorage {

    public int energy;
    protected int capacity;
    protected int maxInsert;
    protected int maxExtract;
    protected boolean canExtract;

    public MachineInternalEnergyBuffer(int capacity, int maxInsert, LazyOptional<Integer> maxExtract, int energy){
        this.capacity = capacity;
        this.maxInsert = maxInsert;

        this.canExtract = maxExtract.isPresent();
        if(canExtract){
            this.maxExtract = maxExtract.orElse(Integer.MAX_VALUE);
        }

        this.energy = energy;

    }

    public MachineInternalEnergyBuffer(int capacity, int maxInsert, int maxExtract, boolean canExtract){
        this(capacity, maxInsert, LazyOptional.of(() -> maxExtract), 0);
        this.canExtract = canExtract;
    }

    public MachineInternalEnergyBuffer(int capacity, int maxInsert){
        this(capacity, maxInsert, Config.DEFAULT_MACHINE_INTERNAL_ENERGY_EXTRACT.get(), false);//arbitrary constant lol
    }

    public MachineInternalEnergyBuffer(){
        this(Config.DEFAULT_MACHINE_INTERNAL_ENERGY_CAPACITY.get(), Config.DEFAULT_MACHINE_INTERNAL_ENERGY_INSERT.get());
    }

    public void cullEnergy(){
        if(this.energy > this.capacity){
            this.energy = this.capacity;
        }
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {

        int testMaxReceive = Math.min(this.maxInsert, maxReceive);

        int energyToInsert = Math.min(testMaxReceive, this.capacity - this.energy);

        if(!simulate){
            this.energy += energyToInsert;
            cullEnergy();
        }


        return energyToInsert;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return internalExtractEnergy(maxExtract, simulate, ExtractionSource.EXTERNAL);
    }

    public int internalExtractEnergy(int maxExtract, boolean simulate, ExtractionSource source) {
        if(source != ExtractionSource.INTERNAL && !this.canExtract){
            return 0;
        }

        int testMaxExtract = Math.min(this.maxExtract, maxExtract);

        int energyToBeRemoved = Math.min(testMaxExtract, this.energy);

        if(!simulate){
            this.energy -= energyToBeRemoved;
        }

        return energyToBeRemoved;
    }

    @Override
    public int getEnergyStored() {
        this.cullEnergy();
        return this.energy;
    }

    @Override
    public int getMaxEnergyStored() {
        return this.capacity;
    }

    @Override
    public boolean canExtract() {
        cullEnergy();
        return this.canExtract && (this.energy < this.capacity);
    }

    @Override
    public boolean canReceive() {
        this.cullEnergy();
        return this.energy < this.capacity;
    }

    public enum ExtractionSource implements StringRepresentable {
        EXTERNAL, INTERNAL;

        @Override
        public @NotNull String getSerializedName() {
            return switch (this){
                case INTERNAL -> "internal";
                case EXTERNAL -> "external";
            };
        }
    }

    public CompoundTag writeToTag(){
        CompoundTag tag = new CompoundTag();

        tag.putInt("energy", this.energy);

        tag.putInt("capacity", this.capacity);
        tag.putInt("insert", this.maxInsert);
        tag.putBoolean("supports_extract", this.canExtract);

        if(this.canExtract)
            tag.putInt("extract", this.maxExtract);

        return tag;
    }

    public void readTag(CompoundTag tag){
        this.energy = tag.getInt("energy");
        this.capacity = tag.getInt("capacity");
        this.maxInsert = tag.getInt("insert");

        this.canExtract = tag.getBoolean("supports_extract");

        if(this.canExtract){
            this.maxExtract = tag.getInt("extract");
        }else{
            this.maxExtract = Config.DEFAULT_MACHINE_INTERNAL_ENERGY_EXTRACT.get();
        }
    }
}
