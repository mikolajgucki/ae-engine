#include <jni.h>

#include "Log.h"
#include "JNIAutoLocalRef.h"
#include "JNIThrowableUtil.h"
#include "AndroidMusic.h"

using namespace std;
using namespace ae::jni;

extern "C" {
// Declaration. Defined in SDL.
JNIEnv *Android_JNI_GetEnv(void);
}

/// The log tag.
static const char *logTag = "AE/Music";

/// The JNI class name.
static const char *jClassName = "com/andcreations/ae/AEAudio";

namespace ae {
    
namespace audio {

/** */
bool AndroidMusic::callJNINoArgMethod(const string &methodName) {
    JNIEnv *env = Android_JNI_GetEnv();
    
// class
    jclass clazz = env->FindClass(jClassName);
    if (clazz == (jclass)0) {
        setError("Class not found");
        return false;
    }    
    JNIAutoLocalRef clazzLocalRef(env,clazz); 
    
// method
    jmethodID method = env->GetStaticMethodID(clazz,methodName.c_str(),"()V");
    if (method == (jmethodID)0) {
        setError("Method not found");
        return false;
    }    
    
// call
    env->CallStaticVoidMethod(clazz,method);
    
// check exception
    string exceptionStr;
    if (JNIThrowableUtil::toString(env,exceptionStr) == true) {
        setError(exceptionStr);
        return false;
    }
    
    return true;    
}
    
/** */
bool AndroidMusic::play(int loops) {
    return callJNINoArgMethod("playMusic");
}

/** */
bool AndroidMusic::pause() {
    return callJNINoArgMethod("pauseMusic");
}

/** */
bool AndroidMusic::resume() {
    return callJNINoArgMethod("resumeMusic");
}

/** */
bool AndroidMusic::stop() {
    return callJNINoArgMethod("stopMusic");
}

/** */
bool AndroidMusic::setVolume(double volume) {
    JNIEnv *env = Android_JNI_GetEnv();
    
// class
    jclass clazz = env->FindClass(jClassName);
    if (clazz == (jclass)0) {
        setError("Class not found");
        return false;
    }    
    JNIAutoLocalRef clazzLocalRef(env,clazz); 
    
// method
    jmethodID method = env->GetStaticMethodID(clazz,"setMusicVolume","(D)V");
    if (method == (jmethodID)0) {
        setError("Method not found");
        return false;
    }    
    
// call
    env->CallStaticVoidMethod(clazz,method,volume);
    
// check exception
    string exceptionStr;
    if (JNIThrowableUtil::toString(env,exceptionStr) == true) {
        setError(exceptionStr);
        return false;
    }
    
    return true;
}
    
} // namespace
    
} // namespace