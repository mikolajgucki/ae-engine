#include "SDL_helper.h"
#include "MessagePackMap.h"
#include "CommConsole.h"
#include "CommProtocol.h"
#include "comm_msg_dispatcher.h"
#include "CommDesktopEngineListener.h"

using namespace ae::util;
using namespace ae::sdl;

namespace ae {
    
namespace engine {
    
namespace desktop {

/** */
void CommDesktopEngineListener::create() {
    enterLoopSem = SDL_CreateSemaphore(0);
    if (enterLoopSem == (SDL_sem *)0) {
        setError(getSDLError());
        return;
    }
}
    
/** */
void CommDesktopEngineListener::luaStateCreated(lua_State *L) {
    if (debugger != (CommDebugger *)0) {
        debugger->start(L);
    }
}
 
/** */
void CommDesktopEngineListener::engineCreated(Engine *engine) {
    if (debugger != (CommDebugger *)0) {
        debugger->engineCreated(engine);
    }
}

/** */
void CommDesktopEngineListener::willEnterLoop() {
    CommConsole::print("About to enter loop\n");
    
// send message
    MessagePackMap map;
    CommProtocol::addMsgId(map,
        CommProtocol::MSG_ID_PAUSED_BEFORE_ENTERING_LOOP);    
    mapWriter->write(map);
    
// wait
    SDL_SemWait(enterLoopSem);
}

/** */
void CommDesktopEngineListener::enterLoop() {
    CommConsole::print("Entering loop\n");
    SDL_SemPost(enterLoopSem);
}

/** */
void CommDesktopEngineListener::frameRendered() {
// send message
    MessagePackMap map;
    CommProtocol::addMsgId(map,CommProtocol::MSG_ID_FRAME_RENDERED);    
    mapWriter->write(map);    
}

/** */
void CommDesktopEngineListener::stopping() {
// stop dispatcher
    stopCommMsgDispatcher();
    
// send message
    MessagePackMap map;
    CommProtocol::addMsgId(map,CommProtocol::MSG_ID_STOPPING);    
    mapWriter->write(map);
}

/** */
void CommDesktopEngineListener::paused() {
// send message
    MessagePackMap map;
    CommProtocol::addMsgId(map,CommProtocol::MSG_ID_PAUSED);    
    mapWriter->write(map);
}
 
/** */
void CommDesktopEngineListener::resumed() {
// send message
    MessagePackMap map;
    CommProtocol::addMsgId(map,CommProtocol::MSG_ID_RESUMED);
    mapWriter->write(map);
}

/** */
void CommDesktopEngineListener::restarting() {
    willEnterLoop();
}

/** */
void CommDesktopEngineListener::volumeFactorSet(double volumeFactor) {
// send message
    MessagePackMap map;
    CommProtocol::addMsgId(map,CommProtocol::MSG_ID_VOLUME_SET);
    map.putInt(CommProtocol::VOLUME_KEY,(int)(volumeFactor * 100));
    mapWriter->write(map);
}

} // namespace
    
} // namespace
    
} // namespace