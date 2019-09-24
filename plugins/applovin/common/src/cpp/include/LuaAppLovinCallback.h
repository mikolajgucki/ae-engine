#ifndef AE_LUA_APPLOVIN_CALLBACK_H
#define AE_LUA_APPLOVIN_CALLBACK_H

#include "lua.hpp"
#include "LuaEngine.h"
#include "AppLovinCallback.h"

namespace ae {
    
namespace applovin {

/**
 * \brief The Lua AppLovin callback. Called on AppLovin events.
 */
class LuaAppLovinCallback:public AppLovinCallback {
    /// The Lua engine.
    ::ae::engine::LuaEngine *luaEngine;
    
    /**
     * \brief Calls a no-argument function of global applovin.
     * \param funcName The function name.
     */
    void callNoArgFunc(const char *funcName);
    
public:
    /** */
    LuaAppLovinCallback(::ae::engine::LuaEngine *luaEngine_):
        AppLovinCallback(),luaEngine(luaEngine_) {
    }
    
    /** */
    virtual ~LuaAppLovinCallback() {
    }
    
    /** */
    virtual void interstitialAdLoaded();
    
    /** */
    virtual void interstitialAdNoFill();
        
    /** */
    virtual void interstitialAdFailed(int errorCode);
    
    /** */
    virtual void interstitialAdDisplayed();
    
    /** */
    virtual void interstitialAdHidden();    
    
    /** */
    virtual void interstitialAdClicked();    
};
    
}
    
} // namespace

#endif // AE_LUA_APPLOVIN_CALLBACK_H