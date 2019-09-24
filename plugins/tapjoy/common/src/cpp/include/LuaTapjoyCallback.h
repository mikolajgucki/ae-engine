#ifndef AE_LUA_TAPJOY_CALLBACK_H
#define AE_LUA_TAPJOY_CALLBACK_H

#include "LuaEngine.h"
#include "TapjoyCallback.h"

namespace ae {
    
namespace tapjoy {
    
/**
 * \brief The Tapjoy callback.
 */
class LuaTapjoyCallback:public TapjoyCallback {
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
    LuaTapjoyCallback(::ae::engine::LuaEngine *luaEngine_):TapjoyCallback(),
        luaEngine(luaEngine_){
    }
    
    /** */
    virtual ~LuaTapjoyCallback() {
    }

    /** */
    virtual void connectionSucceeded();
    
    /** */
    virtual void connectionFailed();
    
    /** */
    virtual void requestSucceeded(const std::string& placement);
    
    /** */
    virtual void requestFailed(const std::string& placement,
        const std::string& error);
    
    /** */
    virtual void contentIsReady(const std::string& placement);
    
    /** */
    virtual void contentShown(const std::string& placement);
    
    /** */
    virtual void contentDismissed(const std::string& placement);    
};
    
} // namespace
    
} // namespace

#endif // AE_LUA_TAPJOY_CALLBACK_H