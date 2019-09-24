#ifndef AE_IAP_PRODUCTS_LISTENER_H
#define AE_IAP_PRODUCTS_LISTENER_H

#import <Foundation/Foundation.h>
#import <StoreKit/StoreKit.h>

/**
 * \brief Tha IAP products listener.
 */
class AEIAPProductsListener {
public:
    /** */
    AEIAPProductsListener();
    
    /** */
    virtual ~AEIAPProductsListener();
    
    /**
     * \brief Called when products have been received.
     * \param products The products.
     */
    virtual void receivedProducts(SKProductsResponse *response) = 0;
};

#endif // AE_IAP_PRODUCTS_LISTENER_H