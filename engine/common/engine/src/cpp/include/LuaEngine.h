#ifndef AE_LUA_ENGINE_H
#define AE_LUA_ENGINE_H

#include "lua.hpp"
#include <vector>

#include "Logger.h"
#include "lua_common.h"
#include "Error.h"
#include "LuaLoader.h"
#include "LuaPCall.h"
#include "LuaGC.h"
#include "InputStreamProvider.h"
#include "ImageUtil.h"
#include "Engine.h"
#include "EngineRequest.h"
#include "LuaModel.h"
#include "LuaEngineRestartListener.h"
#include "LuaEnginePauseListener.h"
#include "LuaEngineQuitListener.h"
#include "LuaEngineCfg.h"
#include "LuaExtraLibManager.h"
#include "LuaEnginePluginManager.h"

namespace ae {

namespace engine {
  
/**
 * \brief Responsible for Lua stuff.
 */
class LuaEngine:public Error {
    /// The name of the Lua source loaded on start
    static const char* const ROOT_LUA_SRC;
    
    /// The logger.
    Logger *logger;
    
    /// The Lua state.
    lua_State *L;    
    
    /// The configuration.
    LuaEngineCfg cfg;
    
    /// The Lua loader.
    ::ae::lua::LuaLoader *luaLoader;
    
    /// The image utility.
    ::ae::image::ImageUtil *imageUtil;

    /// The Lua protectd call
    ::ae::lua::LuaPCall luaPCall;

    /// The engine related to this Lua engine.
    Engine *engine;
    
    /// The models added from the Lua code
    std::vector<LuaModel *> luaModels;
    
    /// The restart request.
    EngineRequest *restartRequest;
    
    /// The restart listener.
    LuaEngineRestartListener *restartListener;
    
    /// The pause/resume listener.
    LuaEnginePauseListener *pauseListener;
    
    /// The quit listener.
    LuaEngineQuitListener *quitListener;
    
    /// The view width.
    int width;
    
    /// The view height.
    int height;
    
    /// The Lua garbage collector stuff.
    ::ae::lua::LuaGC luaGC;
    
    /// The extra library manager.
    LuaExtraLibManager extraLibManager;
    
    /// The Lua engine plugin manager.
    LuaEnginePluginManager enginePluginManager;
    
    /**
     * \brief Creates the Lua engine.
     */
    void create();
    
    /**
     * \brief Loads the default libraries.
     * \return <code>true</code> on success, otherwise sets error and returns
     *   <code>false</code>.     
     */
    bool loadDefaultLibs();
    
    /**
     * \brief Unloads the default libraries.
     */
    void unloadDefaultLibs();

    /**
     * \brief Runs Lua <code>ae.init.run()</code>.
     * \return <code>true</code> on success, otherwise sets error and returns
     *   <code>false</code>.     
     */
    bool aeInitRun();
    
public:
    /// The log tag.
    static const char * const logTag;
    
    /**
     * \brief Constructs the Lua engine.
     * \param logger_ The logger.
     * \param cfg_ The Lua engine configuration.
     * \param engine_ The engine related to this Lua engine.
     */
    LuaEngine(Logger *logger_,LuaEngineCfg cfg_,Engine *engine_);
    
    /** */
    virtual ~LuaEngine();
    
    /** 
     * \brief Starts the Lua engine.
     * \return <code>true</code> if the Lue engine has been successfully
     *   started, otherwise sets error and returns <code>false</code>.
     */
    bool start();
    
    /**
     * \brief Stops the Lua engine.
     * \return <code>true</code> on success, otherwise sets error and returns
     *   <code>false</code>.     
     */
    bool stop();
    
    /**
     * \brief Invoked when the engine is going to pause.
     * \return <code>true</code> on success, otherwise sets error and returns
     *   <code>false</code>.
     */
    bool pausing();
    
    /**
     * \brief Invoked when the engine is going to pause.
     * \return <code>true</code> on success, otherwise sets error and returns
     *   <code>false</code>.
     */
    bool resuming();
    
    /**
     * \brief Restars the Lua engine.
     * \return <code>true</code> on success, sets error and returns
     *     <code>false</code> on error.
     */
    bool restart();
    
    /**
     * \brief Posts a requests to restart the Lua engine.
     */
    void postRestart();
    
    /**
     * \brief Loads a source from the source provider.
     * \param name The source (chunk) name.
     * \return <code>true</code> if the source has been loaded,
     *     otherwise sets error and returns <code>false</code>.
     */
    bool loadSource(const std::string &name);
    
    /**
     * \brief Invoked when the display has been resized.
     * \param width_ The display width.
     * \param height_ The display height.
     * \return <code>true</code> if the event has been successfully processed,
     *     otherwise sets error and returns <code>false</code>.
     */
    bool displayResized(int width_,int height_);
    
    /**
     * \brief Gets the Lua state.
     * \return The Lua state.
     */ 
    lua_State *getLuaState() const {
        return L;
    }    
    
    /**
     * \brief Gets the engine associated with this Lua engine.
     * \return The engine.
     */
    Engine *getEngine() const {
        return engine;
    }
    
    /**
     * \brief Adds a Lua model.
     * \param luaModel The model to add.
     */
    void addLuaModel(LuaModel *luaModel);
    
    /**
     * \brief Removes a Lua model.
     * \param luaModel The model to remove.
     */
    void removeLuaModel(LuaModel *luaModel);
    
    /**
     * \brief Sets the restart listener.
     * \param listener The restart listener.
     */
    void setLuaEngineRestartListener(LuaEngineRestartListener *listener) {
        restartListener = listener;
    }
    
    /**
     * \brief Sets the pause/resume listener.
     * \param listener The pause/resume listener.
     */
    void setLuaEnginePauseListener(LuaEnginePauseListener *listener) {
        pauseListener = listener;
    }
    
    /**
     * \brief Gets the pause/resume listener.
     * \return The pause/resume listener.
     */
    LuaEnginePauseListener *getLuaEnginePauseListener() {
        return pauseListener;
    }
    
    /**
     * \brief Sets the quit listener.
     * \param listener The quit listener.
     */
    void setLuaEngineQuitListener(LuaEngineQuitListener *listener) {
        quitListener = listener;
    }
    
    /**
     * \brief Gets the quit listener.
     * \return The quit listener.
     */
    LuaEngineQuitListener *getLuaEngineQuitListener() {
        return quitListener;
    }
    
    /**
     * \brief The name of the OS under which the engine is running.
     * \return The OS name.
     */
    const char *getOSName() {
        return cfg.getOS()->getName();
    }
    
    /**
     * \brief Adds an extra library to be loaded on start.
     * \param lib The library to loader.
     */
    void addExtraLib(LuaExtraLib *lib);
    
    /**
     * \brief Deletes the extra libraries.
     */
    void deleteExtraLibs();
    
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
     * \brief Deletes the Lua engine plugins.
     */
    void deletePlugins();
    
    /**
     * \brief Gets the memory used by Lua (in kilobytes).
     * \param usedMemory The argument in which to store the result.
     */    
    void getUsedMemory(double &usedMemory);
    
    /**
     * \brief Logs the memory used by Lua.
     * \return <code>true</code> on success, otherwise sets error and returns
     *   <code>false</code>.     
     */
    bool logUsedMemory();
};
    
} // namespace

} // namespace

#endif // AE_LUA_ENGINE_H