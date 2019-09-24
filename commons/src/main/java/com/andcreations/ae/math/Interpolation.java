package com.andcreations.ae.math;

/**
 * Interface for interpolation implementations.
 *
 * @author Mikolaj Gucki
 */
public interface Interpolation {
    /**
     * Interpolates a value.
     * 
     * @param a The 1st value. 
     * @param b The 2nd value.
     * @param t The parameter from 0 to 1.
     * @return The interpolated value from <code>a</code> to <code>b</code>
     *   (<code>a</code> when <code>t</code> equals 0, <code>b</code> when
     *   <code>t</code> equals 1).
     */    
    double interpolate(double a,double b,double t);
}
