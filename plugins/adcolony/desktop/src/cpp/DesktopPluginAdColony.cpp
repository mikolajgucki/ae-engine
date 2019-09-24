#include "AdColonyLuaExtraLib.h"
#include "DesktopPluginAdColony.h"

namespace ae {
    
namespace adcolony {
    
/** */
DesktopPluginAdColony::~DesktopPluginAdColony() {
    if (luaLibAdColony != (DesktopLuaLibAdColony *)0) {
        delete luaLibAdColony;
    }
}
    
/** */
bool DesktopPluginAdColony::init() {
// create library
    luaLibAdColony = new DesktopLuaLibAdColony(getCfg(),getTimer(),getLog());
    return true;
}

/** */
bool DesktopPluginAdColony::addLuaExtraLibs() {
    getLuaEngine()->addExtraLib(new AdColonyLuaExtraLib(luaLibAdColony));
    return true;
}

} // namespace
    
} // namespace