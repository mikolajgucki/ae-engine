#include "GPUGLESDesktopPlugin.h"
#include "desktop_plugin_gpu_gles_lib.h"

using namespace ae::gpu::gles;

/** */
void* createDesktopPlugin() {
    return new GPUGLESDesktopPlugin();
}

/** */
void deleteDesktopPlugin(void *pluginPtr) {
    GPUGLESDesktopPlugin *plugin = (GPUGLESDesktopPlugin *)pluginPtr;
    delete plugin;
}