#ifndef AE_GAME_CENTER_LUA_EXTRA_LIB_H
#define AE_GAME_CENTER_LUA_EXTRA_LIB_H

#include "LuaEngine.h"
#include "LuaExtraLib.h"
#include "LuaLibGameCenter.h"

namespace ae {
    
namespace gamecenter {
    
/**
 * \brief The Game Center Lua extra library.
 */
class GameCenterLuaExtraLib:public ::ae::engine::LuaExtraLib {
    /// The platform-specific implementation of the library.
    LuaLibGameCenter *luaLibGameCenter;
    
public:
    /** */
    GameCenterLuaExtraLib(LuaLibGameCenter *luaLibGameCenter_):LuaExtraLib(),
        luaLibGameCenter(luaLibGameCenter_) {
    }
    
    /** */
    virtual ~GameCenterLuaExtraLib() {
        if (luaLibGameCenter != (LuaLibGameCenter *)0) {
            delete luaLibGameCenter;
        }
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

#endif // AE_GAME_CENTER_LUA_EXTRA_LIB_H