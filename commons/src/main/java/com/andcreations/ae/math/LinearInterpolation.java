package com.andcreations.ae.math;

/**
 * Implementation of the linear interpolation.
 *
 * @author Mikolaj Gucki
 */
public class LinearInterpolation implements Interpolation {
    /** The class instance. */
    public static final LinearInterpolation INSTANCE =
        new LinearInterpolation();
    
    /** */
    private LinearInterpolation() {    
    }
    
    /** */
    @Override
    public double interpolate(double a,double b,double t) {
        return a * (1 - t) + b * t;
    }
}
