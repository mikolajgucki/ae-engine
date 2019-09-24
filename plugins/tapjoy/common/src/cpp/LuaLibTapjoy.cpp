#include "Log.h"
#include "ae_lock.h"
#include "LuaLibTapjoy.h"

using namespace std;

namespace ae {
    
namespace tapjoy {
    
/** */
class TapjoyEvent:public Runnable {
protected:
    /** */
    LuaLibTapjoy *luaLibTapjoy;
    
public:
    /** */
    TapjoyEvent(LuaLibTapjoy *lib_):Runnable(),
        luaLibTapjoy(lib_) {
    }
    
    /** */
    virtual ~TapjoyEvent() {
    }
};

/** */
class ConnectionSucceeded:public TapjoyEvent {
public:
    /** */
    ConnectionSucceeded(LuaLibTapjoy *lib_):TapjoyEvent(lib_) {
    }
    
    /** */
    virtual bool run() {
        luaLibTapjoy->connectionSucceeded();
        return true;
    }
};

/** */
class ConnectionFailed:public TapjoyEvent {
public:
    /** */
    ConnectionFailed(LuaLibTapjoy *lib_):TapjoyEvent(lib_) {
    }
    
    /** */
    virtual bool run() {
        luaLibTapjoy->connectionFailed();
        return true;
    }
};

/** */
class RequestSucceeded:public TapjoyEvent {
    /** */
    const string placement;
    
public:
    /** */
    RequestSucceeded(LuaLibTapjoy *lib_,const string &placement_):
        TapjoyEvent(lib_),placement(placement_) {
    }
    
    /** */
    virtual bool run() {
        luaLibTapjoy->requestSucceeded(placement);
        return true;
    }
};

/** */
class RequestFailed:public TapjoyEvent {
    /** */
    const string placement;
    
    /** */
    const string error;
    
public:
    /** */
    RequestFailed(LuaLibTapjoy *lib_,const string &placement_,
        const string &error_):TapjoyEvent(lib_),placement(placement_),
        error(error_) {
    }
    
    /** */
    virtual bool run() {
        luaLibTapjoy->requestFailed(placement,error);
        return true;
    }
};

/** */
class ContentIsReady:public TapjoyEvent {
    /** */
    const string placement;
    
public:
    /** */
    ContentIsReady(LuaLibTapjoy *lib_,const string &placement_):
        TapjoyEvent(lib_),placement(placement_) {
    }
    
    /** */
    virtual bool run() {
        luaLibTapjoy->contentIsReady(placement);
        return true;
    }
};

/** */
class ContentShown:public TapjoyEvent {
    /** */
    const string placement;
    
public:
    /** */
    ContentShown(LuaLibTapjoy *lib_,const string &placement_):
        TapjoyEvent(lib_),placement(placement_) {
    }
    
    /** */
    virtual bool run() {
        luaLibTapjoy->contentShown(placement);
        return true;
    }
};

/** */
class ContentDismissed:public TapjoyEvent {
    /** */
    const string placement;
    
public:
    /** */
    ContentDismissed(LuaLibTapjoy *lib_,const string &placement_):
        TapjoyEvent(lib_),placement(placement_) {
    }
    
    /** */
    virtual bool run() {
        luaLibTapjoy->contentDismissed(placement);
        return true;
    }
};

/// The log tag.
static const char *logTag = "AE/Tapjoy";
    
/** */
void LuaLibTapjoy::trace(const char *msg) {
    if (dlog != (DLog *)0) {
        dlog->trace(logTag,msg);
    }
    else {
        Log::trace(logTag,msg);
    }        
}

/** */
void LuaLibTapjoy::setCallback(TapjoyCallback *callback_) {
    aeGlobalLock();
    if (callback != (TapjoyCallback *)0) {
        delete callback;
    }    
    callback = callback_;
    
    if (callback != (TapjoyCallback *)0) {
        if (runnableQueue.run() == false) {
            trace(runnableQueue.getError().c_str());
        }
    }
    aeGlobalUnlock();
}

/** */
void LuaLibTapjoy::connectionSucceeded() {
    aeGlobalLock();
// log
    trace("connectionSucceeded()");
    
// callback
    if (getCallback() != (TapjoyCallback *)0) {
        getCallback()->connectionSucceeded();
    }
    else {
        trace("connectionSucceeded() called with null callback");
        runnableQueue.add(new ConnectionSucceeded(this));
    }
    aeGlobalUnlock();    
}

/** */
void LuaLibTapjoy::connectionFailed() {
    aeGlobalLock();
// log
    trace("connectionFailed()");
    
// callback
    if (getCallback() != (TapjoyCallback *)0) {
        getCallback()->connectionFailed();
    }
    else {
        trace("connectionFailed() called with null callback");
        runnableQueue.add(new ConnectionFailed(this));
    }
    aeGlobalUnlock();     
}
    
/** */
void LuaLibTapjoy::requestSucceeded(const string& placement) {
    aeGlobalLock();
// log
    trace("requestSucceeded()");
    
// callback
    if (getCallback() != (TapjoyCallback *)0) {
        getCallback()->requestSucceeded(placement);
    }
    else {
        trace("requestSucceeded() called with null callback");
        runnableQueue.add(new RequestSucceeded(this,placement));
    }
    aeGlobalUnlock();     
}

/** */
void LuaLibTapjoy::requestFailed(const string& placement,const string& error) {
    aeGlobalLock();
// log
    trace("requestFailed()");
    
// callback
    if (getCallback() != (TapjoyCallback *)0) {
        getCallback()->requestFailed(placement,error);
    }
    else {
        trace("requestFailed() called with null callback");
        runnableQueue.add(new RequestFailed(this,placement,error));
    }
    aeGlobalUnlock();     
}

/** */
void LuaLibTapjoy::contentIsReady(const string& placement) {
    aeGlobalLock();
// log
    trace("contentIsReady()");
    
// callback
    if (getCallback() != (TapjoyCallback *)0) {
        getCallback()->contentIsReady(placement);
    }
    else {
        trace("contentIsReady() called with null callback");
        runnableQueue.add(new ContentIsReady(this,placement));
    }
    aeGlobalUnlock();     
}

/** */
void LuaLibTapjoy::contentShown(const string& placement) {
    aeGlobalLock();
// log
    trace("contentShown()");
    
// callback
    if (getCallback() != (TapjoyCallback *)0) {
        getCallback()->contentShown(placement);
    }
    else {
        trace("contentShown() called with null callback");
        runnableQueue.add(new ContentShown(this,placement));
    }
    aeGlobalUnlock();     
}

/** */
void LuaLibTapjoy::contentDismissed(const string& placement) {
    aeGlobalLock();
// log
    trace("contentDismissed()");
    
// callback
    if (getCallback() != (TapjoyCallback *)0) {
        getCallback()->contentDismissed(placement);
    }
    else {
        trace("contentDismissed() called with null callback");
        runnableQueue.add(new ContentDismissed(this,placement));
    }
    aeGlobalUnlock();     
}

} // namespace
    
} // namespace