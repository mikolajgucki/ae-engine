#include "DesktopPluginHTTP.h"
#include "desktop_plugin_http_lib.h"

using namespace ae::http;

/** */
void* createDesktopPlugin() {
    return new DesktopPluginHTTP();
}

/** */
void deleteDesktopPlugin(void *pluginPtr) {
    DesktopPluginHTTP *plugin = (DesktopPluginHTTP *)pluginPtr;
    delete plugin;
}