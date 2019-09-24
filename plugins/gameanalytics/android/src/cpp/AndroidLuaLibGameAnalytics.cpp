#include "Log.h"
#include "JNIAutoLocalRef.h"
#include "JNIThrowableUtil.h"
#include "AndroidLuaLibGameAnalytics.h"

extern "C" {
// Declaration. Defined in SDL.
JNIEnv *Android_JNI_GetEnv(void);
}

using namespace std;
using namespace ae::jni;

namespace ae {
    
namespace gameanalytics {
    
/// The log tag.
static const char *logTag = "AE/GameAnalytics";
    
/** The Java class name. */
static const char *jClassName =
    "com/andcreations/gameanalytics/AEGameAnalytics"; 

/** */
void AndroidLuaLibGameAnalytics::getJNIEnv() {
    env = Android_JNI_GetEnv();
}

/** */
const char *AndroidLuaLibGameAnalytics::progressionStatusToStr(
    ProgressionStatus status) {
//
    if (status == PROGRESSION_STATUS_START) {
        return "start";
    }
    else if (status == PROGRESSION_STATUS_COMPLETE) {
        return "complete";
    }
    else if (status == PROGRESSION_STATUS_FAIL) {
        return "fail";
    }
    
    return "";
}

/** */
const char *AndroidLuaLibGameAnalytics::flowTypeToStr(FlowType flowType) {
    if (flowType == FLOW_TYPE_SINK) {
        return "sink";
    }
    else if (flowType == FLOW_TYPE_SOURCE) {
        return "source";
    }
    
    return "";
}

/** */
void AndroidLuaLibGameAnalytics::init() {
    Log::trace(logTag,"AndroidLuaLibGameAnalytics::init()");
}

/** */
void AndroidLuaLibGameAnalytics::addProgressionEvent(ProgressionStatus status,
    const string& progression01) {
//
    Log::trace(logTag,"AndroidLuaLibGameAnalytics::addProgressionEvent()");
    
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
        clazz,"addProgressionEvent","(Ljava/lang/String;Ljava/lang/String;)V");
    if (method == (jmethodID)0) {
        Log::trace(logTag,"Method not found");
        return;
    }
    
// status
    jstring jStatus = env->NewStringUTF(progressionStatusToStr(status));    
    JNIAutoLocalRef jStatusLocalRef(env,jStatus);

// progression 01
    jstring jProgression01 = env->NewStringUTF(progression01.c_str());
    JNIAutoLocalRef jProgression01LocalRef(env,jProgression01);
    
// call
    env->CallStaticVoidMethod(clazz,method,jStatus,jProgression01);
    if (JNIThrowableUtil::logException(env,logTag) == true) {
        return;
    }      
}   

/** */
void AndroidLuaLibGameAnalytics::addProgressionEvent(ProgressionStatus status,
    const string& progression01,const string& progression02) {
//
    Log::trace(logTag,"AndroidLuaLibGameAnalytics::addProgressionEvent()");
    
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
        clazz,"addProgressionEvent",
        "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
    if (method == (jmethodID)0) {
        Log::trace(logTag,"Method not found");
        return;
    }
    
// status
    jstring jStatus = env->NewStringUTF(progressionStatusToStr(status));    
    JNIAutoLocalRef jStatusLocalRef(env,jStatus);

// progression 01
    jstring jProgression01 = env->NewStringUTF(progression01.c_str());
    JNIAutoLocalRef jProgression01LocalRef(env,jProgression01);

// progression 02
    jstring jProgression02 = env->NewStringUTF(progression02.c_str());
    JNIAutoLocalRef jProgression02LocalRef(env,jProgression02);
    
// call
    env->CallStaticVoidMethod(clazz,method,
        jStatus,jProgression01,jProgression02);
    if (JNIThrowableUtil::logException(env,logTag) == true) {
        return;
    }    
}

/** */
void AndroidLuaLibGameAnalytics::addProgressionEvent(ProgressionStatus status,
    const string& progression01,const string& progression02,
    const string& progression03) {
//
    Log::trace(logTag,"AndroidLuaLibGameAnalytics::addProgressionEvent()");
    
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
        clazz,"addProgressionEvent","(Ljava/lang/String;Ljava/lang/String;"
        "Ljava/lang/String;Ljava/lang/String;)V");
    if (method == (jmethodID)0) {
        Log::trace(logTag,"Method not found");
        return;
    }
    
// status
    jstring jStatus = env->NewStringUTF(progressionStatusToStr(status));    
    JNIAutoLocalRef jStatusLocalRef(env,jStatus);

// progression 01
    jstring jProgression01 = env->NewStringUTF(progression01.c_str());
    JNIAutoLocalRef jProgression01LocalRef(env,jProgression01);

// progression 02
    jstring jProgression02 = env->NewStringUTF(progression02.c_str());
    JNIAutoLocalRef jProgression02LocalRef(env,jProgression02);

// progression 03
    jstring jProgression03 = env->NewStringUTF(progression03.c_str());
    JNIAutoLocalRef jProgression03LocalRef(env,jProgression03);
    
// call
    env->CallStaticVoidMethod(clazz,method,
        jStatus,jProgression01,jProgression02,jProgression03);
    if (JNIThrowableUtil::logException(env,logTag) == true) {
        return;
    }          
}

/** */
void AndroidLuaLibGameAnalytics::addDesignEvent(const string& eventId) {
    Log::trace(logTag,"AndroidLuaLibGameAnalytics::addDesignEvent()");
    
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
        clazz,"addDesignEvent","(Ljava/lang/String;)V");
    if (method == (jmethodID)0) {
        Log::trace(logTag,"Method not found");
        return;
    }
    
// event identifier
    jstring jEventId = env->NewStringUTF(eventId.c_str());    
    JNIAutoLocalRef jStatusLocalRef(env,jEventId);
    
// call
    env->CallStaticVoidMethod(clazz,method,jEventId);
    if (JNIThrowableUtil::logException(env,logTag) == true) {
        return;
    }      
}

/** */
void AndroidLuaLibGameAnalytics::addDesignEvent(const string& eventId,
    double value) {
//
    Log::trace(logTag,"AndroidLuaLibGameAnalytics::addDesignEvent()");
    
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
        clazz,"addDesignEvent","(Ljava/lang/String;D)V");
    if (method == (jmethodID)0) {
        Log::trace(logTag,"Method not found");
        return;
    }
    
// event identifier
    jstring jEventId = env->NewStringUTF(eventId.c_str());    
    JNIAutoLocalRef jStatusLocalRef(env,jEventId);
    
// call
    env->CallStaticVoidMethod(clazz,method,jEventId,(jdouble)value);
    if (JNIThrowableUtil::logException(env,logTag) == true) {
        return;
    }      
}

/** */
void AndroidLuaLibGameAnalytics::addErrorEvent(ErrorSeverity severity,
    const string& msg) {
//
    Log::trace(logTag,
        "AndroidLuaLibGameAnalytics::addErrorEvent(not implemented)");
}

/** */
void AndroidLuaLibGameAnalytics::addBusinessEvent(const string& currency,
    int amount,const string& itemType,const string& itemId,
    const string& cartType,const string *receipt,
    const string *store,const string *signature) {
// log message
    ostringstream msg;
    msg << "AndroidLuaLibGameAnalytics::addBusinessEvent(" << currency <<
        "," << amount << "," << itemType << "," << itemId << "," << cartType;
        
    if (receipt != (const string *)0) {
        msg << "," << receipt;
    }
    else {
        msg << ",null";
    }
    if (store != (const string *)0) {
        msg << "," << store;
    }
    else {
        msg << ",null";
    }
    if (signature != (const string *)0) {
        msg << "," << signature;
    }
    else {
        msg << ",null";
    }
    msg << ")";
        
// log
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
        clazz,"addBusinessEvent","(Ljava/lang/String;ILjava/lang/String;"
        "Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;"
        "Ljava/lang/String;Ljava/lang/String;)V");
    if (method == (jmethodID)0) {
        Log::trace(logTag,"Method not found");
        return;
    }
    
// currency
    jstring jCurrency = env->NewStringUTF(currency.c_str());
    JNIAutoLocalRef jCurrencyLocalRef(env,jCurrency);
    
// item type
    jstring jItemType = env->NewStringUTF(itemType.c_str());
    JNIAutoLocalRef jItemTypeLocalRef(env,jItemType);
    
// item identifier
    jstring jItemId = env->NewStringUTF(itemId.c_str());
    JNIAutoLocalRef jItemIdLocalRef(env,jItemId);    
    
// cart type
    jstring jCartType = env->NewStringUTF(cartType.c_str());
    JNIAutoLocalRef jCartTypeLocalRef(env,jCartType);
    
// receipt
    jstring jReceipt = (jstring)0;
    if (receipt != (const string *)0) {
        jReceipt = env->NewStringUTF(receipt->c_str());
    }
    
// store
    jstring jStore = (jstring)0;
    if (store != (const string *)0) {
        jStore = env->NewStringUTF(store->c_str());
    }
    
// signature
    jstring jSignature = (jstring)0;
    if (signature != (const string *)0) {
        jSignature = env->NewStringUTF(signature->c_str());
    }
    
// call
    env->CallStaticVoidMethod(clazz,method,jCurrency,(jint)amount,
        jItemType,jItemId,jCartType,jReceipt,jStore,jSignature);
    if (JNIThrowableUtil::logException(env,logTag) == true) {
    }   
    
    if (jReceipt != (jstring)0) {
        env->DeleteLocalRef(jReceipt);
    }
    if (jStore != (jstring)0) {
        env->DeleteLocalRef(jStore);
    }
    if (jSignature != (jstring)0) {
        env->DeleteLocalRef(jSignature);
    }
}

/** */
void AndroidLuaLibGameAnalytics::addResourceEvent(FlowType flowType,
    const string &currency,float amount,const string& itemType,
    const string &itemId) {
// 
    Log::trace(logTag,"AndroidLuaLibGameAnalytics::addResourceEvent()");
    
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
        clazz,"addResourceEvent","(Ljava/lang/String;Ljava/lang/String;"
        "FLjava/lang/String;Ljava/lang/String;)V");
    if (method == (jmethodID)0) {
        Log::trace(logTag,"Method not found");
        return;
    }    

// flow type
    jstring jFlowType = env->NewStringUTF(flowTypeToStr(flowType));
    JNIAutoLocalRef jFlowTypeLocalRef(env,jFlowType);
    
// currency
    jstring jCurrency = env->NewStringUTF(currency.c_str());
    JNIAutoLocalRef jCurrencyLocalRef(env,jCurrency);
    
// item type
    jstring jItemType = env->NewStringUTF(itemType.c_str());
    JNIAutoLocalRef jItemTypeLocalRef(env,jItemType);
    
// item identifier
    jstring jItemId = env->NewStringUTF(itemId.c_str());
    JNIAutoLocalRef jItemIdLocalRef(env,jItemId); 
    
// call
    env->CallStaticVoidMethod(clazz,method,jFlowType,jCurrency,amount,
        jItemType,jItemId);
    if (JNIThrowableUtil::logException(env,logTag) == true) {
        return;
    }     
}

} // namespace
    
} // namespace