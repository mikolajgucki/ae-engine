#include <memory>
#include <jni.h>

#include "Log.h"
#include "JNIAutoLocalStr.h"
#include "AndroidLuaLibTapjoy.h"
#include "TapjoyLuaExtraLib.h"
#include "ae_sdl2.h"

using namespace std;
using namespace ae;
using namespace ae::jni;
using namespace ae::tapjoy;

/// The log cat.
static const char *logTag = "AE/Tapjoy";

/// The Android implementation of the Lua library.
static AndroidLuaLibTapjoy *luaLibTapjoy;

/** */
extern "C" {
    /** */
    void Java_com_andcreations_tapjoy_AETapjoy_loadLuaLib(
        JNIEnv *env,jclass clazz);
    
    /** */
    void Java_com_andcreations_tapjoy_AETapjoy_connectionSucceeded(
        JNIEnv *env,jclass clazz);
    
    /** */
    void Java_com_andcreations_tapjoy_AETapjoy_connectionFailed(
        JNIEnv *env,jclass clazz);
    
    /** */
    void Java_com_andcreations_tapjoy_AETapjoy_requestSucceeded(
        JNIEnv *env,jclass clazz,jstring jPlacement);
    
    /** */
    void Java_com_andcreations_tapjoy_AETapjoy_requestFailed(
        JNIEnv *env,jclass clazz,jstring jPlacement,jstring jError);
    
    /** */
    void Java_com_andcreations_tapjoy_AETapjoy_contentIsReady(
        JNIEnv *env,jclass clazz,jstring jPlacement);
    
    /** */
    void Java_com_andcreations_tapjoy_AETapjoy_contentShown(
        JNIEnv *env,jclass clazz,jstring jPlacement);
    
    /** */
    void Java_com_andcreations_tapjoy_AETapjoy_contentDismissed(
        JNIEnv *env,jclass clazz,jstring jPlacement);
};

/** */
void Java_com_andcreations_tapjoy_AETapjoy_loadLuaLib(JNIEnv *env,jclass clazz) {
    if (luaLibTapjoy != (AndroidLuaLibTapjoy *)0) {
        Log::warning(logTag,"Tapjoy Lua library already loaded. "
            "Skipping loading again.");
        return;
    }    
    
    luaLibTapjoy = new (nothrow) AndroidLuaLibTapjoy();
    if (luaLibTapjoy == (AndroidLuaLibTapjoy *)0) {
        Log::error(logTag,"Failed to load Tapjoy library. No memory.");
        return;
    }
    
    TapjoyLuaExtraLib *lib = new (nothrow) TapjoyLuaExtraLib(luaLibTapjoy);
    if (lib == (TapjoyLuaExtraLib *)0) {
        Log::error(logTag,"Failed to load Tapjoy library. No memory.");
        return;
    }
    
    aeAddLuaExtraLib(lib);
}

/** */
void Java_com_andcreations_tapjoy_AETapjoy_connectionSucceeded(
    JNIEnv *env,jclass clazz) {
//
    luaLibTapjoy->connectionSucceeded();
}
    
/** */
void Java_com_andcreations_tapjoy_AETapjoy_connectionFailed(
    JNIEnv *env,jclass clazz) {
//
    luaLibTapjoy->connectionFailed();
}

/** */
void Java_com_andcreations_tapjoy_AETapjoy_requestSucceeded(
    JNIEnv *env,jclass clazz,jstring jPlacement) {
//
    const char *placement = env->GetStringUTFChars(jPlacement,(jboolean *)0);
    JNIAutoLocalStr placementLocalStr(env,jPlacement,placement);
    
    luaLibTapjoy->requestSucceeded(string(placement));
}

/** */
void Java_com_andcreations_tapjoy_AETapjoy_requestFailed(
    JNIEnv *env,jclass clazz,jstring jPlacement,jstring jError) {
//
    const char *placement = env->GetStringUTFChars(jPlacement,(jboolean *)0);
    JNIAutoLocalStr placementLocalStr(env,jPlacement,placement);
    
    const char *error = env->GetStringUTFChars(jError,(jboolean *)0);
    JNIAutoLocalStr errorLocalStr(env,jError,error);
    
    luaLibTapjoy->requestFailed(string(placement),string(error));
}

/** */
void Java_com_andcreations_tapjoy_AETapjoy_contentIsReady(
    JNIEnv *env,jclass clazz,jstring jPlacement) {
//
    const char *placement = env->GetStringUTFChars(jPlacement,(jboolean *)0);
    JNIAutoLocalStr placementLocalStr(env,jPlacement,placement);
    
    luaLibTapjoy->contentIsReady(string(placement));
}

/** */
void Java_com_andcreations_tapjoy_AETapjoy_contentShown(
    JNIEnv *env,jclass clazz,jstring jPlacement) {
//
    const char *placement = env->GetStringUTFChars(jPlacement,(jboolean *)0);
    JNIAutoLocalStr placementLocalStr(env,jPlacement,placement);
    
    luaLibTapjoy->contentShown(string(placement));
}

/** */
void Java_com_andcreations_tapjoy_AETapjoy_contentDismissed(
    JNIEnv *env,jclass clazz,jstring jPlacement) {
//
    const char *placement = env->GetStringUTFChars(jPlacement,(jboolean *)0);
    JNIAutoLocalStr placementLocalStr(env,jPlacement,placement);
    
    luaLibTapjoy->contentDismissed(string(placement));
}