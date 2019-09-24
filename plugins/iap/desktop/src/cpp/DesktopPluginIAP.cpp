#include "IAPLuaExtraLib.h"
#include "DesktopPluginIAP.h"

namespace ae {
    
namespace iap {
    
/** */
DesktopPluginIAP::~DesktopPluginIAP() {
    if (luaLibIAP != (DesktopLuaLibIAP *)0) {
        delete luaLibIAP;
    }
}
    
/** */
bool DesktopPluginIAP::init() {
    luaLibIAP = new DesktopLuaLibIAP(getLog(),getCfg(),getTimer());
    if (luaLibIAP->initLuaLib() == false) {
        setError(luaLibIAP->getError());
        return false;
    }
    
    return true;
}

/** */
bool DesktopPluginIAP::addLuaExtraLibs() {
    getLuaEngine()->addExtraLib(new IAPLuaExtraLib(luaLibIAP));
    return true;
}

} // namespace
    
} // namespace