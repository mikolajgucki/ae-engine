#include <cstdlib>
#include <sstream>

#include "Log.h"
#include "string_util.h"
#include "LuaEngineCfgOS.h"
#include "cfg_props.h"
#include "DesktopEngineCfg.h"

using namespace std;
using namespace ae::util;

namespace ae {
    
namespace engine {
    
namespace desktop {

/// The log tag.
const char *const DesktopEngineCfg::logTag = "desktop.engine";

/** */
bool DesktopEngineCfg::hasProp(const string& name) {
    map<string,string>::iterator prop = props.find(name);
    return prop != props.end();
}

/** */
bool DesktopEngineCfg::getPropValue(const string& name,string& value) {
    map<string,string>::iterator prop = props.find(name);
    if (prop != props.end()) {
        string propValue = prop->second;
        replaceEnvVars(propValue,extraVars);
        value = propValue;
        return true;
    }
    
    return false;
}

/** */
bool DesktopEngineCfg::validateOSProp(const std::string &value) {
    const LuaEngineCfgOS *os = LuaEngineCfgOS::getOSByName(value.c_str());
    return os != (LuaEngineCfgOS *)0;
}

/** */
bool DesktopEngineCfg::parseWinSize(const std::string &winSize) {
    vector<string> winSizeValues = split(winSize,'x');    
    if (winSizeValues.size() != 2) {
        setError("Invalid window size");
    }
    
// width
    winWidth = atoi(winSizeValues[0].c_str());
    if (winWidth <= 0) {
        setError("Invalid window width");
    }
    
// height
    winHeight = atoi(winSizeValues[1].c_str());
    if (winHeight <= 0) {
        setError("Invalid window height");
    }
    
    return true;
}

/** */
bool DesktopEngineCfg::parseBoolean(const string &propValue,bool &result) {
    if (propValue == "yes" || propValue == "true") {
        result = true;
        return true;
    }
    if (propValue == "no" || propValue == "false") {
        result = false;
        return true;
    }
    
    return false;
}

/** */
const string DesktopEngineCfg::getPluginsDir() {
    ostringstream dir;
    dir << aeDistDir << "/plugins";
    return dir.str();    
}

/** */
void DesktopEngineCfg::addExtraVars() {
// plugins directories
    string pluginsDir = getPluginsDir();
    ostringstream pluginsDirsStr;
    
    vector<string> plugins = getPlugins();
// for each plugin
    vector<string>::iterator pluginsItr = plugins.begin();
    for (; pluginsItr != plugins.end(); ++pluginsItr) {
        ostringstream dir;
        dir << pluginsDir << "/" << *pluginsItr << "/common/src/lua";
        if (pluginsDirsStr.str().empty() == false) {
            pluginsDirsStr << ";";
        }
        pluginsDirsStr << dir.str();
    }
    extraVars["LUA_PLUGINS_DIRS"] = pluginsDirsStr.str();
}

/** */
void DesktopEngineCfg::create() {
// AE distribution directory
    const char *aeDistDirEnv = getenv("AE_DIST");
    if (aeDistDirEnv == (const char *)0) {
        setError("Environment variable AE_DIST not set");
        return;
    }
    aeDistDir.append(aeDistDirEnv);
    
// directories with assets
    if (hasProp("asset.dirs") == false) {
        setError("Asset directories not set (property asset.dirs)");
        return;
    }
        
// directories with Lua sources
    if (hasProp("lua.src.dirs") == false) {
        setError("Lua source directories not set (property lua.src.dirs)");
        return;
    }
    
// directories with textures
    if (hasProp("texture.dirs") == false) {
        setError("Texture directories not set (property texture.dirs)");
        return;
    }
    
// OS under which the app is running
    if (hasProp("os") == true) {
        getPropValue(string("os"),os);
        if (validateOSProp(os) == false) {
            ostringstream err;
            err << "Invalid OS " << os;
            setError(err.str());
            return;
        }
    }
    else {
        setError("OS not set (property os)");
        return;
    }
    
// window size
    const string winSizeName = "win.size";
    if (hasProp(winSizeName) == true) {
        string winSizeValue;
        getPropValue(string(winSizeName),winSizeValue);
        if (parseWinSize(winSizeValue) == false) {
            return;
        }
    }
    
// audio
    const string audioName = "audio";
    if (hasProp(audioName) == true) {
        string audioValue;
        getPropValue(audioName,audioValue);
        if (parseBoolean(audioValue,audio) == false) {
            ostringstream err;
            err << "Invalid " << audioName << " property value " << audioValue;
            setError(err.str());
            return;
        }
    }
    
// volume
    const string volumeName = "volume";
    if (hasProp(volumeName) == true) {
        string volumeValue;
        getPropValue(volumeName,volumeValue);
        bool valid = parseInt(volumeValue,volume);
        if (valid == false || volume < 0 || volume > 100) {
            ostringstream err;
            err << "Invalid " << volumeName << " property value " <<
                volumeValue;
            setError(err.str());
            return;            
        }
    }
    
// Lua profiler
    const string luaProfilerName = "lua.profiler";
    if (hasProp(luaProfilerName) == true) {
        string luaProfilerValue;
        getPropValue(luaProfilerName,luaProfilerValue);
        if (parseBoolean(luaProfilerValue,luaProfiler) == false) {
            ostringstream err;
            err << "Invalid " << luaProfilerName << " property value " <<
                luaProfilerValue;
            setError(err.str());
            return;
        }
    }    
    
// plugins
    ostringstream pluginsName;
    pluginsName << os << ".plugins";
    if (hasProp(pluginsName.str()) == true) {
        string pluginsValue;
        getPropValue(pluginsName.str(),pluginsValue);
        plugins = split(pluginsValue,',');
    }
    
    addExtraVars();
}

/** */
const string DesktopEngineCfg::getAppName() {
    string value;
    getPropValue(string("app.name"),value);
    return value;
}

/** */
const vector<string> DesktopEngineCfg::getAssetDirs() {
    if (hasProp("asset.dirs") == true) {
        string value;
        getPropValue(string("asset.dirs"),value);
        return split(value,';');
    }
    
    vector<string> dirs;
    return dirs;
}

/** */
const vector<string> DesktopEngineCfg::getLuaSrcDirs() {
    if (hasProp("lua.src.dirs") == true) {
        string value;
        getPropValue(string("lua.src.dirs"),value);
        return split(value,';');
    }
    
    vector<string> dirs;
    return dirs;
}

/** */
const vector<string> DesktopEngineCfg::getTextureDirs() {
    if (hasProp("texture.dirs") == true) {
        string value;
        getPropValue(string("texture.dirs"),value);
        return split(value,';');
    }
    
    vector<string> dirs;
    return dirs;
}
    

} // namespace

} // namespace

} // namespace