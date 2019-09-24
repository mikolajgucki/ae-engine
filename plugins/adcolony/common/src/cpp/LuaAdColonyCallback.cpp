#include "Log.h"
#include "LuaCallNoArgFuncRequest.h"
#include "LuaCallStrFuncRequest.h"
#include "LuaAdColonyCallback.h"

using namespace std;
using namespace ae::engine;

namespace ae {
    
namespace adcolony {
    
/// The log tag.
static const char *logTag = "AE/AdColony";
    
/** */
void LuaAdColonyCallback::callNoArgFunc(const char *funcName) {
    LuaCallNoArgFuncRequest::call(luaEngine,funcName,false);
}

/** */
void LuaAdColonyCallback::callStrFunc(const char *funcName,
    const string& arg) {
//
    LuaCallStrFuncRequest::call(luaEngine,funcName,arg,false);
}

/** */
void LuaAdColonyCallback::interstitialAvailable(const string &zoneId) {
    callStrFunc("adcolony.interstitialAvailable",zoneId);
}

/** */
void LuaAdColonyCallback::interstitialUnavailable(const string &zoneId) {
    callStrFunc("adcolony.interstitialUnavailable",zoneId);
}

/** */
void LuaAdColonyCallback::interstitialOpened(const string &zoneId) {
    Log::trace(logTag,"LuaAdColonyCallback::interstitialOpened()");
    callStrFunc("adcolony.interstitialOpened",zoneId);
}

/** */
void LuaAdColonyCallback::interstitialClosed(const string &zoneId) {
    Log::trace(logTag,"LuaAdColonyCallback::interstitialClosed()");
    callStrFunc("adcolony.interstitialClosed",zoneId);
}

} // namespace

} // namespace