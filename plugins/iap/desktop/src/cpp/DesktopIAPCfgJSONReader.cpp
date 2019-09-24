#include <sstream>

#include "Log.h"
#include "DesktopIAPProduct.h"
#include "DesktopIAPCfgJSONReader.h"

using namespace std;

namespace ae {

namespace iap {

/** */
bool DesktopIAPCfgJSONReader::readRestorePurchases(DesktopIAPCfg *cfg,
    Json::Value json) {
// has restore purchase?
    Json::Value jsonRestorePurchases = json["restorePurchases"];
    if (jsonRestorePurchases.isNull() == true) {
        return true;
    }
    
// time
    Json::Value jsonTime = jsonRestorePurchases["time"];
    if (jsonTime.isNull() == false) {
        if (jsonTime.isInt() == false) {
            setError("Response time for restore purchases "
                "must be an integer (responseTime)");
            return false;
        }
        cfg->setRestorePurchasesResponseTime(jsonTime.asInt());        
    }
    
// failure reason
    Json::Value jsonFailReason = jsonRestorePurchases["failReason"];
    if (jsonFailReason.isNull() == false) {
        if (jsonFailReason.isString() == false) {
            setError("Failure reason for restore purchases "
                "must be a string (failReason)");
            return false;
        }            
        cfg->setRestorePurchasesFailReason(jsonFailReason.asString());        
    }
    
    return true;
}
 
/** */
bool DesktopIAPCfgJSONReader::readProducts(DesktopIAPCfg *cfg,
    Json::Value json) {
// products
    if (json["products"].isNull() == true) {
        setError("Products not found (array 'products')");
        return false;
    }    
    Json::Value jsonProducts = json["products"];
    if (jsonProducts.isArray() == false) {
        setError("Products node must be an array ('products')");
        return false;
    }
        
// for each product
    Json::ArrayIndex size = jsonProducts.size();
    Json::ArrayIndex index;
    for (index = 0; index < size; index++) {
        Json::Value jsonProduct = jsonProducts[index];
        
    // identifier
        Json::Value jsonId = jsonProduct["id"];
        if (jsonId.isNull() == true) {
            setError("Product without identifier (id)");
            return false;
        }
        
    // title
        Json::Value jsonTitle = jsonProduct["title"];
        if (jsonTitle.isNull() == true) {
            setError("Product without title (title)");
            return false;
        }
        
    // price
        Json::Value jsonPrice = jsonProduct["price"];
        if (jsonPrice.isNull() == true) {
            setError("Product without price (price)");
            return false;
        }
        
    // price in cents
        Json::Value jsonPriceInCents = jsonProduct["priceInCents"];
        if (jsonPriceInCents.isNull() == true) {
            setError("Product without price in cents (priceInCents)");
            return false;
        }
        if (jsonPriceInCents.isInt() == false) {
            setError("Invalid price in cents (priceInCents)");
            return false;
        }

    // currency code
        Json::Value jsonCurrencyCode = jsonProduct["currencyCode"];
        if (jsonCurrencyCode.isNull() == true) {
            setError("Product without currency code (currencyCode)");
            return false;
        }                
        
    // create the product
        DesktopIAPProduct *product = new DesktopIAPProduct(
            jsonId.asString(),jsonTitle.asString(),jsonPrice.asString(),
            jsonPriceInCents.asInt(),jsonCurrencyCode.asString());
        cfg->addProduct(product);

    // response time
        Json::Value jsonResponseTime = jsonProduct["responseTime"];
        if (jsonResponseTime.isNull() == false) {
            if (jsonPriceInCents.isInt() == false) {
                setError("Response time must be an integer (responseTime)");
                return false;
            }
            product->setResponseTime(jsonResponseTime.asInt());
        }
        
    // fail
        Json::Value jsonFailReason = jsonProduct["failReason"];
        if (jsonFailReason.isNull() == false) {
            if (jsonFailReason.isString() == false) {
                setError("Product failure reason must be a string (failReason)");
                return false;
            }            
            product->setFailReason(jsonFailReason.asString());
        }
        
    // owned
        Json::Value jsonOwned = jsonProduct["owned"];
        if (jsonOwned.isNull() == false) {
            if (jsonOwned.isBool() == false) {
                setError("Owned must be an boolean (owned)");
                return false;
            }
            product->setOwned(jsonOwned.asBool());
        }
    }
    
    return true;
}

/** */
DesktopIAPCfg *DesktopIAPCfgJSONReader::read(Json::Value json) {
    DesktopIAPCfg *cfg = new DesktopIAPCfg();
    
// restore purchases
    if (readRestorePurchases(cfg,json) == false) {
        delete cfg;
        return (DesktopIAPCfg *)0;
    }
    
// products
    if (readProducts(cfg,json) == false) {
        delete cfg;
        return (DesktopIAPCfg *)0;
    }
    
    return cfg;
}
    
} // namespace

} // namespace