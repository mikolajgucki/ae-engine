#include <memory>

#include "LuaCallUtil.h"
#include "LuaCallFuncRequest.h"

using namespace std;
using namespace ae::lua;

namespace ae {
    
namespace engine {
    
/** */
bool LuaCallFuncRequest::run() {
    LuaCallUtil luaCallUtil;
    if (luaCallUtil.callFunc(L,funcName,args,failOnNoFunc) == false) {
        setError(luaCallUtil.getError());
        luaCallUtil.deleteArgs(args);
        return false;        
    }    
    
    luaCallUtil.deleteArgs(args);
    return true;
}

/** */
bool LuaCallFuncRequest::call(LuaEngine *luaEngine,const char *funcName,
    vector<LuaCallArg *> args,bool failOnNoFunc) {
//
    LuaCallFuncRequest *request = new (nothrow) LuaCallFuncRequest(
        luaEngine->getLuaState(),funcName,args,failOnNoFunc);
    if (request == (LuaCallFuncRequest *)0) {
        luaEngine->getEngine()->fatalError(
            "Failed to create Lua call request. No memory.");
        return false;
    }
    
    luaEngine->getEngine()->addRequest(request);
    return true;    
}
    
} // namespace
    
} // namespace