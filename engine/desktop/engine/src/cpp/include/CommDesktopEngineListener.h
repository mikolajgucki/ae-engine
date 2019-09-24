#ifndef AE_COMM_DESKTOP_ENGINE_LISTENER_H
#define AE_COMM_DESKTOP_ENGINE_LISTENER_H

#include "SDL.h"
#include "Error.h"
#include "CommMapWriter.h"
#include "CommDebugger.h"
#include "DesktopEngineListener.h"

namespace ae {
    
namespace engine {
    
namespace desktop {
  
/**
 * \brief The desktop engine listener for the communication.
 */
class CommDesktopEngineListener:public Error,public DesktopEngineListener {
    /// The map writer.
    CommMapWriter *mapWriter;   
    
    /// The debugger.
    CommDebugger *debugger;
    
    /// The enter loop semaphore.
    SDL_sem *enterLoopSem;
    
    /** */
    void create();
    
public:
    CommDesktopEngineListener(CommMapWriter *mapWriter_,
        CommDebugger *debugger_):Error(),DesktopEngineListener(),
        mapWriter(mapWriter_),debugger(debugger_),enterLoopSem((SDL_sem *)0) {
    //
        create();
    }
    
    /** */
    virtual ~CommDesktopEngineListener() {
    }
    
    /** */
    virtual void luaStateCreated(lua_State *L);
   
    /** */
    virtual void engineCreated(Engine *engine);
    
    /** */
    virtual void willEnterLoop();
    
    /** */
    void enterLoop();
    
    /** */
    virtual void frameRendered();
    
    /** */
    virtual void stopping();
    
    /** */
    virtual void paused();
    
    /** */
    virtual void resumed();

    /** */
    virtual void restarting();
    
    /** */
    virtual void volumeFactorSet(double volumeFactor);    
};
    
} // namespace
    
} // namespace
    
} // namespace

#endif // AE_COMM_DESKTOP_ENGINE_LISTENER_H