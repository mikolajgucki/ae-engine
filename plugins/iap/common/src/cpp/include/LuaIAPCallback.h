#ifndef AE_LUA_IAP_CALLBACK_H
#define AE_LUA_IAP_CALLBACK_H

#include "DLog.h"
#include "LuaCallUtil.h"
#include "LuaEngine.h"
#include "IAPCallback.h"

namespace ae {
    
namespace iap {
  
/**
 * \brief The IAP callback which calls Lua functions.
 */
class LuaIAPCallback:public IAPCallback {
    /// The log.
    DLog *log;
    
    /// The Lua engine.
    ::ae::engine::LuaEngine *luaEngine;
    
    /// The Lua call utility.
    ::ae::lua::LuaCallUtil luaCallUtil;
    
public:
    /** */
    LuaIAPCallback(DLog *log_,::ae::engine::LuaEngine *luaEngine_):
        IAPCallback(),log(log_),luaEngine(luaEngine_),luaCallUtil() {
    }    
    
    /** */
    virtual ~LuaIAPCallback() {
    }
    
    /** */
    virtual void loaded();
    
    /** */
    virtual void started(); 
    
    /** */
    virtual void failedToStart();

    /** */
    virtual void purchasing(const char *productId);   
    
    /** */
    virtual void deferred(const char *productId);      
    
    /** */
    virtual void purchased(const char *productId);
    
    /** */
    virtual void purchaseFailed(const char *reason);
    
    /** */
    virtual void purchaseCanceled();    
    
    /** */
    virtual void alreadyOwned(const char *productId);
    
    /** */
    virtual void purchasesRestored();
    
    /** */
    virtual void restorePurchasesFailed(const char *reason);
};
    
} // namespace
    
} // namespace

#endif // AE_LUA_IAP_CALLBACK_H