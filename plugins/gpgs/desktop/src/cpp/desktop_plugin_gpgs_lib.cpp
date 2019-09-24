#include "DesktopPluginGPGS.h"
#include "desktop_plugin_gpgs_lib.h"

using namespace ae::gpgs;

/** */
void* createDesktopPlugin() {
    return new DesktopPluginGPGS();
}

/** */
void deleteDesktopPlugin(void *pluginPtr) {
    DesktopPluginGPGS *plugin = (DesktopPluginGPGS *)pluginPtr;
    delete plugin;
}