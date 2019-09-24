#ifndef AE_GPU_LUA_ENGINE_PLUGIN_H
#define AE_GPU_LUA_ENGINE_PLUGIN_H

#include "ae_gpu.h"
#include "LuaEnginePlugin.h"
#include "GPUMiscFuncs.h"
#include "DefaultDrawerFactory.h"
#include "DrawerIndexFactory.h"
#include "GPUQueueFactory.h"

namespace ae {

namespace gpu {
    
/**
 * \brief The GPU Lua engine plugin.
 */
class GPULuaEnginePlugin:public ::ae::engine::LuaEnginePlugin {
    /// The miscellaneous functions.
    GPUMiscFuncs *miscFuncs;
    
    /// The default drawer factory.
    DefaultDrawerFactory *defaultDrawerFactory;
    
    /// The drawer buffer factory.
    DrawerIndexFactory *drawerIndexFactory;
    
    /// The GPU queue factory.
    GPUQueueFactory *gpuQueueFactory;
    
public:
    /** */
    GPULuaEnginePlugin():LuaEnginePlugin(name()),
        miscFuncs((GPUMiscFuncs *)0),
        defaultDrawerFactory((DefaultDrawerFactory *)0),
        drawerIndexFactory((DrawerIndexFactory *)0),
        gpuQueueFactory((GPUQueueFactory *)0) {
    }
    
    /** */
    virtual ~GPULuaEnginePlugin() {
    }
    
    /**
     * \brief Sets the miscellaneous functions.
     * \param miscFuncs_ The functions.
     */
    void setMiscFuncs(GPUMiscFuncs *miscFuncs_);
    
    /**
     * \brief Sets the default drawer factory.
     * \param defaultDrawerFactory_ The factory.
     */
    void setDefaultDrawerFactory(DefaultDrawerFactory *defaultDrawerFactory_);
    
    /**
     * \brief Gets the default drawer factory.
     * \return The default drawer factory.
     */
    DefaultDrawerFactory *getDefaultDrawerFactory() const;
    
    /**
     * \brief Sets the drawer index factory.
     * \param drawerIndexFactory_ The factory.
     */
    void setDrawerIndexFactory(DrawerIndexFactory *drawerIndexFactory_);
    
    /**
     * \brief Sets the GPU queue factory.
     * \param gpuQueueFactory_ The queue factory.
     */
    void setGPUQueueFactory(GPUQueueFactory *gpuQueueFactory_);
    
    /** */
    virtual bool start();
    
    /** */
    virtual bool stop();    
    
    /**
     * \brief Gets the plugin name.
     * \return The plugin name.
     */
    static const char *name() {
        return getGPUName();
    }     
};
    
} // namespace

} // namespace

#endif // AE_GPU_LUA_ENGINE_PLUGIN_H