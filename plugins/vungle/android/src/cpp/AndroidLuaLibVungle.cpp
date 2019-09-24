#include <jni.h>

#include "Log.h"
#include "JNIAutoLocalRef.h"
#include "JNIThrowableUtil.h"
#include "AndroidLuaLibVungle.h"

extern "C" {
// Declaration. Defined in SDL.
JNIEnv *Android_JNI_GetEnv(void);
}

/// The log tag.
static const char *logTag = "AE/Vungle";

/** The JNI class name. */
static const char *jClassName = "com/andcreations/vungle/AEVungle";

using namespace ae::jni;

namespace ae {
    
namespace vungle {
    
/** */
void AndroidLuaLibVungle::getJNIEnv() {
    env = Android_JNI_GetEnv();    
}

/** */
void AndroidLuaLibVungle::init() {
    Log::trace(logTag,"AndroidLuaLibVungle::init()");
}

/** */
void AndroidLuaLibVungle::playAd() {
    Log::trace(logTag,"AndroidLuaLibVungle::playAd()");
    
    getJNIEnv();
// class
    jclass clazz = env->FindClass(jClassName);
    if (clazz == (jclass)0) {
        Log::trace(logTag,"Class not found");
        return;
    }    
    JNIAutoLocalRef clazzLocalRef(env,clazz);
    
// method    
    jmethodID method = env->GetStaticMethodID(clazz,"playAd","()V");
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