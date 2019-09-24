#ifndef AE_COMM_DEBUGGER_H
#define AE_COMM_DEBUGGER_H

#include <string>
#include "lua.hpp"
#include "SDL.h"
#include "Error.h"
#include "LuaValue.h"
#include "LuaDebugListener.h"
#include "LuaDebug.h"
#include "LuaDebugMutex.h"
#include "CommMapWriter.h"
#include "Engine.h"

namespace ae {
    
namespace engine {
    
namespace desktop {
    
/** */
class CommLuaDebugMutex:public ::ae::lua::LuaDebugMutex {
    /** */
    SDL_mutex *mutex;
    
public:
    /** */
    CommLuaDebugMutex(SDL_mutex *mutex_):LuaDebugMutex(),mutex(mutex_) {
    }
    
    /** */
    virtual bool lock();
    
    /** */
    virtual bool unlock();
};
 
/** 
 * \brief The debugger for the over-standard-streams communication.
 */
class CommDebugger:public Error,public ::ae::lua::LuaDebugListener {
    /** */
    enum ContinueAction {
        /** */
        CONTINUE_EXECUTION,
        
        /** */
        STEP_INTO,
        
        /** */
        STEP_OVER,
        
        /** */
        STEP_RETURN
    };
    
    /// The Lua state.
    lua_State *L;
    
    /// The engine.
    Engine *engine;
    
    /// The message pack map writer.
    CommMapWriter *mapWriter;
    
    /// Indicates if to stop.
    bool stopFlag;
    
    /// The debug.
    ::ae::lua::LuaDebug *debug;    
    
    /// The suspended semaphore.
    SDL_sem *suspendedSem;
    
    /// The Lua debug mutex.
    SDL_mutex *luaDebugMutex;
    
    /// The Lua debug mutex class.
    CommLuaDebugMutex *commLuaDebugMutex;
    
    /// The action taken after execution suspension.
    ContinueAction continueAction;
    
    /** */
    void create();
    
    /** */
    ::ae::util::MessagePackMap *createLuaValueMap(::ae::lua::LuaValue *value);
    
    /** */
    bool writeToMap(::ae::lua::LuaValueList *values,
        ::ae::util::MessagePackMap *map);
    
    /** */
    bool writeTable(const std::string &tablePointer,
        const std::string &requestId,::ae::lua::LuaValueList *tableValues);
    
public:
    /** */
    CommDebugger(CommMapWriter *mapWriter_):LuaDebugListener(),
        mapWriter(mapWriter_),stopFlag(false),debug((::ae::lua::LuaDebug *)0),
        suspendedSem((SDL_sem *)0),luaDebugMutex((SDL_mutex *)0),
        commLuaDebugMutex((CommLuaDebugMutex *)0),
        continueAction(CONTINUE_EXECUTION) {
    //
        create();
    }
    
    /** */
    virtual ~CommDebugger();
    
    /** */
    void start(lua_State *L_);
    
    /** */
    void stop();
    
    /** */
    void engineCreated(Engine *engine_);
    
    /** */
    void addBreakpoint(const std::string &source,int line);       
    
    /** */
    void removeBreakpoint(const std::string &source,int line);  
    
    /** */
    void clearBreakpoints();
    
    /** */
    virtual void hooked(const std::string &source,int line);
    
    /** */
    virtual void suspended(const std::string &source,int line);
    
    /** */
    void continueExecution();    
    
    /** */
    void stepInto();
    
    /** */
    void stepOver();
    
    /** */
    void stepReturn(); 
    
    /** */
    void writeTraceback();
    
    /** */
    void findTableInTraceback(const std::string &tablePointer,
        const std::string &requestId);
    
    /** */
    void getGlobals();
    
    /** */
    void findTableInGlobals(const std::string &tablePointer,
        const std::string &requestId);
    
    /** */
    void doLuaString(const char *str);
    
    /** */
    void doLuaStringFinished();
    
    /** */
    void doLuaStringFailed(const std::string& errorMsg);
};
    
} // namespace
    
} // namespace
    
} // namespace

#endif // AE_COMM_DEBUGGER_H