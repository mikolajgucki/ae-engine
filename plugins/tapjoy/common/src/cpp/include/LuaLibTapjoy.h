#ifndef AE_LUA_LIB_TAPJOY_H
#define AE_LUA_LIB_TAPJOY_H

#include <string>
#include "DLog.h"
#include "RunnableQueue.h"
#include "TapjoyCallback.h"

namespace ae {
    
namespace tapjoy {
    
/**
 * \brief The base class for Tapjoy implementation of the Lua library.
 */
class LuaLibTapjoy {
    /// The log.
    DLog *dlog;
    
    /// The Tapjoy callback.
    TapjoyCallback *callback;
    
    /// The queue for the events fired when no callback is set.
    RunnableQueue runnableQueue;    
    
    /** */
    void trace(const char *msg);    
    
protected:
    /** */
    LuaLibTapjoy():dlog((DLog *)0),callback((TapjoyCallback *)0),
        runnableQueue() {
    }
    
    /**
     * \brief Gets the callback.
     * \return The callback.
     */
    TapjoyCallback *getCallback() {
        return callback;
    }    
    
public:
    /** */
    virtual ~LuaLibTapjoy() {
        if (callback != (TapjoyCallback *)0) {
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
    virtual void setCallback(TapjoyCallback *callback_);    
    
    /**
     * \brief Called when the connection has succeeded.
     */
    virtual void connectionSucceeded();
    
    /**
     * \brief Called when the connection has failed failed.
     */
    virtual void connectionFailed();    
    
    /**
     * \brief Called when request for content succeeded.
     * \param placement The placement name.    
     */
    virtual void requestSucceeded(const std::string& placement);
    
    /**
     * \brief Called when request for content failed.
     * \param placement The placement name.
     * \param error The eror.
     */
    virtual void requestFailed(const std::string& placement,
        const std::string& error);   
    
    /**
     * \brief Called when content is ready to be shown.
     * \param placement The placement name.
     */     
    virtual void contentIsReady(const std::string& placement);
    
    /**
     * \brief Called when content has been shown.
     * \param placement The placement name.
     */
    virtual void contentShown(const std::string& placement);
    
    /**
     * \brief Called when content has been dismissed.
     * \param placement The placement name.
     */
    virtual void contentDismissed(const std::string& placement);    
    
    /**
     * \brief Indicates if Tapjoy is connected to server.
     * \return <code>true</code> if connected, <code>false</code> otherwise.
     */
    virtual bool isConnected() = 0;
    
    /**
     * \brief Reqests content for a placement.
     * \param placement The placement.
     */
    virtual void requestContent(const std::string &placement) = 0;
  
    /**
     * \brief Checks if content is ready.
     * \param placement The placement.
     * \return <code>true</code> if ready, <code>false</code> otherwise.
     */
    virtual bool isContentReady(const std::string &placement) = 0;
    
    /**
     * \brief Shows content for a placement.
     * \param placement The placement.
     */
    virtual void showContent(const std::string &placement) = 0;
};
    
} // namespace
    
} // namespace

#endif // AE_LUA_LIB_TAPJOY_H