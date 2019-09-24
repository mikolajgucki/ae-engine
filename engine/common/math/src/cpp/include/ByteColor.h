#ifndef AE_BYTE_COLOR_H
#define AE_BYTE_COLOR_H

#include "lua.hpp"
#include "BasicColor.h"

namespace ae {

namespace math {
    
/**
 * \brief The default RGBA color of elements of type unsigned char.
 */
class ByteColor:public BasicColor<unsigned char> {
public: 
    /// The white color.
    static const ByteColor WHITE;
    
    /** */
    ByteColor():BasicColor(0,0,0,255) {
    }
    
    /** */
    ByteColor(unsigned char r_,unsigned char g_,unsigned char b_,
        unsigned char a_):BasicColor(r_,g_,b_,a_) {
    }
    
    /** */
    ByteColor(const ByteColor &color):BasicColor(color) {
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
        int blueIndex,int alphaIndex,ByteColor &color);    
};
    
} // namespace
    
} // namespace

#endif // AE_BYTE_COLOR_H