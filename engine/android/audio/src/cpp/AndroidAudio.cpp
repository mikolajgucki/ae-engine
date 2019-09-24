#include <jni.h>

#include "Log.h"
#include "JNIAutoLocalRef.h"
#include "JNIThrowableUtil.h"
#include "DummySound.h"
#include "AndroidSound.h"
#include "AndroidAudio.h"

using namespace std;
using namespace ae::jni;

extern "C" {
// Declaration. Defined in SDL.
JNIEnv *Android_JNI_GetEnv(void);
}

/// The log tag.
static const char *logTag = "AE/Audio";

/// The JNI class name.
static const char *jClassName = "com/andcreations/ae/AEAudio";

namespace ae {
    
namespace audio {
    
/** */
Sound *AndroidAudio::loadSound(const string& filename) {
    JNIEnv *env = Android_JNI_GetEnv();
    
// class
    jclass clazz = env->FindClass(jClassName);
    if (clazz == (jclass)0) {
        setError("Class not found");
        return (Sound *)0;
    }    
    JNIAutoLocalRef clazzLocalRef(env,clazz); 
    
// filename
    jstring jFilename = env->NewStringUTF(filename.c_str());
    JNIAutoLocalRef jFilenameLocalRef(env,jFilename);
    
// method
    jmethodID method = env->GetStaticMethodID(clazz,"loadSound",
        "(Ljava/lang/String;)I");
    if (method == (jmethodID)0) {
        setError("Method not found");
        return (Sound *)0;
    }    
    
// call
    int soundId = (int)env->CallStaticIntMethod(clazz,method,jFilename);
    
// check exception
    string exceptionStr;
    if (JNIThrowableUtil::toString(env,exceptionStr) == true) {
        setError(exceptionStr);
        return (Sound *)0;
    }  
    
// create
    AndroidSound *sound = new (nothrow) AndroidSound(soundId);
    if (sound == (AndroidSound *)0) {
        // TODO
    }
    
    return sound;
}

/** */
void AndroidAudio::deleteSound(Sound *sound) {
    JNIEnv *env = Android_JNI_GetEnv();
    
// class
    jclass clazz = env->FindClass(jClassName);
    if (clazz == (jclass)0) {
        setError("Class not found");
        return;
    }    
    JNIAutoLocalRef clazzLocalRef(env,clazz); 
    
// method
    jmethodID method = env->GetStaticMethodID(clazz,"deleteSound","(I)V");
    if (method == (jmethodID)0) {
        setError("Method not found");
        return;
    }    
    
    AndroidSound *androidSound = static_cast<AndroidSound *>(sound);
// call
    env->CallStaticVoidMethod(clazz,method,androidSound->getSoundId());
    
// check exception
    string exceptionStr;
    if (JNIThrowableUtil::toString(env,exceptionStr) == true) {
        setError(exceptionStr);
        return;
    }
    
    delete sound;
}

/** */
Music *AndroidAudio::loadMusic(const string& filename) {
    JNIEnv *env = Android_JNI_GetEnv();
    
// class
    jclass clazz = env->FindClass(jClassName);
    if (clazz == (jclass)0) {
        setError("Class not found");
        return (Music *)0;
    }    
    JNIAutoLocalRef clazzLocalRef(env,clazz); 
    
// filename
    jstring jFilename = env->NewStringUTF(filename.c_str());
    JNIAutoLocalRef jFilenameLocalRef(env,jFilename);
    
// method
    jmethodID method = env->GetStaticMethodID(clazz,"loadMusic",
        "(Ljava/lang/String;)V");
    if (method == (jmethodID)0) {
        setError("Method not found");
        return (Music *)0;
    }    
    
// call
    env->CallStaticVoidMethod(clazz,method,jFilename);
    
// check exception
    string exceptionStr;
    if (JNIThrowableUtil::toString(env,exceptionStr) == true) {
        setError(exceptionStr);
        return (Music *)0;
    } 
    
    return &music;
}

/** */
void AndroidAudio::deleteMusic(Music *music) {
    JNIEnv *env = Android_JNI_GetEnv();
    
// class
    jclass clazz = env->FindClass(jClassName);
    if (clazz == (jclass)0) {
        setError("Class not found");
        return;
    }    
    JNIAutoLocalRef clazzLocalRef(env,clazz); 
    
// method
    jmethodID method = env->GetStaticMethodID(clazz,"deleteMusic","()V");
    if (method == (jmethodID)0) {
        setError("Method not found");
        return;
    }    
    
// call
    env->CallStaticVoidMethod(clazz,method);
    
// check exception
    string exceptionStr;
    if (JNIThrowableUtil::toString(env,exceptionStr) == true) {
        setError(exceptionStr);
        return;
    } 
}

/** */
bool AndroidAudio::setVolume(double volume) {
    JNIEnv *env = Android_JNI_GetEnv();
    
// class
    jclass clazz = env->FindClass(jClassName);
    if (clazz == (jclass)0) {
        setError("Class not found");
        return false;
    }    
    JNIAutoLocalRef clazzLocalRef(env,clazz); 
    
// method
    jmethodID method = env->GetStaticMethodID(clazz,"setVolume","(D)V");
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

/** */
bool AndroidAudio::setVolumeFactor(double volumeFactor) {
    return true;
}
    
} // namespace
    
} // namespace