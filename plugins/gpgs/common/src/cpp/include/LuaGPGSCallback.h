#ifndef AE_LUA_GPGS_CALLBACK_H
#define AE_LUA_GPGS_CALLBACK_H

#include "LuaEngine.h"
#include "GPGSCallback.h"

namespace ae {
    
namespace gpgs {
  
/**
 * \brief The Google Play Game Services callback which calls Lua functions.
 */
class LuaGPGSCallback:public GPGSCallback {
    /// The Lua engine.
    ::ae::engine::LuaEngine *luaEngine;
    
    /**
     * \brief Calls a no-argument function.
     * \param funcName The function name.
     */
    void callNoArgFunc(const char *funcName);    
    
public:
    /** */
    LuaGPGSCallback(::ae::engine::LuaEngine *luaEngine_):GPGSCallback(),
        luaEngine(luaEngine_) {    
    }
    
    /** */
    virtual ~LuaGPGSCallback() {
    }
    
    /** */
    virtual void signedIn();
};
    
} // namespace
    
} // namespace

#endif // AE_LUA_GPGS_CALLBACK_H