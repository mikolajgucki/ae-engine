#include "Log.h"
#include "ae_lock.h"
#include "LuaLibAdColony.h"

using namespace std;

namespace ae {
    
namespace adcolony {
    
/** */
class AdColonyEvent:public Runnable {
protected:
    /** */
    LuaLibAdColony *luaLibAdColony;
    
    /** */
    AdColonyEvent(LuaLibAdColony *lib_):Runnable(),luaLibAdColony(lib_) {
    }
    
    /** */
    virtual ~AdColonyEvent() {
    }
};

/** */
class InterstitialAvailable:public AdColonyEvent {
    /** */
    const string zoneId;
    
public:
    /** */
    InterstitialAvailable(LuaLibAdColony *lib_,const string& zoneId_):
        AdColonyEvent(lib_),zoneId(zoneId_) {
    }
    
    /** */
    virtual bool run() {
        luaLibAdColony->interstitialAvailable(zoneId);
        return true;
    }
    
    /** */
    virtual ~InterstitialAvailable() {
    }
};

/** */
class InterstitialUnavailable:public AdColonyEvent {
    /** */
    const string zoneId;
    
public:
    /** */
    InterstitialUnavailable(LuaLibAdColony *lib_,const string& zoneId_):
        AdColonyEvent(lib_),zoneId(zoneId_) {
    }
    
    /** */
    virtual bool run() {
        luaLibAdColony->interstitialUnavailable(zoneId);
        return true;
    }
    
    /** */
    virtual ~InterstitialUnavailable() {
    }
};

/** */
class InterstitialOpened:public AdColonyEvent {
    /** */
    const string zoneId;
    
public:
    /** */
    InterstitialOpened(LuaLibAdColony *lib_,const string& zoneId_):
        AdColonyEvent(lib_),zoneId(zoneId_) {
    }
    
    /** */
    virtual bool run() {
        luaLibAdColony->interstitialOpened(zoneId);
        return true;
    }
    
    /** */
    virtual ~InterstitialOpened() {
    }
};

/** */
class InterstitialClosed:public AdColonyEvent {
    /** */
    const string zoneId;
    
public:
    /** */
    InterstitialClosed(LuaLibAdColony *lib_,const string& zoneId_):
        AdColonyEvent(lib_),zoneId(zoneId_) {
    }
    
    /** */
    virtual bool run() {
        luaLibAdColony->interstitialClosed(zoneId);
        return true;
    }
    
    /** */
    virtual ~InterstitialClosed() {
    }
};

/// The log tag.
static const char *logTag = "AE/AdColony";

/** */
void LuaLibAdColony::trace(const char *msg) {
    if (dlog != (DLog *)0) {
        dlog->trace(logTag,msg);
    }
    else {
        Log::trace(logTag,msg);
    }        
}

/** */
void LuaLibAdColony::setCallback(AdColonyCallback *callback_) {
    aeGlobalLock();
    if (callback != (AdColonyCallback *)0) {
        delete callback;
    }    
    callback = callback_;
    
    if (callback != (AdColonyCallback *)0) {
        if (runnableQueue.run() == false) {
            trace(runnableQueue.getError().c_str());
        }
    }
    aeGlobalUnlock();
}    
    
/** */
void LuaLibAdColony::interstitialAvailable(const string& zoneId) {
    aeGlobalLock();
// log
    ostringstream msg;
    msg << "interstitialAvailable(" << zoneId << ")";
    trace(msg.str().c_str());
    
// callback
    if (getCallback() != (AdColonyCallback *)0) {
        getCallback()->interstitialAvailable(zoneId);
    }
    else {
        trace("interstitialAvailable() called with null callback");
        runnableQueue.add(new InterstitialAvailable(this,zoneId));
    }
    aeGlobalUnlock();
}

/** */
void LuaLibAdColony::interstitialUnavailable(const string& zoneId) {
    aeGlobalLock();
// log
    ostringstream msg;
    msg << "interstitialUnavailable(" << zoneId << ")";
    trace(msg.str().c_str());
    
// callback
    if (getCallback() != (AdColonyCallback *)0) {
        getCallback()->interstitialUnavailable(zoneId);
    }
    else {
        trace("interstitialUnavailable() called with null callback");
        runnableQueue.add(new InterstitialUnavailable(this,zoneId));
    }
    aeGlobalUnlock();
}

/** */
void LuaLibAdColony::interstitialOpened(const string& zoneId) {
    aeGlobalLock();
// log
    ostringstream msg;
    msg << "interstitialOpened(" << zoneId << ")";
    trace(msg.str().c_str());
    
// callback
    if (getCallback() != (AdColonyCallback *)0) {
        getCallback()->interstitialOpened(zoneId);
    }
    else {
        trace("interstitialOpened() called with null callback");
        runnableQueue.add(new InterstitialOpened(this,zoneId));
    }
    aeGlobalUnlock();
}

/** */
void LuaLibAdColony::interstitialClosed(const string& zoneId) {
    aeGlobalLock();
// log
    ostringstream msg;
    msg << "interstitialClosed(" << zoneId << ")";
    trace(msg.str().c_str());
    
// callback
    if (getCallback() != (AdColonyCallback *)0) {
        getCallback()->interstitialClosed(zoneId);
    }
    else {
        trace("interstitialClosed() called with null callback");
        runnableQueue.add(new InterstitialClosed(this,zoneId));
    }
    aeGlobalUnlock();
}

} // namespace
    
} // namespace