#ifndef AE_LUA_LIB_UNITY_ADS_H
#define AE_LUA_LIB_UNITY_ADS_H

#include "Error.h"
#include "DLog.h"
#include "RunnableQueue.h"
#include "UnityAdsCallback.h"

namespace ae {
    
namespace unityads {
    
/**
 * \brief The superclass for platform-specific implementation of the Unity Ads
 *   Lua library.
 */
class LuaLibUnityAds:public Error {
    /// The log.
    DLog *dlog;
    
    /// The UnityAds callback.
    UnityAdsCallback *callback;
    
    /// The queue for the events fired when no callback is set.
    RunnableQueue runnableQueue;     
    
    /** */
    void trace(const char *msg);  
    
protected:
    /** */
    LuaLibUnityAds():Error(),dlog((DLog *)0),callback((UnityAdsCallback *)0),
        runnableQueue() {
    }
    
    /**
     * \brief Gets the callback.
     * \return The callback.
     */
    UnityAdsCallback *getCallback() {
        return callback;
    }  
    
public:
    /** */
    virtual ~LuaLibUnityAds() {
        if (callback != (UnityAdsCallback *)0) {
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
    void setCallback(UnityAdsCallback *callback_);
    
    /**
     * \brief Checks if an advert for the default placement is ready.
     * \return <code>true</code> if ready, <code>false</code> otherwise.
     */
    virtual bool isReady() = 0;
    
    /**
     * \brief Checks if an advert is ready to be shown.
     * \param placementId The placement identifier.
     * \return <code>true</code> if ready, <code>false</code> otherwise.
     */
    virtual bool isReady(const std::string &placementId) = 0;
    
    /** 
     * \brief Shows an advert for the default placement.
     */
    virtual void show() = 0;
    
    /**
     * \brief Shows an advert.
     * \param placementId The placement identifier.
     */
    virtual void show(const std::string &placementId) = 0;
    
    /** */
    void setReady(const std::string &placementId);
    
    /** */
    void started(const std::string &placementId);
    
    /** */
    void failed(const std::string &error,const std::string &msg);
    
    /** */
    void finished(const std::string &placementId,
        UnityAdsCallback::FinishState state);       
};
    
} // namespace
    
} // namespace

#endif // AE_LUA_LIB_UNITY_ADS_H