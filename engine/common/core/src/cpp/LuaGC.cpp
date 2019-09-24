#include "LuaGetGlobal.h"
#include "LuaGC.h"

namespace ae {
    
namespace lua {
    
/** */
void LuaGC::getUsedMemory(double &usedMemory) {
    double count = lua_gc(L,LUA_GCCOUNT,0);
    double remainder = lua_gc(L,LUA_GCCOUNTB,0);
    
    usedMemory = count + remainder / 1024.0;    
}
    
} // namespace

} // namespace