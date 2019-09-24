#include "DesktopPluginSpine.h"
#include "desktop_plugin_spine_lib.h"

using namespace ae::spine;

/** */
void* createDesktopPlugin() {
    return new DesktopPluginSpine();
}