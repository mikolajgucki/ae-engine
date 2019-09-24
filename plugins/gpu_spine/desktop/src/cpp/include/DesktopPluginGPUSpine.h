#ifndef AE_DESKTOP_PLUGIN_GPU_SPINE_H
#define AE_DESKTOP_PLUGIN_GPU_SPINE_H

#include "GPUSpineLuaEnginePlugin.h"
#include "DesktopPlugin.h"

namespace ae {
    
namespace spine {

/**
 * \brief The desktop Spine plugin.
 */
class DesktopPluginGPUSpine:public ::ae::engine::desktop::DesktopPlugin {
    /** */
    GPUSpineLuaEnginePlugin *gpuSpineLuaEnginePlugin;
    
public:
    /** */
    DesktopPluginGPUSpine():DesktopPlugin(name()),
        gpuSpineLuaEnginePlugin((GPUSpineLuaEnginePlugin *)0) {
    }
    
    /** */
    virtual ~DesktopPluginGPUSpine() {
    }
    
    /** */
    virtual bool init();
    
    /** */
    virtual bool addLuaExtraLibs();
    
    /**
     * \brief Gets the plugin name.
     * \return The plugin name.
     */
    static const char *name() {
        return "spine";
    }
};
    
} // namespace
    
} // namespace

#endif // AE_DESKTOP_PLUGIN_GPU_SPINE_H