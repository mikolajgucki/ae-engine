#include "GPGSLuaExtraLib.h"
#include "DesktopPluginGPGS.h"

namespace ae {
    
namespace gpgs {
    
/** */
DesktopPluginGPGS::~DesktopPluginGPGS() {
    if (luaLibGPGS != (DesktopLuaLibGPGS *)0) {
        delete luaLibGPGS;
    }
}
    
/** */
bool DesktopPluginGPGS::init() {
// create library
    luaLibGPGS = new DesktopLuaLibGPGS(getLog(),getTimer());
    return true;
}

/** */
bool DesktopPluginGPGS::addLuaExtraLibs() {
    getLuaEngine()->addExtraLib(new GPGSLuaExtraLib(luaLibGPGS));
    return true;
}

} // namespace
    
} // namespace
