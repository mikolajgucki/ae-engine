#include <jni.h>

#include "Log.h"
#include "JNIAutoLocalRef.h"
#include "JNIThrowableUtil.h"
#include "AndroidLuaLibChartboost.h"

using namespace std;
using namespace ae::jni;

extern "C" {
// Declaration. Defined in SDL.
JNIEnv *Android_JNI_GetEnv(void);
}

/// The log tag.
static const char *logTag = "AE/Chartboost";

/** The JNI class name. */
static const char *jClassName = "com/andcreations/chartboost/AEChartboost";

namespace ae {
    
namespace chartboost {
    
/** */
void AndroidLuaLibChartboost::getJNIEnv() {
    env = Android_JNI_GetEnv();    
}

/** */
void AndroidLuaLibChartboost::init() {
    Log::trace(logTag,"AndroidLuaLibChartboost::init()");
}

/** */
void AndroidLuaLibChartboost::cacheInterstitial(const string& location) {
    Log::trace(logTag,"AndroidLuaLibChartboost::cacheInterstitial()");
    
    getJNIEnv();
// class
    jclass clazz = env->FindClass(jClassName);
    if (clazz == (jclass)0) {
        Log::trace(logTag,"Class not found");
        return;
    }    
    JNIAutoLocalRef clazzLocalRef(env,clazz);
  
// method
    jmethodID method = env->GetStaticMethodID(clazz,"cacheInterstitial",
        "(Ljava/lang/String;)V");
    if (method == (jmethodID)0) {
        Log::trace(logTag,"Method not found");
        return;
    }
    
// location
    jstring jLocation = env->NewStringUTF(location.c_str());
    JNIAutoLocalRef jLocationLocalRef(env,jLocation);

// call
    env->CallStaticVoidMethod(clazz,method,jLocation);   
    if (JNIThrowableUtil::logException(env,logTag) == true) {
        return;
    }    
}
 
/** */
void AndroidLuaLibChartboost::showInterstitial(const string& location) {
    Log::trace(logTag,"AndroidLuaLibChartboost::showInterstitial()");
    
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
    }
    
// location
    jstring jLocation = env->NewStringUTF(location.c_str());
    JNIAutoLocalRef jLocationLocalRef(env,jLocation);
    
// call
    env->CallStaticVoidMethod(clazz,method,jLocation);
    if (JNIThrowableUtil::logException(env,logTag) == true) {
        return;
    }    
}

} // namespace
    
} // namespace