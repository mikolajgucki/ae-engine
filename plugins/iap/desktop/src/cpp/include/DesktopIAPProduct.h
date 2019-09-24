#ifndef AE_DESKTOP_IAP_PRODUCT_H
#define AE_DESKTOP_IAP_PRODUCT_H

#include "IAPProduct.h"

namespace ae {

namespace iap {
  
/**
 * \brief Represents a product.
 */
class DesktopIAPProduct:public IAPProduct {
public:
    /** */
    enum {
        /// The default response time (in milliseconds).
        DEFAULT_RESPONSE_TIME = 4000
    };
    
private:
    /// The fail reason.
    std::string failReason;
    
    /// The response time (in milliseconds).
    int responseTime;
    
    /// Indicates if the product is already owned.
    bool owned;
    
public:
    /**
     * \brief Constructs a DesktopIAPProduct.
     * \param id The product identifier.
     * \param title The title.
     * \param price The price.
     * \param priceInCents The price in cents.     
     * \param currencyCode The (ISO 4217) currency code.
     */
    DesktopIAPProduct(const std::string &id,const std::string &title,
        const std::string &price,long priceInCents,
        const std::string &currencyCode):
        IAPProduct(id,title,price,priceInCents,currencyCode),
        responseTime(DEFAULT_RESPONSE_TIME),owned(false) {
    }
    
    /** */
    DesktopIAPProduct(const DesktopIAPProduct &product):
        IAPProduct(product),failReason(product.failReason),
        responseTime(product.responseTime),owned(product.owned) {
    }
    
    /** */
    virtual ~DesktopIAPProduct() {
    }
    
    /**
     * \brief Sets the fail reason.
     * \param failReason_ The fail reason.
     */
    void setFailReason(const std::string& failReason_) {
        failReason = failReason_;
    }
    
    /**
     * \brief Checks if the product should fail when purchased.
     * \return <code>true</code> if should fail, <code>false</code> otherwise.
     */
    bool hasFailReason() {
        return failReason.empty() == false;
    }
    
    /**
     * \brief Gets the fail reason.
     * \return The fail reason.
     */
    const std::string &getFailReason() const {
        return failReason;
    }
    
    /**
     * \brief Sets the response time
     * \param responseTime_ The response time.     
     */
    void setResponseTime(int responseTime_) {
        responseTime = responseTime_;
    }
    
    /**
     * \brief Gets the response time.
     * \return The response time.
     */
    int getResponseTime() const {
        return responseTime;
    }
    
    /**
     * \brief Sets the owned flag.
     * \param owned_ The owned flag.
     */
    void setOwned(bool owned_) {
        owned = owned_;
    }
    
    /**
     * \brief Gets the owned flag.
     * \return The owned flag.
     */
    bool getOwned() const {
        return owned;
    }
};
    
} // namespace

} // namespace

#endif // AE_DESKTOP_IAP_PRODUCT_H