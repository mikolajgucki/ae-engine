#include <memory>
#include <jni.h>

#include "Log.h"
#include "ae_sdl2.h"
#include "AndroidLuaLibGameAnalytics.h"
#include "GameAnalyticsLuaExtraLib.h"

using namespace std;
using namespace ae;
using namespace ae::gameanalytics;

/// The log cat.
static const char *logTag = "AE/GameAnalytics";

/// The Android implementation of the Lua library.
static AndroidLuaLibGameAnalytics *luaLibGameAnalytics;

/** */
extern "C" {
    /** */
    void Java_com_andcreations_gameanalytics_AEGameAnalytics_loadLuaLib(
        JNIEnv *env);   
};

/** */
void Java_com_andcreations_gameanalytics_AEGameAnalytics_loadLuaLib(
    JNIEnv *env) {
//
    if (luaLibGameAnalytics != (AndroidLuaLibGameAnalytics *)0) {
        Log::warning(logTag,"GameAnalytics Lua library already loaded. "
            "Skipping loading again.");
        return;
    }    
    
    luaLibGameAnalytics = new (nothrow) AndroidLuaLibGameAnalytics();
    if (luaLibGameAnalytics == (AndroidLuaLibGameAnalytics *)0) {
        Log::error(logTag,"Failed to load gameanalytics library. No memory.");
        return;        
    }
    
    GameAnalyticsLuaExtraLib *lib =
        new (nothrow) GameAnalyticsLuaExtraLib(luaLibGameAnalytics);
    if (lib == (GameAnalyticsLuaExtraLib *)0) {
        Log::error(logTag,"Failed to load gameanalytics library. No memory.");
        return;        
    }
    
    aeAddLuaExtraLib(lib);
}