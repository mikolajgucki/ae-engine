#include "Log.h"
#include "LuaCallStrFuncRequest.h"
#include "LuaChartboostCallback.h"

using namespace ae::engine;

namespace ae {
    
namespace chartboost {
    
/** */
void LuaChartboostCallback::callStrFunc(const char *funcName,const char *arg) {
    LuaCallStrFuncRequest::call(luaEngine,funcName,arg,true);
}
    
/** */
void LuaChartboostCallback::didCacheInterstitial(const char *location) {
    callStrFunc("chartboost.didCacheInterstitial",location);
}
    
/** */
void LuaChartboostCallback::didFailToLoadInterstitial(const char *location) {
    callStrFunc("chartboost.didFailToLoadInterstitial",location);
}
    
/** */
void LuaChartboostCallback::didClickInterstitial(const char *location) {
    callStrFunc("chartboost.didClickInterstitial",location);
}
    
/** */
void LuaChartboostCallback::didCloseInterstitial(const char *location) {
    callStrFunc("chartboost.didCloseInterstitial",location);
}
    
/** */
void LuaChartboostCallback::didDismissInterstitial(const char *location) {
    callStrFunc("chartboost.didDismissInterstitial",location);
}
    
/** */
void LuaChartboostCallback::didDisplayInterstitial(const char *location) {
    callStrFunc("chartboost.didDisplayInterstitial",location);
}
    
} // namespace
    
} // namespace