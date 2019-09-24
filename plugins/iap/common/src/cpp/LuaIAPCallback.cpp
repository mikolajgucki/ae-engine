#include "LuaIAPCallback.h"
#include "LuaCallNoArgFuncRequest.h"
#include "LuaCallStrFuncRequest.h"

using namespace ae::lua;
using namespace ae::engine;

namespace ae {
    
namespace iap {
    
/** */
void LuaIAPCallback::loaded() {
    LuaCallNoArgFuncRequest::call(luaEngine,"iapLoaded",false);
}
    
/** */
void LuaIAPCallback::started() {
    LuaCallNoArgFuncRequest::call(luaEngine,"iap.started",true);
}

/** */
void LuaIAPCallback::failedToStart() {
    LuaCallNoArgFuncRequest::call(luaEngine,"iap.failedToStart",true);
}

/** */
void LuaIAPCallback::purchasing(const char *productId) {
    LuaCallStrFuncRequest::call(luaEngine,"iap.purchasing",productId,true);
}

/** */
void LuaIAPCallback::deferred(const char *productId) {
    LuaCallStrFuncRequest::call(luaEngine,"iap.deferred",productId,true);
}

/** */
void LuaIAPCallback::purchased(const char *productId) {
    LuaCallStrFuncRequest::call(luaEngine,"iap.purchased",productId,true);
}

/** */
void LuaIAPCallback::purchaseFailed(const char *reason) {
    LuaCallStrFuncRequest::call(luaEngine,"iap.purchaseFailed",reason,true);
}

/** */
void LuaIAPCallback::purchaseCanceled() {
    LuaCallNoArgFuncRequest::call(luaEngine,"iap.purchaseCanceled",true);
}

/** */
void LuaIAPCallback::alreadyOwned(const char *productId) {
    LuaCallStrFuncRequest::call(luaEngine,"iap.alreadyOwned",productId,true);
}
    
/** */
void LuaIAPCallback::purchasesRestored() {
    LuaCallNoArgFuncRequest::call(luaEngine,"iap.purchasesRestored",true);
}
    
/** */
void LuaIAPCallback::restorePurchasesFailed(const char *reason) {
    LuaCallStrFuncRequest::call(luaEngine,"iap.restorePurchasesFailed",reason,true);
}
    
} // namespace
    
} // namespace