#ifndef AE_GPU_SPINE_LUA_ENGINE_PLUGIN_H
#define AE_GPU_SPINE_LUA_ENGINE_PLUGIN_H

#include "LuaEnginePlugin.h"

namespace ae {

namespace spine {
    
/**
 * \brief 
 */
class GPUSpineLuaEnginePlugin:public ::ae::engine::LuaEnginePlugin {
public:
    /** */
    GPUSpineLuaEnginePlugin():LuaEnginePlugin(name()) {
    }
    
    /** */
    virtual ~GPUSpineLuaEnginePlugin() {
    }
    
    /** */
    virtual bool init();
    
    /**
     * \brief Gets the plugin name.
     * \return The plugin name.
     */
    static const char *name() {
        return "gpu_spine";
    }    
};
    
} // namespace

} // namespace

#endif // AE_GPU_SPINE_LUA_ENGINE_PLUGIN_H