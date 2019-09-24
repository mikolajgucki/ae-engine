#include "lua_common.h"
#include "LuaGetField.h"
#include "LuaCallUtil.h"

using namespace std;

namespace ae {
    
namespace lua {
    
/** */
bool LuaCallUtil::getFunc(lua_State *L,const char *funcName,bool &hasFunc) {
    LuaGetField getFunc(L,funcName);
    if (getFunc.isNil() == true) {
        hasFunc = false;
        return true;
    }
    hasFunc = true;
    
// get the function
    getFunc.run();
    if (getFunc.chkError()) {
        setError(getFunc.getError());
        return false;
    }
    
    return true;    
}

/** */
void LuaCallUtil::noFuncError(const char *funcName) {
    ostringstream err;
    err << "Lua function " << funcName <<
        " not found while trying to call";
    setError(err.str());    
}

/** */
bool LuaCallUtil::callNoArgFunc(lua_State *L,const char *funcName,
    bool failOnNoFunc) {
// push error handler to get traceback on error
    luaPCall.pushErrorHandler(L); 

    bool hasFunc;
// get the function
    if (getFunc(L,funcName,hasFunc) == false) {
        return false;
    }
    if (hasFunc == false) {
        if (failOnNoFunc == true) {
            noFuncError(funcName);
            return false;
        }        
        return true;
    }

// call
    int callResult;
    if (luaPCall.tryCall(L,0,0,callResult) == false) {
        setError(luaPCall.getError());
        return false;
    }
    
    return true;
}

/** */
bool LuaCallUtil::callBooleanFunc(lua_State *L,const char *funcName,
    bool arg,bool failOnNoFunc) {
// push error handler to get traceback on error
    luaPCall.pushErrorHandler(L);      
    
    bool hasFunc;
// get the function
    if (getFunc(L,funcName,hasFunc) == false) {
        return false;
    }
    if (hasFunc == false) {
        if (failOnNoFunc == true) {
            noFuncError(funcName);
            return false;
        }        
        return true;
    }
    
// push argument
    lua_pushboolean(L,luaBoolean(arg));
    
// call
    int callResult;
    if (luaPCall.tryCall(L,1,0,callResult) == false) {
        setError(luaPCall.getError());
        return false;
    }

    return true;    
}

/** */
bool LuaCallUtil::callStrFunc(lua_State *L,const char *funcName,
    const char *arg,bool failOnNoFunc) {
// push error handler to get traceback on error
    luaPCall.pushErrorHandler(L);      
    
    bool hasFunc;
// get the function
    if (getFunc(L,funcName,hasFunc) == false) {
        return false;
    }
    if (hasFunc == false) {
        if (failOnNoFunc == true) {
            noFuncError(funcName);
            return false;
        }        
        return true;
    }
    
// push argument
    lua_pushstring(L,arg);
    
// call
    int callResult;
    if (luaPCall.tryCall(L,1,0,callResult) == false) {
        setError(luaPCall.getError());
        return false;
    }

    return true;    
}

/** */
bool LuaCallUtil::callFunc(lua_State *L,const char *funcName,
    vector<LuaCallArg *> args,bool failOnNoFunc,int nresults) {
//
// push error handler to get traceback on error
    luaPCall.pushErrorHandler(L);      
    
    bool hasFunc;
// get the function
    if (getFunc(L,funcName,hasFunc) == false) {
        return false;
    }
    if (hasFunc == false) {
        if (failOnNoFunc == true) {
            noFuncError(funcName);
            return false;
        }        
        return true;
    }
    
// push arguments
    int nargs = (int)args.size();
    for (int index = 0; index < nargs; index++) {
        args[index]->pushArg(L);
    }
    
// call
    int callResult;
    if (luaPCall.tryCall(L,nargs,nresults,callResult) == false) {
        setError(luaPCall.getError());
        return false;
    }

    return true;
}

/** */
void LuaCallUtil::deleteArgs(vector<LuaCallArg *> args) {
    for (size_t index = 0; index < args.size(); index++) {
        delete args[index];
    }
}
    
} // namespace

} // namespace