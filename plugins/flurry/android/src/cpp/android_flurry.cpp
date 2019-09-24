#include <memory>
#include <jni.h>

#include "Log.h"
#include "ae_sdl2.h"
#include "AndroidLuaLibFlurry.h"
#include "FlurryLuaExtraLib.h"

using namespace std;
using namespace ae;
using namespace ae::flurry;

/// The log cat.
static const char *logTag = "AE/Flurry";

/// The Android implementation of the Lua library.
static AndroidLuaLibFlurry *luaLibFlurry = (AndroidLuaLibFlurry *)0;

/** */
extern "C" {
    /** */
    void Java_com_andcreations_flurry_AEFlurry_loadLuaLib(JNIEnv *env);   
};

/** */
void Java_com_andcreations_flurry_AEFlurry_loadLuaLib(JNIEnv *env) {
    if (luaLibFlurry != (AndroidLuaLibFlurry *)0) {
        Log::warning(logTag,"Flurry Lua library already loaded. "
            "Skipping loading again.");
        return;
    }    
    
    luaLibFlurry = new (nothrow) AndroidLuaLibFlurry();
    if (luaLibFlurry == (AndroidLuaLibFlurry *)0) {
        Log::error(logTag,"Failed to load flurry library. No memory.");
        return;
    }
    
    FlurryLuaExtraLib *lib = new (nothrow) FlurryLuaExtraLib(luaLibFlurry);
    if (lib == (FlurryLuaExtraLib *)0) {
        delete luaLibFlurry;
        Log::error(logTag,"Failed to load flurry library. No memory.");
        return;
    }
    
    aeAddLuaExtraLib(lib);
}