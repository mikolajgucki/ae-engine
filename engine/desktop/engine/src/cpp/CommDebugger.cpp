#include <sstream>
#include "SDL_helper.h"
#include "LuaPCall.h"
#include "LuaValue.h"
#include "LuaValueList.h"
#include "LuaTracebackItem.h"
#include "LuaTraceback.h"
#include "DoLuaStringRequest.h"
#include "CommConsole.h"
#include "CommLogger.h"
#include "CommProtocol.h"
#include "CommDebugger.h"

using namespace std;
using namespace ae::lua;
using namespace ae::util;
using namespace ae::sdl;

namespace ae {
    
namespace engine {
    
namespace desktop {
    
/** */
bool CommLuaDebugMutex::lock() {
    SDL_LockMutex(mutex);
    return true;    
}
    
/** */
bool CommLuaDebugMutex::unlock() {
    SDL_UnlockMutex(mutex);
    return true;    
}
    
/** */
void CommDebugger::create() {
// debugs
    debug = new LuaDebug(this,commLuaDebugMutex);    
    
// suspend semaphore
    suspendedSem = SDL_CreateSemaphore(0);
    if (suspendedSem == (SDL_sem *)0) {
        setError(getSDLError());
        return;
    }    
    
// Lua debug mutex
    luaDebugMutex = SDL_CreateMutex();
    if (luaDebugMutex == (SDL_mutex *)0) {
        setError(getSDLError());
        return;
    }
    
// communication debug mutex
    commLuaDebugMutex = new (nothrow) CommLuaDebugMutex(luaDebugMutex);
    if (commLuaDebugMutex == (CommLuaDebugMutex *)0) {
        setNoMemoryError();
        return;
    }
}
    
/** */
CommDebugger::~CommDebugger() {
    if (debug != (LuaDebug *)0) {
        delete debug;
    }    
}
 
/** */
void CommDebugger::start(lua_State *L_) {
    CommConsole::trace("Starting debugger\n");
    L = L_;
    stopFlag = false;
    debug->attach(L);
}

/** */
void CommDebugger::stop() {
    CommConsole::print("Stopping debugger\n");
    stopFlag = true;
    
    // It might be waiting in the suspended() method so let it continue.
    SDL_SemPost(suspendedSem);
    
    // It might be *not* waiting in the suspended() method so decrease the
    // semaphore value so that it will wait next time suspended() is called.
    SDL_SemTryWait(suspendedSem);
}

/** */
void CommDebugger::engineCreated(Engine *engine_) {
    engine = engine_;
}

/** */
void CommDebugger::addBreakpoint(const string &source,int line) {
    ostringstream msg;
    msg << "Adding breakpoint " << source << ":" << line << endl;
    CommConsole::trace(msg.str());
    
    debug->addBreakpoint(source,line);   
}

/** */
void CommDebugger::removeBreakpoint(const string &source,int line) {
    ostringstream msg;
    msg << "Removing breakpoint " << source << ":" << line << endl;
    CommConsole::trace(msg.str());
    
    debug->removeBreakpoint(source,line);   
}

/** */
void CommDebugger::clearBreakpoints() {
    CommConsole::trace("Removing all breakpoints\n");
    debug->clearBreakpoints();
}

/** */
void CommDebugger::hooked(const std::string &source,int line) {
// Commented out as it's very slow to send every line.
/*
// send message
    MessagePackMap suspendedMap;
    CommProtocol::addMsgId(suspendedMap,CommProtocol::MSG_ID_HOOKED);    
    suspendedMap.putStr(CommProtocol::SOURCE_KEY,source);
    suspendedMap.putInt(CommProtocol::LINE_KEY,line);
    mapWriter->write(suspendedMap);
*/
}

/** */
void CommDebugger::suspended(const std::string &source,int line) {
    if (stopFlag == true) {
        return;
    }      
    
    ostringstream msg;
    msg << "Suspended at " << source << ":" << line << endl;
    CommConsole::trace(msg.str());
    
// send message (suspended)
    MessagePackMap suspendedMap;
    CommProtocol::addMsgId(suspendedMap,CommProtocol::MSG_ID_SUSPENDED);    
    suspendedMap.putStr(CommProtocol::SOURCE_KEY,source);
    suspendedMap.putInt(CommProtocol::LINE_KEY,line);
    mapWriter->write(suspendedMap);
    
// wait
    SDL_SemWait(suspendedSem);
    if (stopFlag == true) {
        return;
    } 
    
// send message (continuing)
    MessagePackMap continuingMap;
    CommProtocol::addMsgId(
        continuingMap,CommProtocol::MSG_ID_CONTINUING_EXECUTION);    
    mapWriter->write(continuingMap);    
    
// action
    if (continueAction == STEP_INTO) {
        debug->stepInto();
    }
    else if (continueAction == STEP_OVER) {
        debug->stepOver();
    }
    else if (continueAction == STEP_RETURN) {
        debug->stepReturn();
    }
}

/** */
void CommDebugger::continueExecution() {
    continueAction = CONTINUE_EXECUTION;
    SDL_SemPost(suspendedSem);
}

/** */
void CommDebugger::stepInto() {
    continueAction = STEP_INTO;
    SDL_SemPost(suspendedSem);
}

/** */
void CommDebugger::stepOver() {
    continueAction = STEP_OVER;
    SDL_SemPost(suspendedSem);
}

/** */
void CommDebugger::stepReturn() {
    continueAction = STEP_RETURN;
    SDL_SemPost(suspendedSem);
}

/** */
MessagePackMap *CommDebugger::createLuaValueMap(LuaValue *value) {
    MessagePackMap *map = new (nothrow) MessagePackMap();
    if (map == (MessagePackMap *)0) {
        return (MessagePackMap *)0;
    }
    
    map->putStr(CommProtocol::NAME_KEY,value->getName());
    map->putStr(CommProtocol::VALUE_KEY,value->getValue());
    map->putStr(CommProtocol::SCOPE_KEY,
        CommProtocol::valueScopeToStr(value->getScope()));    
    map->putStr(CommProtocol::TYPE_KEY,
        CommProtocol::valueTypeToStr(value->getType()));    
    if (value->getPointer().empty() == false) {
        map->putStr(CommProtocol::POINTER_KEY,value->getPointer());
    }
    
    return map;
}

/** */
bool CommDebugger::writeToMap(LuaValueList *values,MessagePackMap *map) {
    for (int index = 0; index < values->getValueCount(); index++) {
        ostringstream valueKey;
        valueKey << "value." << index;
        
        MessagePackMap *valueMap = createLuaValueMap(values->getValue(index));
        if (valueMap == (MessagePackMap *)0) {
            CommLogger::noMemory();
            return false;            
        }                
        map->putMap(valueKey.str(),valueMap);
    }
    
    return true;
}

/** */
bool CommDebugger::writeTable(const string &tablePointer,
    const string &requestId,LuaValueList *tableValues) {
// build message
    MessagePackMap map;
    CommProtocol::addMsgId(map,CommProtocol::MSG_ID_TABLE);
    map.putStr(CommProtocol::REQUEST_ID_KEY,requestId);
    map.putStr(CommProtocol::POINTER_KEY,tablePointer);
    map.putInt(CommProtocol::COUNT_KEY,tableValues->getValueCount());
    
// for each child value
    if (writeToMap(tableValues,&map) == false) {
        return false;
    }
    
// send message
    mapWriter->write(map);
    return true;    
}

/** */
void CommDebugger::writeTraceback() {
    LuaTraceback *traceback = debug->getTraceback();
    if (traceback == (LuaTraceback *)0) {
        CommLogger::noMemory();
        return;
    }
    
// build message
    MessagePackMap map;
    CommProtocol::addMsgId(map,CommProtocol::MSG_ID_TRACEBACK);    
    map.putInt(CommProtocol::COUNT_KEY,traceback->getItemCount());
    
// for each traceback item
    for (int index = 0; index < traceback->getItemCount(); index++) {
        LuaTracebackItem *item = traceback->getItem(index);
        
    // item map
        MessagePackMap *itemMap = new (nothrow) MessagePackMap();
        if (itemMap == (MessagePackMap *)0) {
            delete traceback;
            CommLogger::noMemory();            
            return;            
        }
        
    // item map
        ostringstream key;
        key << "item." << index;
        map.putMap(key.str(),itemMap);
        
    // map values
        itemMap->putInt(CommProtocol::INDEX_KEY,item->getIndex());
        itemMap->putStr(CommProtocol::SOURCE_KEY,item->getSource());
        itemMap->putInt(CommProtocol::LINE_KEY,item->getLine());
        itemMap->putStr(CommProtocol::WHAT_KEY,
            CommProtocol::whatToStr(item->getWhat()));
        itemMap->putStr(CommProtocol::NAME_KEY,item->getName());
        
    // Lua value count
        LuaValueList *values = item->getValues();
        itemMap->putInt(CommProtocol::COUNT_KEY,values->getValueCount());

    // Lua values
        if (writeToMap(values,itemMap) == false) {
            return;
        }
    }
    
// send message
    mapWriter->write(map);
    
// clean up
    delete traceback;
}

/** */
void CommDebugger::findTableInTraceback(const string &tablePointer,
    const string &requestId) {
// find the table
    LuaValueList *tableValues = debug->findTableInTraceback(tablePointer);
    if (tableValues == (LuaValueList *)0) {
        // TODO Handle no table.
        return;
    }
    
// write
    if (writeTable(tablePointer,requestId,tableValues) == false) {
        delete tableValues;
        return;
    }
    
// clean up
    delete tableValues;
}

/** */
void CommDebugger::getGlobals() {
    LuaValueList *globals = debug->getGlobals();

// build message
    MessagePackMap map;
    CommProtocol::addMsgId(map,CommProtocol::MSG_ID_GLOBALS);
    map.putInt(CommProtocol::COUNT_KEY,globals->getValueCount());
    
// globals
    if (writeToMap(globals,&map) == false) {
        return;
    }
    
// send message
    mapWriter->write(map);    
    
// clean up
    delete globals;    
}

/** */
void CommDebugger::findTableInGlobals(const string &tablePointer,
    const string &requestId) {
// find the table
    LuaValueList *tableValues = debug->findTableInGlobals(tablePointer);
    if (tableValues == (LuaValueList *)0) {
        // TODO Handle no table.
        return;
    }
    
// write
    if (writeTable(tablePointer,requestId,tableValues) == false) {
        delete tableValues;
        return;
    }
    
// clean up
    delete tableValues;
}

/** */
void CommDebugger::doLuaString(const char *str) {
    DoLuaStringRequest *request = new (nothrow) DoLuaStringRequest(L,this,str);
    if (request == (DoLuaStringRequest *)0) {
        // TODO Handle error.
    }
    engine->addRequest(request);
}

/** */
void CommDebugger::doLuaStringFinished() {
    MessagePackMap map;
    CommProtocol::addMsgId(map,CommProtocol::MSG_ID_LUA_DO_STRING_FINISHED);    
    
// send message
    mapWriter->write(map);    
}

/** */
void CommDebugger::doLuaStringFailed(const std::string& errorMsg) {
    MessagePackMap map;
    CommProtocol::addMsgId(map,CommProtocol::MSG_ID_LUA_DO_STRING_FAILED);    
    map.putStr(CommProtocol::ERROR_MSG_KEY,errorMsg);
    
// send message
    mapWriter->write(map);    
}

} // namespace
    
} // namespace
    
} // namespace