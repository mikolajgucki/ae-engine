#include <string>
#include <jni.h>

#include "Log.h"
#include "JNIAutoLocalRef.h"
#include "JNIThrowableUtil.h"
#include "AndroidLuaLibAppLovin.h"

using namespace std;
using namespace ae;
using namespace ae::jni;

extern "C" {
// Declaration. Defined in SDL.
JNIEnv *Android_JNI_GetEnv(void);
}

/// The log tag.
static const char *logTag = "AE/AppLovin";

/// The JNI class name.
static const char *jClassName = "com/andcreations/applovin/AEAppLovin";

namespace ae {
    
namespace applovin {

/** */
void AndroidLuaLibAppLovin::getJNIEnv() {
    env = Android_JNI_GetEnv();    
}

/** */
bool AndroidLuaLibAppLovin::isInterstitialAdReadyForDisplay() {
    Log::trace(logTag,
        "AndroidLuaLibAppLovin::isInterstitialAdReadyForDisplay()");
    
    getJNIEnv();
// class
    jclass clazz = env->FindClass(jClassName);
    if (clazz == (jclass)0) {
        Log::trace(logTag,"Class not found");
        return false;
    }    
    JNIAutoLocalRef clazzLocalRef(env,clazz);
    
// method
    jmethodID method = env->GetStaticMethodID(clazz,
        "isInterstitialAdReadyForDisplay","()Z");
    if (method == (jmethodID)0) {
        Log::trace(logTag,"Method not found");
        return false;
    }    
    
// call
    jboolean ready = env->CallStaticBooleanMethod(clazz,method);
    if (JNIThrowableUtil::logException(env,logTag) == true) {
        return false;
    }    
    
    return ready == JNI_TRUE;
}

/** */
void AndroidLuaLibAppLovin::showInterstitialAd() {
    Log::trace(logTag,"AndroidLuaLibAppLovin::showInterstitialAd()");
    
    getJNIEnv();
// class
    jclass clazz = env->FindClass(jClassName);
    if (clazz == (jclass)0) {
        Log::trace(logTag,"Class not found");
        return;
    }    
    JNIAutoLocalRef clazzLocalRef(env,clazz);
    
// method
    jmethodID method = env->GetStaticMethodID(clazz,"showInterstitialAd","()V");
    if (method == (jmethodID)0) {
        Log::trace(logTag,"Method not found");
        return;
    }    
    
// call
    env->CallStaticVoidMethod(clazz,method);
    if (JNIThrowableUtil::logException(env,logTag) == true) {
        return;
    }    
}

} // namespace
    
} // namespace    
