#ifndef AE_DESKTOP_PLUGIN_H
#define AE_DESKTOP_PLUGIN_H

#include <string>
#include "DLog.h"
#include "Error.h"
#include "Timer.h"
#include "LuaEngine.h"
#include "DesktopPluginCfg.h"

namespace ae {

namespace engine {

namespace desktop {

/**
 * \brief The superclass for desktop plugins.
 */
class DesktopPlugin:public ::ae::Error {
    /// The plugin name.
    const std::string name;
    
    /// The log.
    DLog *log;
    
    /// The timer.
    Timer *timer;
    
    /// The plugin configuration.
    DesktopPluginCfg *cfg;
    
    /// The Lua engine.
    LuaEngine *luaEngine;
    
protected:
    /** */
    DesktopPlugin(const std::string& _name):Error(),name(_name),
        log((DLog *)0),timer((Timer *)0),
        cfg((DesktopPluginCfg *)0),luaEngine((LuaEngine *)0) {
    }
    
public:
    /** */
    virtual ~DesktopPlugin() {
        if (cfg != (DesktopPluginCfg *)0) {
            delete cfg;
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
     * \brief Sets the time.
     * \param timer The timer.
     */
    void setTimer(Timer *timer_) {
        timer = timer_;
    }
    
    /**
     * \brief Gets the timer.
     * \return The timer.
     */
    Timer *getTimer() {
        return timer;
    }
    
    /**
     * \brief Sets the configuration.
     * \param cfg_ The configuration.
     */
    void setCfg(DesktopPluginCfg *cfg_) {
        cfg = cfg_;
    }
    
    /**
     * \brief Gets the configuration.
     * \return The configuration.
     */
    DesktopPluginCfg *getCfg() {
        return cfg;
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
     * \brief Initializes the plugin.
     * \return true on success, sets error and returns false on error.
     */
    virtual bool init() {
        return true;
    }
    
    /**
     * \brief Adds Lua extra libraries.
     * \return true on success, sets error and returns false on error.     
     */
    virtual bool addLuaExtraLibs() {
        return true;
    }
    
    /**
     * \brief Adds Lua engine plugins.
     * \return true on success, sets error and returns false on error.     
     */
    virtual bool addLuaEnginePlugins() {
        return true;
    }
};
    
} // namespace

} // namespace

} // namespace

#endif // AE_DESKTOP_PLUGIN_H