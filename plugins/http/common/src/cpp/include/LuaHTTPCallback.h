#ifndef AE_LUA_HTTP_CALLBACK_H
#define AE_LUA_HTTP_CALLBACK_H

#include "LuaEngine.h"
#include "LuaCallUtil.h"
#include "HTTPCallback.h"

namespace ae {
    
namespace http {
    
/**
 * \brief The Lua HTTP callback. Called on HTTP events.
 */
class LuaHTTPCallback:public HTTPCallback {
    /// The Lua engine.
    ::ae::engine::LuaEngine *luaEngine;
    
    /// The Lua call utility.
    ::ae::lua::LuaCallUtil luaCallUtil;
    
public:
    /** */
    LuaHTTPCallback(::ae::engine::LuaEngine *luaEngine_):HTTPCallback(),
        luaEngine(luaEngine_),luaCallUtil() {
    }
    
    /** */
    virtual ~LuaHTTPCallback() {
    }
    
    /** */
    virtual void receivedResponse(const std::string& id,int code,
        const std::string& body);
    
    /** */
    virtual void failedToReceiveResponse(const std::string& id,
        const std::string& error);
};
    
} // namespace
    
} // namespace

#endif // AE_LUA_HTTP_CALLBACK_H