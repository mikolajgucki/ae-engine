#ifndef AE_IAP_PRODUCT_H
#define AE_IAP_PRODUCT_H

#include <string>

namespace ae {

namespace iap {
  
/**
 * \brief Represents a product.
 */
class IAPProduct {
    /// The product identifier;
    const std::string id;
    
    /// The product title.
    const std::string title;
    
    /// The price.
    const std::string price;
    
    /// The price in cents.
    const long priceInCents;
    
    /// The (ISO 4217) currency code.
    const std::string currencyCode;
    
public:
    /**
     * \brief Constructs a DesktopIAPProduct.
     * \param id_ The product identifier.
     * \param title_ The title.
     * \param price_ The price.
     * \param priceInCents_ The price in cents.
     * \param currencyCode_ The (ISO 4217) currency code.
     */
    IAPProduct(const std::string &id_,const std::string &title_,
        const std::string &price_,long priceInCents_,
        const std::string &currencyCode_):id(id_),title(title_),
        price(price_),priceInCents(priceInCents_),
        currencyCode(currencyCode_) {        
    }
    
    /** */
    IAPProduct(const IAPProduct &product):id(product.getId()),
        title(product.getTitle()),price(product.getPrice()),
        priceInCents(product.getPriceInCents()),
        currencyCode(product.getCurrencyCode()) {
    }
    
    /** */
    virtual ~IAPProduct() {
    }
    
    /**
     * \brief Gets the product identifier.
     * \return The product identifier.
     */
    const std::string &getId() const {
        return id;
    }
    
    /**
     * \brief Gets the title.
     * \return The title.
     */
    const std::string &getTitle() const {
        return title;
    }
    
    /**
     * \brief Gets the price.
     * \return The price.
     */
    const std::string &getPrice() const {
        return price;
    }
    
    /**
     * \brief Gets the price in cents.
     * \return The price in cents.
     */
    long getPriceInCents() const {
        return priceInCents;
    }
    
    /**
     * \brief Gets the (ISO 4217) currency code.
     * \return The currency code.
     */
    const std::string &getCurrencyCode() const {
        return currencyCode;
    }
};
    
} // namespace

} // namespace

#endif // AE_IAP_PRODUCT_H