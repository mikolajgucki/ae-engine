#ifndef AE_DESKTOP_PLUGIN_HTTP_LIB_H
#define AE_DESKTOP_PLUGIN_HTTP_LIB_H

#include "desktop_plugin_lib.h"

extern "C" {
    /** */
    DYNLIB_EXPORT void* DYNLIB_EXPORT_CALL createDesktopPlugin();
    
    /** */
    DYNLIB_EXPORT void DYNLIB_EXPORT_CALL deleteDesktopPlugin(void *pluginPtr);    
}

#endif // AE_DESKTOP_PLUGIN_HTTP_LIB_H