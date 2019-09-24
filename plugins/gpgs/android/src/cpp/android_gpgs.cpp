#include <memory>
#include <jni.h>

#include "Log.h"
#include "GPGSLuaExtraLib.h"
#include "ae_sdl2.h"
#include "AndroidLuaLibGPGS.h"

using namespace std;
using namespace ae;
using namespace ae::gpgs;

/// The log cat.
static const char *logTag = "AE/GPGS";

/// The Android implementation of the Lua library.
static AndroidLuaLibGPGS *luaLibGpgs;

/** */
extern "C" {
    /** */
    void Java_com_andcreations_gpgs_AEGPGS_loadLuaLib(
        JNIEnv *env,jclass clazz);
    
    /** */
    void Java_com_andcreations_gpgs_AEGPGS_signedIn(JNIEnv *env,jclass clazz);    
    
    /** */
    void Java_com_andcreations_gpgs_AEGPGS_signedOut(JNIEnv *env,jclass clazz);    
};

/** */
void Java_com_andcreations_gpgs_AEGPGS_loadLuaLib(
    JNIEnv *env,jclass clazz) {
//
    if (luaLibGpgs != (AndroidLuaLibGPGS *)0) {
        Log::warning(logTag,"GPGS Lua library already loaded. "
            "Skipping loading again.");
        return;
    }

    luaLibGpgs = new (nothrow) AndroidLuaLibGPGS();
    if (luaLibGpgs == (AndroidLuaLibGPGS *)0) {
        Log::error(logTag,"Failed to load gpgs library. No memory.");
        return;            
    }
    
    GPGSLuaExtraLib *lib = new (nothrow) GPGSLuaExtraLib(luaLibGpgs);
    if (lib == (GPGSLuaExtraLib *)0) {
        delete luaLibGpgs;
        Log::error(logTag,"Failed to load gpgs library. No memory.");
        return;         
    }
    
    aeAddLuaExtraLib(lib);    
}

/** */
void Java_com_andcreations_gpgs_AEGPGS_signedIn(JNIEnv *env,jclass clazz) {
    luaLibGpgs->signedIn();
}    

/** */
void Java_com_andcreations_gpgs_AEGPGS_signedOut(JNIEnv *env,jclass clazz) {
    luaLibGpgs->signedOut();
}    
