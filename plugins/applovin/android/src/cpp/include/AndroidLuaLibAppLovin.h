#ifndef AE_ANDROID_LUA_LIB_APP_LOVIN_H
#define AE_ANDROID_LUA_LIB_APP_LOVIN_H

#include <jni.h>
#include "LuaLibAppLovin.h"

namespace ae {
    
namespace applovin {
    
/**
 * \brief The Android implementation of the AppLovin Lua library.
 */
class AndroidLuaLibAppLovin:public LuaLibAppLovin {
    /// The JNI environment (from SDL).
    JNIEnv *env;
    
    /** */
    void getJNIEnv();
    
public:
    /** */
    AndroidLuaLibAppLovin():LuaLibAppLovin(),env((JNIEnv *)0) {
    }
    
    /** */
    virtual ~AndroidLuaLibAppLovin() {
    }
    
    /** */
    virtual bool isInterstitialAdReadyForDisplay();
    
    /** */
    virtual void showInterstitialAd();
};
    
} // namespace
    
} // namespace

#endif // AE_ANDROID_LUA_LIB_APP_LOVIN_H
