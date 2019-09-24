#include "LuaCallNoArgFuncRequest.h"
#include "LuaCallBooleanFuncRequest.h"
#include "LuaVungleCallback.h"

using namespace ae::engine;

namespace ae {

namespace vungle {
    
/** */
void LuaVungleCallback::callNoArgFunc(const char *funcName) {
    LuaCallNoArgFuncRequest::call(luaEngine,funcName,true);
}    
    
/** */
void LuaVungleCallback::adPlayableChanged(bool isAdPlayable_) {
    LuaCallBooleanFuncRequest::call(luaEngine,"vungle.adPlayableChanged",
        isAdPlayable_,true);
}
    
/** */
void LuaVungleCallback::willShowAd() {
    callNoArgFunc("vungle.willShowAd");
}
    
/** */
void LuaVungleCallback::willCloseAd() {
    callNoArgFunc("vungle.willCloseAd");
}
    
/** */
void LuaVungleCallback::adClicked() {
    callNoArgFunc("vungle.adClicked");
}

/** */
void LuaVungleCallback::failedToPlayAd() {
    callNoArgFunc("vungle.failedToPlayAd");
}

/** */
void LuaVungleCallback::viewCompleted() {
    callNoArgFunc("vungle.viewCompleted");
}
    
} // namespace

} // namespace