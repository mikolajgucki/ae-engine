#ifndef AE_BASIC_COLOR_H
#define AE_BASIC_COLOR_H

namespace ae {

namespace math {
  
/**
 * \brief The template class for RGBA color.
 */
template<class T> class BasicColor {
protected:
    /// The red component.
    T r;
    
    /// The green component.
    T g;
    
    /// The blue component.
    T b;
    
    /// The red component.
    T a;
    
public:
    /** */
    BasicColor(T r_,T g_,T b_,T a_):r(r_),g(g_),b(b_),a(a_) {
    }
    
    /** */
    BasicColor(const BasicColor &color):r(color.getRed()),g(color.getGreen()),
        b(color.getBlue()),a(color.getAlpha()) {
    }
    
    /**
     * \brief Gets the red component.
     * \return The red component.
     */
    T getRed() const {
        return r;
    }
    
    /**
     * \brief Gets the green component.
     * \return The green component.
     */
    T getGreen() const {
        return g;
    }
    
    /**
     * \brief Gets the blue component.
     * \return The blue component.
     */
    T getBlue() const {
        return b;
    }
    
    /**
     * \brief Gets the alpha component.
     * \return The alpha component.
     */
    T getAlpha() const {
        return a;
    }
    
    /**
     * \brief Sets the red component.
     * \param r_ The red component.
     */
    void setRed(T r_) {
        r = r_;
    }
    
    /**
     * \brief Sets the green component.
     * \param g_ The green component.
     */
    void setGreen(T g_) {
        g = g_;
    }
    
    /**
     * \brief Sets the blue component.
     * \param r_ The blue component.
     */
    void setBlue(T b_) {
        b = b_;
    }
    
    /**
     * \brief Sets the alpha component.
     * \param r_ The alpha component.
     */
    void setAlpha(T a_) {
        a = a_;
    }
    
    /**
     * \brief Sets the components.
     * \param r_ The red component.
     * \param g_ The green component.
     * \param b_ The blue component.
     * \param a_ The alpha component.
     */
    void set(T r_,T g_,T b_,T a_) {
        r = r_;
        g = g_;
        b = b_;
        a = a_;
    }
    
    /**
     * \brief Sets a color.
     * \param color The color to assign.
     */
    void set(const BasicColor<T> &color) {
        r = color.getRed();
        g = color.getGreen();
        b = color.getBlue();
        a = color.getAlpha();
    }
};
    
} // namespace
    
} // namespace

#endif // AE_BASIC_COLOR_H