#include "DesktopPluginFlurry.h"
#include "desktop_plugin_flurry_lib.h"

using namespace ae::flurry;

/** */
void* createDesktopPlugin() {
    return new DesktopPluginFlurry();
}

/** */
void deleteDesktopPlugin(void *pluginPtr) {
    DesktopPluginFlurry *plugin = (DesktopPluginFlurry *)pluginPtr;
    delete plugin;
}