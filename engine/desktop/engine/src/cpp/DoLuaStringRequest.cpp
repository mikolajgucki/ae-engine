#include <sstream>

#include "LuaPCall.h"
#include "DebugLog.h"
#include "DoLuaStringRequest.h"

using namespace std;
using namespace ae::lua;
using namespace ae::engine::desktop;

/// The log tag for the debug log.
static const char *const debugLogTag = "DoLuaStringRequest";

namespace ae {
    
namespace engine {
    
/** */
bool DoLuaStringRequest::run() {
// error handler
    LuaPCall pcall;
    pcall.pushErrorHandler(L);

    int top = lua_gettop(L);
// log
    ostringstream msg;
    msg << "DoLuaString (with Lua top " << top << "):" << endl << str;
    DebugLog::trace(debugLogTag,msg.str());
    
// run
    if (pcall.doString(L,str) == false) {
        ostringstream err;
        err << "DoLuaString failed: " << pcall.getError();
        DebugLog::error(debugLogTag,err.str());
        
    // notify
        debugger->doLuaStringFailed(pcall.getError());
    }
    else {
        DebugLog::trace(debugLogTag,"DoLuaString finished");
        
    // notify
        debugger->doLuaStringFinished();
    }
    
    return true;
}
    
} // namespace

} // namespace