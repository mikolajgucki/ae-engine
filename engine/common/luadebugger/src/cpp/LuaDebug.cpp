#include <cstdio>
#include <cstring>
#include <sstream>
#include <algorithm>

#include "LuaPCall.h"
#include "LuaDebug.h"

using namespace std;

namespace ae {
    
namespace lua {
 
/// The name of the Lua global with the debug object.
static const char *debugGlobalDebug = "ae_debug";

/** */
static void stripSource(string& source) {
    if (source[0] == '@') {
        source.erase(0,1);
    }
}

/**
 * \brief Gets information about a function in the call stack and pushes the
 *   function onto the stack.
 * \param L The Lua state.
 * \param info The structure to fill with information.
 * \param index The index of the function in the call stack.
 */
static bool getDebugInfo(lua_State *L,lua_Debug *info,int index) {
    info->source = (const char *)0;
    info->currentline = -1;
    info->what = (const char *)0;
    
    if (lua_getstack(L,index,info) == 0) {
        return false;
    }
    
    if (lua_getinfo(L,"Slnuf",info) == 0) {
        // TODO Handle lua_getinfo() failed.
    }
    return true;
}

/** */
static bool getTopDebugInfo(lua_State *L,lua_Debug *info) {
    return getDebugInfo(L,info,0);
}

/** */
LuaDebug::~LuaDebug() {
    vector<LuaDebugBreakpoint *>::iterator itr;
    for (itr = breakpoints.begin(); itr != breakpoints.end(); ++itr) {
        LuaDebugBreakpoint *breakpoint = *itr;
        delete breakpoint;
    }
}

/** */
static LuaDebug *getLuaDebug(lua_State *L) {
    lua_getglobal(L,debugGlobalDebug);
    LuaDebug *debug = (LuaDebug *)lua_touserdata(L,-1);
    lua_pop(L,1);
    return debug;
}

/** */
bool LuaDebug::lockBreakpoints() {
    if (mutex != (LuaDebugMutex *)0) {
        return mutex->lock();
    }
    return true;
}

/** */
bool LuaDebug::unlockBreakpoints() {
    if (mutex != (LuaDebugMutex *)0) {
        return mutex->unlock();
    }
    return true;
}

/** */
LuaDebugBreakpoint *LuaDebug::getBreakpoint(const string &source,int line) {
    for (unsigned int index = 0; index < breakpoints.size(); index++) {
        LuaDebugBreakpoint *breakpoint = breakpoints[index];
        
        if (breakpoint->getSource() == source &&
            breakpoint->getLine() == line) {
        //
            return breakpoint;
        }        
    }
    
    return (LuaDebugBreakpoint *)0;
}

/** */
void LuaDebug::addBreakpoint(const std::string &source,int line) {
    lockBreakpoints();
    if (getBreakpoint(source,line) == (LuaDebugBreakpoint *)0) {
        breakpoints.push_back(new LuaDebugBreakpoint(source,line));
    }
    unlockBreakpoints();
}

/** */
void LuaDebug::removeBreakpoint(const std::string &source,int line) {
    lockBreakpoints();
    vector<LuaDebugBreakpoint *>::iterator itr;
    for (itr = breakpoints.begin(); itr != breakpoints.end(); ++itr) {
        LuaDebugBreakpoint *breakpoint = *itr;
        if (breakpoint->getSource() == source &&
            breakpoint->getLine() == line) {
        //
            delete breakpoint;
            breakpoints.erase(itr);
            return;
        }
    }    
    unlockBreakpoints();    
}

/** */
void LuaDebug::clearBreakpoints() {
    breakpoints.clear();
}

/** */
bool LuaDebug::hasBreakpoint(const std::string &source,int line) {
    return getBreakpoint(source,line) != (LuaDebugBreakpoint *)0;
}

/** */
void LuaDebug::hook() {
    lua_Debug top;
    if (getTopDebugInfo(L,&top) == false) {
        return;
    }
    
// pop the function
    lua_pop(L,1);
    
    string source(top.source);
    stripSource(source);
    
    if (listener != (LuaDebugListener *)0) {
        listener->hooked(source,top.currentline);
    }
    
// step into?
    if (stepIntoTrigger == true) {
        stepIntoTrigger = false;
        
        if (listener != (LuaDebugListener *)0) {
            listener->suspended(source,top.currentline);
        }               
        return;
    }
    
    int stackSize = getStackSize();
// step over?
    if (stackSize <= stepOverTrigger) {
        stepOverTrigger = NO_TRIGGER;
        
        if (listener != (LuaDebugListener *)0) {
            listener->suspended(source,top.currentline);
        }                
        return;        
    }
    
// step return?
    if (stackSize < stepReturnTrigger) {
        stepReturnTrigger = NO_TRIGGER;
        
        if (listener != (LuaDebugListener *)0) {
            listener->suspended(source,top.currentline);
        }
        return;        
    }
    
// suspended at a breakpoint?
    lockBreakpoints();
    LuaDebugBreakpoint *breakpoint = getBreakpoint(source,top.currentline);
    if (breakpoint == (LuaDebugBreakpoint *)0) {
        unlockBreakpoints();
        return;
    }
    string breakpointSource(breakpoint->getSource());
    int breakpointLine = breakpoint->getLine();
    unlockBreakpoints();
    
    if (listener != (LuaDebugListener *)0) {
        listener->suspended(breakpointSource,breakpointLine);
    }
}

/** */
static void hookCallback(lua_State *L,lua_Debug *ar) {
    LuaDebug *debug = getLuaDebug(L);  
    debug->hook();    
}

/** */
void LuaDebug::attach(lua_State *L_) {
    L = L_;
    stepIntoTrigger = false;
    stepOverTrigger = NO_TRIGGER;
    stepReturnTrigger = NO_TRIGGER;
    
// Lua global with the debug object
    lua_pushlightuserdata(L,this);
    lua_setglobal(L,debugGlobalDebug);
    
// hook
    lua_sethook(L,hookCallback,LUA_MASKLINE,0);
}

/** */
void LuaDebug::detach() {
    lua_sethook(L,hookCallback,0,0);
}

/** */
void LuaDebug::stepInto() {
    stepIntoTrigger = true;
}

/** */
void LuaDebug::stepOver() {
    stepOverTrigger = getStackSize();
}

/** */
void LuaDebug::stepReturn() {
    stepReturnTrigger = getStackSize();
}

/** */
int LuaDebug::getStackSize() {
    int index = 0;
    
    while (true) {
        lua_Debug info;
        if (getDebugInfo(L,&info,index) == false) {
            break;
        }
        
    // pop the function
        lua_pop(L,1);
        
        index++;        
    }
    
    return index;
}

/** */
static void getUserDataStr(lua_State *L,char *str) {
// __tostring called on the userdata
    const char *toStr = luaL_tolstring(L,-1,(size_t *)0);
    sprintf(str,"%s",toStr);
    
// pop the __tostring result
    lua_pop(L,1); 
}

/** */
static int getStackValue(lua_State *L,char *str) {
    const int index = -1;
    int type = lua_type(L,index);
    
    if (type == LUA_TNIL) {
        sprintf(str,"(nil)");
        return 1;
    }
    if (type == LUA_TNUMBER) {
        sprintf(str,"%f",lua_tonumber(L,index));
        return 1;
    }
    if (type == LUA_TBOOLEAN) {
        if (lua_toboolean(L,index) == 0) {
            sprintf(str,"false");
        }
        else {
            sprintf(str,"true");
        }
        return 1;
    }
    if (type == LUA_TSTRING) {       
        sprintf(str,"%s",lua_tostring(L,index));
        return 1;
    }
    if (type == LUA_TTABLE) {
        sprintf(str,"table: %08lX",(long)lua_topointer(L,index));
        return 1;
    }
    if (type == LUA_TFUNCTION) {
        sprintf(str,"function: %08lX",(long)lua_topointer(L,index));
        return 1;
    }
    if (type == LUA_TUSERDATA) {
        getUserDataStr(L,str);
        return 1;
    }
    
    return 0;
}

/** */
void LuaDebug::getTableKeyStr(int index,char *key) {
    int type = lua_type(L,index);
    
    if (type == LUA_TNUMBER) {
        // TODO Check floating-point numbers.
        sprintf(key,"[%d]",(int)lua_tonumber(L,index));
        return;
    }
    
    if (type == LUA_TSTRING) {
        sprintf(key,"%s",lua_tostring(L,index));
        return;
    }
    
    sprintf(key,"?");
}

/** */
LuaValue *LuaDebug::convertValue() {
    int index = -1;
    int type = lua_type(L,index);
    
// other values
    char valueStr[1024];
    getStackValue(L,valueStr);
    LuaValue *value = new (nothrow) LuaValue(type,valueStr);
    if (value == (LuaValue *)0) {
        setNoMemoryError();
        return (LuaValue *)0;
    }

// pointer to table, userdata, function
    if (type == LUA_TTABLE || type == LUA_TUSERDATA || type == LUA_TFUNCTION) {
        char ptr[16];
        sprintf(ptr,"%08lX",(long)lua_topointer(L,index));
        value->setPointer(ptr);
    }
    
    return value;    
}

/** */
LuaValueList *LuaDebug::convertTable() {
    int index = lua_gettop(L);
    
    LuaValueList *values = new (nothrow) LuaValueList();
    if (values == (LuaValueList *)0) {
        setNoMemoryError();
        return (LuaValueList *)0;
    }
    char key[1024];
    
// itarate over the table
    lua_pushnil(L);
    while (lua_next(L,index) != 0) {
    // key at index -2
        getTableKeyStr(-2,key);
        
    // value at index -1
        LuaValue *value = convertValue();
        value->setName(string(key));
        values->addValue(value);
        
    // pop the value, keep the key for the next iteration 
        lua_pop(L,1);        
    }
    
    return values;
}

/** */
void LuaDebug::addLocalValues(LuaValueList *values,lua_Debug *info) {    
// for each local variable
    int index = 1;
    while (true) {
        const char *name = lua_getlocal(L,info,index);
        if (name == (const char *)0) {
            break;
        }
        index++;
        
    // don't consider internal values
        if (name[0] == '(') {
            lua_pop(L,1);
            continue;
        }  
        
    // create value
        LuaValue *value = convertValue();
        value->setName(name);
        value->setScope(LuaValue::LOCAL);
        
     // add
        values->addValue(value);        
        lua_pop(L,1);
    }
}

/** */
void LuaDebug::addUpvalues(LuaValueList *values,int funcIndex,int nups) {
    for (int index = 0; index < nups; index++) {
        const char *name = lua_getupvalue(L,funcIndex,index);        
        if (name == (const char *)0) {
            continue;
        }        
        
    // ignore the environment table        
        if (strcmp(name,"_ENV") == 0) {
            lua_pop(L,1);
            continue;
        }
        
    // don't consider internal values
        if (name[0] == '(') {
            lua_pop(L,1);
            continue;
        }     
        
    // create value
        LuaValue *value = convertValue();
        value->setName(name);
        value->setScope(LuaValue::UPVALUE);
        
     // add
        values->addValue(value);        
        lua_pop(L,1);       
    }
}

/** */
LuaValueList* LuaDebug::getTracebackValues(lua_Debug *info) {
    LuaValueList *values = new (nothrow) LuaValueList();
    if (values == (LuaValueList *)0) {
        setNoMemoryError();
        return 0;
    }
    
// get the values
    addLocalValues(values,info);
    addUpvalues(values,-1,info->nups);
    
    return values;
}

/** */
LuaTraceback *LuaDebug::getTraceback() {
    LuaTraceback *traceback = new (nothrow) LuaTraceback();
    if (traceback == (LuaTraceback *)0) {
        setNoMemoryError();
        return (LuaTraceback *)0;
    }
    
    int index = 0;
    while (true) {
        lua_Debug info;
        if (getDebugInfo(L,&info,index) == false) {
            break;
        }
        
    // source
        string source(info.source);
        stripSource(source);
                
    // what
        LuaTracebackItem::What what = LuaTracebackItem::UNKNOWN;
        if (info.what != (const char *)0) {
            string whatStr(info.what);        
            if (whatStr == "Lua") {
                what = LuaTracebackItem::LUA_FUNCTION;
            }
            if (whatStr == "C") {
                what = LuaTracebackItem::C_FUNCTION;
            }
            if (whatStr == "main") {
                what = LuaTracebackItem::MAIN_CHUNK;
            }
            if (whatStr == "tail") {
                what = LuaTracebackItem::TAIL_FUNCTION;
            }
        }
        
    // name
        string name;
        if (info.name != (const char *)0) {
            name.append(info.name);
        }
        
    // value
        LuaValueList *values = getTracebackValues(&info);
        
    // traceback item
        LuaTracebackItem *item = new (nothrow) LuaTracebackItem(
            index,source,info.currentline,what,name,values);
        if (item == (LuaTracebackItem *)0) {
            lua_pop(L,1); // pop the function
            delete traceback;
            setNoMemoryError();
            return (LuaTraceback *)0;
        }
        traceback->addItem(item);
        
    // pop the function
        lua_pop(L,1);        
        index++;        
    }
    
    return traceback;
}

/** */
LuaValueList *LuaDebug::findTable(const string &tablePointer,int index,
    vector<long> &pointers) {
// type
    int type = lua_type(L,index);
    
// not a table
    if (type != LUA_TTABLE) {            
        return (LuaValueList *)0;
    }
        
// check if already visited
    long pointer = (long)lua_topointer(L,index);
    if (find(pointers.begin(),pointers.end(),pointer) != pointers.end()) {
        return (LuaValueList *)0;
    }
    pointers.push_back(pointer);
    
// check pointer    
    char pointerStr[16];
    sprintf(pointerStr,"%08lX",pointer);
    if (tablePointer == pointerStr) {
        return convertTable();
    }
    
    int tableIndex = lua_gettop(L);
    LuaValueList *values = (LuaValueList *)0;    
// itarate over the table
    lua_pushnil(L); 
    while (lua_next(L,tableIndex) != 0) {
    // if haven't found the table yet
        if (values == (LuaValueList *)0) {
        // value at index -1
            int childType = lua_type(L,-1);
            if (childType == LUA_TTABLE) {
                values = findTable(tablePointer,-1,pointers);
            }
        }
        
    // pop the value, keep the key for the next iteration 
        lua_pop(L,1); 
    }
    
    return values;
}

/** */
LuaValueList *LuaDebug::findTable(const string &tablePointer,int index) {
    vector<long> pointers;    
    return findTable(tablePointer,index,pointers);
}

/** */
LuaValueList *LuaDebug::findTable(const string &tablePointer,int funcIndex,
    int nups) {
//
    for (int index = 0; index < nups; index++) {
        const char *name = lua_getupvalue(L,funcIndex,index);        
        if (name == (const char *)0) {
            continue;
        }        
        
    // values
        LuaValueList *values = findTable(tablePointer,-1);
        lua_pop(L,1);
        if (values != (LuaValueList *)0) {
            return values;
        }        
    }
    
    return (LuaValueList *)0;
}

/** */
LuaValueList *LuaDebug::findTable(const string& tablePointer,lua_Debug *info) {
// for each local variable
    int index = 1;
    while (true) {
        const char *name = lua_getlocal(L,info,index);
        if (name == (const char *)0) {
            break;
        }
        index++;
        
    // don't consider internal values
        if (name[0] == '(') {
            lua_pop(L,1);
            continue;    
        }  
        
    // values
        LuaValueList *values = findTable(tablePointer,-1);
        lua_pop(L,1);
        if (values != (LuaValueList *)0) {
            return values;
        }
    }
    
    return (LuaValueList *)0;
}

/** */
LuaValueList *LuaDebug::findTableInTraceback(const string &tablePointer) {
    int index = 0;
// for each traceback item
    while (true) {
        lua_Debug info;
        if (getDebugInfo(L,&info,index) == false) {
            break;
        }
                
    // try to find in local values
        LuaValueList *locals = findTable(tablePointer,&info);
        if (locals != (LuaValueList *)0) {
            lua_pop(L,1); // pop the function           
            return locals;
        }
        
    // try to find in upvalues
        LuaValueList *upvalues = findTable(tablePointer,-1,info.nups);
        if (upvalues != (LuaValueList *)0) {
            lua_pop(L,1); // pop the function           
            return upvalues;
        }
        
    // pop the function
        lua_pop(L,1);        
        index++;         
    }
    
    return (LuaValueList *)0;
}

/** */
LuaValueList *LuaDebug::getGlobals() {
// push the _G table onto the stack
    lua_getglobal(L,"_G");

// convert the table
    LuaValueList *globals = convertTable();
    if (globals != (LuaValueList *)0) {
        for (int index = 0; index < globals->getValueCount(); index++) {
            globals->getValue(index)->setScope(LuaValue::GLOBAL);
        }
    }
    
// pop the table with the globals
    lua_pop(L,1);
    
    return globals;
}
    
/** */
LuaValueList *LuaDebug::findTableInGlobals(const string &tablePointer) {
// push the _G table onto the stack
    lua_getglobal(L,"_G");

// find the table
    LuaValueList *values = findTable(tablePointer,-1);    
    
// pop the table with the globals
    lua_pop(L,1);
    
    return values;
}

} // namespace
    
} // namespace