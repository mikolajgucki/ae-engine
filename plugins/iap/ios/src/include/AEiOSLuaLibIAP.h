#ifndef AE_IOS_LUA_LIB_IAP_H    
#define AE_IOS_LUA_LIB_IAP_H

#import <Foundation/Foundation.h>

#include "LuaLibIAP.h"
#include "AEIAPProductsListener.h"
#include "AESKProductsRequestDelegate.h"
#include "AESKPaymentTransactionObserver.h"

/**
 * \brief The iOS implementation of the IAP Lua library.
 */
class AEiOSLuaLibIAP:public ::ae::iap::LuaLibIAP,
    public AEIAPProductsListener {
    /// The product identifiers.
    std::vector<const std::string> productIds;
    
    /// The products request delegate.
    AESKProductsRequestDelegate *productsRequestDelegate;
    
    /// The transaction observer.
    AESKPaymentTransactionObserver *transactionObserver;
    
    /// The products.
    NSArray *products;
    
    /**
     * \brief Finds a product.
     * \param nsProductId The product identifier.
     * \return The product or <code>nil</code> if there is no such product.
     */
    SKProduct *findProduct(NSString *nsProductId);
    
public:
    /** */
    AEiOSLuaLibIAP(std::vector<const std::string> &productIds_):LuaLibIAP(),
        AEIAPProductsListener(),productIds(productIds_) {
    }
    
    /** */
    virtual ~AEiOSLuaLibIAP() {
    }
    
    /**
     * \brief Called when products have been received.
     * \param response The products response.
     */
    virtual void receivedProducts(SKProductsResponse *response);
    
    /** This method is called from Lua.
        Do not call it manually! */
    virtual void init();
    
    /** This method is called from Lua.
        Do not call it manually! */
    virtual bool isSupported();
    
    /** This method is called from Lua.
        Do not call it manually! */
    virtual void purchase(const std::string& productId);
    
    /** This method is called from Lua.
        Do not call it manually! */
    virtual bool getProducts(std::vector<ae::iap::IAPProduct *> &iapProducts);
    
    /** This method is called from Lua.
        Do not call it manually! */
    virtual void restorePurchases();

    /** */
    void purchased(NSString *productId);
    
    /** */
    void purchaseFailed(NSString *reason);    
};

#endif // AE_IOS_LUA_LIB_IAP_H