#include "TapjoyLuaExtraLib.h"
#include "DesktopPluginTapjoy.h"

namespace ae {

namespace tapjoy {
    
/** */
DesktopPluginTapjoy::~DesktopPluginTapjoy() {
    if (luaLibTapjoy != (DesktopLuaLibTapjoy *)0) {
        delete luaLibTapjoy;
    }
}
    
/** */
bool DesktopPluginTapjoy::init() {
// create library
    luaLibTapjoy = new DesktopLuaLibTapjoy(getLog(),getCfg());
    return true;
}

/** */
bool DesktopPluginTapjoy::addLuaExtraLibs() {
    TapjoyLuaExtraLib *extraLib = new TapjoyLuaExtraLib(luaLibTapjoy);
    ae::engine::LuaEngine *engine = getLuaEngine();
    engine->addExtraLib(extraLib);
    return true;
}

} // namespace

} // namespace