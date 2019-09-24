#ifndef AE_ANDROID_LUA_LIB_UNITY_ADS_H
#define AE_ANDROID_LUA_LIB_UNITY_ADS_H

#include <jni.h>
#include "LuaLibUnityAds.h"

namespace ae {
    
namespace unityads {
    
/**
 * \brief The Android implementation of the Unity Ads Lua library.
 */
class AndroidLuaLibUnityAds:public LuaLibUnityAds {
    /// The JNI environment (from SDL).
    JNIEnv *env;
    
    /** */
    void getJNIEnv();
    
public:
    /** */
    AndroidLuaLibUnityAds():LuaLibUnityAds(),env((JNIEnv *)0) {
    };
    
    /** */
    virtual ~AndroidLuaLibUnityAds() {
    }
    
    /** */
    virtual bool isReady();
    
    /** */
    virtual bool isReady(const std::string &placementId);
    
    /** */
    virtual void show();
    
    /** */
    virtual void show(const std::string &placementId);
};

    
} // namespace
    
} // namespace

#endif // AE_ANDROID_LUA_LIB_UNITY_ADS_H