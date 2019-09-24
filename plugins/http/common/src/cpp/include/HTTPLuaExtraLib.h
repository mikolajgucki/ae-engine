#ifndef AE_HTTP_LUA_EXTRA_LIB_H
#define AE_HTTP_LUA_EXTRA_LIB_H

#include "LuaExtraLib.h"
#include "LuaLibHTTP.h"

namespace ae {
    
namespace http {
    
/**
 * \brief HTTP Lua extra library.
 */
class HTTPLuaExtraLib:public ::ae::engine::LuaExtraLib {
    /// The HTTP platform-specific implementation.
    LuaLibHTTP *luaLibHttp;
    
public:
    /** */
    HTTPLuaExtraLib(LuaLibHTTP *luaLibHttp_):LuaExtraLib(),
        luaLibHttp(luaLibHttp_) {
    }
    
    /** */
    virtual ~HTTPLuaExtraLib() {
        if (luaLibHttp != (LuaLibHTTP *)0) {
            delete luaLibHttp;
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

#endif // AE_HTTP_LUA_EXTRA_LIB_H