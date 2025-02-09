package com.landscapesreimagined.forgefrontier.util;


import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
import appeng.api.networking.energy.IAEPowerStorage;
import com.landscapesreimagined.forgefrontier.Config;
import net.minecraft.nbt.CompoundTag;

public class AE2InternalEnergyBuffer implements IAEPowerStorage {

    public static double MAX_INSERT_AMOUNT = 0.0001;

    protected double AEPower = 0;

    protected double energyCapacity;

    protected double maxInsert;

    public AE2InternalEnergyBuffer(double capacity, double maxInsert, double power){
        this.AEPower = power;
        this.energyCapacity = capacity;
    }

    public AE2InternalEnergyBuffer(){
        this(Config.DEFAULT_MACHINE_INTERNAL_ENERGY_CAPACITY.get(), Config.DEFAULT_MACHINE_INTERNAL_ENERGY_INSERT.get(), 0);
    }


    @Override
    public double injectAEPower(double amt, Actionable mode) {

        if(amt <= MAX_INSERT_AMOUNT){
            return 0;
        }

        double testMaxReceive = Math.min(Config.ENERGETIC_BLAZE_MAX_FE_RECEIVE.get(), amt);

        double energyToInsert = Math.min(testMaxReceive, this.energyCapacity - this.AEPower);

        if(mode == Actionable.MODULATE){
            this.AEPower += energyToInsert;

        }


        return energyToInsert;
    }

    @Override
    public double getAEMaxPower() {
        return this.energyCapacity;
    }

    @Override
    public double getAECurrentPower() {
        return this.AEPower;
    }

    @Override
    public boolean isAEPublicPowerStorage() {
        return true;
    }

    @Override
    public AccessRestriction getPowerFlow() {
        return AccessRestriction.WRITE;
    }

    @Override
    public double extractAEPower(double amt, Actionable mode, PowerMultiplier usePowerMultiplier) {


        return 0;
    }

    public CompoundTag writeToTag(){
        CompoundTag tag = new CompoundTag();

        tag.putDouble("power", this.AEPower);

        tag.putDouble("capacity", this.energyCapacity);

        tag.putDouble("maxInsert", this.maxInsert);


        return tag;

    }

    public void readTag(CompoundTag tag){
        this.AEPower = tag.contains("requiredEnergy") ? tag.getInt("requiredEnergy") : tag.getInt("energy");
        this.energyCapacity = tag.getInt("capacity");
        this.maxInsert = tag.getDouble("maxInsert");

    }
}
