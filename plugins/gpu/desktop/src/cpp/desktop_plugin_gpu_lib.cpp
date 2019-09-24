#include "GPUDesktopPlugin.h"
#include "desktop_plugin_gpu_lib.h"

using namespace ae::gpu;

/** */
void* createDesktopPlugin() {
    return new GPUDesktopPlugin();
}

/** */
void deleteDesktopPlugin(void *pluginPtr) {
    GPUDesktopPlugin *plugin = (GPUDesktopPlugin *)pluginPtr;
    delete plugin;
}