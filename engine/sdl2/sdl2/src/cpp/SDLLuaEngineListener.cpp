#include "SDLLuaEngineListener.h"

namespace ae {
    
namespace engine {

/** */
void SDLLuaEngineListener::luaEnginePause(LuaEngine *luaEngine) {
    pauseRequest = true;
}

/** */
void SDLLuaEngineListener::luaEngineResume(LuaEngine *luaEngine) {
    resumeRequest = true;
}
    
/** */
void SDLLuaEngineListener::luaEngineQuit(LuaEngine *luaEngine) {
    quitRequest = true;
}
    
} // namespace
    
} // namespace