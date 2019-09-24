#include <memory>

#include "LuaCallUtil.h"
#include "LuaCallStrFuncRequest.h"

using namespace std;
using namespace ae::lua;

namespace ae {
    
namespace engine {
    
/** */
bool LuaCallStrFuncRequest::run() {
    LuaCallUtil luaCallUtil;
    if (luaCallUtil.callStrFunc(L,funcName,arg.c_str(),failOnNoFunc) == false) {
        setError(luaCallUtil.getError());
        return false;        
    }    
    
    return true;
}

/** */
bool LuaCallStrFuncRequest::call(LuaEngine *luaEngine,const char *funcName,
    const string &arg,bool failOnNoFunc) {
//
    LuaCallStrFuncRequest *request = new (nothrow) LuaCallStrFuncRequest(
        luaEngine->getLuaState(),funcName,arg,failOnNoFunc);
    if (request == (LuaCallStrFuncRequest *)0) {
        luaEngine->getEngine()->fatalError(
            "Failed to create Lua call request. No memory.");
        return false;
    }
    
    luaEngine->getEngine()->addRequest(request);
    return true;    
}
    
} // namespace
    
} // namespace