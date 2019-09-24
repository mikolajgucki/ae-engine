#include "Log.h"
#include "LuaLibHTTP.h"

namespace ae {
    
namespace http {
    
/// The log tag.
static const char *logTag = "AE/HTTP";

/** */
void LuaLibHTTP::setCallback(HTTPCallback *callback_) {
    if (callback != (HTTPCallback *)0) {
        delete callback;
    }   
    callback = callback_;
}    
    
/** */
void LuaLibHTTP::receivedResponse(const std::string& id,int code,
    const std::string& body) {
//
    if (getCallback() != (HTTPCallback *)0) {
        getCallback()->receivedResponse(id,code,body);
    }
    else {
        Log::trace(logTag,"receivedResponse() called with null callback");
    }
}

/** */
void LuaLibHTTP::failedToReceiveResponse(const std::string& id,
    const std::string& errorMsg) {
//
    if (getCallback() != (HTTPCallback *)0) {
        getCallback()->failedToReceiveResponse(id,errorMsg);
    }
    else {
        Log::trace(logTag,"failedToReceiveResponse() called with null callback");
    }
}    
    
} // namespace
    
} // namespace