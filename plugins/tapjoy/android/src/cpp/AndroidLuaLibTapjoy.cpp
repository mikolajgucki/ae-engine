#include <sstream>

#include "Log.h"
#include "JNIAutoLocalRef.h"
#include "JNIThrowableUtil.h"
#include "AndroidLuaLibTapjoy.h"

using namespace std;
using namespace ae::jni;

extern "C" {
// Declaration. Defined in SDL.
JNIEnv *Android_JNI_GetEnv(void);
}

/// The log tag.
static const char *logTag = "AE/Tapjoy";

/// The JNI class name.
static const char *jClassName = "com/andcreations/tapjoy/AETapjoy";

namespace ae {

namespace tapjoy {
    
/** */
void AndroidLuaLibTapjoy::getJNIEnv() {
    env = Android_JNI_GetEnv();    
}

/** */
void AndroidLuaLibTapjoy::callStrJNIMethod(const string& methodName,
    const string& arg) {
// JNI
    getJNIEnv();
    
// class
    jclass clazz = env->FindClass(jClassName);
    if (clazz == (jclass)0) {
        Log::trace(logTag,"Class not found");
        return;
    }    
    JNIAutoLocalRef clazzLocalRef(env,clazz);
    
// argument
    jstring jArg = env->NewStringUTF(arg.c_str());
    JNIAutoLocalRef jArgLocalRef(env,jArg);
    
// method
    jmethodID method = env->GetStaticMethodID(clazz,methodName.c_str(),
        "(Ljava/lang/String;)V");
    if (method == (jmethodID)0) {
        Log::trace(logTag,"Method not found");
        return;
    }
    
// call
    env->CallStaticVoidMethod(clazz,method,jArg);
    if (JNIThrowableUtil::logException(env,logTag) == true) {
        return;
    }
}

/** */
bool AndroidLuaLibTapjoy::isConnected() {
    Log::trace(logTag,"AndroidLuaLibTapjoy::isConnected()");
    
    getJNIEnv();
// class
    jclass clazz = env->FindClass(jClassName);
    if (clazz == (jclass)0) {
        Log::trace(logTag,"Class not found");
        return false;
    }    
    JNIAutoLocalRef clazzLocalRef(env,clazz);   
    
// method
    jmethodID method = env->GetStaticMethodID(clazz,"isConnected",
        "()Z");
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
bool AndroidLuaLibTapjoy::isContentReady(const string &placement) {
// log
    ostringstream msg;
    msg << "AndroidLuaLibTapjoy::isContentReady(" << placement << ")";
    Log::trace(logTag,msg.str());
    
    getJNIEnv();
// class
    jclass clazz = env->FindClass(jClassName);
    if (clazz == (jclass)0) {
        Log::trace(logTag,"Class not found");
        return false;
    }    
    JNIAutoLocalRef clazzLocalRef(env,clazz);   
    
// placement
    jstring jPlacement = env->NewStringUTF(placement.c_str());
    JNIAutoLocalRef jPlacementLocalRef(env,jPlacement);    
    
// method
    jmethodID method = env->GetStaticMethodID(clazz,"isContentReady",
        "(Ljava/lang/String;)Z");
    if (method == (jmethodID)0) {
        Log::trace(logTag,"Method not found");
        return false;
    }    
    
// call
    jboolean result = env->CallStaticBooleanMethod(clazz,method,jPlacement);
    if (JNIThrowableUtil::logException(env,logTag) == true) {
        return false;
    }    
    
    return result == JNI_TRUE;   
}

/** */
void AndroidLuaLibTapjoy::requestContent(const string &placement) {
// log
    ostringstream msg;
    msg << "AndroidLuaLibTapjoy::requestContent(" << placement << ")";
    Log::trace(logTag,msg.str());
    
// call method
    callStrJNIMethod("requestContent",placement);
}
    
/** */
void AndroidLuaLibTapjoy::showContent(const string &placement) {
// log
    ostringstream msg;
    msg << "AndroidLuaLibTapjoy::showContent(" << placement << ")";
    Log::trace(logTag,msg.str());
    
// call method
    callStrJNIMethod("showContent",placement);
}
        
} // namespace
    
} // namespace