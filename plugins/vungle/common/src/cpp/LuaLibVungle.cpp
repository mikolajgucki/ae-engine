#include <sstream>

#include "Log.h"
#include "ae_lock.h"
#include "LuaLibVungle.h"

using namespace std;

namespace ae {
    
namespace vungle {
    
/** */
class VungleEvent:public Runnable {
protected:
    /** */
    LuaLibVungle *luaLibVungle;
    
    /** */
    VungleEvent(LuaLibVungle *lib_):Runnable(),luaLibVungle(lib_) {
    }
    
    /** */
    virtual ~VungleEvent() {
    }
};

/** */
class WillShowAd:public VungleEvent {
public:
    /** */
    WillShowAd(LuaLibVungle *lib_):VungleEvent(lib_) {
    }
    
    /** */
    virtual bool run() {
        luaLibVungle->willShowAd();
        return true;
    }
    
    /** */
    virtual ~WillShowAd() {
    }
};

/** */
class WillCloseAd:public VungleEvent {
public:
    /** */
    WillCloseAd(LuaLibVungle *lib_):VungleEvent(lib_) {
    }
    
    /** */
    virtual bool run() {
        luaLibVungle->willCloseAd();
        return true;
    }
    
    /** */
    virtual ~WillCloseAd() {
    }
};
    
/** */
class AdClicked:public VungleEvent {
public:
    /** */
    AdClicked(LuaLibVungle *lib_):VungleEvent(lib_) {
    }
    
    /** */
    virtual bool run() {
        luaLibVungle->adClicked();
        return true;
    }
    
    /** */
    virtual ~AdClicked() {
    }
};

/** */
class ViewCompleted:public VungleEvent {
public:
    /** */
    ViewCompleted(LuaLibVungle *lib_):VungleEvent(lib_) {
    }
    
    /** */
    virtual bool run() {
        luaLibVungle->viewCompleted();
        return true;
    }
    
    /** */
    virtual ~ViewCompleted() {
    }
};

/// The log tag.
static const char *logTag = "AE/Vungle";
    
/** */
void LuaLibVungle::trace(const char *msg) {
    if (dlog != (DLog *)0) {
        dlog->trace(logTag,msg);
    }
    else {
        Log::trace(logTag,msg);
    }        
}

/** */
void LuaLibVungle::setCallback(VungleCallback *callback_) {
    aeGlobalLock();
    if (callback != (VungleCallback *)0) {
        delete callback;
    }
    callback = callback_;
    adPlayableChanged(isAdPlayable);
    
    if (callback != (VungleCallback *)0) {
        if (runnableQueue.run() == false) {
            trace(runnableQueue.getError().c_str());
        }
    }    
    aeGlobalUnlock();    
}
    
/** */
void LuaLibVungle::adPlayableChanged(bool isAdPlayable_) {
    aeGlobalLock();    
    isAdPlayable = isAdPlayable_;
    
// log
    ostringstream msg;
    msg << "adPlayableChanged(" << isAdPlayable_ << ")";
    trace(msg.str().c_str());
   
// callback
    if (getCallback() != (VungleCallback *)0) {
        getCallback()->adPlayableChanged(isAdPlayable);
    }
    else {
        trace("adPlayableChanged() called with null callback");
    }
    aeGlobalUnlock();
}

/** */
void LuaLibVungle::willShowAd() {
    aeGlobalLock();
    trace("willShowAd()");
    if (getCallback() != (VungleCallback *)0) {
        getCallback()->willShowAd();
    }
    else {
        trace("willShowAd() called with null callback");
        runnableQueue.add(new WillShowAd(this));
    }
    aeGlobalUnlock();
}

/** */
void LuaLibVungle::willCloseAd() {
    aeGlobalLock();
    trace("willCloseAd()");
    if (getCallback() != (VungleCallback *)0) {
        getCallback()->willCloseAd();
    }
    else {
        trace("willCloseAd() called with null callback");
        runnableQueue.add(new WillCloseAd(this));
    }
    aeGlobalUnlock();
}

/** */
void LuaLibVungle::adClicked() {
    aeGlobalLock();
    trace("adClicked()");
    if (getCallback() != (VungleCallback *)0) {
        getCallback()->adClicked();
    }
    else {
        trace("adClicked() called with null callback");
        runnableQueue.add(new AdClicked(this));
    }
    aeGlobalUnlock();
}

/** */
void LuaLibVungle::viewCompleted() {
    aeGlobalLock();
    trace("viewCompleted()");
    if (getCallback() != (VungleCallback *)0) {
        getCallback()->viewCompleted();
    }
    else {
        trace("viewCompleted() called with null callback");
        runnableQueue.add(new ViewCompleted(this));
    }
    aeGlobalUnlock();
}
    
} // namespace
    
} // namespace
