#include "ByteColor.h"

namespace ae {
    
namespace math {
    
/** */  
const ByteColor ByteColor::WHITE(255,255,255,255);
  
/** */
void ByteColor::fromLua(lua_State *L,int redIndex,int greenIndex,
    int blueIndex,int alphaIndex,ByteColor &color) {
//
    if (lua_isnoneornil(L,redIndex) == 0 && lua_isnumber(L,redIndex)) {
        color.setRed((unsigned char)lua_tonumber(L,redIndex));
    }
    if (lua_isnoneornil(L,greenIndex) == 0 && lua_isnumber(L,greenIndex)) {
        color.setGreen((unsigned char)lua_tonumber(L,greenIndex));
    }
    if (lua_isnoneornil(L,blueIndex) == 0 && lua_isnumber(L,blueIndex)) {
        color.setBlue((unsigned char)lua_tonumber(L,blueIndex));
    }
    if (lua_isnoneornil(L,alphaIndex) == 0 && lua_isnumber(L,alphaIndex)) {
        color.setAlpha((unsigned char)lua_tonumber(L,alphaIndex));
    }
}

} // namespace

} // namespace