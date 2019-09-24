#include <sstream>

#include "Log.h"
#include "System.h"
#include "LuaEngine.h"
#include "CommConsole.h"
#include "DesktopLuaEngineListener.h"

using namespace std;
using namespace ae::system;
using namespace ae::image;

/// The log tag.
static const char *logTag = "desktop.engine";

namespace ae {

namespace engine {
    
namespace desktop {

/** */
void DesktopLuaEngineListener::luaEngineRestarting(LuaEngine *luaEngine) {
    vector<CachedPNGImageLoader *>::iterator itr;
    for (itr = imageLoaders.begin(); itr != imageLoaders.end(); ++itr) {
        CachedPNGImageLoader *imageLoader = *itr;
        if (imageLoader->update() == false) {
            Log::error(logTag,imageLoader->getError());
        }
    }
    
    restartStartTime = sys.getSystemTime();
}

/** */
void DesktopLuaEngineListener::luaEngineRestarted(LuaEngine *luaEngine) {
    luaTouchHandler->setLuaState(luaEngine->getLuaState());
    luaKeyHandler->setLuaState(luaEngine->getLuaState());
    if (desktopEngineListener != (DesktopEngineListener *)0) {
        CommConsole::print("desktopEngineListener->luaStateCreated()\n");
        desktopEngineListener->luaStateCreated(luaEngine->getLuaState());
    }
    
    unsigned long restartTime = sys.getSystemTime() - restartStartTime;
    ostringstream msg;
    msg << "Lua engine restart took " << restartTime << "ms";
    Log::info(logTag,msg.str());
}

/** */
void DesktopLuaEngineListener::luaEngineRestartFailed(LuaEngine *luaEngine) {
// TODO Implement the method.
}

/** */
void DesktopLuaEngineListener::luaEnginePause(LuaEngine *luaEngine) {
    pauseRequest = true;
}   
    
/** */
void DesktopLuaEngineListener::luaEngineResume(LuaEngine *luaEngine) {
    resumeRequest = true;
}

/** */
void DesktopLuaEngineListener::luaEngineQuit(LuaEngine *luaEngine) {
    quitRequest = true;
}

} // namespace
    
} // namespace
    
} // namespace