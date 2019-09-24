#ifndef AE_DESKTOP_PLUGIN_SB_JSON_H
#define AE_DESKTOP_PLUGIN_SB_JSON_H

#include "DesktopPlugin.h"

namespace ae {
    
namespace sbjson {

/**
 * \brief The desktop SBJson plugin.
 */
class DesktopPluginSBJson:public ::ae::engine::desktop::DesktopPlugin {
public:
    /** */
    DesktopPluginSBJson():DesktopPlugin(name()) {
    }
    
    /** */
    virtual ~DesktopPluginSBJson() {
    }
    
    /**
     * \brief Gets the plugin name.
     * \return The plugin name.
     */
    static const char *name() {
        return "sbjson";
    }
};
    
} // namespace
    
} // namespace

#endif // AE_DESKTOP_PLUGIN_SB_JSON_H