#ifndef AE_LUA_LIB_APP_LOVIN_H
#define AE_LUA_LIB_APP_LOVIN_H

#include <string>
#include "Error.h"
#include "DLog.h"
#include "RunnableQueue.h"
#include "AppLovinCallback.h"

namespace ae {
    
namespace applovin {
  
/**
 * \brief The superclass for platform-specific implementations of AppLovin
 *   Lua library.
 */
class LuaLibAppLovin:public Error {
    /// The log.
    DLog *dlog;
    
    /// The AppLovin callback.
    AppLovinCallback *callback;
    
    /// The queue for the events fired when no callback is set.
    RunnableQueue runnableQueue;    
    
    /** */
    void trace(const char *msg);
    
protected:
    /** */
    LuaLibAppLovin():Error(),dlog((DLog *)0),callback((AppLovinCallback *)0),
        runnableQueue() {
    }
    
    /**
     * \brief Gets the callback.
     * \return The callback.
     */
    AppLovinCallback* getCallback() {
        return callback;
    }

public:
    /** */
    virtual ~LuaLibAppLovin() {
        if (callback != (AppLovinCallback *)0) {
            delete callback;
        }
    }
    
    /** */
    void setDLog(DLog *dlog_) {
        dlog = dlog_;
    }
    
    /**
     * \brief Sets the callback.
     * \param callback_ callback.
     */
    virtual void setCallback(AppLovinCallback *callback_);
    
    /**
     * \brief Checks if an interstitial ad is ready for display.
     * \return <code>true</code> if ready, <code>false</code> otherwise.
     */
    virtual bool isInterstitialAdReadyForDisplay() = 0;
    
    /**
     * \brief Shows the loaded interstitial ad.
     */
    virtual void showInterstitialAd() = 0;
    
    /** */
    virtual void interstitialAdLoaded();
    
    /** */
    virtual void interstitialAdNoFill();
    
    /** */
    virtual void interstitialAdFailed(int errorCode_);
    
    /** */
    virtual void interstitialAdDisplayed();
    
    /** */
    virtual void interstitialAdHidden();
    
    /** */
    virtual void interstitialAdClicked();    
};
    
} // namespace
    
} // namespace

#endif // AE_LUA_LIB_APP_LOVIN_H