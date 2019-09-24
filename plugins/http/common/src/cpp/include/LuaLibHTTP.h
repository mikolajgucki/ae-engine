#ifndef AE_LUA_LIB_HTTP_H
#define AE_LUA_LIB_HTTP_H

#include <string>
#include "Error.h"
#include "HTTPCallback.h"

namespace ae {
    
namespace http {
    
/**
 * \brief The superclass for platform-specific implementations of HTTP Lua
 *   library.
 */
class LuaLibHTTP:public Error {
    /// The HTTP callback.
    HTTPCallback *callback;
    
protected:
    /** */
    LuaLibHTTP():Error(),callback((HTTPCallback *)0) {
    }
    
    /**
     * \brief Gets the callback.
     * \return The callback.
     */
    HTTPCallback *getCallback() {
        return callback;
    }
    
public:
    /** */
    virtual ~LuaLibHTTP() {
        if (callback != (HTTPCallback *)0) {
            delete callback;
        }        
    }
    
    /**
     * \brief Sets the callback.
     * \param callback_ callback.
     */
    virtual void setCallback(HTTPCallback *callback_);
    
    /**
     * \brief Asynchronously sends a GET request.
     * \param id The request identifier.
     * \param url The URL.
     */
    virtual void get(const std::string& id,const std::string& url) = 0;
    
    /**
     * \brief Opens an URL.
     * \param url The URL to open.
     */
    virtual void openURL(const std::string& url) = 0;
    
    /** */
    virtual void receivedResponse(const std::string& id,int code,
        const std::string& body);
    
    /** */
    virtual void failedToReceiveResponse(const std::string& id,
        const std::string& errorMsg);    
};
    
} // namespace
    
} // namespace

#endif // AE_LUA_LIB_HTTP_H