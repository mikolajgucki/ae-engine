#include "LuaCallNoArgFuncRequest.h"
#include "LuaAppLovinCallback.h"

using namespace ae::engine;

namespace ae {
    
namespace applovin {
    
/** */
void LuaAppLovinCallback::callNoArgFunc(const char *funcName) {
    LuaCallNoArgFuncRequest::call(luaEngine,funcName,true);
}
    
/** */
void LuaAppLovinCallback::interstitialAdLoaded() {
    callNoArgFunc("applovin.interstitialAdLoaded");
}
    
/** */
void LuaAppLovinCallback::interstitialAdNoFill() {
    callNoArgFunc("applovin.interstitialAdNoFill");
}

/** */
void LuaAppLovinCallback::interstitialAdFailed(int errorCode) {
    callNoArgFunc("applovin.interstitialAdFailed");
}

/** */
void LuaAppLovinCallback::interstitialAdDisplayed() {
    callNoArgFunc("applovin.interstitialAdDisplayed");
}

/** */
void LuaAppLovinCallback::interstitialAdHidden() {
    callNoArgFunc("applovin.interstitialAdHidden");
}

/** */
void LuaAppLovinCallback::interstitialAdClicked() {
    callNoArgFunc("applovin.interstitialAdClicked");
}
    
} // namespace
    
} // namespace