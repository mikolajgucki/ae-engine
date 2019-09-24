#include "GPUDesktopPlugin.h"

namespace ae {

namespace gpu {
    
/** */
bool GPUDesktopPlugin::init() {
// create the Lua engine plugin
    gpuLuaEnginePlugin = new GPULuaEnginePlugin();
    
    return true;
}

/** */
bool GPUDesktopPlugin::addLuaEnginePlugins() {
    getLuaEngine()->addPlugin(gpuLuaEnginePlugin);
    return true;
}
    
} // namespace

} // namespace