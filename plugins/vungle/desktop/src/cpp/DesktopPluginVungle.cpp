#include "VungleLuaExtraLib.h"
#include "DesktopPluginVungle.h"

namespace ae {
    
namespace vungle {
    
/** */
DesktopPluginVungle::~DesktopPluginVungle() {
    if (luaLibVungle != (DesktopLuaLibVungle *)0) {
        delete luaLibVungle;
    }
}
    
/** */
bool DesktopPluginVungle::init() {
    luaLibVungle = new DesktopLuaLibVungle(getLog(),getCfg(),getTimer());
    if (luaLibVungle->initLuaLib() == false) {
        setError(luaLibVungle->getError());
        return false;
    }
    
    return true;
}

/** */
bool DesktopPluginVungle::addLuaExtraLibs() {
    getLuaEngine()->addExtraLib(new VungleLuaExtraLib(luaLibVungle));
    return true;
}

} // namespace
    
} // namespace