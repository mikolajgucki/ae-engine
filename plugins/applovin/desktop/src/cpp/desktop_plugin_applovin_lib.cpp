#include "DesktopPluginAppLovin.h"
#include "desktop_plugin_applovin_lib.h"

using namespace ae::applovin;

/** */
void* createDesktopPlugin() {
    return new DesktopPluginAppLovin();
}

/** */
void deleteDesktopPlugin(void *pluginPtr) {
    DesktopPluginAppLovin *plugin = (DesktopPluginAppLovin *)pluginPtr;
    delete plugin;
}