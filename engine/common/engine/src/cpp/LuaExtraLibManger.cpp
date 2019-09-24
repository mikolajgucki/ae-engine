#include <vector>

#include "Log.h"
#include "DLog.h"
#include "LuaExtraLibManager.h"

using namespace std;
using namespace ae;

namespace ae {
    
namespace engine {

/// The log tag.
static const char *logTag = "lua.engine";

/** */
void LuaExtraLibManager::addLib(LuaExtraLib *lib) {
    libs.push_back(lib);
}

/** */
bool LuaExtraLibManager::loadLibs(LuaEngine *luaEngine) {
    Log::trace(logTag,"Loading extra libraries:");
    
    vector<LuaExtraLib *>::iterator itr;
// for each library
    for (itr = libs.begin(); itr != libs.end(); ++itr) {
        LuaExtraLib *lib = *itr;
        
    // log
        DLog *log = new DLog(Log::getLogger());
        lib->setLog(log);
            
    // log
        ostringstream msg;
        msg << "  " << lib->getName();
        Log::trace(logTag,msg.str());
        
    // load the library
        if (lib->loadExtraLib(luaEngine) == false) {
            setError(lib->getError());
            return false;
        }
    }    
    
    return true;
}

/** */
bool LuaExtraLibManager::unloadLibs() {
    Log::trace(logTag,"Unloading extra libraries:");
    
    vector<LuaExtraLib *>::iterator itr;
// for each library
    for (itr = libs.begin(); itr != libs.end(); ++itr) {
        LuaExtraLib *lib = *itr;
            
    // log
        ostringstream msg;
        msg << "  " << lib->getName();
        Log::trace(logTag,msg.str());    
        
    // unload the library
        if (lib->unloadExtraLib() == false) {
            setError(lib->getError());
            return false;
        }
    }    
    
    return true;        
}

/** */
void LuaExtraLibManager::deleteLibs() {
    vector<LuaExtraLib *>::iterator itr;
    for (itr = libs.begin(); itr != libs.end(); ++itr) {
        LuaExtraLib *lib = *itr;
        delete lib;
    }    
}

} // namespace
    
} // namespace
