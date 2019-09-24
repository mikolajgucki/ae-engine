#include <string>

#include "Log.h"
#include "ae_lock.h"
#include "LuaLibIAP.h"

using namespace std;

namespace ae {
    
namespace iap {
    
/** */
class IAPEvent:public Runnable {
protected:
    /** */
    LuaLibIAP *luaLibIap;
    
    /** */
    IAPEvent(LuaLibIAP *lib_):Runnable(),luaLibIap(lib_) {
    }
    
    /** */
    virtual ~IAPEvent() {
    }
};

/** */
class Loaded:public IAPEvent {
public:
    /** */
    Loaded(LuaLibIAP *lib_):IAPEvent(lib_) {
    }
    
    /** */
    virtual bool run() {
        luaLibIap->loaded();
        return true;
    }
    
    /** */
    virtual ~Loaded() {
    }
};

/** */
class Started:public IAPEvent {
public:
    /** */
    Started(LuaLibIAP *lib_):IAPEvent(lib_) {
    }
    
    /** */
    virtual bool run() {
        luaLibIap->started();
        return true;
    }
    
    /** */
    virtual ~Started() {
    }
};
    
/** */
class FailedToStart:public IAPEvent {
public:
    /** */
    FailedToStart(LuaLibIAP *lib_):IAPEvent(lib_) {
    }
    
    /** */
    virtual bool run() {
        luaLibIap->failedToStart();
        return true;
    }
    
    /** */
    virtual ~FailedToStart() {
    }
};
    
/** */
class Purchasing:public IAPEvent {
    /** */
    const string productId;
    
public:
    /** */
    Purchasing(LuaLibIAP *lib_,const string productId_):IAPEvent(lib_),
        productId(productId_) {
    }
    
    /** */
    virtual bool run() {
        luaLibIap->purchasing(productId.c_str());
        return true;
    }
    
    /** */
    virtual ~Purchasing() {
    }
};

/** */
class Deferred:public IAPEvent {
    /** */
    const string productId;
    
public:
    /** */
    Deferred(LuaLibIAP *lib_,const string productId_):IAPEvent(lib_),
        productId(productId_) {
    }
    
    /** */
    virtual bool run() {
        luaLibIap->deferred(productId.c_str());
        return true;
    }
    
    /** */
    virtual ~Deferred() {
    }
};

/** */
class Purchased:public IAPEvent {
    /** */
    const string productId;
    
public:
    /** */
    Purchased(LuaLibIAP *lib_,const string productId_):IAPEvent(lib_),
        productId(productId_) {
    }
    
    /** */
    virtual bool run() {
        luaLibIap->purchased(productId.c_str());
        return true;
    }
    
    /** */
    virtual ~Purchased() {
    }
};
    
/** */
class PurchaseFailed:public IAPEvent {
    /** */
    const string reason;
    
public:
    /** */
    PurchaseFailed(LuaLibIAP *lib_,const string reason_):IAPEvent(lib_),
        reason(reason_) {
    }
    
    /** */
    virtual bool run() {
        luaLibIap->purchaseFailed(reason.c_str());
        return true;
    }
    
    /** */
    virtual ~PurchaseFailed() {
    }
};
    
/** */
class PurchaseCanceled:public IAPEvent {
public:
    /** */
    PurchaseCanceled(LuaLibIAP *lib_):IAPEvent(lib_) {
    }
    
    /** */
    virtual bool run() {
        luaLibIap->purchaseCanceled();
        return true;
    }
    
    /** */
    virtual ~PurchaseCanceled() {
    }
};

/** */
class AlreadyOwned:public IAPEvent {
    /** */
    const string productId;
    
public:
    /** */
    AlreadyOwned(LuaLibIAP *lib_,const string productId_):IAPEvent(lib_),
        productId(productId_) {
    }
    
    /** */
    virtual bool run() {
        luaLibIap->alreadyOwned(productId.c_str());
        return true;
    }
    
    /** */
    virtual ~AlreadyOwned() {
    }
};

/** */
class PurchasesRestored:public IAPEvent {
public:
    /** */
    PurchasesRestored(LuaLibIAP *lib_):IAPEvent(lib_) {
    }
    
    /** */
    virtual bool run() {
        luaLibIap->purchasesRestored();
        return true;
    }
    
    /** */
    virtual ~PurchasesRestored() {
    }
};

/** */
class RestorePurchasesFailed:public IAPEvent {
    /** */
    const string reason;
    
public:
    /** */
    RestorePurchasesFailed(LuaLibIAP *lib_,const string reason_):IAPEvent(lib_),
        reason(reason_) {
    }
    
    /** */
    virtual bool run() {
        luaLibIap->restorePurchasesFailed(reason.c_str());
        return true;
    }
    
    /** */
    virtual ~RestorePurchasesFailed() {
    }
};

/// The log tag.
static const char *logTag = "AE/IAP";
    
/** */
void LuaLibIAP::trace(const char *msg) {
    if (dlog != (DLog *)0) {
        dlog->trace(logTag,msg);
    }
    else {
        Log::trace(logTag,msg);
    }        
}

/** */
void LuaLibIAP::setCallback(IAPCallback *callback_) {
    aeGlobalLock();
    if (callback != (IAPCallback *)0) {
        delete callback;
    }
    callback = callback_;
    
    if (callback != (IAPCallback *)0) {
        if (runnableQueue.run() == false) {
            trace(runnableQueue.getError().c_str());
        }
    }    
    aeGlobalUnlock();
}

/** */
void LuaLibIAP::loaded() {
    aeGlobalLock();
    trace("loaded()");
    if (getCallback() != (IAPCallback *)0) {
        getCallback()->loaded();
    }
    else {
        trace("loaded() called with null callback");
        runnableQueue.add(new Loaded(this));
    }
    aeGlobalUnlock();
}

/** */
void LuaLibIAP::started() {
    aeGlobalLock();
    trace("started");
    if (getCallback() != (IAPCallback *)0) {
        getCallback()->started();
    }
    else {
        trace("started() called with null callback");
        runnableQueue.add(new Started(this));
    }
    aeGlobalUnlock();
}

/** */
void LuaLibIAP::failedToStart() {
    aeGlobalLock();
    trace("failedToStart()");
    if (getCallback() != (IAPCallback *)0) {
        getCallback()->failedToStart();
    }
    else {
        trace("failedToStart() called with null callback");
        runnableQueue.add(new FailedToStart(this));
    }
    aeGlobalUnlock();
}

/** */
void LuaLibIAP::purchasing(const char *productId) {
    aeGlobalLock();
    
// log
    ostringstream msg;
    msg << "purchasing(" << productId << ")";
    trace(msg.str().c_str());
    
// callback
    if (getCallback() != (IAPCallback *)0) {
        getCallback()->purchasing(productId);
    }
    else {
        trace("purchasing() called with null callback");
        runnableQueue.add(new Purchased(this,productId));
    }
    aeGlobalUnlock();
}

/** */
void LuaLibIAP::deferred(const char *productId) {
    aeGlobalLock();
    
// log
    ostringstream msg;
    msg << "deferred(" << productId << ")";
    trace(msg.str().c_str());
    
// callback
    if (getCallback() != (IAPCallback *)0) {
        getCallback()->deferred(productId);
    }
    else {
        trace("deferred() called with null callback");
        runnableQueue.add(new Deferred(this,productId));
    }
    aeGlobalUnlock();
}

/** */
void LuaLibIAP::purchased(const char *productId) {
    aeGlobalLock();
    
// log
    ostringstream msg;
    msg << "purchased(" << productId << ")";
    trace(msg.str().c_str());
    
// callback
    if (getCallback() != (IAPCallback *)0) {
        getCallback()->purchased(productId);
    }
    else {
        trace("purchased() called with null callback");
        runnableQueue.add(new Purchased(this,productId));
    }
    aeGlobalUnlock();
}

/** */
void LuaLibIAP::purchaseFailed(const char *reason) {
    aeGlobalLock();
    
// log
    ostringstream msg;
    msg << "purchaseFailed(" << reason << ")";
    trace(msg.str().c_str());
    
// callback
    if (getCallback() != (IAPCallback *)0) {
        getCallback()->purchaseFailed(reason);
    }
    else {
        trace("purchaseFailed() called with null callback");
        runnableQueue.add(new Purchased(this,reason));
    }
    aeGlobalUnlock();
}

/** */
void LuaLibIAP::purchaseCanceled() {
    aeGlobalLock();
    trace("purchaseCanceled()");
    if (getCallback() != (IAPCallback *)0) {
        getCallback()->purchaseCanceled();
    }
    else {
        trace("purchaseCanceled() called with null callback");
        runnableQueue.add(new PurchaseCanceled(this));
    }
    aeGlobalUnlock();
}

/** */
void LuaLibIAP::alreadyOwned(const char *productId) {
    aeGlobalLock();    
    
// log
    ostringstream msg;
    msg << "alreadyOwned(" << productId << ")";
    trace(msg.str().c_str());
    
// callback
    if (getCallback() != (IAPCallback *)0) {
        getCallback()->alreadyOwned(productId);
    }
    else {
        trace("alreadyOwned() called with null callback");
        runnableQueue.add(new AlreadyOwned(this,productId));
    }
    aeGlobalUnlock();
}
    
/** */
void LuaLibIAP::purchasesRestored() {
    aeGlobalLock();
    trace("purchasesRestored()");
    if (getCallback() != (IAPCallback *)0) {
        getCallback()->purchasesRestored();
    }
    else {
        trace("purchasesRestored() called with null callback");
        runnableQueue.add(new PurchasesRestored(this));
    }
    aeGlobalUnlock();
}

/** */
void LuaLibIAP::restorePurchasesFailed(const char *reason) {
    aeGlobalLock();
    trace("restorePurchasesFailed()");
    if (getCallback() != (IAPCallback *)0) {
        getCallback()->restorePurchasesFailed(reason);
    }
    else {
        trace("restorePurchasesFailed() called with null callback");
        runnableQueue.add(new RestorePurchasesFailed(this,reason));
    }
    aeGlobalUnlock();
}

} // namespace
    
} // namespace