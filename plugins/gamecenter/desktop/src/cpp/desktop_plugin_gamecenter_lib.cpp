#include "DesktopPluginGameCenter.h"
#include "desktop_plugin_gamecenter_lib.h"

using namespace ae::gamecenter;

/** */
void* createDesktopPlugin() {
    return new DesktopPluginGameCenter();
}

/** */
void deleteDesktopPlugin(void *pluginPtr) {
    DesktopPluginGameCenter *plugin = (DesktopPluginGameCenter *)pluginPtr;
    delete plugin;
}