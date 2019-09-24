#include <sstream>
#include <jni.h>
#include "Log.h"
#include "JNIAutoLocalRef.h"
#include "ae_sdl2.h"
#include "ae_sdl2_android.h"

using namespace std;
using namespace ae;
using namespace ae::jni;

extern "C" {
// Declaration. Defined in SDL.
JNIEnv *Android_JNI_GetEnv(void);

/** */
void Java_com_andcreations_ae_AE_initDebug(JNIEnv *env,jclass clazz);
}

/// The log tag.
static const char *logTag = "AE";

/** */
void throwAEException(const char *msg) {
    ostringstream logMsg;
    logMsg << "throwAEException(" << msg << ")";
    Log::trace(logTag,logMsg.str());
    
    JNIEnv *env = Android_JNI_GetEnv();   
// class
    jclass clazz = env->FindClass("org/libsdl/app/SDLActivity");
    if (clazz == (jclass)0) {
        Log::trace(logTag,"org.libsdl.app.SDLActivity class not found");
        return;
    }     
    
// method
    jmethodID method = env->GetStaticMethodID(
        clazz,"aeThrowException","(Ljava/lang/String;)V");
    if (method == (jmethodID)0) {
        Log::trace(logTag,
            "org.libsdl.app.SDLActivity.aeThrowException method not found");
        return;
    }     
    
// message
    jstring jMsg = env->NewStringUTF(msg);
    JNIAutoLocalRef jMsgLocalRef(env,jMsg);

// call
    env->CallStaticVoidMethod(clazz,method,jMsg);   
}

/** */
void Java_com_andcreations_ae_AE_initDebug(JNIEnv *env,jclass clazz) {
    aeInitDebug();
}