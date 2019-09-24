#include "DesktopPluginIAP.h"
#include "desktop_plugin_iap_lib.h"

using namespace ae::iap;

/** */
void* createDesktopPlugin() {
    return new DesktopPluginIAP();
}

/** */
void deleteDesktopPlugin(void *pluginPtr) {
    DesktopPluginIAP *plugin = (DesktopPluginIAP *)pluginPtr;
    delete plugin;
}