#include "JNIAutoLocalRef.h"
#include "JNIMapUtil.h"

using namespace std;

namespace ae {
    
namespace jni {
    
/** */
bool JNIMapUtil::createHashMap() {
// class
    jclass clazz = env->FindClass("java/util/HashMap");
    JNIAutoLocalRef clazzLocalRef(env,clazz);
    
// constructor
    jmethodID ctr = env->GetMethodID(clazz,"<init>","()V");
    
// create
    map = env->NewObject(clazz,ctr);
    if (map == (jobject)0) {
        setError("Failed to create object of class java.util.HashMap");
        return false;
    }
    
    return true;
}

/** */
bool JNIMapUtil::put(const string &key,const string &value) {
// class
    jclass clazz = env->FindClass("java/util/Map");
    JNIAutoLocalRef clazzLocalRef(env,clazz);
    
// method
    jmethodID putMethod = env->GetMethodID(clazz,"put",
        "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");

// arguments
    jstring jKey = env->NewStringUTF(key.c_str());
    JNIAutoLocalRef jKeyLocalRef(env,jKey);
    jstring jValue = env->NewStringUTF(value.c_str());
    JNIAutoLocalRef jValueLocalRef(env,jValue);
    
// call
    env->CallObjectMethod(map,putMethod,jKey,jValue);
    
    return true;
}
    
} // namespace
    
} // namespace