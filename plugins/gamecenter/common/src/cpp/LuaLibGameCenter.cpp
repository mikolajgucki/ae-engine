#include "Log.h"
#include "LuaLibGameCenter.h"

namespace ae {
    
namespace gamecenter {
    
/// The log cat.
static const char *logTag = "AE/GameCenter";
    
/** */
void LuaLibGameCenter::setCallback(GameCenterCallback *callback_) {
    callback = callback_;
}

/** */
void LuaLibGameCenter::willShowLoginView() {
    if (getCallback() != (GameCenterCallback *)0) {
        getCallback()->willShowLoginView();
    }
    else {
        Log::trace(logTag,"willShowLoginView() called with null callback");
    }
}

/** */
void LuaLibGameCenter::authenticated() {
    if (getCallback() != (GameCenterCallback *)0) {
        getCallback()->authenticated();
    }
    else {
        Log::trace(logTag,"authenticated() called with null callback");
    }        
}
    
/** */
void LuaLibGameCenter::notAuthenticated() {
    if (getCallback() != (GameCenterCallback *)0) {
        getCallback()->notAuthenticated();
    }
    else {
        Log::trace(logTag,"notAuthenticated() called with null callback");
    }        
}

/** */
void LuaLibGameCenter::notAuthenticatedWithError() {
    if (getCallback() != (GameCenterCallback *)0) {
        getCallback()->notAuthenticatedWithError();
    }
    else {
        Log::trace(logTag,"notAuthenticatedWithError() called with null callback");
    }        
}

/** */
void LuaLibGameCenter::loginViewHidden() {
    if (getCallback() != (GameCenterCallback *)0) {
        getCallback()->loginViewHidden();
    }
    else {
        Log::trace(logTag,"loginViewHidden() called with null callback");
    }        
}

/** */
void LuaLibGameCenter::achievementsLoaded() {
    if (getCallback() != (GameCenterCallback *)0) {
        getCallback()->achievementsLoaded();
    }
    else {
        Log::trace(logTag,"achievementsLoaded() called with null callback");
    }        
}
    
} // namespace
    
} // namespace