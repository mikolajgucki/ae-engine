#include <sstream>

#include "Log.h"
#include "ae_lock.h"
#include "LuaLibUnityAds.h"

using namespace std;

namespace ae {
    
namespace unityads {
    
/** */
class UnityAdsEvent:public Runnable {
protected:
    /** */
    LuaLibUnityAds *luaLibUnityAds;
    
    /** */
    UnityAdsEvent(LuaLibUnityAds *lib_):Runnable(),luaLibUnityAds(lib_) {
    }
    
    /** */
    virtual ~UnityAdsEvent() {
    }
};

/** */
class ReadyEvent:public UnityAdsEvent {
    /** */
    const string placementId;
    
public:
    /** */
    ReadyEvent(LuaLibUnityAds *lib_,const string &placementId_):
        UnityAdsEvent(lib_),placementId(placementId_) {
    }
    
    /** */
    virtual bool run() {
        luaLibUnityAds->setReady(placementId);
        return true;
    }
};

/** */
class StartedEvent:public UnityAdsEvent {
    /** */
    const string placementId;
    
public:
    /** */
    StartedEvent(LuaLibUnityAds *lib_,const string &placementId_):
        UnityAdsEvent(lib_),placementId(placementId_) {
    }
    
    /** */
    virtual bool run() {
        luaLibUnityAds->started(placementId);
        return true;
    }
};

/** */
class FailedEvent:public UnityAdsEvent {
    /** */
    const string error;
    
    /** */
    const string msg;
    
public:
    /** */
    FailedEvent(LuaLibUnityAds *lib_,const string &error_,const string &msg_):
        UnityAdsEvent(lib_),error(error_),msg(msg_) {
    }
    
    /** */
    virtual bool run() {
        luaLibUnityAds->failed(error,msg);
        return true;
    }
};

/** */
class FinishedEvent:public UnityAdsEvent {
    /** */
    const string placementId;
    
    /** */
    UnityAdsCallback::FinishState state;
    
public:
    /** */
    FinishedEvent(LuaLibUnityAds *lib_,const string &placementId_,
        UnityAdsCallback::FinishState state_):UnityAdsEvent(lib_),
        placementId(placementId_),state(state_) {
    }
    
    /** */
    virtual bool run() {
        luaLibUnityAds->finished(placementId,state);
        return true;
    }
};

/// The log tag.
static const char *logTag = "AE/UnityAds";

/** */
void LuaLibUnityAds::trace(const char *msg) {
    if (dlog != (DLog *)0) {
        dlog->trace(logTag,msg);
    }
    else {
        Log::trace(logTag,msg);
    }        
}
    
/** */
void LuaLibUnityAds::setCallback(UnityAdsCallback *callback_) {
    aeGlobalLock();
    if (callback != (UnityAdsCallback *)0) {
        delete callback;
    }
    callback = callback_;
    
    if (callback != (UnityAdsCallback *)0) {
        if (runnableQueue.run() == false) {
            trace(runnableQueue.getError().c_str());
        }
    }  
    aeGlobalUnlock();
}

/** */
void LuaLibUnityAds::setReady(const string &placementId) {
    Log::trace(logTag,"pre.setReady()");
    if (dlog) {
        Log::trace(logTag,"dlog set");
    }
    aeGlobalLock();
// log
    ostringstream msg;
    msg << "setReady(" << placementId << ")";
    trace(msg.str().c_str());

// callback
    if (getCallback() != (UnityAdsCallback *)0) {
        getCallback()->setReady(placementId);
    }
    else {
        trace("setReady() called with null callback");
        runnableQueue.add(new ReadyEvent(this,placementId));
    }
    aeGlobalUnlock();
}

/** */
void LuaLibUnityAds::started(const string &placementId) {
    aeGlobalLock();
// log
    ostringstream msg;
    msg << "started(" << placementId << ")";
    trace(msg.str().c_str());

// callback
    if (getCallback() != (UnityAdsCallback *)0) {
        getCallback()->started(placementId);
    }
    else {
        trace("started() called with null callback");
        runnableQueue.add(new StartedEvent(this,placementId));
    }
    aeGlobalUnlock();    
}

/** */
void LuaLibUnityAds::failed(const string &error,const string &msg) {
    aeGlobalLock();
// log
    ostringstream logMsg;
    logMsg << "failed(" << error << "," << msg << ")";
    trace(logMsg.str().c_str());

// callback
    if (getCallback() != (UnityAdsCallback *)0) {
        getCallback()->failed(error,msg);
    }
    else {
        trace("failed() called with null callback");
        runnableQueue.add(new FailedEvent(this,error,msg));
    }
    aeGlobalUnlock();     
}

/** */
void LuaLibUnityAds::finished(const string &placementId,
    UnityAdsCallback::FinishState state) {
//
   aeGlobalLock();
   
// log
    ostringstream msg;
    msg << "finished(" << placementId << "," <<
        UnityAdsCallback::finishStateToStr(state) << ")";
    trace(msg.str().c_str());

// callback
    if (getCallback() != (UnityAdsCallback *)0) {
        getCallback()->finished(placementId,state);
    }
    else {
        trace("finished() called with null callback");
        runnableQueue.add(new FinishedEvent(this,placementId,state));
    }
    aeGlobalUnlock();
}
    
} // namespace
    
} // namespace