#include <memory>
#include <jni.h>

#include "Log.h"
#include "JNIAutoLocalStr.h"
#include "AndroidLuaLibUnityAds.h"
#include "UnityAdsLuaExtraLib.h"
#include "ae_sdl2.h"

using namespace std;
using namespace ae;
using namespace ae::jni;
using namespace ae::unityads;

/// The log tag.
static const char *logTag = "AE/UnityAds";


/// The Android implementation of the Lua library.
static AndroidLuaLibUnityAds *luaLibUnityAds = (AndroidLuaLibUnityAds *)0;

/** */
extern "C" {
    /** */
    void Java_com_andcreations_unityads_AEUnityAds_loadLuaLib(JNIEnv *env);
    
    /** */
    void Java_com_andcreations_unityads_AEUnityAds_setReady(
        JNIEnv *env,jclass clazz,jstring jPlacementId);
    
    /** */
    void Java_com_andcreations_unityads_AEUnityAds_started(
        JNIEnv *env,jclass clazz,jstring jPlacementId); 
    
    /** */
    void Java_com_andcreations_unityads_AEUnityAds_finished(
        JNIEnv *env,jclass clazz,jstring jPlacementId,jstring jStateStr); 
    
    /** */
    void Java_com_andcreations_unityads_AEUnityAds_failed(
        JNIEnv *env,jclass clazz,jstring jError,jstring jMsg); 
};

/** */
void Java_com_andcreations_unityads_AEUnityAds_loadLuaLib(JNIEnv *env) {
    if (luaLibUnityAds != (AndroidLuaLibUnityAds *)0) {
        Log::warning(logTag,"Unity Ads Lua library already loaded. "
            "Skipping loading again.");
        return;
    }
    
    luaLibUnityAds = new (nothrow) AndroidLuaLibUnityAds();
    if (luaLibUnityAds == (AndroidLuaLibUnityAds *)0) {
        Log::error(logTag,"Failed to load unityads library. No memory.");
        return;
    }
    
    UnityAdsLuaExtraLib *lib =
        new (nothrow) UnityAdsLuaExtraLib(luaLibUnityAds);
    if (lib == (UnityAdsLuaExtraLib *)0) {
        delete luaLibUnityAds;
        Log::error(logTag,"Failed to load unityads library. No memory.");
        return;        
    }
    
    aeAddLuaExtraLib(lib);     
}

/** */
void Java_com_andcreations_unityads_AEUnityAds_setReady(
    JNIEnv *env,jclass clazz,jstring jPlacementId) {
#ifdef AE_DEBUG
    if (luaLibUnityAds == (AndroidLuaLibUnityAds *)0) {
        Log::error(logTag,"Unity Ads library not loaded");
        return;
    }
#endif
    
    const char *placementId = env->GetStringUTFChars(jPlacementId,(jboolean *)0);
    JNIAutoLocalStr placementIdLocalStr(env,jPlacementId,placementId);
    luaLibUnityAds->setReady(placementId);
}

/** */
void Java_com_andcreations_unityads_AEUnityAds_started(
    JNIEnv *env,jclass clazz,jstring jPlacementId) {
#ifdef AE_DEBUG
    if (luaLibUnityAds == (AndroidLuaLibUnityAds *)0) {
        Log::error(logTag,"Unity Ads library not loaded");
        return;
    }
#endif
    
    const char *placementId = env->GetStringUTFChars(jPlacementId,(jboolean *)0);
    JNIAutoLocalStr placementIdLocalStr(env,jPlacementId,placementId);
    luaLibUnityAds->started(placementId);
}

/** */
void Java_com_andcreations_unityads_AEUnityAds_finished(
    JNIEnv *env,jclass clazz,jstring jPlacementId,jstring jStateStr) {
// placement
    const char *placementId = env->GetStringUTFChars(jPlacementId,(jboolean *)0);
    JNIAutoLocalStr placementIdLocalStr(env,jPlacementId,placementId);
    
// state as string
    const char *stateCStr = env->GetStringUTFChars(jStateStr,(jboolean *)0);
    JNIAutoLocalStr stateStrLocalStr(env,jStateStr,stateCStr);
    
// state
    UnityAdsCallback::FinishState state;
    const string stateStr(stateCStr);
    if (stateStr == "error") {
        state = UnityAdsCallback::FINISH_STATE_ERROR;
    }
    else if (stateStr == "skipped") {
        state = UnityAdsCallback::FINISH_STATE_SKIPPED;
    }
    else if (stateStr == "completed") {
        state = UnityAdsCallback::FINISH_STATE_COMPLETED;
    }
    
// call
    luaLibUnityAds->finished(placementId,state);
}

/** */
void Java_com_andcreations_unityads_AEUnityAds_failed(
    JNIEnv *env,jclass clazz,jstring jError,jstring jMsg) {
// error
    const char *error = env->GetStringUTFChars(jError,(jboolean *)0);
    JNIAutoLocalStr errorLocalStr(env,jError,error);
    
// message
    const char *msg = env->GetStringUTFChars(jMsg,(jboolean *)0);
    JNIAutoLocalStr msgLocalStr(env,jMsg,msg);
    
// call
    luaLibUnityAds->failed(error,msg);
}   