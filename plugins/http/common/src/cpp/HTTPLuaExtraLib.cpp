#include "Log.h"
#include "LuaEngine.h"
#include "lua_http.h"
#include "HTTPLuaExtraLib.h"

using namespace ae::engine;
using namespace ae::http::lua;

namespace ae {

namespace http {
        
/** */
const char *HTTPLuaExtraLib::getName() {
    return "http";
}

/** */
bool HTTPLuaExtraLib::loadExtraLib(LuaEngine *luaEngine) {
    loadHTTPLib(getLog(),luaEngine,luaLibHttp,this);
    
    if (chkError()) {
        return false;
    }    
    return true;
}

/** */
bool HTTPLuaExtraLib::unloadExtraLib() {
    unloadHTTPLib(getLog(),luaLibHttp);
    return true;
}

} // namespace

} // namespace