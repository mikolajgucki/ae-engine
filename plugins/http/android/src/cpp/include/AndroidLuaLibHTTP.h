#ifndef AE_ANDROID_LUA_LIB_HTTP_H
#define AE_ANDROID_LUA_LIB_HTTP_H

#include <jni.h>
#include "LuaLibHTTP.h"

namespace ae {
    
namespace http {
  
/**
 * \brief The Android implementation of the HTTP Lua library.
 */
class AndroidLuaLibHTTP:public LuaLibHTTP {
    /// The JNI environment (from SDL).
    JNIEnv *env;
    
    /** */
    void getJNIEnv();
    
public:
    /** */
    AndroidLuaLibHTTP():LuaLibHTTP(),env((JNIEnv *)0) {
    }
    
    /** */
    virtual ~AndroidLuaLibHTTP() {
    }    
    
    /** */
    virtual void get(const std::string& id,const std::string& url);
    
    /** */
    virtual void openURL(const std::string& url);
};
    
} // namespace
    
} // namespace

#endif // AE_ANDROID_LUA_LIB_HTTP_H