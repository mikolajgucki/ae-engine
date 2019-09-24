#include <sstream>

#include "Log.h"
#include "LuaPCall.h"

using namespace std;
using namespace ae;

namespace ae {
    
namespace lua {

/** */
bool LuaPCall::call(lua_State *L,int nargs,int nresults,int &result,
    int errorFunc) {
// call
    result = lua_pcall(L,nargs,nresults,errorFunc);
    if (result != 0) {
        const char *msg = lua_tostring(L,-1);
        
        if (msg != (const char *)0) {
            setError(msg);
        }
        else {
            if (result == LUA_ERRERR) {
                setError("Error running error handler (LUA_ERRERR)");
            }
            else if (result == LUA_ERRMEM) {
                setError("Memory allocation error (LUA_ERRMEM)");
            }
            else if (result == LUA_ERRRUN) {
                setError("Runtime error (LUA_ERRRUN)");
            }    
            else {
                setError("Unknown error");
            }
        }
        
        // pop the error object
        lua_pop(L,1);
        
        return false;
    }
    
    return true;
}
    
/** */
static int errorHandler(lua_State *L) {
    const char *errorMsg = lua_tostring(L,-1);    
    luaL_traceback(L,L,errorMsg,1); // push traceback on the stack
    lua_remove(L,-2); // remove the error message from the stack
    return 1;
}
    
/** */
void LuaPCall::removeErrorHandler(lua_State *L,int nresults) {
    if (errorHandling == false) {
        return;
    }
    
// the error handler which is just below the results
    lua_remove(L,lua_gettop(L) - nresults);
    errorHandling = false;
}

/** */
void LuaPCall::pushErrorHandler(lua_State *L) {
    lua_pushcfunction(L,errorHandler);
    errorHandling = true;
}
    
/** */
bool LuaPCall::tryCall(lua_State *L,int nargs,int nresults,int &result) {
    int errorFunc = 0;
    if (errorHandling == true) {
        errorFunc = lua_gettop(L) - nargs - 1;
    }
    
    if (call(L,nargs,nresults,result,errorFunc) == false) {
        removeErrorHandler(L,nresults); // TODO nresults = 0 on error? 
        return false;
    }
    
    removeErrorHandler(L,nresults);
    return true;
}

/** */
bool LuaPCall::doString(lua_State *L,const string &str) {
// load the string
    luaL_loadstring(L,str.c_str());
    
// call
    int result;
    if (tryCall(L,0,LUA_MULTRET,result) == false) {
        return false;
    }
    
    return true;
}

/** */
bool LuaPCall::require(lua_State *L,const std::string &path) {
// source
    ostringstream str;
    str << "require('" << path << "')";
    
// run
    return doString(L,str.str());
}

} // namespace

} // namespace