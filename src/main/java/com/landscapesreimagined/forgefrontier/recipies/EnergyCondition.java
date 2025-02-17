package com.landscapesreimagined.forgefrontier.recipies;

import com.landscapesreimagined.forgefrontier.ForgeFrontier;
import com.landscapesreimagined.forgefrontier.ModBlocks.EnergeticBlazeBurner;
import com.landscapesreimagined.forgefrontier.ModBlocks.EnergeticBlazeBurner.EnergyLevel;

public enum EnergyCondition {
    NONE(0xffffff), CRYSTALLIZE(0x502850), INFUSE(0x4998BF);

    private int color;

    EnergyCondition(int color){
        this.color = color;
    }

    public boolean testEnergeticBlazeBurner(EnergyLevel level){
        return switch (this) {
            case INFUSE -> level == EnergyLevel.INFUSE;
            case CRYSTALLIZE ->
                    level != EnergyLevel.NONE &&
                    level != EnergyLevel.INFUSE &&
                    level != EnergyLevel.SLEEPING;
            case NONE -> true;
        };

    }

    public EnergeticBlazeBurner.EnergyLevel visiualizeAsEnergeticBlazeBurner(){
        return switch (this) {
            case INFUSE -> EnergyLevel.INFUSE;
            case CRYSTALLIZE -> EnergyLevel.CRYSTALLIZE;
            case NONE -> EnergyLevel.NONE;
        };
    }

    public String serialize(){
        return name().toLowerCase();
    }

    public String getTranslationKey(){
        return "recipe.energy_level_requirement." + serialize();
    }

    public static EnergyCondition deserialize(String name){
        for(EnergyCondition c : values())
            if(c.serialize().equals(name))
                return c;

        ForgeFrontier.LOGGER.warn("Tried to deserialize invalid requiredEnergy level condition: \"" + name + "\"");
        return NONE;
    }

    public int getColor(){ return color; }
}
