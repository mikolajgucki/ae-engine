#include "FlurryLuaExtraLib.h"
#include "DesktopPluginFlurry.h"

namespace ae {
    
namespace flurry {
    
/** */
DesktopPluginFlurry::~DesktopPluginFlurry() {
    if (luaLibFlurry != (DesktopLuaLibFlurry *)0) {
        delete luaLibFlurry;
    }
}
    
/** */
bool DesktopPluginFlurry::init() {
// create library
    luaLibFlurry = new DesktopLuaLibFlurry(getLog());
    return true;
}

/** */
bool DesktopPluginFlurry::addLuaExtraLibs() {
    getLuaEngine()->addExtraLib(new FlurryLuaExtraLib(luaLibFlurry));
    return true;
}

} // namespace
    
} // namespace