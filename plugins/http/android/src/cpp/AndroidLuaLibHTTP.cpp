#include <string>
#include <jni.h>

#include "Log.h"
#include "JNIAutoLocalRef.h"
#include "JNIThrowableUtil.h"
#include "AndroidLuaLibHTTP.h"

using namespace std;
using namespace ae;
using namespace ae::jni;

extern "C" {
// Declaration. Defined in SDL.
JNIEnv *Android_JNI_GetEnv(void);
}

/// The log tag.
static const char *logTag = "AE/HTTP";

/// The JNI class name.
static const char *jClassName = "com/andcreations/http/AEHTTP";

namespace ae {

namespace http {
    
/** */
void AndroidLuaLibHTTP::getJNIEnv() {
    env = Android_JNI_GetEnv();    
}

/** */
void AndroidLuaLibHTTP::get(const string& id,const string& url) {
// log
    ostringstream msg;
    msg << "AndroidLuaLibHTTP::get(" << id << "," << url << ")";
    Log::trace(logTag,msg.str());
    
    getJNIEnv();
// class
    jclass clazz = env->FindClass(jClassName);
    if (clazz == (jclass)0) {
        Log::trace(logTag,"Class not found");
        return;
    }    
    JNIAutoLocalRef clazzLocalRef(env,clazz);
    
// identifier
    jstring jId = env->NewStringUTF(id.c_str());
    JNIAutoLocalRef jIdLocalRef(env,jId);
    
// URL
    jstring jUrl = env->NewStringUTF(url.c_str());
    JNIAutoLocalRef jUrlLocalRef(env,jUrl);
    
// method
    jmethodID method = env->GetStaticMethodID(clazz,"sendGETRequest",
        "(Ljava/lang/String;Ljava/lang/String;)V");
    if (method == (jmethodID)0) {
        Log::trace(logTag,"Method not found");
        return;
    }
    
// call
    env->CallStaticVoidMethod(clazz,method,jId,jUrl);
    if (JNIThrowableUtil::logException(env,logTag) == true) {
        return;
    }     
}

/** */
void AndroidLuaLibHTTP::openURL(const std::string& url) {
// log
    ostringstream msg;
    msg << "AndroidLuaLibHTTP::openURL(" << url << ")";
    Log::trace(logTag,msg.str());
    
    getJNIEnv();
// class
    jclass clazz = env->FindClass(jClassName);
    if (clazz == (jclass)0) {
        Log::trace(logTag,"Class not found");
        return;
    }    
    JNIAutoLocalRef clazzLocalRef(env,clazz);
    
// URL
    jstring jUrl = env->NewStringUTF(url.c_str());
    JNIAutoLocalRef jUrlLocalRef(env,jUrl);
    
// method
    jmethodID method = env->GetStaticMethodID(clazz,"openURL",
        "(Ljava/lang/String;)V");
    if (method == (jmethodID)0) {
        Log::trace(logTag,"Method not found");
        return;
    }
    
// call
    env->CallStaticVoidMethod(clazz,method,jUrl);
    if (JNIThrowableUtil::logException(env,logTag) == true) {
        return;
    }    
}
    
} // namespace
    
} // namespace