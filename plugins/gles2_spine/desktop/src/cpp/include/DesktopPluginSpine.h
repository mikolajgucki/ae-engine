#ifndef AE_DESKTOP_PLUGIN_SPINE_H
#define AE_DESKTOP_PLUGIN_SPINE_H

#include "DesktopPlugin.h"

namespace ae {
    
namespace spine {

/**
 * \brief The desktop Spine plugin.
 */
class DesktopPluginSpine:public ::ae::engine::desktop::DesktopPlugin {
public:
    /** */
    DesktopPluginSpine():DesktopPlugin(name()) {
    }
    
    /** */
    virtual ~DesktopPluginSpine() {
    }
    
    /** */
    virtual bool addLuaExtraLibs();
    
    /**
     * \brief Gets the plugin name.
     * \return The plugin name.
     */
    static const char *name() {
        return "spine";
    }
};
    
} // namespace
    
} // namespace

#endif // AE_DESKTOP_PLUGIN_SPINE_H