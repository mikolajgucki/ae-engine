#ifndef AE_LUA_GAME_CENTER_CALLBACK_H
#define AE_LUA_GAME_CENTER_CALLBACK_H

#include "LuaEngine.h"
#include "GameCenterCallback.h"

namespace ae {
    
namespace gamecenter {
  
/**
 * \brief The Game Center callback.
 */
class LuaGameCenterCallback:public GameCenterCallback {
    /// The Lua engine.
    ::ae::engine::LuaEngine *luaEngine;
    
    /**
     * \brief Calls a no-argument function.
     * \param funcName The function name.
     */
    void callNoArgFunc(const char *funcName);       
    
public:
    /** */
    LuaGameCenterCallback(::ae::engine::LuaEngine *luaEngine_):
        GameCenterCallback(),luaEngine(luaEngine_) {
    }
    
    /** */
    virtual ~LuaGameCenterCallback() {
    }
    
    /** */
    virtual void willShowLoginView();
    
    /** */
    virtual void authenticated();
    
    /** */
    virtual void notAuthenticated();
    
    /** */
    virtual void notAuthenticatedWithError();
    
    /** */
    virtual void loginViewHidden();
    
    /** */
    virtual void achievementsLoaded();
};
    
} // namespace
    
} // namespace

#endif // AE_GAME_CEAE_LUA_GAME_CENTER_CALLBACK_HNTER_CALLBACK_H