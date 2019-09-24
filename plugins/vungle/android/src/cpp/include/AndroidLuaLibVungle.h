#ifndef AE_ANDROID_LUA_LIB_VUNGLE_H
#define AE_ANDROID_LUA_LIB_VUNGLE_H

#include <jni.h>
#include "LuaLibVungle.h"

namespace ae {

namespace vungle {
  
/**
 * \brief The Android implementation of the Vungle Lua library.
 */
class AndroidLuaLibVungle:public LuaLibVungle {
    /// The JNI environment (from SDL).
    JNIEnv *env;
    
    /** */
    void getJNIEnv();
    
public:
    /** */
    AndroidLuaLibVungle():LuaLibVungle(),env((JNIEnv *)0) {
    };
    
    /** */
    virtual ~AndroidLuaLibVungle() {
    }
    
    /** */
    virtual void init();
    
    /** */
    virtual void playAd();
};

} // namespace

} // namespace

#endif // AE_ANDROID_LUA_LIB_VUNGLE_H