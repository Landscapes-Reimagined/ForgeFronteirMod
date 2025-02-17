package com.landscapesreimagined.forgefrontier.util;

public class MathUtil {

    public static int DEFAULT_PRECISION = 5;
    public static double DEFAULT_PRECISION_MUL = Math.pow(10, DEFAULT_PRECISION);
    public static double INVERSE_DEFAULT_PRECISION_MUL = 1/DEFAULT_PRECISION_MUL;

    public static double roundPrecision(double val, int decimals){
        double precisionMultiplier = Math.pow(10, decimals);
        double inversePrecisionMultiplier = Math.pow(10, -decimals);

        double bigVal = val * precisionMultiplier;

        double rounded = Math.round(bigVal);

        return rounded * inversePrecisionMultiplier;
    }

    public static double roundDefaultPrecision(double val){
        return (Math.round(val * DEFAULT_PRECISION_MUL) * INVERSE_DEFAULT_PRECISION_MUL);
    }
}
