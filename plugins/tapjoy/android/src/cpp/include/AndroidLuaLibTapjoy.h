#ifndef AE_ANDROID_LUA_LIB_TAPJOY_H
#define AE_ANDROID_LUA_LIB_TAPJOY_H

#include <jni.h>
#include "LuaLibTapjoy.h"

namespace ae {
    
namespace tapjoy {
    
/**
 * \brief The Android implementation of the Tapjoy Lua library.
 */
class AndroidLuaLibTapjoy:public LuaLibTapjoy {
    /// The JNI environment (from SDL).
    JNIEnv *env;
    
    /** */
    void getJNIEnv();
    
    /** */
    void callStrJNIMethod(const std::string& methodName,
        const std::string& arg); 
    
public:
    /** */
    AndroidLuaLibTapjoy():LuaLibTapjoy(),env((JNIEnv *)0) {
    }
    
    /** */
    virtual ~AndroidLuaLibTapjoy() {
    }    
    
    /** */
    virtual bool isConnected();
    
    /** */
    virtual void requestContent(const std::string &placement);
    
    /** */
    virtual bool isContentReady(const std::string &placement);
    
    /** */
    virtual void showContent(const std::string &placement);    
};    
    
} // namespace
    
} // namespace

#endif // AE_ANDROID_LUA_LIB_TAPJOY_H