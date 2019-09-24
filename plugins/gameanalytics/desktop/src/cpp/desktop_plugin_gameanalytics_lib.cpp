#include "DesktopPluginGameAnalytics.h"
#include "desktop_plugin_gameanalytics_lib.h"

using namespace ae::gameanalytics;

/** */
void* createDesktopPlugin() {
    return new DesktopPluginGameAnalytics();
}

/** */
void deleteDesktopPlugin(void *pluginPtr) {
    DesktopPluginGameAnalytics *plugin =
        (DesktopPluginGameAnalytics *)pluginPtr;
    delete plugin;
}