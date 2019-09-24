#include "LuaCallNoArgFuncRequest.h"
#include "LuaGPGSCallback.h"

using namespace ae::engine;

namespace ae {
    
namespace gpgs {
    
/** */
void LuaGPGSCallback::callNoArgFunc(const char *funcName) {
    LuaCallNoArgFuncRequest::call(luaEngine,funcName,false);
}

/** */
void LuaGPGSCallback::signedIn() {
    callNoArgFunc("gpgs.signedIn");
}

} // namespace
    
} // namespace