#ifndef AE_DESKTOP_IAP_CFG_H
#define AE_DESKTOP_IAP_CFG_H

#include <vector>
#include "DesktopIAPProduct.h"

namespace ae {

namespace iap {
  
/**
 * \brief Represents a desktop IAP configuration.
 */
class DesktopIAPCfg {
    /** */
    enum {
        /** */
        DEFAULT_RESTORE_PURCHASES_TIME = 3000
    };
    
    /// The products.
    std::vector<DesktopIAPProduct *> products;
    
    /// The restore purchases response time.
    int restorePurchasesTime;
    
    /// The restore purchases rail reason.
    std::string restorePurchasesFailReason;
    
    /**
     * \brief Deletes all the products.
     */
    void deleteProducts();
    
public:
    /**
     * \brief Constructs a DesktopIAPCfg.
     */
    DesktopIAPCfg():products(),
        restorePurchasesTime(DEFAULT_RESTORE_PURCHASES_TIME),
        restorePurchasesFailReason() {
    }
    
    /** */
    ~DesktopIAPCfg() {
        deleteProducts();
    }
    
    /**
     * \brief Gets the product identifier.
     * \return The product identifier.
     */
    void addProduct(DesktopIAPProduct *product) {
        products.push_back(product);
    }
    
    /**
     * \brief Gets a product.
     * \param id The product identifier.
     * \return The product or null if there is no such product.
     */
    DesktopIAPProduct *getProduct(const std::string &id);
    
    /**
     * \brief Gets all the products.
     * \param products_ The result vector.
     */
    void getProducts(std::vector<IAPProduct *> &products_);
    
    /**
     * \brief Checks if a product is owned.
     * \param productId The product identifier.
     * \return <code>true</code> if owned, <code>false</code> otherwise.
     */
    bool isOwned(const std::string& productId);
    
    /**
     * \brief Sets the restore purchases response time.
     * \param restorePurchasesTime_ The restore purchases response time.
     */
    void setRestorePurchasesResponseTime(int restorePurchasesTime_) {
        restorePurchasesTime = restorePurchasesTime_;
    }
    
    /**
     * \brief Gets the restore purchases response time.
     * \return The restore purchases response time.
     */
    int getRestorePurchasesResponseTime() {
        return restorePurchasesTime;
    }
    
    /**
     * \brief Sets the restore purchases fail reason.
     * \param reason The fail reason.
     */
    void setRestorePurchasesFailReason(const std::string& reason) {
        restorePurchasesFailReason = reason;
    }
    
    /**
     * \brief Checks if it should fail when restoring purchases.
     * \return <code>true</code> if should fail, <code>false</code> otherwise.
     */
    bool hasRestorePurchasesFailReason() {
        return restorePurchasesFailReason.empty() == false;
    }
    
    /**
     * \brief Gets the restore purchases fail reason.
     * \return The restore purchaes fail reason.
     */
    const std::string &getRestorePurchasesFailReason() {
        return restorePurchasesFailReason;
    }    
};
    
} // namespace

} // namespace

#endif // AE_DESKTOP_IAP_CFG_H