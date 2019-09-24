#include <sstream>

#include "Log.h"
#include "MessagePackMap.h"
#include "CommConsole.h"
#include "CommProtocol.h"
#include "desktop_engine.h"
#include "comm_msg_dispatcher.h"

using namespace std;
using namespace ae;
using namespace ae::io;
using namespace ae::util;

namespace ae {
    
namespace engine {
    
namespace desktop {

/// The input stream for the messages.
static InputStream *input = (InputStream *)0;

/// The debugger.
static CommDebugger *debugger;

/// The engine listener.
static CommDesktopEngineListener *listener;

/** Indicates if to stop the dispatcher. */
static bool stopFlag;

/** */
static void enterLoop() {
    stopFlag = false;
    while (stopFlag == false) {
    // read map
        MessagePackMap map;
        if (map.read(input) == false) {
            ostringstream err;
            err << "Failed to read message pack: " << map.getError() << endl;             
            CommConsole::print(err.str());            
            exit(-1);
        }
        if (stopFlag == true) {
            break;
        }

    // identifier
        if (map.hasInt(CommProtocol::MSG_ID_KEY) == false) {
            ostringstream err;
            err << "Received message without identifier" << endl;             
            CommConsole::print(err.str()); 
            exit(-1);
        }
        int msgId = map.getInt(CommProtocol::MSG_ID_KEY);
        
    // pause
        if (msgId == CommProtocol::MSG_ID_PAUSE) {
            pauseDesktopEngine();
            continue;
        }
        
    // resume
        if (msgId == CommProtocol::MSG_ID_RESUME) {
            resumeDesktopEngine();
            continue;
        }
        
    // stop
        if (msgId == CommProtocol::MSG_ID_STOP) {
            if (debugger != (CommDebugger *)0) {
                debugger->stop();
            }
            stopDesktopEngine();
            continue;
        }
        
    // restart
        if (msgId == CommProtocol::MSG_ID_RESTART) {
            if (debugger != (CommDebugger *)0) {
                debugger->stop();
            }            
            restartDesktopEngine();
            continue;
        }
        
    // set volume
        if (msgId == CommProtocol::MSG_ID_SET_VOLUME) {
            int volume = map.getInt(CommProtocol::VOLUME_KEY);
            setVolumeFactor(volume / (double)100,
                CommProtocol::strToBool(map,CommProtocol::NOTIFY_KEY));
        }
        
    // enter loop
        if (msgId == CommProtocol::MSG_ID_ENTER_LOOP) {
            listener->enterLoop();
        }
        
    // add breakpoint
        if (msgId == CommProtocol::MSG_ID_ADD_BREAKPOINT) {
            if (debugger == (CommDebugger *)0) {
                CommConsole::print(
                    "Attempt to add breakpoint with debugger disabled\n");
                return;   
            }
            
            string source = map.getStr(CommProtocol::SOURCE_KEY);
            int line = map.getInt(CommProtocol::LINE_KEY);            
            debugger->addBreakpoint(source,line);
        }        
        
    // add breakpoint
        if (msgId == CommProtocol::MSG_ID_REMOVE_BREAKPOINT) {
            if (debugger == (CommDebugger *)0) {
                CommConsole::print(
                    "Attempt to remove breakpoint with debugger disabled\n");
                return;   
            }
            
            string source = map.getStr(CommProtocol::SOURCE_KEY);
            int line = map.getInt(CommProtocol::LINE_KEY);            
            debugger->removeBreakpoint(source,line);
        }        
        
    // continue execution
        if (msgId == CommProtocol::MSG_ID_CONTINUE_EXECUTION) {
            debugger->continueExecution();
        }
        
    // step into
        if (msgId == CommProtocol::MSG_ID_STEP_INTO) {
            debugger->stepInto();
        }
        
    // step over
        if (msgId == CommProtocol::MSG_ID_STEP_OVER) {
            debugger->stepOver();
        }
        
    // step return
        if (msgId == CommProtocol::MSG_ID_STEP_RETURN) {
            debugger->stepReturn();
        }
        
    // get traceback
        if (msgId == CommProtocol::MSG_ID_GET_TRACEBACK) {
            debugger->writeTraceback();
        }        
        
    // finds a table in traceback
        if (msgId == CommProtocol::MSG_ID_FIND_TABLE_IN_TRACEBACK) {
            string requestId = map.getStr(CommProtocol::REQUEST_ID_KEY);
            string tablePointer = map.getStr(CommProtocol::POINTER_KEY);
            debugger->findTableInTraceback(tablePointer,requestId);
        }
        
    // clear breakpoints
        if (msgId == CommProtocol::MSG_ID_CLEAR_BREAKPOINTS) {
            debugger->clearBreakpoints();
        }
        
    // gets globals
        if (msgId == CommProtocol::MSG_ID_GET_GLOBALS) {
            debugger->getGlobals();
        }
        
    // finds a table in globals
        if (msgId == CommProtocol::MSG_ID_FIND_TABLE_IN_GLOBALS) {
            string requestId = map.getStr(CommProtocol::REQUEST_ID_KEY);
            string tablePointer = map.getStr(CommProtocol::POINTER_KEY);
            debugger->findTableInGlobals(tablePointer,requestId);
        }
        
    // loads and run Lua code
        if (msgId == CommProtocol::MSG_ID_LUA_DO_STRING) {
            string source = map.getStr(CommProtocol::SOURCE_KEY);
            debugger->doLuaString(source.c_str());
        }
    }    
}

/** */
void initCommMsgDispatcher(InputStream *input_,CommDebugger *debugger_,
    CommDesktopEngineListener *listener_) {
//
    input = input_;
    debugger = debugger_;
    listener = listener_;
}

/** */
void runCommMsgDispatcher() {
    enterLoop();
}
 
/** */
void stopCommMsgDispatcher() {
    stopFlag = true;
}

} // namespace
    
} // namespace
    
} // namespace