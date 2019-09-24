#ifndef AE_INTERPOLATION_H
#define AE_INTERPOLATION_H

namespace ae {

namespace math {
  
/**
 * \brief Abstract interpolation class.
 */
class Interpolation {
public:
    /** */
    virtual ~Interpolation() {
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
    virtual double interpolate(float a,float b,float t) = 0;
};
    
} // namespace

} // namespace

#endif // AE_INTERPOLATION_H