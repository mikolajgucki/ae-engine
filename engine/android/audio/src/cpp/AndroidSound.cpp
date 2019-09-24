#include <string>
#include <jni.h>

#include "Log.h"
#include "JNIAutoLocalRef.h"
#include "JNIThrowableUtil.h"
#include "AndroidSound.h"

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
bool AndroidSound::play() {
    JNIEnv *env = Android_JNI_GetEnv();
    
// class
    jclass clazz = env->FindClass(jClassName);
    if (clazz == (jclass)0) {
        setError("Class not found");
        return false;
    }    
    JNIAutoLocalRef clazzLocalRef(env,clazz); 
    
// method
    jmethodID method = env->GetStaticMethodID(clazz,"playSound","(I)V");
    if (method == (jmethodID)0) {
        setError("Method not found");
        return false;
    }    
    
// call
    env->CallStaticVoidMethod(clazz,method,soundId);
    
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