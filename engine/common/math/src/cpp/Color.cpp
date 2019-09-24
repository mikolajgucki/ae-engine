#include "Color.h"

namespace ae {
    
namespace math {
    
/** */  
const Color Color::WHITE(1,1,1,1);
    
/** */
void Color::fromLua(lua_State *L,int redIndex,int greenIndex,
    int blueIndex,int alphaIndex,Color &color) {
//
    if (lua_isnoneornil(L,redIndex) == 0 && lua_isnumber(L,redIndex)) {
        color.setRed((float)lua_tonumber(L,redIndex));
    }
    if (lua_isnoneornil(L,greenIndex) == 0 && lua_isnumber(L,greenIndex)) {
        color.setGreen((float)lua_tonumber(L,greenIndex));
    }
    if (lua_isnoneornil(L,blueIndex) == 0 && lua_isnumber(L,blueIndex)) {
        color.setBlue((float)lua_tonumber(L,blueIndex));
    }
    if (lua_isnoneornil(L,alphaIndex) == 0 && lua_isnumber(L,alphaIndex)) {
        color.setAlpha((float)lua_tonumber(L,alphaIndex));
    }
}

} // namespace

} // namespace