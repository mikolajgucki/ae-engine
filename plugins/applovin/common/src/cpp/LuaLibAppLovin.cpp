#include "Log.h"
#include "ae_lock.h"
#include "LuaLibAppLovin.h"

namespace ae {
    
namespace applovin {
    
/** */
class AppLovinEvent:public Runnable {
protected:
    /** */
    LuaLibAppLovin *luaLibAppLovin;
    
    /** */
    AppLovinEvent(LuaLibAppLovin *lib_):Runnable(),luaLibAppLovin(lib_) {
    }
    
    /** */
    virtual ~AppLovinEvent() {
    }
};

/** */
class InterstitialAdLoaded:public AppLovinEvent {
public:
    /** */
    InterstitialAdLoaded(LuaLibAppLovin *lib_):AppLovinEvent(lib_) {
    }
    
    /** */
    virtual bool run() {
        luaLibAppLovin->interstitialAdLoaded();
        return true;
    }
    
    /** */
    virtual ~InterstitialAdLoaded() {
    }
};

/** */
class InterstitialAdNoFill:public AppLovinEvent {
public:
    /** */
    InterstitialAdNoFill(LuaLibAppLovin *lib_):AppLovinEvent(lib_) {
    }
    
    /** */
    virtual bool run() {
        luaLibAppLovin->interstitialAdNoFill();
        return true;
    }
    
    /** */
    virtual ~InterstitialAdNoFill() {
    }
};

/** */
class InterstitialAdFailed:public AppLovinEvent {
    /** */
    int errorCode;
    
public:
    /** */
    InterstitialAdFailed(LuaLibAppLovin *lib_,int errorCode_):
        AppLovinEvent(lib_),errorCode(errorCode_) {
    }
    
    /** */
    virtual bool run() {
        luaLibAppLovin->interstitialAdFailed(errorCode);
        return true;
    }
    
    /** */
    virtual ~InterstitialAdFailed() {
    }
};    

/** */
class InterstitialAdDisplayed:public AppLovinEvent {
public:
    /** */
    InterstitialAdDisplayed(LuaLibAppLovin *lib_):AppLovinEvent(lib_) {
    }
    
    /** */
    virtual bool run() {
        luaLibAppLovin->interstitialAdDisplayed();
        return true;
    }
    
    /** */
    virtual ~InterstitialAdDisplayed() {
    }
};

/** */
class InterstitialAdHidden:public AppLovinEvent {
public:
    /** */
    InterstitialAdHidden(LuaLibAppLovin *lib_):AppLovinEvent(lib_) {
    }
    
    /** */
    virtual bool run() {
        luaLibAppLovin->interstitialAdHidden();
        return true;
    }
    
    /** */
    virtual ~InterstitialAdHidden() {
    }
};

/** */
class InterstitialAdClicked:public AppLovinEvent {
public:
    /** */
    InterstitialAdClicked(LuaLibAppLovin *lib_):AppLovinEvent(lib_) {
    }
    
    /** */
    virtual bool run() {
        luaLibAppLovin->interstitialAdClicked();
        return true;
    }
    
    /** */
    virtual ~InterstitialAdClicked() {
    }
};

/// The log tag
static const char *logTag = "AE/AppLovin";

/** */
void LuaLibAppLovin::trace(const char *msg) {
    if (dlog != (DLog *)0) {
        dlog->trace(logTag,msg);
    }
    else {
        Log::trace(logTag,msg);
    }        
}

/** */
void LuaLibAppLovin::setCallback(AppLovinCallback *callback_) {
    aeGlobalLock();
    if (callback != (AppLovinCallback *)0) {
        delete callback;
    }    
    callback = callback_;
    
    if (callback != (AppLovinCallback *)0) {
        if (runnableQueue.run() == false) {
            Log::error(logTag,runnableQueue.getError());
        }
    }
    aeGlobalUnlock();
}

/** */
void LuaLibAppLovin::interstitialAdLoaded() {
    aeGlobalLock();
    trace("interstitialAdLoaded()");    
    if (getCallback() != (AppLovinCallback *)0) {
        getCallback()->interstitialAdLoaded();
    }
    else {
        trace("interstitialAdLoaded() called with null callback");
        runnableQueue.add(new InterstitialAdLoaded(this));
    }
    aeGlobalUnlock();
}

/** */
void LuaLibAppLovin::interstitialAdNoFill() {
    aeGlobalLock();
    trace("interstitialAdNoFill()");
    if (getCallback() != (AppLovinCallback *)0) {
        getCallback()->interstitialAdNoFill();
    }
    else {
        trace("interstitialAdNoFill() called with null callback");
        runnableQueue.add(new InterstitialAdNoFill(this));
    }
    aeGlobalUnlock();
}

/** */
void LuaLibAppLovin::interstitialAdFailed(int errorCode) {
    aeGlobalLock();
    trace("interstitialAdFailed()");    
    if (getCallback() != (AppLovinCallback *)0) {    
        getCallback()->interstitialAdFailed(errorCode);
    }
    else {
        trace("interstitialAdFailed() called with null callback");
        runnableQueue.add(new InterstitialAdFailed(this,errorCode));
    }
    aeGlobalUnlock();
}

/** */
void LuaLibAppLovin::interstitialAdDisplayed() {
    aeGlobalLock();
    trace("interstitialAdDisplayed()");    
    if (getCallback() != (AppLovinCallback *)0) {    
        getCallback()->interstitialAdDisplayed();
    }
    else {
        trace("interstitialAdDisplayed() called with null callback");
        runnableQueue.add(new InterstitialAdDisplayed(this));
    }
    aeGlobalUnlock();
}

/** */
void LuaLibAppLovin::interstitialAdHidden() {
    aeGlobalLock();
    trace("interstitialAdHidden()");
    if (getCallback() != (AppLovinCallback *)0) {
        getCallback()->interstitialAdHidden();
    }
    else {
        trace("interstitialAdHidden() called with null callback");
        runnableQueue.add(new InterstitialAdHidden(this));
    }
    aeGlobalUnlock();
}

/** */
void LuaLibAppLovin::interstitialAdClicked() {
    aeGlobalLock();
    trace("interstitialAdClicked()");
    if (getCallback() != (AppLovinCallback *)0) {
        getCallback()->interstitialAdClicked();
    }
    else {
        trace("interstitialAdClicked() called with null callback");
        runnableQueue.add(new InterstitialAdClicked(this));
    }
    aeGlobalUnlock();
}
    
} // namespace
    
} // namespace