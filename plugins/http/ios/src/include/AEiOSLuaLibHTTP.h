#ifndef AE_IOS_LUA_LIB_HTTP_H
#define AE_IOS_LUA_LIB_HTTP_H

#include "LuaLibHTTP.h"

/**
 * \brief The iOS implementation of the HTTP Lua library.
 */
class AEiOSLuaLibHTTP:public ::ae::http::LuaLibHTTP {
    /** */
    NSURLSession *session;
    
    /** */
    void create();
    
public:
    /** */
    AEiOSLuaLibHTTP():LuaLibHTTP(),session((NSURLSession *)0) {
        create();
    }
    
    /** */
    virtual ~AEiOSLuaLibHTTP() {
    }
    
    /** */
    virtual void get(const std::string& requestId,const std::string& url);
    
    /** */
    virtual void openURL(const std::string& url);    
};

#endif // AE_IOS_LUA_LIB_HTTP_H