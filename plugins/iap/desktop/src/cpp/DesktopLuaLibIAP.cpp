#include <sstream>

#include "Log.h"
#include "DesktopIAPCfgJSONReader.h"
#include "DesktopLuaLibIAP.h"

using namespace std;
using namespace ae;

namespace ae {

namespace iap {
    
/**
 * \brief The alarm which fires 'purchased' event.
 */
class PurchasedAlarm:public ::ae::engine::TimerAlarm {
    /// The IAP Lua library.
    DesktopLuaLibIAP *luaLibIap;
    
    /// The product identifier.
    const string productId;
    
public:
    /** */
    PurchasedAlarm(DesktopLuaLibIAP *luaLibIap_,const string& productId_,
        int time_):TimerAlarm(time_),luaLibIap(luaLibIap_),
        productId(productId_) {
    }
    
    /** */
    virtual ~PurchasedAlarm() {
    }
    
    /** */
    virtual void fire() {
        luaLibIap->purchased(productId.c_str());
        delete this;
    }
};    
    
/**
 * \brief The alarm which fires 'purchase failed' event.
 */
class PurchaseFailedAlarm:public ::ae::engine::TimerAlarm {
    /// The IAP Lua library.
    DesktopLuaLibIAP *luaLibIap;
    
    /// The product identifier.
    const string productId;
    
    /// The failure reason.
    const string reason;    
    
public:
    /** */
    PurchaseFailedAlarm(DesktopLuaLibIAP *luaLibIap_,const string& productId_,
        const string& reason_,int time_):TimerAlarm(time_),
        luaLibIap(luaLibIap_),productId(productId_),reason(reason_) {
    }
    
    /** */
    virtual ~PurchaseFailedAlarm() {
    }
    
    /** */
    virtual void fire() {
        luaLibIap->purchaseFailed(reason.c_str());
        delete this;
    }
};

/**
 * \brief The alarm which fires 'purchases restored' event.
 */
class PurchasesRestoredAlarm:public ::ae::engine::TimerAlarm {
    /// The IAP Lua library.
    DesktopLuaLibIAP *luaLibIap;
    
public:
    /** */
    PurchasesRestoredAlarm(DesktopLuaLibIAP *luaLibIap_,int time_):
        TimerAlarm(time_),luaLibIap(luaLibIap_) {
    }
    
    /** */
    virtual ~PurchasesRestoredAlarm() {
    }
    
    /** */
    virtual void fire() {
        vector<IAPProduct *> products;
        luaLibIap->getProducts(products);
        
    // for each product
        vector<IAPProduct *>::iterator itr = products.begin();
        for (;itr != products.end(); ++itr) {
            IAPProduct *product = *itr;
            if (luaLibIap->isOwned(product->getId()) == true) {
                luaLibIap->alreadyOwned(product->getId().c_str());
            }
        }
        
    // restored
        luaLibIap->purchasesRestored();
        delete this;
    }
};

/**
 * \brief The alarm which fires 'restore purchases failed' event.
 */
class PurchasesRestoredFailedAlarm:public ::ae::engine::TimerAlarm {
    /// The IAP Lua library.
    DesktopLuaLibIAP *luaLibIap;
    
    /// The failure reason.
    const string reason;      
    
public:
    /** */
    PurchasesRestoredFailedAlarm(DesktopLuaLibIAP *luaLibIap_,
        const string& reason_,int time_):TimerAlarm(time_),
        luaLibIap(luaLibIap_),reason(reason_) {
    }
    
    /** */
    virtual ~PurchasesRestoredFailedAlarm() {
    }
    
    /** */
    virtual void fire() {
        luaLibIap->restorePurchasesFailed(reason.c_str());
        delete this;
    }
};

// The log tag.
static const char *logTag = "Desktop/IAP";
 
/** */
int DesktopLuaLibIAP::getResponseTime(DesktopIAPProduct *product) {
    return product->getResponseTime();
}

/** */
bool DesktopLuaLibIAP::initLuaLib() {
    Json::Value json;
    if (cfg->readJSONCfg(json) == false) {
        setError(cfg->getError());
        return false;
    } 
    
// read from the JSON
    DesktopIAPCfgJSONReader iapCfgReader;    
    iapCfg = iapCfgReader.read(json);
    if (iapCfg == (DesktopIAPCfg *)0) {
        setError(iapCfgReader.getError());
        return false;
    }    
    
    return true;
}

/** */
bool DesktopLuaLibIAP::isOwned(const std::string& productId) {
    return iapCfg->isOwned(productId);
}

/** */
void DesktopLuaLibIAP::init() {
    log->trace(logTag,"DesktopLuaLibIAP::init()");
    getCallback()->started();
}

/** */
bool DesktopLuaLibIAP::isSupported() {
    log->trace(logTag,"DesktopLuaLibIAP::isSupported()");
    return true;
}

/** */
void DesktopLuaLibIAP::purchase(const string& productId) {
// log
    ostringstream msg;
    msg << "DesktopLuaLibIAP::purchase(" << productId << ")";
    log->trace(logTag,msg.str());
    
// get the product
    DesktopIAPProduct *product = iapCfg->getProduct(productId);
    
// no such product
    if (product == (DesktopIAPProduct *)0) {
        PurchaseFailedAlarm *purchaseFailedAlarm =
            new PurchaseFailedAlarm(this,productId,
            "Product not found",PRODUCT_NOT_FOUND_RESPONSE_TIME);
        timer->addAlarm(purchaseFailedAlarm);
        return;
    }
    
// fail?
    if (product->hasFailReason() == true) {
        PurchaseFailedAlarm *purchaseFailedAlarm =
            new PurchaseFailedAlarm(this,productId,
            product->getFailReason(),getResponseTime(product));
        timer->addAlarm(purchaseFailedAlarm);
        return;
    }
    
// purchasing
    getCallback()->purchasing(productId.c_str());
    
// purchase
    PurchasedAlarm *purchasedAlarm = new PurchasedAlarm(
        this,productId,getResponseTime(product));
    timer->addAlarm(purchasedAlarm);
}

/** */
bool DesktopLuaLibIAP::getProducts(vector<IAPProduct *> &products) {
    iapCfg->getProducts(products);
    return true;
}

/** */
void DesktopLuaLibIAP::restorePurchases() {
    log->trace(logTag,"DesktopLuaLibIAP::restorePurchases()");
    
// fail?
    if (iapCfg->hasRestorePurchasesFailReason() == true) {
        PurchasesRestoredFailedAlarm *failedAlarm =
            new PurchasesRestoredFailedAlarm(this,
            iapCfg->getRestorePurchasesFailReason(),
            iapCfg->getRestorePurchasesResponseTime());
        timer->addAlarm(failedAlarm); 
        return;
    }
    
// alarm
    PurchasesRestoredAlarm *restoredAlarm = new PurchasesRestoredAlarm(
        this,iapCfg->getRestorePurchasesResponseTime());
    timer->addAlarm(restoredAlarm);
}

} // namespace

} // namespace