#include <memory>
#include <jni.h>

#include "Log.h"
#include "JNIAutoLocalStr.h"
#include "AndroidLuaLibAdColony.h"
#include "AdColonyLuaExtraLib.h"
#include "ae_sdl2.h"

using namespace std;
using namespace ae;
using namespace ae::jni;
using namespace ae::adcolony;

/// The log tag.
static const char *logTag = "lua.adcolony";

/// The Android implementation of the Lua library.
static AndroidLuaLibAdColony *luaLibAdColony;

/** */
extern "C" {
    /** */
    void Java_com_andcreations_adcolony_AEAdColony_loadLuaLib(JNIEnv *env);
    
    /** */
    void Java_com_andcreations_adcolony_AEAdColony_interstitialAvailable(
        JNIEnv *env,jclass clazz,jstring jZoneId);
    
    /** */
    void Java_com_andcreations_adcolony_AEAdColony_interstitialUnavailable(
        JNIEnv *env,jclass clazz,jstring jZoneId);
    
    /** */
    void Java_com_andcreations_adcolony_AEAdColony_interstitialOpened(
        JNIEnv *env,jclass clazz,jstring jZoneId);
    
    /** */
    void Java_com_andcreations_adcolony_AEAdColony_interstitialClosed(
        JNIEnv *env,jclass clazz,jstring jZoneId);
};

/** */
void Java_com_andcreations_adcolony_AEAdColony_loadLuaLib(JNIEnv *env) {
    if (luaLibAdColony != (AndroidLuaLibAdColony *)0) {
        Log::warning(logTag,"AdColony Lua library already loaded. "
            "Skipping loading again.");
        return;
    }
    
    luaLibAdColony = new (nothrow) AndroidLuaLibAdColony();
    if (luaLibAdColony == (AndroidLuaLibAdColony *)0) {
        Log::error(logTag,"Failed to load adcolony library. No memory.");
        return;
    }
    
    AdColonyLuaExtraLib *lib =
        new (nothrow) AdColonyLuaExtraLib(luaLibAdColony);
    if (lib == (AdColonyLuaExtraLib *)0) {
        delete luaLibAdColony;
        Log::error(logTag,"Failed to load adcolony library. No memory.");
        return;
    }
    
    aeAddLuaExtraLib(lib);
}

/** */
void Java_com_andcreations_adcolony_AEAdColony_interstitialAvailable(
    JNIEnv *env,jclass clazz,jstring jZoneId) {
//
    const char *zoneId = env->GetStringUTFChars(jZoneId,(jboolean *)0);
    JNIAutoLocalStr locationLocalStr(env,jZoneId,zoneId);    
    luaLibAdColony->interstitialAvailable(zoneId);
}
    
/** */
void Java_com_andcreations_adcolony_AEAdColony_interstitialUnavailable(
    JNIEnv *env,jclass clazz,jstring jZoneId) {
//
    const char *zoneId = env->GetStringUTFChars(jZoneId,(jboolean *)0);
    JNIAutoLocalStr locationLocalStr(env,jZoneId,zoneId);    
    luaLibAdColony->interstitialUnavailable(zoneId);
}

/** */
void Java_com_andcreations_adcolony_AEAdColony_interstitialOpened(
    JNIEnv *env,jclass clazz,jstring jZoneId) {
//
    const char *zoneId = env->GetStringUTFChars(jZoneId,(jboolean *)0);
    JNIAutoLocalStr locationLocalStr(env,jZoneId,zoneId);    
    luaLibAdColony->interstitialOpened(zoneId);
}

/** */
void Java_com_andcreations_adcolony_AEAdColony_interstitialClosed(
    JNIEnv *env,jclass clazz,jstring jZoneId) {
//
    const char *zoneId = env->GetStringUTFChars(jZoneId,(jboolean *)0);
    JNIAutoLocalStr locationLocalStr(env,jZoneId,zoneId);    
    luaLibAdColony->interstitialClosed(zoneId);
}