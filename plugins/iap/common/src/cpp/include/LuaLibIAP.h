#ifndef AE_LUA_LIB_IAP_H
#define AE_LUA_LIB_IAP_H

#include <string>
#include <vector> 

#include "Error.h"
#include "DLog.h"
#include "RunnableQueue.h"
#include "IAPCallback.h"
#include "IAPProduct.h"

namespace ae {
    
namespace iap {
    
/**
 * \brief The superclass for platform-specific implementations of the IAP
 *   Lua library.
 */
class LuaLibIAP:public Error {
    /// The log.
    DLog *dlog;
    
    /// The IAP callback.
    IAPCallback *callback;
    
    /// The queue for the events fired when no callback is set.
    RunnableQueue runnableQueue;       
    
    /** */
    void trace(const char *msg);    
    
protected:
    /** */
    LuaLibIAP():Error(),dlog((DLog *)0),callback((IAPCallback *)0),
        runnableQueue() {
    }
    
    /**
     * \brief Gets the callback.
     * \return The callback.
     */
    IAPCallback *getCallback() {
        return callback;
    } 
    
public:
    /** */
    virtual ~LuaLibIAP() {
        if (callback != (IAPCallback *)0) {
            delete callback;
        }
    }
    
    /** */
    void setDLog(DLog *dlog_) {
        dlog = dlog_;
    }    
    
    /**
     * \brief Sets the callback.
     * \param callback_ The callback.
     */
    void setCallback(IAPCallback *callback_);
    
    /**
     * \brief Called when the library has been loaded.
     */    
    virtual void loaded();
    
    /**
     * \brief Initializes the library. Called right after having loaded the Lua
     *   library.
     */
    virtual void init() = 0;
    
    /**
     * \brief Checks if IAP is supported.
     * \return <code>true</code> if supported, <code>false</code> otherwise.
     */
    virtual bool isSupported() = 0;
    
    /**
     * \brief Purchases a product.
     * \param productId The product identifier.
     */
    virtual void purchase(const std::string& productId) = 0;
    
    /**
     * \brief Gets products that can be purchased.
     * \param products The result vector.
     * \return <code>true</code> on success, sets error and returns
     *   <code>false</code> otherwise.
     */
    virtual bool getProducts(std::vector<IAPProduct *> &products) = 0;
    
    /**
     * \brief Restores the purchases.
     */
    virtual void restorePurchases() = 0;    
    
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

#endif // AE_LUA_LIB_IAP_H