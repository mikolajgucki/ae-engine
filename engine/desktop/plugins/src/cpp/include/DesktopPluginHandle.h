#ifndef AE_DESKTOP_PLUGIN_HANDLE_H
#define AE_DESKTOP_PLUGIN_HANDLE_H

#include <string>
#include "DynLib.h"
#include "DesktopPlugin.h"

namespace ae {

namespace engine {

namespace desktop {
    
/**
 * \brief Represents a desktop plugin handle.
 */
class DesktopPluginHandle {
    /// The dynamic library loader.
    ::ae::system::DynLib *dynLib;
    
    /// The plugin itself.
    DesktopPlugin *plugin;
    
public:
    /** */
    DesktopPluginHandle(::ae::system::DynLib *dynLib_,DesktopPlugin *plugin_):
        dynLib(dynLib_),plugin(plugin_) {
    }
    
    /** */
    ~DesktopPluginHandle() {
    }
    
    /** */
    ::ae::system::DynLib *getDynLib() {
        return dynLib;
    }
    
    /** */
    DesktopPlugin *getPlugin() {
        return plugin;
    }
};
    
} // namespace

} // namespace

} // namespace

#endif // AE_DESKTOP_PLUGIN_HANDLE_H