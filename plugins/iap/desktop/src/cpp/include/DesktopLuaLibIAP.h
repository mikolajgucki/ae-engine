#ifndef AE_DESKTOP_LUA_LIB_IAP_H
#define AE_DESKTOP_LUA_LIB_IAP_H

#include <string>
#include <vector>

#include "DLog.h"
#include "Timer.h"
#include "DesktopPluginCfg.h"
#include "DesktopIAPCfg.h"
#include "LuaLibIAP.h"
#include "IAPLuaExtraLib.h"

namespace ae {

namespace iap {
  
/**
 * \brief The desktop implementation of the IAP Lua library.
 */
class DesktopLuaLibIAP:public LuaLibIAP {
    /** */
    enum {
        /// The respone time in milliseconds for the product-not-found error.
        PRODUCT_NOT_FOUND_RESPONSE_TIME = 3000
    };
    
    /// The log.
    DLog *log;
        
    /// The plugin configuration.
    ::ae::engine::desktop::DesktopPluginCfg *cfg;
    
    /// The timer.
    ::ae::engine::Timer *timer;    
    
    /// The IAP configuration;
    DesktopIAPCfg *iapCfg;
    
    /**
     * \brief Gets the response time for a product.
     * \param product The product.
     * \return The response time in milliseconds.
     */
    int getResponseTime(DesktopIAPProduct *product);
    
public:
    /** */
    DesktopLuaLibIAP(DLog *log_,::ae::engine::desktop::DesktopPluginCfg *cfg_,
        ::ae::engine::Timer *timer_):LuaLibIAP(),log(log_),cfg(cfg_),
        timer(timer_),iapCfg((DesktopIAPCfg *)0) {
    }
    
    /** */
    virtual ~DesktopLuaLibIAP() {
    }
    
    /** */
    bool initLuaLib();
    
    /** */
    bool isOwned(const std::string& productId);
    
    /** */
    virtual void init();
    
    /** */
    virtual bool isSupported();

    /** */    
    virtual void purchase(const std::string& productId);
    
    /** */
    virtual bool getProducts(std::vector<IAPProduct *> &products);
    
    /** */
    virtual void restorePurchases();    
};
    
} // namespace

} // namespace

#endif // AE_DESKTOP_LUA_LIB_IAP_H