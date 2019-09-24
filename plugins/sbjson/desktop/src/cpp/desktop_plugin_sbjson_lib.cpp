#include "DesktopPluginSBJson.h"
#include "desktop_plugin_sbjson_lib.h"

using namespace ae::sbjson;

/** */
void* createDesktopPlugin() {
    return new DesktopPluginSBJson();
}

/** */
void deleteDesktopPlugin(void *pluginPtr) {
    DesktopPluginSBJson *plugin = (DesktopPluginSBJson *)pluginPtr;
    delete plugin;
}