#ifndef AE_LUA_AD_COLONY_CALLBACK_H
#define AE_LUA_AD_COLONY_CALLBACK_H

#include <string>

#include "LuaEngine.h"
#include "AdColonyCallback.h"

namespace ae {
    
namespace adcolony {
    
/**
 * \brief The AdColony callback which calls Lua functions.
 */
class LuaAdColonyCallback:public AdColonyCallback {
    /// The Lua engine.
    ::ae::engine::LuaEngine *luaEngine;
    
    /**
     * \brief Calls a no-argument function.
     * \param funcName The function name.
     */
    void callNoArgFunc(const char *funcName);
    
    /**
     * \brief Calls a string function.
     * \param funcName The function name.
     * \param arg The argument.
     */
    void callStrFunc(const char *funcName,const std::string& arg);
    
public:
    /** */
    LuaAdColonyCallback(::ae::engine::LuaEngine *luaEngine_):AdColonyCallback(),
        luaEngine(luaEngine_) {
    }
    
    /** */
    virtual ~LuaAdColonyCallback() {
    }    
    
    /** */
    virtual void interstitialAvailable(const std::string &zoneId);
    
    /** */
    virtual void interstitialUnavailable(const std::string &zoneId);
    
    /** */
    virtual void interstitialOpened(const std::string &zoneId);
    
    /** */
    virtual void interstitialClosed(const std::string &zoneId);
};
    
} // namespace
    
} // namespace

#endif // AE_LUA_AD_COLONY_CALLBACK_H