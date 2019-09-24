#include "GameCenterLuaExtraLib.h"
#include "DesktopPluginGameCenter.h"

namespace ae {
    
namespace gamecenter {
    
/** */
DesktopPluginGameCenter::~DesktopPluginGameCenter() {
    if (luaLibGameCenter != (DesktopLuaLibGameCenter *)0) {
        delete luaLibGameCenter;
    }
}
    
/** */
bool DesktopPluginGameCenter::init() {
// create library
    luaLibGameCenter = new DesktopLuaLibGameCenter(
        getLog(),getCfg(),getTimer());
    return true;
}

/** */
bool DesktopPluginGameCenter::addLuaExtraLibs() {
    getLuaEngine()->addExtraLib(new GameCenterLuaExtraLib(luaLibGameCenter));
    return true;
}

} // namespace
    
} // namespace