#include <memory>
#include <jni.h>

#include "Log.h"
#include "AndroidLuaLibIAP.h"
#include "IAPLuaExtraLib.h"
#include "ae_sdl2.h"

using namespace std;
using namespace ae;
using namespace ae::iap;

/// The log cat.
static const char *logTag = "AE/IAP";

/// The Android implementation of the Lua library.
static AndroidLuaLibIAP *luaLibIap;

/** */
extern "C" {
    /** */
    void Java_com_andcreations_iap_IAP_loadLuaLib(JNIEnv *env);    
    
    /** */
    void Java_com_andcreations_iap_IAP_started(JNIEnv *env);
    
    /** */
    void Java_com_andcreations_iap_IAP_startFailed(JNIEnv *env);
    
    /** */
    void Java_com_andcreations_iap_IAP_startFailed(JNIEnv *env);
    
    /** */
    void Java_com_andcreations_iap_IAP_purchasing(JNIEnv *env,
        jclass clazz,jstring jProductId);    
    
    /** */
    void Java_com_andcreations_iap_IAP_purchased(JNIEnv *env,
        jclass clazz,jstring jProductId);
    
    /** */
    void Java_com_andcreations_iap_IAP_purchaseFailed(JNIEnv *env,
        jclass clazz,jstring jReason);
    
    /** */
    void Java_com_andcreations_iap_IAP_purchaseCanceled(JNIEnv *env);
    
    /** */
    void Java_com_andcreations_iap_IAP_alreadyOwned(JNIEnv *env,
        jclass clazz,jstring jProductId);
    
    /** */
    void Java_com_andcreations_iap_IAP_restorePurchasesFailed(JNIEnv *env,
        jclass clazz,jstring jReason);
    
    /** */
    void Java_com_andcreations_iap_IAP_purchasesRestored(JNIEnv *env);
};

/** */
void Java_com_andcreations_iap_IAP_loadLuaLib(JNIEnv *env) {
    if (luaLibIap != (AndroidLuaLibIAP *)0) {
        Log::warning(logTag,"IAP Lua library already loaded. "
            "Skipping loading again.");
        return;
    }    
    
    luaLibIap = new (nothrow) AndroidLuaLibIAP();
    if (luaLibIap == (AndroidLuaLibIAP *)0) {
        Log::error(logTag,"Failed to load iap library. No memory.");
        return;
    }
    
    IAPLuaExtraLib *lib = new (nothrow) IAPLuaExtraLib(luaLibIap);
    if (lib == (IAPLuaExtraLib *)0) {
        delete luaLibIap;
        Log::error(logTag,"Failed to load iap library. No memory.");
        return;
    }
    
    aeAddLuaExtraLib(lib);
}

/** */
void Java_com_andcreations_iap_IAP_started(JNIEnv *env) {
    luaLibIap->started();
}

/** */
void Java_com_andcreations_iap_IAP_startFailed(JNIEnv *env) {
    luaLibIap->failedToStart();
}

/** */
void Java_com_andcreations_iap_IAP_purchasing(JNIEnv *env,
    jclass clazz,jstring jProductId) {
//
    const char *productId = env->GetStringUTFChars(jProductId,(jboolean *)0);
    luaLibIap->purchasing(productId);
    env->ReleaseStringUTFChars(jProductId,productId);
}

/** */
void Java_com_andcreations_iap_IAP_purchased(JNIEnv *env,
    jclass clazz,jstring jProductId) {
//
    const char *productId = env->GetStringUTFChars(jProductId,(jboolean *)0);
    luaLibIap->purchased(productId);
    env->ReleaseStringUTFChars(jProductId,productId);
}

/** */
void Java_com_andcreations_iap_IAP_purchaseFailed(JNIEnv *env,
    jclass clazz,jstring jReason) {
//
    const char *reason = env->GetStringUTFChars(jReason,(jboolean *)0);
    luaLibIap->purchaseFailed(reason);
    env->ReleaseStringUTFChars(jReason,reason);
}

/** */
void Java_com_andcreations_iap_IAP_purchaseCanceled(JNIEnv *env) {
    luaLibIap->purchaseCanceled();
}

/** */
void Java_com_andcreations_iap_IAP_alreadyOwned(JNIEnv *env,
    jclass clazz,jstring jProductId) {
//
    const char *productId = env->GetStringUTFChars(jProductId,(jboolean *)0);
    luaLibIap->alreadyOwned(productId);
    env->ReleaseStringUTFChars(jProductId,productId);
}

/** */
void Java_com_andcreations_iap_IAP_restorePurchasesFailed(JNIEnv *env,
	jclass clazz,jstring jReason) {
//
    const char *reason = env->GetStringUTFChars(jReason,(jboolean *)0);
    luaLibIap->restorePurchasesFailed(reason);
    env->ReleaseStringUTFChars(jReason,reason);
}

/** */
void Java_com_andcreations_iap_IAP_purchasesRestored(JNIEnv *env) {
    luaLibIap->purchasesRestored();
}