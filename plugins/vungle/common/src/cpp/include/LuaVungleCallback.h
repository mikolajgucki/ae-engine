#ifndef AE_LUA_VUNGLE_CALLBACK_H
#define AE_LUA_VUNGLE_CALLBACK_H

#include "lua.hpp"
#include "LuaEngine.h"
#include "VungleCallback.h"

namespace ae {

namespace vungle {
  
/**
 * \brief The Lua implementation of the Vungle callback.
 */
class LuaVungleCallback:public VungleCallback {
    /// The Lua engine.
    ::ae::engine::LuaEngine *luaEngine;

    /**
     * \brief Calls a no-argument function of global applovin.
     * \param funcName The function name.
     */
    void callNoArgFunc(const char *funcName);    
    
public:
    /** */
    LuaVungleCallback(::ae::engine::LuaEngine *luaEngine_):
        VungleCallback(),luaEngine(luaEngine_) {
    }
    
    /** */
    virtual ~LuaVungleCallback() {
    }
    
    /** */
    virtual void adPlayableChanged(bool isAdPlayable_);
    
    /** */
    virtual void willShowAd();
    
    /** */
    virtual void willCloseAd();
    
    /** */
    virtual void adClicked();   
    
    /** */
    virtual void failedToPlayAd();
    
    /** */
    virtual void viewCompleted();
};
    
} // namespace

} // namespace

#endif // AE_LUA_VUNGLE_CALLBACK_H