#include <memory>
#include <jni.h>

#include "Log.h"
#include "AndroidLuaLibAppLovin.h"
#include "AppLovinLuaExtraLib.h"
#include "ae_sdl2.h"

using namespace std;
using namespace ae;
using namespace ae::applovin;

/// The log tag.
static const char *logTag = "AE/AppLovin";

/// The Android implementation of the Lua library.
static AndroidLuaLibAppLovin *luaLibAppLovin;

/** */
extern "C" {
    /** */
    void Java_com_andcreations_applovin_AEAppLovin_loadLuaLib(JNIEnv *env);
    
    /** */
    void Java_com_andcreations_applovin_AEAppLovin_interstitialAdLoaded(
        JNIEnv *env);
    
    /** */
    void Java_com_andcreations_applovin_AEAppLovin_interstitialAdNoFill(
        JNIEnv *env);
    
    /** */
    void Java_com_andcreations_applovin_AEAppLovin_interstitialAdFailed(
        JNIEnv *env,jint errorCode);
    
    /** */
    void Java_com_andcreations_applovin_AEAppLovin_interstitialAdDisplayed(
        JNIEnv *env);
    
    /** */
    void Java_com_andcreations_applovin_AEAppLovin_interstitialAdHidden(
        JNIEnv *env);
    
    /** */
    void Java_com_andcreations_applovin_AEAppLovin_interstitialAdClicked(
        JNIEnv *env);
};

/** */
void Java_com_andcreations_applovin_AEAppLovin_loadLuaLib(JNIEnv *env) {
    if (luaLibAppLovin != (AndroidLuaLibAppLovin *)0) {
        Log::warning(logTag,"AppLovin Lua library already loaded. "
            "Skipping loading again.");
        return;        
    }
    
    luaLibAppLovin = new (nothrow) AndroidLuaLibAppLovin();
    if (luaLibAppLovin == (AndroidLuaLibAppLovin *)0) {
        Log::error(logTag,"Failed to load applovin library. No memory.");
        return;
    }
    
    AppLovinLuaExtraLib *lib = new (nothrow) AppLovinLuaExtraLib(luaLibAppLovin);
    if (lib == (AppLovinLuaExtraLib *)0) {
        delete luaLibAppLovin;
        Log::error(logTag,"Failed to load applovin library. No memory.");
        return;        
    }
    
    aeAddLuaExtraLib(lib);    
}

/** */
void Java_com_andcreations_applovin_AEAppLovin_interstitialAdLoaded(
    JNIEnv *env) {
//
    luaLibAppLovin->interstitialAdLoaded();
}

/** */
void Java_com_andcreations_applovin_AEAppLovin_interstitialAdNoFill(
    JNIEnv *env) {
//
    luaLibAppLovin->interstitialAdNoFill();
}
    
/** */
void Java_com_andcreations_applovin_AEAppLovin_interstitialAdFailed(
    JNIEnv *env,jint errorCode) {
//
    luaLibAppLovin->interstitialAdFailed((int)errorCode);
}

/** */
void Java_com_andcreations_applovin_AEAppLovin_interstitialAdDisplayed(
    JNIEnv *env) {
//
    luaLibAppLovin->interstitialAdDisplayed();
}

/** */
void Java_com_andcreations_applovin_AEAppLovin_interstitialAdHidden(
    JNIEnv *env) {
//
    luaLibAppLovin->interstitialAdHidden();
}

/** */
void Java_com_andcreations_applovin_AEAppLovin_interstitialAdClicked(
    JNIEnv *env) {
//
    luaLibAppLovin->interstitialAdClicked();
}
