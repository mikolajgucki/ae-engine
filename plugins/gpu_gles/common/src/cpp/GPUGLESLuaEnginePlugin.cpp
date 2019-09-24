#include <sstream>
#include <memory>

#include "Log.h"
#include "GLDefaultDrawerFactory.h"
#include "GLDrawerIndexFactory.h"
#include "GLGPUQueueFactory.h"
#include "GPULuaEnginePlugin.h"
#include "GPUGLESLuaEnginePlugin.h"

using namespace std;
using namespace ae::engine;
using namespace ae::gpu;

namespace ae {
    
namespace gles {
    
/// The log tag.
static const char *logTag = "gpu_gles";

/** */
void GPUGLESLuaEnginePlugin::deleteObjects() {
// delete display listener
    if (displayListener != (GLLuaEngineDisplayListener *)0) {
        delete displayListener;
        displayListener = (GLLuaEngineDisplayListener *)0;
    }
    
// delete texture factory
    if (textureFactory != (GLTextureFactory *)0) {
        delete textureFactory;
        textureFactory = (GLTextureFactory *)0;
    } 
    
// misc functions
    if (miscFuncs != (GLGPUMiscFuncs *)0) {
        delete miscFuncs;
        miscFuncs = (GLGPUMiscFuncs *)0;
    }
}

/** */
bool GPUGLESLuaEnginePlugin::init() {
// GPU plugin
    GPULuaEnginePlugin *gpuPlugin =
        (GPULuaEnginePlugin *)(getLuaEngine()->getPlugin(string("gpu")));
    if (gpuPlugin == (GPULuaEnginePlugin *)0) {
        setError("Lua engine plugin 'gpu' not found");
        return false;
    }
    
// misc functions
    miscFuncs = new (nothrow) GLGPUMiscFuncs();
    if (miscFuncs == (GLGPUMiscFuncs *)0) {
        setNoMemoryError();
        return false;
    }
    gpuPlugin->setMiscFuncs(miscFuncs);

// default drawer
    GLDefaultDrawerFactory *drawerFactory = 
        new (nothrow) GLDefaultDrawerFactory();
    if (drawerFactory == (GLDefaultDrawerFactory *)0) {
        setNoMemoryError();
        return false;
    }
    gpuPlugin->setDefaultDrawerFactory(drawerFactory);
    
// drawer index
    GLDrawerIndexFactory *drawerIndexFactory =
        new (nothrow) GLDrawerIndexFactory();
    if (drawerIndexFactory == (GLDrawerIndexFactory *)0) {
        setNoMemoryError();
        return false;
    }
    gpuPlugin->setDrawerIndexFactory(drawerIndexFactory);
    
// GPU factory
    GLGPUQueueFactory *gpuQueueFactory = new (nothrow) GLGPUQueueFactory();
    if (gpuQueueFactory == (GLGPUQueueFactory *)0) {
        setNoMemoryError();
        return false;        
    }
    gpuPlugin->setGPUQueueFactory(gpuQueueFactory);
    
    return true;
}

/** */
bool GPUGLESLuaEnginePlugin::configureLuaEngine(LuaEngineCfg *cfg) {
// texture factory
    textureFactory = new (nothrow) GLTextureFactory();
    if (textureFactory == (GLTextureFactory *)0) {
        setNoMemoryError();
        return false;
    }
    cfg->setTextureFactory(textureFactory);

// display listener
    displayListener = new (nothrow) GLLuaEngineDisplayListener();
    if (displayListener == (GLLuaEngineDisplayListener *)0) {
        setNoMemoryError();
        return false;
    }
    cfg->setLuaEngineDisplayListener(displayListener);
    
    return true;
}

/** */
bool GPUGLESLuaEnginePlugin::start() {
    getLog()->trace(logTag,"Starting GPU OpenGL ES plugin");
    
// blend func
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
    
    return true;
}

/** */
bool GPUGLESLuaEnginePlugin::stop() {
    getLog()->trace(logTag,"Stopping GPU OpenGL ES plugin");

// delete stuff
    deleteObjects();
    
    return true;
}

} // namespace
    
} // namespace