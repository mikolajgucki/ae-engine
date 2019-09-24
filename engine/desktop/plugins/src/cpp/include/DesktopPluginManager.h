#ifndef AE_DESKTOP_PLUGIN_MANAGER_H
#define AE_DESKTOP_PLUGIN_MANAGER_H

#include <string>
#include <vector>

#include "Logger.h"
#include "Error.h"
#include "DynLib.h"
#include "Timer.h"
#include "LuaEngine.h"
#include "DesktopPluginHandle.h"
#include "desktop_plugin_lib.h"

namespace ae {

namespace engine {

namespace desktop {

/**
 * \brief The desktop plugin manager.
 */
class DesktopPluginManager:public Error {
    /// The directories with plugins.
    std::vector<std::string> pluginDirs;
    
    /// The logger.
    Logger *logger;
    
    /// The timer.
    Timer *timer;
    
    /// The Lua engine.
    LuaEngine *luaEngine;
    
    /// The plugin handles.
    std::vector<DesktopPluginHandle *> handles;
    
    /** */
    bool deletePlugin(::ae::system::DynLib *dynLib,DesktopPlugin *plugin);
    
    /** */
    void destroy();
    
    /** */
    const std::string getPathToPluginLib(const std::string& pluginName);
    
    /** */
    void pluginStartFailed(ae::system::DynLib *dynLib,DesktopPlugin *plugin);
    
public:
    /** */
    DesktopPluginManager(std::vector<std::string> pluginDirs_,Logger *logger_,
        Timer *timer_,LuaEngine *luaEngine_):Error(),pluginDirs(pluginDirs_),
        logger(logger_),timer(timer_),luaEngine(luaEngine_),handles() {
    }
    
    /** */
    ~DesktopPluginManager() {
        destroy();
    }
    
    /**
     * \brief Initializes a plugin.
     * \param pluginId The plugin identifier.
     * \return true on success, sets error and returns false otherwise.
     */
    bool initPlugin(const std::string& pluginId);
};
    
} // namespace

} // namespace

} // namespace

#endif // AE_DESKTOP_PLUGIN_MANAGER_H
