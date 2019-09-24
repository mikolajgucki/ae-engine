#ifndef AE_IAP_CALLBACK_H
#define AE_IAP_CALLBACK_H

namespace ae {
    
namespace iap {
  
/**
 * \brief The in-pp purchaces callback called on IAP events.
 */
class IAPCallback {
public:
    /** */
    IAPCallback() {
    }
    
    /** */
    virtual ~IAPCallback() {
    }
    
    /**
     * \brief Called when the IAP has beed loaded.
     */
    virtual void loaded() = 0;
    
    /**    
     * \brief Called when the IAP has started.
     */
    virtual void started() = 0; 
    
    /**
     * \brief Called when IAP failed to start.
     */
    virtual void failedToStart() = 0;
    
    /**
     * \brief Called when a product is being purchased.
     * \param productId The product identifier.
     */
    virtual void purchasing(const char *productId) = 0;
    
    /**
     * \brief Called when a purchase has been deferred.
     * \param productId The product identifier.
     */
    virtual void deferred(const char *productId) = 0;
    
    /**
     * \brief Called when a product has been purchased.
     * \param productId The product identifier.
     */
    virtual void purchased(const char *productId) = 0;
    
    /**
     * \brief Called when a purchase has failed.
     * \param reason The failure reason.
     */
    virtual void purchaseFailed(const char *reason) = 0;
    
    /**
     * \brief Called when an a purchas has been canceled.
     */
    virtual void purchaseCanceled() = 0;    
    
    /**
     * \brief Called when an already owned product has been purchased.
     * \param productId The product identifier.
     */
    virtual void alreadyOwned(const char *productId) = 0;
    
    /**
     * \brief Called when purchases have been restored.
     */
    virtual void purchasesRestored() = 0;
    
    /**
     * \brief Called when it failed to restore purchases.
     * \param reason The failure reason.
     */
    virtual void restorePurchasesFailed(const char *reason) = 0;
};
    
} // namespace
    
} // namespace

#endif // AE_IAP_CALLBACK_H