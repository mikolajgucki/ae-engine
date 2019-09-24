#ifndef AE_DESKTOP_PLUGIN_LIB_H
#define AE_DESKTOP_PLUGIN_LIB_H

#include "dynlib_export.h"

/** The name of the function which creates a plugin. */
const char *const createDesktopPluginFuncName = "createDesktopPlugin";

/** The name of the function which deletes a plugin. */
const char *const deleteDesktopPluginFuncName = "deleteDesktopPlugin";

/** The prototype of the function which creates a plugin. */
typedef void* (*createDesktopPluginFunc)();

/** The prototype of the function which deletes a plugin. */
typedef void (*deleteDesktopPluginFunc)(void *);

#endif // AE_DESKTOP_PLUGIN_LIB_H