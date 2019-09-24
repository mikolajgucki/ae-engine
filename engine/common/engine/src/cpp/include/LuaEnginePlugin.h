#ifndef AE_LUA_ENGINE_PLUGIN_H
#define AE_LUA_ENGINE_PLUGIN_H

#include "lua.hpp"

#include "Error.h"
#include "DLog.h"
#include "LuaEngineCfg.h"

namespace ae {
    
namespace engine {
    
/// Forward declaration.
class LuaEngine;
    
/**
 * \brief The superclass for Lua engine plugins.
 */
class LuaEnginePlugin:public Error {
    /// The plugin name.
    const std::string name;
    
    /// The log.
    DLog *log;
    
    /// The Lua state.
    lua_State *L;
    
    /// The Lua engine.
    LuaEngine *luaEngine;
    
public:
    /** */
    LuaEnginePlugin(const std::string& name_):Error(),name(name_),
        log((DLog *)0),L((lua_State *)0),luaEngine((LuaEngine *)0) {
    }
    
    /** */
    virtual ~LuaEnginePlugin() {
        if (log != (DLog *)0) {
            delete log;
        }
    }
    
    /**
     * \brief Gets the plugin name.
     * \return The plugin name.
     */
    const std::string &getName() const {
        return name;
    }    
    
    /**
     * \brief Sets the Lua engine.
     * \param luaEngine_ The Lua engine.
     */
    void setLuaEngine(LuaEngine *luaEngine_) {
        luaEngine = luaEngine_;
    }
    
    /**
     * \brief Gets the Lua engine.
     * \return The Lua engine.
     */
    LuaEngine *getLuaEngine() {
        return luaEngine;
    }
    
    /**
     * \brief Sets the log.
     * \param log_ The log.
     */
    void setLog(DLog *log_) {
        log = log_;
    }
    
    /**
     * \brief Gets the log.
     * \return The log.
     */
    DLog *getLog() {
        return log;
    }
    
    /**
     * \brief Sets the Lua state.
     * \param L_ The Lua state.
     */
    void setLuaState(lua_State *L_) {
        L = L_;
    }
    
    /**
     * \brief Gets the Lua state.
     * \return The Lua state.
     */
    lua_State *getLuaState() const {
        return L;
    }
    
    /**
     * \brief Initializes the plugin. It's a good point to pass objects to
     *   to other plugins.
     * \return <code>true</code> on success, sets error and returns
     *   <code>false</code> otherwise.
     */
    virtual bool init() {
        return true;
    }
    
    /**
     * \brief Changes the Lua engine configuration.
     * \param cfg The Lua engine configuration.
     * \return <code>true</code> on success, sets error and returns
     *   <code>false</code> otherwise.
     */
    virtual bool configureLuaEngine(LuaEngineCfg *cfg) {
        return true;
    }
    
    /**
     * \brief Starts the plugin.
     * \return <code>true</code> on success, sets error and returns
     *   <code>false</code> otherwise.
     */
    virtual bool start() {
        return true;
    }
    
    /**
     * \brief Stops the plugin.
     * \return <code>true</code> on success, sets error and returns
     *   <code>false</code> otherwise.
     */
    virtual bool stop() {
        return true;
    }
};
    
} // namespace
    
} // namespace

#endif // AE_LUA_ENGINE_PLUGIN_H