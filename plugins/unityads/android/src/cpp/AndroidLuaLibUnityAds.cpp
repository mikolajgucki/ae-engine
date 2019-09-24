#include <jni.h>

#include "Log.h"
#include "JNIAutoLocalRef.h"
#include "JNIThrowableUtil.h"
#include "AndroidLuaLibUnityAds.h"

extern "C" {
// Declaration. Defined in SDL.
JNIEnv *Android_JNI_GetEnv(void);
}

/// The log tag.
static const char *logTag = "AE/UnityAds";

/** The JNI class name. */
static const char *jClassName = "com/andcreations/unityads/AEUnityAds";

using namespace std;
using namespace ae::jni;

namespace ae {
    
namespace unityads {
    
/** */
void AndroidLuaLibUnityAds::getJNIEnv() {
    env = Android_JNI_GetEnv();    
}

/** */
bool AndroidLuaLibUnityAds::isReady() {
// log
    Log::trace(logTag,"AndroidLuaLibUnityAds::isReady()");
    
    getJNIEnv();
// class
    jclass clazz = env->FindClass(jClassName);
    if (clazz == (jclass)0) {
        Log::trace(logTag,"Class not found");
        return false;
    }    
    JNIAutoLocalRef clazzLocalRef(env,clazz);
    
// method
    jmethodID method = env->GetStaticMethodID(clazz,"isReady","()Z");
    if (method == (jmethodID)0) {
        Log::trace(logTag,"Method not found");
        return false;
    }
    
// call
    jboolean result = env->CallStaticBooleanMethod(clazz,method);
    if (JNIThrowableUtil::logException(env,logTag) == true) {
        return false;
    }    
    
    return result == JNI_TRUE;    
}

/** */
bool AndroidLuaLibUnityAds::isReady(const string& placementId) {
// log
    ostringstream msg;
    msg << "AndroidLuaLibUnityAds::isReady(" << placementId << ")";
    Log::trace(logTag,msg.str());
    
    getJNIEnv();
// class
    jclass clazz = env->FindClass(jClassName);
    if (clazz == (jclass)0) {
        Log::trace(logTag,"Class not found");
        return false;
    }    
    JNIAutoLocalRef clazzLocalRef(env,clazz);
    
// method
    jmethodID method = env->GetStaticMethodID(clazz,"isReady",
        "(Ljava/lang/String;)Z");
    if (method == (jmethodID)0) {
        Log::trace(logTag,"Method not found");
        return false;
    }
    
// placement identifier
    jstring jPlacementId = env->NewStringUTF(placementId.c_str());
    JNIAutoLocalRef jZoneIdRef(env,jPlacementId);
    
// call
    jboolean result = env->CallStaticBooleanMethod(clazz,method,jPlacementId);
    if (JNIThrowableUtil::logException(env,logTag) == true) {
        return false;
    }    
    
    return result == JNI_TRUE;    
}

/** */
void AndroidLuaLibUnityAds::show() {
// log
    Log::trace(logTag,"AndroidLuaLibUnityAds::show()");
    
    getJNIEnv();
// class
    jclass clazz = env->FindClass(jClassName);
    if (clazz == (jclass)0) {
        Log::trace(logTag,"Class not found");
        return;
    }    
    JNIAutoLocalRef clazzLocalRef(env,clazz);
  
// method   
    jmethodID method = env->GetStaticMethodID(clazz,"show","()V");
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

/** */
void AndroidLuaLibUnityAds::show(const string& placementId) {
// log
    ostringstream msg;
    msg << "AndroidLuaLibUnityAds::show(" << placementId << ")";
    Log::trace(logTag,msg.str());
    
    getJNIEnv();
// class
    jclass clazz = env->FindClass(jClassName);
    if (clazz == (jclass)0) {
        Log::trace(logTag,"Class not found");
        return;
    }    
    JNIAutoLocalRef clazzLocalRef(env,clazz);
  
// method   
    jmethodID method = env->GetStaticMethodID(clazz,"show",
        "(Ljava/lang/String;)V");
    if (method == (jmethodID)0) {
        Log::trace(logTag,"Method not found");
        return;
    }
    
// placement identifier
    jstring jPlacementId = env->NewStringUTF(placementId.c_str());
    JNIAutoLocalRef jZoneIdRef(env,jPlacementId);
    
// call
    env->CallStaticVoidMethod(clazz,method,jPlacementId);
    if (JNIThrowableUtil::logException(env,logTag) == true) {
        return;
    }      
}
    
} // namespace
    
} // namespace    