#include "DesktopPluginChartboost.h"
#include "desktop_plugin_chartboost_lib.h"

using namespace ae::chartboost;

/** */
void* createDesktopPlugin() {
    return new DesktopPluginChartboost();
}

/** */
void deleteDesktopPlugin(void *pluginPtr) {
    DesktopPluginChartboost *plugin = (DesktopPluginChartboost *)pluginPtr;
    delete plugin;
}