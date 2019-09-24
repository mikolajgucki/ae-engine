#ifndef AE_LUA_LIB_AD_COLONY_H
#define AE_LUA_LIB_AD_COLONY_H

#include <string>
#include <vector>

#include "Error.h"
#include "DLog.h"
#include "RunnableQueue.h"
#include "AdColonyCallback.h"

namespace ae {
    
namespace adcolony {
 
/**
 * \brief The superclass for platform-specific implementations of the AdColony
 *   Lua library.
 */
class LuaLibAdColony:public Error {
    /// The log.
    DLog *dlog;
    
    /// The AdColony callback.
    AdColonyCallback *callback;
    
    /// The queue for the events fired when no callback is set.
    RunnableQueue runnableQueue;    
    
    /** */
    void trace(const char *msg);
    
protected:
    /** */
    LuaLibAdColony():Error(),dlog((DLog *)0),callback((AdColonyCallback *)0),
        runnableQueue() {
    }
    
    /**
     * \brief Gets the callback.
     * \return The callback.
     */
    AdColonyCallback *getCallback() {
        return callback;
    }    
    
public:        
    /** */
    virtual ~LuaLibAdColony() {
        if (callback != (AdColonyCallback *)0) {
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
    void setCallback(AdColonyCallback *callback_);
    
    /**
     * \brief Requests to load an interstitial video.
     * \param zoneId The zone identifier.
     */
    virtual void requestInterstitial(const std::string &zoneId) = 0;
    
    /**
     * \brief Checks if an interstitial is expired.
     * \param zoneId The zone identifier.
     * \return <code>true</code> if expired, <code>false</code> otherwise.
     */
    virtual bool isInterstitialExpired(const std::string &zoneId) = 0;
    
    /**
     * \brief Shows an interstitial video.
     * \param zoneId The zone identifier.
     */
    virtual void showInterstitial(const std::string &zoneId) = 0;
    
    /**
     * \brief Called when an interstitial is available.
     * \param zoneId The zone identifier.
     */
    void interstitialAvailable(const std::string &zoneId);
    
    /**
     * \brief Called when an interstitial is unavailable.
     * \param zoneId The zone identifier.
     */
    void interstitialUnavailable(const std::string &zoneId);
    
    /**
     * \brief Called when an interstitial has been opened.
     * \param zoneId The zone identifier.
     */
    void interstitialOpened(const std::string &zoneId);
    
    /**
     * \brief Called when an interstitial has been closed.
     * \param zoneId The zone identifier.
     */
    void interstitialClosed(const std::string &zoneId);
};
    
} // namespace
    
} // namespace

#endif // AE_LUA_LIB_AD_COLONY_H