#ifndef AE_ANDROID_LUA_EXTRA_LIB_H
#define AE_ANDROID_LUA_EXTRA_LIB_H

#include "LuaExtraLib.h"

namespace ae {
    
namespace android {
  
/**
 * \brief Android Lua extra library.
 */
class AndroidLuaExtraLib:public ::ae::engine::LuaExtraLib {
public:
    /** */
    AndroidLuaExtraLib():LuaExtraLib() {
    }
    
    /** */
    virtual const char *getName();
    
    /** */
    virtual bool loadExtraLib(::ae::engine::LuaEngine *luaEngine);
    
    /** */
    virtual bool unloadExtraLib();    
};
    
} // namespace
    
} // namespace

#endif // AE_ANDROID_LUA_EXTRA_LIB_H