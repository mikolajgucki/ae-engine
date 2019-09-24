#include "DesktopPluginGLES2.h"
#include "desktop_plugin_gles2_lib.h"

using namespace ae::gles2;

/** */
void* createDesktopPlugin() {
    return new DesktopPluginGLES2();
}

/** */
void deleteDesktopPlugin(void *pluginPtr) {
    DesktopPluginGLES2 *plugin = (DesktopPluginGLES2 *)pluginPtr;
    delete plugin;
}