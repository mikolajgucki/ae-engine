#ifndef AE_LUA_ENGINE_PLUGIN_MANAGER_H
#define AE_LUA_ENGINE_PLUGIN_MANAGER_H

#include <vector>
#include "lua.hpp"

#include "Logger.h"
#include "Error.h"
#include "LuaEngineCfg.h"
#include "LuaEnginePlugin.h"

namespace ae {
    
namespace engine {
  
/**
 * \brief Manages Lua engine plugins.
 */
class LuaEnginePluginManager:public Error {
    /// The plugins.
    std::vector<LuaEnginePlugin *> plugins;
    
public:
    /** */
    LuaEnginePluginManager():Error(),plugins() {
    }
    
    /**
     * \brief Adds a Lua engine plugin.
     * \param plugin The plugin.
     */
    void addPlugin(LuaEnginePlugin *plugin);
    
    /**
     * \brief Gets a Lua engine plugin by name.
     * \param name The plugin name.
     * \return The plugin or null if there is no such plugin.
     */
    LuaEnginePlugin *getPlugin(const std::string& name);    
    
    /**
     * \brief Initializes the plugins.
     * \param logger The logger.
     * \param L The Lua state.
     * \param luaEngine The Lua engine.
     * \return <code>true</code> on success, sets error and returns
     *   <code>false</code> otherwise.
     */
    bool initPlugins(Logger *logger,lua_State *L,LuaEngine *luaEngine);
    
    /**
     * \brief Configures the Lua engine.
     * \param cfg The configuration to change.
     * \return <code>true</code> on success, sets error and returns
     *   <code>false</code> otherwise.
     */
    bool configureLuaEngine(LuaEngineCfg *cfg);
    
    /**
     * \brief Starts the plugins.
     * \return <code>true</code> on success, sets error and returns
     *   <code>false</code> otherwise.
     */
    bool startPlugins();
    
    /**
     * \brief Stops the plugins.
     * \return <code>true</code> on success, sets error and returns
     *   <code>false</code> otherwise.
     */
    bool stopPlugins();    
    
    /**
     * \brief Deletes the plugins. They cannot be deleted in the destructor due
     *   to DLL memory handling. If a plugin is created inside DLL, it has to
     *   be deleted there. Therefore, we cannot delete the plugins when the
     *   manager is deleted.
     */
    void deletePlugins();
};
    
} // namespace
    
} // namespace

#endif // AE_LUA_ENGINE_PLUGIN_MANAGER_H