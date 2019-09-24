#include <sstream>

#include "Log.h"
#include "LuaEnginePluginManager.h"

using namespace std;

namespace ae {
    
namespace engine {
    
/// The log tag.
static const char *logTag = "lua.engine";    
    
/** */
void LuaEnginePluginManager::addPlugin(LuaEnginePlugin *plugin) {
    plugins.push_back(plugin);
}

/** */
LuaEnginePlugin *LuaEnginePluginManager::getPlugin(const string& name) {
    vector<LuaEnginePlugin *>::iterator itr = plugins.begin();
// for each plugin
    for (; itr != plugins.end(); ++itr) {
        LuaEnginePlugin *plugin = *itr;
        if (plugin->getName() == name) {
            return plugin;
        }
    }
    
    return (LuaEnginePlugin *)0;
}

/** */
bool LuaEnginePluginManager::initPlugins(Logger *logger,lua_State *L,
    LuaEngine *luaEngine) {
// for each plugin
    vector<LuaEnginePlugin *>::iterator itr = plugins.begin();
    for (; itr != plugins.end(); ++itr) {
        LuaEnginePlugin *plugin = *itr;
        
    // set stuff
        plugin->setLog(new DLog(logger));
        plugin->setLuaState(L);
        plugin->setLuaEngine(luaEngine);
        
    // init
        if (plugin->init() == false) {
            ostringstream err;
            err << "Failed to initialize plugin " << plugin->getName() <<
                ": " << plugin->getError();
            setError(err.str());
            return false;
        }
    }
    
    return true;
}

/** */
bool LuaEnginePluginManager::configureLuaEngine(LuaEngineCfg *cfg) {
    if (plugins.empty() == true) {
        return true;
    }
    
    vector<LuaEnginePlugin *>::iterator itr = plugins.begin();
// for each plugin
    for (; itr != plugins.end(); ++itr) {
        LuaEnginePlugin *plugin = *itr;
        if (plugin->configureLuaEngine(cfg) == false) {
            ostringstream err;
            err << "Failed to configure Lua engine in plugin " <<
                plugin->getName() << ": " << plugin->getError();
            setError(err.str());
            return false;
        }
    }
    
    return true;
}

/** */
bool LuaEnginePluginManager::startPlugins() {
    if (plugins.empty() == true) {
        Log::debug(logTag,"No Lua engine plugins to start");
        return true;
    }
    
    Log::debug(logTag,"Starting Lua engine plugins:");
    vector<LuaEnginePlugin *>::iterator itr = plugins.begin();
// for each plugin
    for (; itr != plugins.end(); ++itr) {
        LuaEnginePlugin *plugin = *itr;
        
    // log
        ostringstream msg;
        msg << "  " << plugin->getName();
        Log::debug(logTag,msg.str());
        
    // start
        if (plugin->start() == false) {
            ostringstream err;
            err << "Failed to start plugin " << plugin->getName() <<
                ": " << plugin->getError();
            setError(err.str());
            return false;
        }
    }
    
    return true;
}

/** */
bool LuaEnginePluginManager::stopPlugins() {
    if (plugins.empty() == true) {
        Log::debug(logTag,"No Lua engine plugins to stop");
        return true;
    }
    
    Log::debug(logTag,"Stopping Lua engine plugins:");
    vector<LuaEnginePlugin *>::iterator itr = plugins.begin();
// for each plugin
    for (; itr != plugins.end(); ++itr) {
        LuaEnginePlugin *plugin = *itr;
        
    // log
        ostringstream msg;
        msg << "  " << plugin->getName();
        Log::debug(logTag,msg.str());

    // stop
        if (plugin->stop() == false) {
            ostringstream err;
            err << "Failed to stop in plugin " << plugin->getName() <<
                ": " << plugin->getError();
            setError(err.str());
            return false;
        }
    }
    
    return true;
}

/** */
void LuaEnginePluginManager::deletePlugins() {
    vector<LuaEnginePlugin *>::iterator itr;
    for (itr = plugins.begin(); itr != plugins.end(); ++itr) {
        LuaEnginePlugin *plugin = *itr;
        delete plugin;
    }    
}

} // namespace
    
} // namespace