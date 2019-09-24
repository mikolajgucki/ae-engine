#include <memory>
#include <jni.h>

#include "Log.h"
#include "AndroidLuaLibVungle.h"
#include "VungleLuaExtraLib.h"
#include "ae_sdl2.h"

using namespace std;
using namespace ae;
using namespace ae::vungle;

/// The log tag.
static const char *logTag = "AE/Vungle";

/// The Android implementation of the Lua library.
static AndroidLuaLibVungle *luaLibVungle;

/** */
extern "C" {
    /** */
    void Java_com_andcreations_vungle_AEVungle_loadLuaLib(JNIEnv *env);

    /** */
    void Java_com_andcreations_vungle_AEVungle_onAdPlayableChanged(
        JNIEnv *env,jclass clazz,jboolean isAdPlayable);
    
    /** */
    void Java_com_andcreations_vungle_AEVungle_willShowAd(JNIEnv *env);
    
    /** */
    void Java_com_andcreations_vungle_AEVungle_willCloseAd(JNIEnv *env);
    
    /** */
    void Java_com_andcreations_vungle_AEVungle_adClicked(JNIEnv *env);
    
    /** */
    void Java_com_andcreations_vungle_AEVungle_viewCompleted(JNIEnv *env);
};

/** */
void Java_com_andcreations_vungle_AEVungle_loadLuaLib(JNIEnv *env) {
    if (luaLibVungle != (AndroidLuaLibVungle *)0) {
        Log::warning(logTag,"Vungle Lua library already loaded. "
            "Skipping loading again.");
        return;
    }
    
    luaLibVungle = new (nothrow) AndroidLuaLibVungle();
    if (luaLibVungle == (AndroidLuaLibVungle *)0) {
        Log::error(logTag,"Failed to load vungle library. No memory.");
        return;
    }
    
    VungleLuaExtraLib *lib = new (nothrow) VungleLuaExtraLib(luaLibVungle);
    if (lib == (VungleLuaExtraLib *)0) {
        delete luaLibVungle;
        Log::error(logTag,"Failed to load vungle library. No memory.");
        return;        
    }
    
    aeAddLuaExtraLib(lib); 
}

/** */
void Java_com_andcreations_vungle_AEVungle_onAdPlayableChanged(
    JNIEnv *env,jclass clazz,jboolean isAdPlayable) {
//
    luaLibVungle->adPlayableChanged(isAdPlayable == JNI_TRUE ? true : false);
}

/** */
void Java_com_andcreations_vungle_AEVungle_willShowAd(JNIEnv *env) {
    luaLibVungle->willShowAd();
}

/** */
void Java_com_andcreations_vungle_AEVungle_willCloseAd(JNIEnv *env) {
    luaLibVungle->willCloseAd();
}

/** */
void Java_com_andcreations_vungle_AEVungle_adClicked(JNIEnv *env) {
    luaLibVungle->adClicked();
}


/** */
void Java_com_andcreations_vungle_AEVungle_viewCompleted(JNIEnv *env) {
    luaLibVungle->viewCompleted();
}