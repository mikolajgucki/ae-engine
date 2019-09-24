#ifndef AE_LINEAR_INTERPOLATION_H
#define AE_LINEAR_INTERPOLATION_H

#include "Interpolation.h"

namespace ae {

namespace math {
  
/**
 * \brief Linear interpolation.
 */
class LinearInterpolation:public Interpolation {
public:
    /** */
    LinearInterpolation():Interpolation() {
    }
    
    /** */
    virtual ~LinearInterpolation() {
    }
    
	/**
	 * \brief Interpolates a value.
	 * \param a The 1st value. 
	 * \param b The 2nd value.
	 * \param t The parameter from 0 to 1.
	 * \return The interpolated value from <code>a</code> to <code>b</code>
	 *   (<code>a</code> when <code>t</code> equals 0, <code>b</code> when
	 *   (<code>t</code> equals 1).
	 */    
    virtual double interpolate(float a,float b,float t) {
        return a * (1 - t) + b * t;
    }
};
    
} // namespace

} // namespace

#endif // AE_LINEAR_INTERPOLATION_H