#ifndef AE_LUA_LIB_VUNGLE_H
#define AE_LUA_LIB_VUNGLE_H

#include "Error.h"
#include "DLog.h"
#include "RunnableQueue.h"
#include "VungleCallback.h"

namespace ae {
    
namespace vungle {
  
/**
 * \brief The superclass for platform-specific implementations of the Vungle
 *   Lua library.
 */
class LuaLibVungle:public Error {
    /// The log.
    DLog *dlog;
    
    /// The Vungle callback.
    VungleCallback *callback;
    
    /// The queue for the events fired when no callback is set.
    RunnableQueue runnableQueue;    
    
    /// Indicates if there is a playable ad.
    bool isAdPlayable;
    
    /** */
    void trace(const char *msg);    
    
protected:
    /** */
    LuaLibVungle():Error(),dlog((DLog *)0),callback((VungleCallback *)0),
        runnableQueue(),isAdPlayable(false) {
    }
        
    /**
     * \brief Gets the callback.
     * \return The callback.
     */
    VungleCallback *getCallback() {
        return callback;
    }    
    
public:
    /** */
    virtual ~LuaLibVungle() {
    }
    
    /** */
    void setDLog(DLog *dlog_) {
        dlog = dlog_;
    }     
    
    /**
     * \brief Sets the callback.
     * \param callback_ The callback.
     */
    virtual void setCallback(VungleCallback *callback_);
    
    /**
     * \brief Initializes the library. Called right after having loaded the Lua
     *   library.
     */
    virtual void init() = 0;
    
    /**    
     * \brief Plays an ad.
     */
    virtual void playAd() = 0;
    
    /**
     * \brief Called when the ad playable state has changed.
     * \param isAdPlayable_ Indicates if there's a playable ad.
     */
    void adPlayableChanged(bool isAdPlayable_);
    
    /**
     * \brief Called when an ad is about to be shown.
     */
    void willShowAd();
    
    /**
     * \brief Called when an ad is about to be closed.
     */
    void willCloseAd();
    
    /**
     * \brief Called when an ad has been clicked.
     */
    void adClicked();    
    
    /**
     * \brief Called when the video can be considered a full view.
     */
    void viewCompleted();
};
    
} // namespace
    
} // namespace

#endif // AE_LUA_LIB_VUNGLE_H