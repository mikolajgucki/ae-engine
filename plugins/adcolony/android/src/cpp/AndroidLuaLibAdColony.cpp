#include "Log.h"
#include "JNIAutoLocalRef.h"
#include "JNIThrowableUtil.h"
#include "AndroidLuaLibAdColony.h"

extern "C" {
// Declaration. Defined in SDL.
JNIEnv *Android_JNI_GetEnv(void);
}

using namespace std;
using namespace ae;
using namespace ae::jni;

namespace ae {
    
namespace adcolony {

/// The log tag.
static const char *logTag = "AE/AdColony";
    
/** The JNI class name. */
static const char *jClassName = "com/andcreations/adcolony/AEAdColony";

/** */
void AndroidLuaLibAdColony::getJNIEnv() {
    env = Android_JNI_GetEnv();    
}

/** */
void AndroidLuaLibAdColony::requestInterstitial(const string& zoneId) {
    Log::trace(logTag,"AndroidLuaLibAdColony::requestInterstitial()");
    
    getJNIEnv();
// class
    jclass clazz = env->FindClass(jClassName);
    if (clazz == (jclass)0) {
        Log::trace(logTag,"Class not found");
        return;
    }    
    JNIAutoLocalRef clazzLocalRef(env,clazz);
    
// method
    jmethodID method = env->GetStaticMethodID(clazz,"requestInterstitial",
        "(Ljava/lang/String;)V");
    if (method == (jmethodID)0) {
        Log::trace(logTag,"Method not found");
        return;
    }
    
// zone identifier
    jstring jZoneId = env->NewStringUTF(zoneId.c_str());
    JNIAutoLocalRef jZoneIdRef(env,jZoneId);
    
// call
    env->CallStaticVoidMethod(clazz,method,jZoneId);
    if (JNIThrowableUtil::logException(env,logTag) == true) {
        return;
    }
}

/** */
void AndroidLuaLibAdColony::showInterstitial(const string& zoneId) {
    Log::trace(logTag,"AndroidLuaLibAdColony::showInterstitial()");
    
    getJNIEnv();
// class
    jclass clazz = env->FindClass(jClassName);
    if (clazz == (jclass)0) {
        Log::trace(logTag,"Class not found");
        return;
    }    
    JNIAutoLocalRef clazzLocalRef(env,clazz);
    
// method
    jmethodID method = env->GetStaticMethodID(clazz,"showInterstitial",
        "(Ljava/lang/String;)V");
    if (method == (jmethodID)0) {
        Log::trace(logTag,"Method not found");
        return;
    }
    
// zone identifier
    jstring jZoneId = env->NewStringUTF(zoneId.c_str());
    JNIAutoLocalRef jZoneIdRef(env,jZoneId);
    
// call
    env->CallStaticVoidMethod(clazz,method,jZoneId);
    if (JNIThrowableUtil::logException(env,logTag) == true) {
        return;
    }
}

/** */
bool AndroidLuaLibAdColony::isInterstitialExpired(const string& zoneId) {
    Log::trace(logTag,"AndroidLuaLibAdColony::isInterstitialExpired()");
    
    getJNIEnv();
// class
    jclass clazz = env->FindClass(jClassName);
    if (clazz == (jclass)0) {
        Log::trace(logTag,"Class not found");
        return false;
    }    
    JNIAutoLocalRef clazzLocalRef(env,clazz);
    
// method
    jmethodID method = env->GetStaticMethodID(clazz,"isInterstitialExpired",
        "(Ljava/lang/String;)Z");
    if (method == (jmethodID)0) {
        Log::trace(logTag,"Method not found");
        return false;
    }
    
// zone identifier
    jstring jZoneId = env->NewStringUTF(zoneId.c_str());
    JNIAutoLocalRef jZoneIdRef(env,jZoneId);
    
// call
    jboolean result = env->CallStaticBooleanMethod(clazz,method,jZoneId);
    if (JNIThrowableUtil::logException(env,logTag) == true) {
        return false;
    }    
    
    return result == JNI_TRUE;
}

} // namespace
    
} // namespace