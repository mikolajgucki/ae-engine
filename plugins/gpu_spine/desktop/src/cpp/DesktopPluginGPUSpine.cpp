#include "SpineLuaExtraLib.h"
#include "DesktopPluginGPUSpine.h"

namespace ae {
    
namespace spine {

/** */
bool DesktopPluginGPUSpine::init() {
    gpuSpineLuaEnginePlugin = new GPUSpineLuaEnginePlugin();
    return true;
}
    
/** */
bool DesktopPluginGPUSpine::addLuaExtraLibs() {
    getLuaEngine()->addPlugin(gpuSpineLuaEnginePlugin);
    return true;
}

} // namespace
    
} // namespace