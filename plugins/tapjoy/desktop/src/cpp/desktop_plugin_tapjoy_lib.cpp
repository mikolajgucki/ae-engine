#include "DesktopPluginTapjoy.h"
#include "desktop_plugin_tapjoy_lib.h"

using namespace ae::tapjoy;

/** */
void* createDesktopPlugin() {
    return new DesktopPluginTapjoy();
}

/** */
void deleteDesktopPlugin(void *pluginPtr) {
    DesktopPluginTapjoy *plugin = (DesktopPluginTapjoy *)pluginPtr;
    delete plugin;
}