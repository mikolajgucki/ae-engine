#include <string>

#include "Log.h"
#include "ae_lock.h"
#include "LuaLibChartboost.h"

using namespace std;

namespace ae {
    
namespace chartboost {
    
/** */
class ChartboostEvent:public Runnable {
protected:
    /** */
    LuaLibChartboost *luaLibChartboost;
    
    /** */
    const string location;
    
    /** */
    ChartboostEvent(LuaLibChartboost *lib_,const string& location_):
        Runnable(),luaLibChartboost(lib_),location(location_) {
    }
    
    /** */
    virtual ~ChartboostEvent() {
    }
};

/** */
class DidCacheInterstitial:public ChartboostEvent {
public:
    /** */
    DidCacheInterstitial(LuaLibChartboost *lib_,const string& location_):
        ChartboostEvent(lib_,location_) {
    }
    
    /** */
    virtual bool run() {
        luaLibChartboost->didCacheInterstitial(location.c_str());
        return true;
    }
    
    /** */
    virtual ~DidCacheInterstitial() {
    }
};

/** */
class DidFailToLoadInterstitial:public ChartboostEvent {
public:
    /** */
    DidFailToLoadInterstitial(LuaLibChartboost *lib_,const string& location_):
        ChartboostEvent(lib_,location_) {
    }
    
    /** */
    virtual bool run() {
        luaLibChartboost->didFailToLoadInterstitial(location.c_str());
        return true;
    }
    
    /** */
    virtual ~DidFailToLoadInterstitial() {
    }
};

/** */
class DidClickInterstitial:public ChartboostEvent {
public:
    /** */
    DidClickInterstitial(LuaLibChartboost *lib_,const string& location_):
        ChartboostEvent(lib_,location_) {
    }
    
    /** */
    virtual bool run() {
        luaLibChartboost->didClickInterstitial(location.c_str());
        return true;
    }
    
    /** */
    virtual ~DidClickInterstitial() {
    }
};

/** */
class DidCloseInterstitial:public ChartboostEvent {
public:
    /** */
    DidCloseInterstitial(LuaLibChartboost *lib_,const string& location_):
        ChartboostEvent(lib_,location_) {
    }
    
    /** */
    virtual bool run() {
        luaLibChartboost->didCloseInterstitial(location.c_str());
        return true;
    }
    
    /** */
    virtual ~DidCloseInterstitial() {
    }
};

/** */
class DidDismissInterstitial:public ChartboostEvent {
public:
    /** */
    DidDismissInterstitial(LuaLibChartboost *lib_,const string& location_):
        ChartboostEvent(lib_,location_) {
    }
    
    /** */
    virtual bool run() {
        luaLibChartboost->didDismissInterstitial(location.c_str());
        return true;
    }
    
    /** */
    virtual ~DidDismissInterstitial() {
    }
};

/** */
class DidDisplayInterstitial:public ChartboostEvent {
public:
    /** */
    DidDisplayInterstitial(LuaLibChartboost *lib_,const string& location_):
        ChartboostEvent(lib_,location_) {
    }
    
    /** */
    virtual bool run() {
        luaLibChartboost->didDisplayInterstitial(location.c_str());
        return true;
    }
    
    /** */
    virtual ~DidDisplayInterstitial() {
    }
};

/// The log cat.
static const char *logTag = "AE/Chartboost";

/** */
void LuaLibChartboost::trace(const char *msg) {
    if (dlog != (DLog *)0) {
        dlog->trace(logTag,msg);
    }
    else {
        Log::trace(logTag,msg);
    }        
}

/** */
void LuaLibChartboost::setCallback(ChartboostCallback *callback_) {
    aeGlobalLock();
    if (callback != (ChartboostCallback *)0) {
        delete callback;
    }
    callback = callback_;
    
    if (callback != (ChartboostCallback *)0) {
        if (runnableQueue.run() == false) {
            Log::error(logTag,runnableQueue.getError());
        }
    }
    aeGlobalUnlock();
}

/** */
void LuaLibChartboost::didCacheInterstitial(const char *location) {
    aeGlobalLock();
    trace("didCacheInterstitial()");
    if (getCallback() != (ChartboostCallback *)0) {
        getCallback()->didCacheInterstitial(location);
    }
    else {
        trace("didCacheInterstitial() called with null callback");
        runnableQueue.add(new DidCacheInterstitial(this,location));
    }
    aeGlobalUnlock();
}

/** */
void LuaLibChartboost::didFailToLoadInterstitial(const char *location) {
    aeGlobalLock();
    trace("didFailToLoadInterstitial()");
    if (getCallback() != (ChartboostCallback *)0) {
        getCallback()->didFailToLoadInterstitial(location);
    }
    else {
        trace(
            "didFailToLoadInterstitial() called with null callback");
        runnableQueue.add(new DidFailToLoadInterstitial(this,location));
    }
    aeGlobalUnlock();
}

/** */
void LuaLibChartboost::didClickInterstitial(const char *location) {
    aeGlobalLock();
    trace("didClickInterstitial()");
    if (getCallback() != (ChartboostCallback *)0) {
        getCallback()->didClickInterstitial(location);
    }
    else {
        trace("didClickInterstitial() called with null callback");
        runnableQueue.add(new DidClickInterstitial(this,location));
    }
    aeGlobalUnlock();
}

/** */
void LuaLibChartboost::didCloseInterstitial(const char *location) {
    aeGlobalLock();
    trace("didCloseInterstitial()");
    if (getCallback() != (ChartboostCallback *)0) {
        getCallback()->didCloseInterstitial(location);
    }
    else {
        trace("didCloseInterstitial() called with null callback");
        runnableQueue.add(new DidCloseInterstitial(this,location));
    }
    aeGlobalUnlock();
}

/** */
void LuaLibChartboost::didDismissInterstitial(const char *location) {
    aeGlobalLock();
    trace("didDismissInterstitial()");
    if (getCallback() != (ChartboostCallback *)0) {
        getCallback()->didDismissInterstitial(location);
    }
    else {
        trace("didDismissInterstitial() called with null callback");
        runnableQueue.add(new DidDismissInterstitial(this,location));
    }
    aeGlobalUnlock();
}

/** */
void LuaLibChartboost::didDisplayInterstitial(const char *location) {
    aeGlobalLock();
    trace("didDisplayInterstitial()");
    if (getCallback() != (ChartboostCallback *)0) {
        getCallback()->didDisplayInterstitial(location);
    }
    else {
        trace("didDisplayInterstitial() called with null callback");
        runnableQueue.add(new DidDisplayInterstitial(this,location));
    }
    aeGlobalUnlock();
}
    
} // namespace
    
} // namespace