#include "DesktopPluginAdColony.h"
#include "desktop_plugin_adcolony_lib.h"

using namespace ae::adcolony;

/** */
void* createDesktopPlugin() {
    return new DesktopPluginAdColony();
}

/** */
void deleteDesktopPlugin(void *pluginPtr) {
    DesktopPluginAdColony *plugin = (DesktopPluginAdColony *)pluginPtr;
    delete plugin;
}