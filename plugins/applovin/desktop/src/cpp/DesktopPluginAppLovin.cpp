#include "AppLovinLuaExtraLib.h"
#include "DesktopPluginAppLovin.h"

namespace ae {
    
namespace applovin {
    
/** */
DesktopPluginAppLovin::~DesktopPluginAppLovin() {
    if (luaLibAppLovin != (DesktopLuaLibAppLovin *)0) {
        delete luaLibAppLovin;
    }
}
    
/** */
bool DesktopPluginAppLovin::init() {
// create library
    luaLibAppLovin = new DesktopLuaLibAppLovin(getLog());
    return true;
}

/** */
bool DesktopPluginAppLovin::addLuaExtraLibs() {
    getLuaEngine()->addExtraLib(new AppLovinLuaExtraLib(luaLibAppLovin));
    return true;
}

} // namespace
    
} // namespace