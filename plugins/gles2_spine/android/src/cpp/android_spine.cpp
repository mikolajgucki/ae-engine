#include <memory>
#include <jni.h>

#include "Log.h"
#include "SpineLuaExtraLib.h"
#include "ae_sdl2.h"

using namespace std;
using namespace ae;
using namespace ae::spine;

/// The log tag.
static const char *logTag = "AE/Spine";

/// Indicates if the library has been loaded.
static bool libLoaded = false;

/** */
extern "C" {
    /** */
    void Java_com_andcreations_spine_AESpine_loadLuaLib(JNIEnv *env);
};

/** */
void Java_com_andcreations_spine_AESpine_loadLuaLib(JNIEnv *env) {   
    if (libLoaded == true) {
        Log::warning(logTag,"Spine Lua library already loaded. "
            "Skipping loading again.");
        return;
    }  
    libLoaded = true;
    
    SpineLuaExtraLib *lib = new (nothrow) SpineLuaExtraLib();
    if (lib == (SpineLuaExtraLib *)0) {
        Log::error(logTag,"Failed to load Spine library. No memory");
        return;
    }
    
    aeAddLuaExtraLib(lib);
}