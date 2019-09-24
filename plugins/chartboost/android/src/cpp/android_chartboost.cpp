#include <memory>
#include <jni.h>

#include "Log.h"
#include "JNIAutoLocalStr.h"
#include "AndroidLuaLibChartboost.h"
#include "ChartboostLuaExtraLib.h"
#include "ae_sdl2.h"

using namespace std;
using namespace ae;
using namespace ae::jni;
using namespace ae::chartboost;

/// The log cat.
static const char *logTag = "AE/Chartboost";

/// The Android implementation of the Lua library.
static AndroidLuaLibChartboost *luaLibChartboost;

/** */
extern "C" {
    /** */
    void Java_com_andcreations_chartboost_AEChartboost_loadLuaLib(
        JNIEnv *env,jclass clazz);
    
    /** */
    void Java_com_andcreations_chartboost_AEChartboost_didCacheInterstitial(
        JNIEnv *env,jclass clazz,jstring jLocation);
    
    /** */
    void Java_com_andcreations_chartboost_AEChartboost_didFailToLoadInterstitial(
        JNIEnv *env,jclass clazz,jstring jLocation);
    
    /** */
    void Java_com_andcreations_chartboost_AEChartboost_didClickInterstitial(
        JNIEnv *env,jclass clazz,jstring jLocation);
    
    /** */
    void Java_com_andcreations_chartboost_AEChartboost_didCloseInterstitial(
        JNIEnv *env,jclass clazz,jstring jLocation);
    
    /** */
    void Java_com_andcreations_chartboost_AEChartboost_didDismissInterstitial(
        JNIEnv *env,jclass clazz,jstring jLocation);
    
    /** */
    void Java_com_andcreations_chartboost_AEChartboost_didDisplayInterstitial(
        JNIEnv *env,jclass clazz,jstring jLocation);
    
};

/** */
void Java_com_andcreations_chartboost_AEChartboost_loadLuaLib(JNIEnv *env,
    jclass clazz) {
//
    if (luaLibChartboost != (AndroidLuaLibChartboost *)0) {
        Log::warning(logTag,"Chartboost Lua library already loaded. "
            "Skipping loading again.");
        return;
    }
    
    luaLibChartboost = new (nothrow) AndroidLuaLibChartboost();
    if (luaLibChartboost == (AndroidLuaLibChartboost *)0) {
        Log::error(logTag,"Failed to load Chartboost library. No memory.");
        return;        
    }
    
    ChartboostLuaExtraLib *lib = new (nothrow) ChartboostLuaExtraLib(
        luaLibChartboost);
    if (lib == (ChartboostLuaExtraLib *)0) {
        delete luaLibChartboost;
        Log::error(logTag,"Failed to load Chartboost library. No memory.");
        return;         
    }
    
    aeAddLuaExtraLib(lib);
}

/** */
void Java_com_andcreations_chartboost_AEChartboost_didCacheInterstitial(
    JNIEnv *env,jclass clazz,jstring jLocation) {
//
    const char *location = env->GetStringUTFChars(jLocation,(jboolean *)0);
    JNIAutoLocalStr locationLocalStr(env,jLocation,location);
    luaLibChartboost->didCacheInterstitial(location);
}

/** */
void Java_com_andcreations_chartboost_AEChartboost_didFailToLoadInterstitial(
    JNIEnv *env,jclass clazz,jstring jLocation) {
//
    const char *location = env->GetStringUTFChars(jLocation,(jboolean *)0);
    JNIAutoLocalStr locationLocalStr(env,jLocation,location);
    luaLibChartboost->didFailToLoadInterstitial(location);
}

/** */
void Java_com_andcreations_chartboost_AEChartboost_didClickInterstitial(
    JNIEnv *env,jclass clazz,jstring jLocation) {
//
    const char *location = env->GetStringUTFChars(jLocation,(jboolean *)0);
    JNIAutoLocalStr locationLocalStr(env,jLocation,location);
    luaLibChartboost->didClickInterstitial(location);
}

/** */
void Java_com_andcreations_chartboost_AEChartboost_didCloseInterstitial(
    JNIEnv *env,jclass clazz,jstring jLocation) {
//
    const char *location = env->GetStringUTFChars(jLocation,(jboolean *)0);
    JNIAutoLocalStr locationLocalStr(env,jLocation,location);
    luaLibChartboost->didCloseInterstitial(location);
}

/** */
void Java_com_andcreations_chartboost_AEChartboost_didDismissInterstitial(
    JNIEnv *env,jclass clazz,jstring jLocation) {
//
    const char *location = env->GetStringUTFChars(jLocation,(jboolean *)0);
    JNIAutoLocalStr locationLocalStr(env,jLocation,location);
    luaLibChartboost->didDismissInterstitial(location);
}

/** */
void Java_com_andcreations_chartboost_AEChartboost_didDisplayInterstitial(
    JNIEnv *env,jclass clazz,jstring jLocation) {
//
    const char *location = env->GetStringUTFChars(jLocation,(jboolean *)0);
    JNIAutoLocalStr locationLocalStr(env,jLocation,location);
    luaLibChartboost->didDisplayInterstitial(location);
}