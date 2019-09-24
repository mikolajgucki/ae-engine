#include "Log.h"
#include "JNIThrowableUtil.h"

using namespace std;

namespace ae {
    
namespace jni {
    
/** */
bool JNIThrowableUtil::hasException() {
    return env->ExceptionCheck() == JNI_TRUE;
}

/** */
void JNIThrowableUtil::clearException() {
    env->ExceptionClear();
}
    
/** */
void JNIThrowableUtil::toString(jthrowable throwable,string &result) {
// throwable class
    jclass throwableClass = env->FindClass("java/lang/Throwable");
    jmethodID throwableToString = env->GetMethodID(throwableClass,
        "toString","()Ljava/lang/String;");
    
// call toString
    jstring toStringObj = (jstring)env->CallObjectMethod(
        throwable,throwableToString);
    const char *toStringCStr = env->GetStringUTFChars(toStringObj,0);
    
// append
    result.append(toStringCStr);
    
// release
    env->ReleaseStringUTFChars(toStringObj,toStringCStr);
    env->DeleteLocalRef(toStringObj);
}    

/** */
bool JNIThrowableUtil::toString(std::string &str) {
    if (hasException() == true) {
        jthrowable exception = env->ExceptionOccurred();
        clearException();
        toString(exception,str);
        
        return true;
    }
    
    return false;
}

/** */
void JNIThrowableUtil::getStackTrace(jthrowable throwable,
    vector<string> &stackTrace) {
// throwable class
    jclass throwableClass = env->FindClass("java/lang/Throwable");
    jmethodID throwableGetStackTrace = env->GetMethodID(throwableClass,
        "getStackTrace","()[Ljava/lang/StackTraceElement;");
    
// stack element class
    jclass elementClass = env->FindClass("java/lang/StackTraceElement");
    jmethodID elementToString = env->GetMethodID(elementClass,
        "toString","()Ljava/lang/String;");
    
// get stack elements
    jobjectArray elements = (jobjectArray)env->CallObjectMethod(
        throwable,throwableGetStackTrace);
    jsize elementsLength = env->GetArrayLength(elements);
    
// for each stack element
    for (int index = 0; index < elementsLength; index++) {
        jobject element = env->GetObjectArrayElement(elements,index);
        
    // call toString
        jstring toStringObj = (jstring)env->CallObjectMethod(
            element,elementToString);
        const char *toStringCStr = env->GetStringUTFChars(toStringObj,0);
        
    // append
        string toString(toStringCStr);
        stackTrace.push_back(toString);
        
    // release
        env->ReleaseStringUTFChars(toStringObj,toStringCStr);
        env->DeleteLocalRef(toStringObj);        
    }
}

/** */
bool JNIThrowableUtil::logException(const char *logTag) {
    if (env->ExceptionCheck()) {
        jthrowable exception = env->ExceptionOccurred();
        env->ExceptionClear();
        
    // toString
        string str;
        toString(exception,str);
        str.append("\n");
        
    // stack trace
        vector<string> stackTrace;
        getStackTrace(exception,stackTrace);
        for (int index = 0; index < stackTrace.size(); index++) {
            str.append("        at ");
            str.append(stackTrace[index]);
            str.append("\n");
        }
        
        Log::error(logTag,str);        
        return true;
    }
    
    return false;
}    
    
} // namespace
    
} // namespace