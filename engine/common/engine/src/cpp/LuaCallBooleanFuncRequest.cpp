#include <memory>

#include "LuaCallUtil.h"
#include "LuaCallBooleanFuncRequest.h"

using namespace std;
using namespace ae::lua;

namespace ae {
    
namespace engine {
    
/** */
bool LuaCallBooleanFuncRequest::run() {
    LuaCallUtil luaCallUtil;
    if (luaCallUtil.callBooleanFunc(L,funcName,arg,failOnNoFunc) == false) {
        setError(luaCallUtil.getError());
        return false;        
    }    
    
    return true;
}

/** */
bool LuaCallBooleanFuncRequest::call(LuaEngine *luaEngine,const char *funcName,
    bool arg,bool failOnNoFunc) {
//
    LuaCallBooleanFuncRequest *request =
        new (nothrow) LuaCallBooleanFuncRequest(
        luaEngine->getLuaState(),funcName,arg,failOnNoFunc);
    if (request == (LuaCallBooleanFuncRequest *)0) {
        luaEngine->getEngine()->fatalError(
            "Failed to create Lua call request. No memory.");
        return false;
    }
    
    luaEngine->getEngine()->addRequest(request);
    return true;    
}
    
} // namespace
    
} // namespace