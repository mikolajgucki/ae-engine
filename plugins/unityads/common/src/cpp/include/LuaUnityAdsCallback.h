#ifndef AE_LUA_UNITY_ADS_CALLBACK_H
#define AE_LUA_UNITY_ADS_CALLBACK_H

#include "LuaEngine.h"
#include "UnityAdsCallback.h"

namespace ae {
    
namespace unityads {
    
/**
 * \brief The Unity Ads callback which calls Lua functions.
 */
class LuaUnityAdsCallback:public UnityAdsCallback {
    /// The Lua engine.
    ::ae::engine::LuaEngine *luaEngine;
    
    /**
     * \brief Calls a string function.
     * \param funcName The function name.
     * \param arg The argument.
     */
    void callStrFunc(const char *funcName,const std::string& arg);   
    
    /**
     * \brief Calls a function with two strings as arguments.
     * \param funcName The function name.
     * \param arg0 The 1sr argument.
     */
    void callStrFunc(const char *funcName,const std::string& arg0,
        const std::string &arg1);   
    
public:
    /** */
    LuaUnityAdsCallback(::ae::engine::LuaEngine *luaEngine_):
        UnityAdsCallback(),luaEngine(luaEngine_) {
    }
    
    /** */
    virtual ~LuaUnityAdsCallback() {
    }
    
    /** */
    virtual void setReady(const std::string &placementId);
    
    /** */
    virtual void started(const std::string &placementId);
    
    /** */
    virtual void failed(const std::string &error,
        const std::string &msg);
    
    /** */
    virtual void finished(const std::string &placementId,
        FinishState state);  
};
    
} // namespace
    
} // namespace

#endif // AE_LUA_UNITY_ADS_CALLBACK_H