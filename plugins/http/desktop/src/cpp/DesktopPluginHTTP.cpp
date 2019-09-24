#include "HTTPLuaExtraLib.h"
#include "DesktopPluginHTTP.h"

namespace ae {

namespace http {
    
/** */
DesktopPluginHTTP::~DesktopPluginHTTP() {
    if (luaLibHttp != (DesktopLuaLibHTTP *)0) {
        delete luaLibHttp;
    }
}
    
/** */
bool DesktopPluginHTTP::init() {
// create library
    luaLibHttp = new DesktopLuaLibHTTP(getLog(),getCfg(),getTimer());
    if (luaLibHttp->initLuaLib() == false) {
        setError(luaLibHttp->getError());
        return false;
    }
    
    return true;
}

/** */
bool DesktopPluginHTTP::addLuaExtraLibs() {
    HTTPLuaExtraLib *extraLib = new HTTPLuaExtraLib(luaLibHttp);
    ae::engine::LuaEngine *engine = getLuaEngine();
    engine->addExtraLib(extraLib);
    return true;
}

} // namespace

} // namespace