#include "DesktopPluginUnityAds.h"
#include "desktop_plugin_unityads_lib.h"

using namespace ae::unityads;

/** */
void* createDesktopPlugin() {
    return new DesktopPluginUnityAds();
}

/** */
void deleteDesktopPlugin(void *pluginPtr) {
    DesktopPluginUnityAds *plugin = (DesktopPluginUnityAds *)pluginPtr;
    delete plugin;
}