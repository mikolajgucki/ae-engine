#include "JNIAutoLocalRef.h"
#include "JNIListUtil.h"

namespace ae {
    
namespace jni {    

/** The Java class name. */
static const char *jClassName = "java/util/List";   
    
/** */
int JNIListUtil::size() {
    jclass clazz = env->FindClass(jClassName);
    JNIAutoLocalRef clazzLocalRef(env,clazz);
    jmethodID method = env->GetMethodID(clazz,"size","()I");
    
    return env->CallIntMethod(list,method);
}

/** */
jobject JNIListUtil::get(int index) {
    jclass clazz = env->FindClass(jClassName);
    JNIAutoLocalRef clazzLocalRef(env,clazz);
    jmethodID method = env->GetMethodID(
        clazz,"get","(I)Ljava/lang/Object;");    
    
    return env->CallObjectMethod(list,method,index);
}
    
} // namespace
    
} // namespace