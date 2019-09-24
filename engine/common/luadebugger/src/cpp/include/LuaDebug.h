#ifndef AE_LUA_DEBUG_H
#define AE_LUA_DEBUG_H

#include <string>
#include <vector>

#include "lua.hpp"
#include "Error.h"
#include "LuaDebugMutex.h"
#include "LuaDebugBreakpoint.h"
#include "LuaTraceback.h"
#include "LuaValue.h"
#include "LuaValueList.h"
#include "LuaDebugListener.h"

namespace ae {
    
namespace lua {
  
/**
 * \brief The main class of Lua debugger.
 */
class LuaDebug:public Error {
    /** */
    enum {
        NO_TRIGGER = -1
    };
    
    /// The Lua state to debug.
    lua_State *L;
    
    /// The listener.
    LuaDebugListener *listener;
    
    /// The breakpoints.
    std::vector<LuaDebugBreakpoint *> breakpoints;
    
    /// Indicates if to step into.
    bool stepIntoTrigger;    
    
    /// Indicates if to step over.
    int stepOverTrigger;
    
    /// Indicates if to step return.
    int stepReturnTrigger;
    
    /// The mutex which synchronizes breakpoints.
    LuaDebugMutex *mutex;
    
    /** */
    LuaDebugBreakpoint *getBreakpoint(const std::string &source,int line);
    
    /** */
    int getStackSize();

    /** */
    void getTableKeyStr(int index,char *key);
    
    /** */
    LuaValue *convertValue();
    
    /** */
    LuaValueList *convertTable();
    
    /** */
    void addLocalValues(LuaValueList *values,lua_Debug *info);
    
    /** */
    void addUpvalues(LuaValueList *values,int funcIndex,int nups);
    
    /** */
    LuaValueList *getTracebackValues(lua_Debug *info);
        
    /** */
    LuaValueList *findTable(const std::string &tablePointer,int index,
        std::vector<long> &pointers);    
    
    /** */
    LuaValueList *findTable(const std::string &tablePointer,int index);
    
    /** */
    LuaValueList *findTable(const std::string &tablePointer,int funcIndex,
        int nups);
    
    /** */
    LuaValueList *findTable(const std::string &tablePointer,lua_Debug *info);
    
public:
    /** */
    LuaDebug(LuaDebugListener *listener_,LuaDebugMutex *mutex_):Error(),
        listener(listener_),breakpoints(),stepIntoTrigger(false),
        stepOverTrigger(NO_TRIGGER),stepReturnTrigger(NO_TRIGGER),
        mutex(mutex_) {
    }
    
    /** */
    ~LuaDebug();
        
    /**
     * Locks the breakpoints. No changes can be made from other threads.
     */
    bool lockBreakpoints();
    
    /**
     * Unlocks the breakpoints.
     */
    bool unlockBreakpoints();    
    
    /**
     * \brief Adds a breakpoint.
     * \param source The name of the source file.
     * \param line The line at which to set the breakpoint.
     */
    void addBreakpoint(const std::string &source,int line);
    
    /**
     * \brief Removes a breakpoint.
     * \param source The name of the source file.
     * \param line The line at which to set the breakpoint.
     */
    void removeBreakpoint(const std::string &source,int line);
    
    /**
     * \brief Removes all the breakpoints.
     */
    void clearBreakpoints();

    /**
     * \brief Checks if a breakpoint is set.
     * \param source The name of the source file.
     * \param line The line at which to set the breakpoint.
     * \return <code>true</code> if set, <code>false</code> otherwise.     
     */
    bool hasBreakpoint(const std::string &source,int line);
    
    /**
     * Attaches the debugger.
     *
     * @param L_ The Lua state.
     */
    void attach(lua_State *L_);
    
    /**
     * \brief Detaches the debugger.
     */
    void detach();
    
    /**
     * \brief The function called on Lua hook.
     */
    void hook();
    
    /**
     * \brief Steps (into a function).
     */
    void stepInto();
    
    /**
     * \brief Steps over a function.
     */
    void stepOver();
    
    /**
     * \brief Returns from a function.
     */
    void stepReturn();
    
    /** */
    int getBreakpointCount() const {
        return breakpoints.size();
    }
    
    /** */
    LuaDebugBreakpoint *getBreakpoint(int index) const {
        return breakpoints[index];
    }
    
    /**
     * \brief Gets the traceback. The traceback must deleted when unnecessary.
     * \return The traceback.
     */ 
    LuaTraceback *getTraceback();
    
    /**
     * \brief Finds a table in traceback.
     * \param tablePointer The table pointer.
     * \return The table values.
     */
    LuaValueList *findTableInTraceback(const std::string &tablePointer);
    
    /**
     * \brief Gets all the globals.
     * \return The global values.
     */
    LuaValueList *getGlobals();
    
    /**
     * \brief Finds a table in globals.
     * \param tablePointer The table pointer.
     * \return The table values.
     */
    LuaValueList *findTableInGlobals(const std::string &tablePointer);
};
    
} // namespace
    
} // namespace

#endif // AE_LUA_DEBUG_H