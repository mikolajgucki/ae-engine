#include "SpineLuaExtraLib.h"
#include "DesktopPluginSpine.h"

namespace ae {
    
namespace spine {

/** */
bool DesktopPluginSpine::addLuaExtraLibs() {
    getLuaEngine()->addExtraLib(new SpineLuaExtraLib());
    return true;
}

} // namespace
    
} // namespace