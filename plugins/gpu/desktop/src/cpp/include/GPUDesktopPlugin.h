#ifndef AE_GPU_DESKTOP_PLUGIN_H
#define AE_GPU_DESKTOP_PLUGIN_H

#include "GPULuaEnginePlugin.h"
#include "DesktopPlugin.h"

namespace ae {

namespace gpu {
    
/**
 * \brief The desktop GPU plugin.
 */
class GPUDesktopPlugin:public ::ae::engine::desktop::DesktopPlugin {
    /// The Lua engine plugin.
    GPULuaEnginePlugin *gpuLuaEnginePlugin;
    
public:
    /** */
    GPUDesktopPlugin():DesktopPlugin(name()),
        gpuLuaEnginePlugin((GPULuaEnginePlugin *)0) {
    }
    
    /** */
    virtual ~GPUDesktopPlugin() {
    }
    
    /** */
    virtual bool init();
    
    /** */
    virtual bool addLuaEnginePlugins();        
    
    /**
     * \brief Gets the plugin name.
     * \return The plugin name.
     */
    static const char *name() {
        return "gpu";
    }
};

} // namespace

} // namespace

#endif // AE_GPU_DESKTOP_PLUGIN_H