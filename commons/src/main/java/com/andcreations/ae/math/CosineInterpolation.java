package com.andcreations.ae.math;

/**
 * The implementation of the cosine interpolation.
 *
 * @author Mikolaj Gucki
 */
public class CosineInterpolation implements Interpolation {
    /** The class instance. */
    public static final CosineInterpolation INSTANCE =
        new CosineInterpolation();
    
    /** */
    private CosineInterpolation() {    
    }
    
    /** */
    @Override
    public double interpolate(double a,double b,double t) {
        double tcos = 0.5 * (1 - Math.cos(t * Math.PI));             
        return a * (1 - tcos) + b * tcos;
    }

}
