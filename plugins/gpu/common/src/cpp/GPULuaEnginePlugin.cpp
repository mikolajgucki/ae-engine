#include <memory>

#include "LuaTableUtil.h"
#include "GPULuaExtraLib.h"
#include "DefaultDrawerLuaExtraLib.h"
#include "DrawerIndexLuaExtraLib.h"
#include "GPUQueueLuaExtraLib.h"
#include "GPULuaEnginePlugin.h"
#include "LuaEngine.h"

using namespace std;
using namespace ae::lua;

namespace ae {

namespace gpu {
    
/// The log tag.
static const char *logTag = "gpu";    
    
/** */
void GPULuaEnginePlugin::setMiscFuncs(GPUMiscFuncs *miscFuncs_) {
    if (miscFuncs != (GPUMiscFuncs *)0) {
        getLog()->error(logTag,"Miscellaneous functions already set");
        return;
    }
    miscFuncs = miscFuncs_;
}

/** */
void GPULuaEnginePlugin::setDefaultDrawerFactory(
    DefaultDrawerFactory *defaultDrawerFactory_) {
//
    if (defaultDrawerFactory != (DefaultDrawerFactory *)0) {
        getLog()->error(logTag,"Default drawer factory already set");
        return;
    }
    defaultDrawerFactory = defaultDrawerFactory_;
}

/** */
DefaultDrawerFactory *GPULuaEnginePlugin::getDefaultDrawerFactory() const {
    return defaultDrawerFactory;
}

/** */
void GPULuaEnginePlugin::setDrawerIndexFactory(
    DrawerIndexFactory *drawerIndexFactory_) {
//
    if (drawerIndexFactory != (DrawerIndexFactory *)0) {
        getLog()->error(logTag,"Drawer index factory already set");
        return;
    }
    drawerIndexFactory = drawerIndexFactory_;
}

/** */
void GPULuaEnginePlugin::setGPUQueueFactory(GPUQueueFactory *gpuQueueFactory_) {
    if (gpuQueueFactory != (GPUQueueFactory *)0) {
        getLog()->error(logTag,"GPU queue factory alread set");
        return;
    }
    gpuQueueFactory = gpuQueueFactory_;
}

/** */
bool GPULuaEnginePlugin::start() {
    getLog()->trace(logTag,"Starting GPU plugin");

// check misc functions
    if (miscFuncs == (GPUMiscFuncs *)0) {
        setError("Miscellaneous functions not set");
        return false;
    }
    
// check default drawer
    if (defaultDrawerFactory == (DefaultDrawerFactory *)0) {
        setError("Default drawer factory not set");
        return false;
    }
    
// check drawer buffer index
    if (drawerIndexFactory == (DrawerIndexFactory *)0) {
        setError("Drawer index factory not set");
        return false;
    }
    
// check GPU queue
    if (gpuQueueFactory == (GPUQueueFactory *)0) {
        setError("GPU queue factory not set");
        return false;
    }
    
// global table
    LuaTableUtil luaTableUtil;
    if (luaTableUtil.newGlobalTable(getLuaState(),getGPUName()) == false) {
        setError(luaTableUtil.getError());
        return false;
    }

// GPU Lua library
    GPULuaExtraLib *gpuLuaExtraLib = new (nothrow) GPULuaExtraLib(miscFuncs);
    if (gpuLuaExtraLib == (GPULuaExtraLib *)0) {
        setNoMemoryError();
        return false;
    }
    getLuaEngine()->addExtraLib(gpuLuaExtraLib);

// default drawer Lua library
    DefaultDrawerLuaExtraLib *defaultDrawerLuaExtraLib =
        new (nothrow) DefaultDrawerLuaExtraLib(defaultDrawerFactory);
    if (defaultDrawerLuaExtraLib == (DefaultDrawerLuaExtraLib *)0) {
        setNoMemoryError();
        return false;
    }
    getLuaEngine()->addExtraLib(defaultDrawerLuaExtraLib);
    
// drawer index Lua library
    DrawerIndexLuaExtraLib *drawerIndexLuaExtraLib = 
        new (nothrow) DrawerIndexLuaExtraLib(drawerIndexFactory);
    if (drawerIndexLuaExtraLib == (DrawerIndexLuaExtraLib *)0) {
        setNoMemoryError();
        return false;
    }
    getLuaEngine()->addExtraLib(drawerIndexLuaExtraLib);
    
// GPU queue Lua library
    GPUQueueLuaExtraLib *gpuQueueLuaExtraLib =
        new (nothrow) GPUQueueLuaExtraLib(gpuQueueFactory);
    if (gpuQueueLuaExtraLib == (GPUQueueLuaExtraLib *)0) {
        setNoMemoryError();
        return false;        
    }
    getLuaEngine()->addExtraLib(gpuQueueLuaExtraLib);
    
    return true;
}

/** */
bool GPULuaEnginePlugin::stop() {
    getLog()->trace(logTag,"Stopping GPU plugin");
    
// delete the drawer factory
    if (defaultDrawerFactory != (DefaultDrawerFactory *)0) {
        delete defaultDrawerFactory;
        defaultDrawerFactory = (DefaultDrawerFactory *)0;
    }
    
    return true;
}
    
} // namespace

} // namespace