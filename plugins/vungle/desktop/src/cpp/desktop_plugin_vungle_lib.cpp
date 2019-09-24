#include "DesktopPluginVungle.h"
#include "desktop_plugin_vungle_lib.h"

using namespace ae::vungle;

/** */
void* createDesktopPlugin() {
    return new DesktopPluginVungle();
}

/** */
void deleteDesktopPlugin(void *pluginPtr) {
    DesktopPluginVungle *plugin = (DesktopPluginVungle *)pluginPtr;
    delete plugin;
}