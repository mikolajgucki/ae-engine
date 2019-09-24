#include <memory>

#include "LuaCallUtil.h"
#include "LuaCallNoArgFuncRequest.h"

using namespace std;
using namespace ae::lua;

namespace ae {
    
namespace engine {
    
/** */
bool LuaCallNoArgFuncRequest::run() {
    LuaCallUtil luaCallUtil;
    if (luaCallUtil.callNoArgFunc(L,funcName,failOnNoFunc) == false) {
        setError(luaCallUtil.getError());
        return false;        
    }    
    
    return true;
}

/** */
bool LuaCallNoArgFuncRequest::call(LuaEngine *luaEngine,const char *funcName,
    bool failOnNoFunc) {
//
    LuaCallNoArgFuncRequest *request = new (nothrow) LuaCallNoArgFuncRequest(
        luaEngine->getLuaState(),funcName,failOnNoFunc);
    if (request == (LuaCallNoArgFuncRequest *)0) {
        luaEngine->getEngine()->fatalError(
            "Failed to create Lua call request. No memory.");
        return false;
    }
    
    luaEngine->getEngine()->addRequest(request);
    return true;    
}
    
} // namespace
    
} // namespace