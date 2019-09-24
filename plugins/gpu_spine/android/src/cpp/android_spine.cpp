#include <memory>
#include <jni.h>

#include "Log.h"
#include "GPUSpineLuaEnginePlugin.h"
#include "ae_sdl2.h"

using namespace std;
using namespace ae;
using namespace ae::spine;

/// The log tag.
static const char *logTag = "AE/Spine";

/// The plugin.
static GPUSpineLuaEnginePlugin *plugin = (GPUSpineLuaEnginePlugin *)0;

/** */
extern "C" {
    /** */
    void Java_com_andcreations_spine_AESpine_addLuaEnginePlugin(JNIEnv *env);
};

/** */
void Java_com_andcreations_spine_AESpine_addLuaEnginePlugin(JNIEnv *env) {   
// check if added
    if (plugin != (GPUSpineLuaEnginePlugin *)0) {
        Log::warning(logTag,"GPU Spine Lua engine plugin already added. "
            "Skipping adding again.");
        return;
    }
    
// create
    plugin = new (nothrow) GPUSpineLuaEnginePlugin();
    if (plugin == (GPUSpineLuaEnginePlugin *)0) {
        Log::error(logTag,"Failed to add GPU Spine Lua engine plugin. "
            "No memory.");
        return;
    }
    
// add
    aeAddLuaEnginePlugin(plugin);
}