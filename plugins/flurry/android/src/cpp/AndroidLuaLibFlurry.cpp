#include "Log.h"
#include "JNIAutoLocalRef.h"
#include "JNIMapUtil.h"
#include "JNIThrowableUtil.h"
#include "AndroidLuaLibFlurry.h"

extern "C" {
// Declaration. Defined in SDL.
JNIEnv *Android_JNI_GetEnv(void);
}

using namespace std;
using namespace ae::jni;

namespace ae {
    
namespace flurry {
    
/// The log tag.
static const char *logTag = "AE/Flurry";
    
/** The IAP Java class name. */
static const char *jClassName = "com/andcreations/flurry/AEFlurry";  
    
/** */
void AndroidLuaLibFlurry::getJNIEnv() {
    env = Android_JNI_GetEnv();
}

/** */
jobject AndroidLuaLibFlurry::createMap(map<string,string> &parameters) {
// create Java map
    JNIMapUtil jniMapUtil(env);
    if (jniMapUtil.createHashMap() == false) {
        Log::error(logTag,jniMapUtil.getError());
        return (jobject)0;
    }
    
// put values
    map<string,string>::iterator itr;
    for (itr = parameters.begin(); itr != parameters.end(); ++itr) {
        if (jniMapUtil.put(itr->first,itr->second) == false) {
            Log::error(logTag,jniMapUtil.getError());
            return (jobject)0;
        }
    }
    
    return jniMapUtil.getObject();
}

/** */
void AndroidLuaLibFlurry::init() {
    Log::trace(logTag,"AndroidLuaLibFlurry::init()");
}

/** */
void AndroidLuaLibFlurry::logEvent(const string &eventId,bool timed) {
// log
    ostringstream msg;
    msg << "AndroidLuaLibFlurry::logEvent(" << eventId << ")";
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
    jmethodID method = env->GetStaticMethodID(
        clazz,"logEvent","(Ljava/lang/String;Z)V");
    if (method == (jmethodID)0) {
        Log::trace(logTag,"Method not found");
        return;
    }
    
// identifier
    jstring jEventId = env->NewStringUTF(eventId.c_str());
    JNIAutoLocalRef jEventIdLocalRef(env,jEventId);

// timed
    jboolean jTimed = timed ? JNI_TRUE : JNI_FALSE;
    
// call
    env->CallStaticVoidMethod(clazz,method,jEventId,jTimed);
    if (JNIThrowableUtil::logException(env,logTag) == true) {
        return;
    }
}

/** */
void AndroidLuaLibFlurry::logEvent(const string &eventId,
    map<string,string> &parameters,bool timed) {
// log
    ostringstream msg;
    msg << "AndroidLuaLibFlurry::logEvent(" << eventId << ") with parameters";
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
    jmethodID method = env->GetStaticMethodID(
        clazz,"logEvent","(Ljava/lang/String;Ljava/util/Map;Z)V");
    if (method == (jmethodID)0) {
        Log::trace(logTag,"Method not found");
        return;
    }  
    
// identifier
    jstring jEventId = env->NewStringUTF(eventId.c_str());    
    JNIAutoLocalRef jEventIdLocalRef(env,jEventId);
    
// timed
    jboolean jTimed = timed ? JNI_TRUE : JNI_FALSE;
    
// parameter map
    jobject jMap = createMap(parameters);
    if (jMap == (jobject)0) {
        return;
    }
    JNIAutoLocalRef jMapLocalRef(env,jMap);

// call
    env->CallStaticVoidMethod(clazz,method,jEventId,jMap,jTimed);    
    if (JNIThrowableUtil::logException(env,logTag) == true) {
        return;
    }      
}

/** */
void AndroidLuaLibFlurry::endTimedEvent(const string &eventId) {
// log
    ostringstream msg;
    msg << "AndroidLuaLibFlurry::endTimedEvent(" << eventId << ")";
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
    jmethodID method = env->GetStaticMethodID(
        clazz,"endTimedEvent","(Ljava/lang/String;)V");
    if (method == (jmethodID)0) {
        Log::trace(logTag,"Method not found");
        return;
    }
    
// identifier
    jstring jEventId = env->NewStringUTF(eventId.c_str());
    JNIAutoLocalRef jEventIdLocalRef(env,jEventId);
    
// call
    env->CallStaticVoidMethod(clazz,method,jEventId);
    if (JNIThrowableUtil::logException(env,logTag) == true) {
        return;
    }      
}

/** */
void AndroidLuaLibFlurry::endTimedEvent(const string &eventId,
    map<string,string> &parameters) {
// log
    ostringstream msg;
    msg << "AndroidLuaLibFlurry::endTimedEvent(" << eventId <<
        ") with parameters";
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
    jmethodID method = env->GetStaticMethodID(
        clazz,"endTimedEvent","(Ljava/lang/String;Ljava/util/Map;)V");
    if (method == (jmethodID)0) {
        Log::trace(logTag,"Method not found");
        return;
    }  
    
// identifier
    jstring jEventId = env->NewStringUTF(eventId.c_str());
    JNIAutoLocalRef jEventIdLocalRef(env,jEventId);    
    
// parameter map
    jobject jMap = createMap(parameters);
    if (jMap == (jobject)0) {
        return;
    }
    JNIAutoLocalRef jMapLocalRef(env,jMap);

// call
    env->CallStaticVoidMethod(clazz,method,jEventId,jMap);
    if (JNIThrowableUtil::logException(env,logTag) == true) {
        return;
    }          
}

} // namespace
    
} // namespace