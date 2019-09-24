#ifndef AE_SPINE_LUA_EXTRA_LIB_H
#define AE_SPINE_LUA_EXTRA_LIB_H

#include "lua.hpp"
#include "LuaExtraLib.h"

namespace ae {
    
namespace spine {

/**
 * \brief Spine extra library.
 */
class SpineLuaExtraLib:public ::ae::engine::LuaExtraLib {
public:
    /** */
    SpineLuaExtraLib():LuaExtraLib() {
    }
    
    /** */
    virtual ~SpineLuaExtraLib() {
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

#endif // AE_SPINE_LUA_EXTRA_LIB_H