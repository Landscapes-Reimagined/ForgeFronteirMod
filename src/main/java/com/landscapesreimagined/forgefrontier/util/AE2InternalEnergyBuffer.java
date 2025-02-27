package com.landscapesreimagined.forgefrontier.util;


import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
import appeng.api.networking.energy.IAEPowerStorage;
import com.landscapesreimagined.forgefrontier.Config;
import net.minecraft.nbt.CompoundTag;

public class AE2InternalEnergyBuffer implements IAEPowerStorage {

    public static double MIN_INSERT_AMOUNT = 0.0001;

    public double AEPower = 0;

    protected double energyCapacity;

    protected double maxInsert;

    public AE2InternalEnergyBuffer(double capacity, double maxInsert, double power){
        this.AEPower = power;
        this.maxInsert = maxInsert;
        this.energyCapacity = capacity;
    }

    public AE2InternalEnergyBuffer(){
        this(Config.DEFAULT_MACHINE_INTERNAL_ENERGY_CAPACITY.get(), Config.DEFAULT_MACHINE_INTERNAL_ENERGY_INSERT.get(), 0);
    }


    @Override
    public double injectAEPower(double amt, Actionable mode) {

        if(amt <= MIN_INSERT_AMOUNT){
            return 0;
        }

        double roundedAmt = MathUtil.roundDefaultPrecision(amt);

        double testMaxReceive = Math.min(this.maxInsert, roundedAmt);

        double remainder = roundedAmt - testMaxReceive;

        double energyToInsert = Math.min(testMaxReceive, this.energyCapacity - this.AEPower);

        if(mode == Actionable.MODULATE){
            this.AEPower += energyToInsert;

        }


        return (roundedAmt - energyToInsert) + (remainder);
    }



    public double getMaxInsert(){
        return this.maxInsert;
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



//        if(amt <= MIN_INSERT_AMOUNT){
//            return 0;
//        }
//
//        double testMaxReceive = Math.min(Config.ENERGETIC_BLAZE_MAX_FE_RECEIVE.get(), usePowerMultiplier.multiply(amt));
//
//        double energyToInsert = Math.min(testMaxReceive, this.energyCapacity - this.AEPower);
//
//        if(mode == Actionable.MODULATE){
//            this.AEPower += energyToInsert;
//
//        }
//
//
//        return usePowerMultiplier.divide(energyToInsert);
        return 0;
    }

    public double usePower(double amt, Actionable mode, PowerMultiplier usePowerMultiplier){
        if(amt < MIN_INSERT_AMOUNT){
            return 0;
        }

        double extractedAmount = MathUtil.roundDefaultPrecision(Math.min(usePowerMultiplier.multiply(amt), this.AEPower));

        if(mode == Actionable.MODULATE){
            this.AEPower -= extractedAmount;
        }

        return usePowerMultiplier.divide(extractedAmount);
    }

    public CompoundTag writeToTag(){
        CompoundTag tag = new CompoundTag();

        tag.putDouble("power", MathUtil.roundDefaultPrecision(this.AEPower));

        tag.putDouble("capacity", MathUtil.roundDefaultPrecision(this.energyCapacity));

        tag.putDouble("maxInsert", MathUtil.roundDefaultPrecision(this.maxInsert));


        return tag;

    }

    public void readTag(CompoundTag tag){
        this.AEPower = MathUtil.roundDefaultPrecision(tag.getInt("power"));
        this.energyCapacity = MathUtil.roundDefaultPrecision(tag.getInt("capacity"));
        this.maxInsert = MathUtil.roundDefaultPrecision(tag.getDouble("maxInsert"));

    }

    public boolean isFull() {
        return this.AEPower >= this.getAEMaxPower();
    }
}
