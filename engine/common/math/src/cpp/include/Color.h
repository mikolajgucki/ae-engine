#ifndef AE_COLOR_H
#define AE_COLOR_H

#include "lua.hpp"
#include "BasicColor.h"

namespace ae {

namespace math {
    
/**
 * \brief The default RGBA color of elements of type float.
 */
class Color:public BasicColor<float> {
public: 
    /// The white color.
    static const Color WHITE;
    
    /** */
    Color():BasicColor(0,0,0,1) {
    }
    
    /** */
    Color(float r_,float g_,float b_,float a_):BasicColor(r_,g_,b_,a_) {
    }
    
    /** */
    Color(const Color &color):BasicColor(color) {
    }
    
    /**
     * \brief Gets a color from a Lua stack.
     * \param L The Lua state.
     * \param redIndex The stack index of the item with the red component.
     * \param greenIndex The stack index of the item with the green component.
     * \param blueIndex The stack index of the item with the blue component.
     * \param alphaIndex The stack index of the item with the alpha component.
     * \param color The result color.
     */
    static void fromLua(lua_State *L,int redIndex,int greenIndex,
        int blueIndex,int alphaIndex,Color &color);
};
    
} // namespace
    
} // namespace

#endif // AE_COLOR_H