#include <sstream>

#include "Log.h"
#include "System.h"
#include "io_util.h"
#include "desktop_plugin_lib.h"
#include "DesktopPlugin.h"
#include "DesktopPluginManager.h"

using namespace std;
using namespace ae;
using namespace ae::system;
using namespace ae::io;

namespace ae {

namespace engine {

namespace desktop {

/// The log tag.
static const char *const logTag = "desktop.engine"; 
    
/** */
bool DesktopPluginManager::deletePlugin(DynLib *dynLib,DesktopPlugin *plugin) {
// get the function
    void *deleteDesktopPluginFuncAddr =
        dynLib->getAddress(deleteDesktopPluginFuncName);
    if (deleteDesktopPluginFuncAddr == (void *)0) {
        setError(dynLib->getError());
        return false;
    }    
    
// call the function
    deleteDesktopPluginFunc deleteDesktopPlugin = 
        (deleteDesktopPluginFunc)deleteDesktopPluginFuncAddr;    
    deleteDesktopPlugin((void *)plugin);
        
    dynLib->close();
    delete dynLib;    
    
    return true;
}

/** */
void DesktopPluginManager::destroy() {
    vector<DesktopPluginHandle *>::iterator itr;
    for (itr = handles.begin(); itr != handles.end(); ++itr) {
        DesktopPluginHandle *handle = *itr;
        DesktopPlugin *plugin = handle->getPlugin();
        
    // delete log
        if (plugin->getLog() != (DLog *)0) {
            delete plugin->getLog();
        }
        
    // delete plugin
        deletePlugin(handle->getDynLib(),handle->getPlugin());
        
    // delete handle
        delete handle;
    }
}
 
/** */
const string DesktopPluginManager::getPathToPluginLib(
    const string& pluginName) {
// for each directory with plugins
    vector<string>::iterator itr;
    for (itr = pluginDirs.begin(); itr != pluginDirs.end(); ++itr) {
        ostringstream path;
        path << *itr << "/" << pluginName << "/desktop";
        if (System::getOS() == System::OS_WIN32) {
            path << "/windows/ae_" << pluginName << ".dll";
        }
        if (System::getOS() == System::OS_MACOSX) {
            path << "/osx/libae_" << pluginName << ".dylib";
        }
        
    // check if file exists
        if (checkFileExists(path.str()) == true) {
            return path.str();
        }
    }
    
    return string("");
}

/** */
void DesktopPluginManager::pluginStartFailed(DynLib *dynLib,
    DesktopPlugin *plugin) {
//
    setError(plugin->getError());
    deletePlugin(dynLib,plugin);
}

/** */
bool DesktopPluginManager::initPlugin(const string& pluginName) {
// path to the library
    const string path = getPathToPluginLib(pluginName);
    if (path.empty() == true) {
        ostringstream warn;
        warn << "Skipping loading plugin " << pluginName << ": library "
            "file not found";
        Log::warning(logTag,warn.str());
        return true;
    }
    
// load the library
    DynLib *dynLib = new DynLib(path);
    if (dynLib->open() == false) {
        setError(dynLib->getError());
        delete dynLib;
        return false;
    }
    
// get the function
    void *createDesktopPluginFuncAddr =
        dynLib->getAddress(createDesktopPluginFuncName);
    if (createDesktopPluginFuncAddr == (void *)0) {
        setError(dynLib->getError());
        delete dynLib;
        return false;
    }
    
// create the plugin
    createDesktopPluginFunc createDesktopPlugin = 
        (createDesktopPluginFunc)createDesktopPluginFuncAddr;
    void *pluginAddr = createDesktopPlugin();
    DesktopPlugin *plugin = (DesktopPlugin *)pluginAddr;

// plugin configuration
    DesktopPluginCfg *cfg = new DesktopPluginCfg(plugin->getName());
    plugin->setLog(new DLog(logger));
    plugin->setCfg(cfg);
    plugin->setTimer(timer);
    plugin->setLuaEngine(luaEngine);
    
// initialize the plugin
    if (plugin->init() == false) {
        pluginStartFailed(dynLib,plugin);
        return false;
    }
    
// add Lua extra libraries
    if (plugin->addLuaExtraLibs() == false) {
        pluginStartFailed(dynLib,plugin);
        return false;        
    }
    
// add Lua engine plugins
    if (plugin->addLuaEnginePlugins() == false) {
        pluginStartFailed(dynLib,plugin);
        return false;        
    }
    
// create handle
    DesktopPluginHandle *handle = new DesktopPluginHandle(dynLib,plugin);
    handles.push_back(handle);
    
    return true;
}
    
} // namespace

} // namespace

} // namespace