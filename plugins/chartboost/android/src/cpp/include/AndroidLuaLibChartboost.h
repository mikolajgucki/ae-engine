#ifndef AE_ANDROID_LUA_LIB_CHARTBOOST_H
#define AE_ANDROID_LUA_LIB_CHARTBOOST_H

#include <jni.h>
#include <string>
#include "LuaLibChartboost.h"

namespace ae {
    
namespace chartboost {
    
/**
 * \brief The Android implementation of the Chartboost Lua library.
 */
class AndroidLuaLibChartboost:public LuaLibChartboost {
    /// The JNI environment (from SDL).
    JNIEnv *env;
    
    /** */
    void getJNIEnv();
    
public:
    /** */
    AndroidLuaLibChartboost():LuaLibChartboost(),env((JNIEnv *)0) {
    }
    
    /** */
    virtual ~AndroidLuaLibChartboost() {
    }
    
    /** */
    virtual void init();
    
    /** */
    virtual void cacheInterstitial(const std::string &location);
    
    /** */
    virtual void showInterstitial(const std::string &location);
};
    
} // namespace
    
} // namespace

#endif //  AE_ANDROID_LUA_LIB_CHARTBOOST_H