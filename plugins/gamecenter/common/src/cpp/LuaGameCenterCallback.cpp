#include "LuaCallNoArgFuncRequest.h"
#include "LuaGameCenterCallback.h"

using namespace ae::engine;

namespace ae {
    
namespace gamecenter {
    
/** */
void LuaGameCenterCallback::callNoArgFunc(const char *funcName) {
    LuaCallNoArgFuncRequest::call(luaEngine,funcName,false);
}

/** */
void LuaGameCenterCallback::willShowLoginView() {
    callNoArgFunc("gamecenter.willShowLoginView");
}

/** */
void LuaGameCenterCallback::authenticated() {
    callNoArgFunc("gamecenter.authenticated");
}
    
/** */
void LuaGameCenterCallback::notAuthenticated() {
    callNoArgFunc("gamecenter.notAuthenticated");
}
    
/** */
void LuaGameCenterCallback::notAuthenticatedWithError() {
    callNoArgFunc("gamecenter.notAuthenticatedWithError");
}
    
/** */
void LuaGameCenterCallback::loginViewHidden() {
    callNoArgFunc("gamecenter.loginViewHidden");
}

/** */
void LuaGameCenterCallback::achievementsLoaded() {
    callNoArgFunc("gamecenter.achievementsLoaded");
}
    
} // namespace
    
} // namespace