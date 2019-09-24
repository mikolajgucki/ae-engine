#ifndef AE_LUA_LIB_CHARTBOOST_H
#define AE_LUA_LIB_CHARTBOOST_H

#include <string>
#include "Error.h"
#include "DLog.h"
#include "RunnableQueue.h"
#include "ChartboostCallback.h"

namespace ae {
    
namespace chartboost {
    
/**
 * \brief The superclass for platform-specific implementations of the Chartboost
 *   Lua library.
 */
class LuaLibChartboost:public Error {
    /// The log.
    DLog *dlog;
    
    /// The Lua Chartboost callback.
    ChartboostCallback *callback;
    
    /// The queue for the events fired when no callback is set.
    RunnableQueue runnableQueue;
    
    /** */
    void trace(const char *msg);    
    
protected:
    /** */
    LuaLibChartboost():Error(),dlog((DLog *)0),
        callback((ChartboostCallback *)0),  runnableQueue() {
    }
    
    /**
     * \brief Gets the callback.
     * \return The callback.
     */
    ChartboostCallback *getCallback() {
        return callback;
    }
    
public:
    /** */
    virtual ~LuaLibChartboost() {
        if (callback != (ChartboostCallback *)0) {
            delete callback;
        }
    }
    
    /** */
    void setDLog(DLog *dlog_) {
        dlog = dlog_;
    }    
    
    /**
     * \brief Sets the callback.
     * \param callback_ The callback.
     */
    virtual void setCallback(ChartboostCallback *callback_);
    
    /**
     * \brief Initializes the library. Called while loading the Lua library.
     */
    virtual void init() = 0;
    
    /**
     * Caches an interstitial ad.
     *
     * \param location The location.
     */
    virtual void cacheInterstitial(const std::string &location) = 0;
    
    /**
     * Shows an interstitial ad.
     *
     * \param location The location.
     */
    virtual void showInterstitial(const std::string &location) = 0;
    
    /** */
    virtual void didCacheInterstitial(const char *location);
    
    /** */
    virtual void didFailToLoadInterstitial(const char *location);
    
    /** */
    virtual void didClickInterstitial(const char *location);
    
    /** */
    virtual void didCloseInterstitial(const char *location);
    
    /** */
    virtual void didDismissInterstitial(const char *location);
    
    /** */
    virtual void didDisplayInterstitial(const char *location);     
};
    
} // namespace
    
} // namespace

#endif //  AE_LUA_LIB_CHARTBOOST_H