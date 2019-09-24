#include <memory>
#include <jni.h>

#include "Log.h"
#include "ae_sdl2.h"
#include "GPULuaEnginePlugin.h"

using namespace std;
using namespace ae;
using namespace ae::gpu;

/// The log cat.
static const char *logTag = "AE/GPU";

/// The plugin.
static GPULuaEnginePlugin *plugin = (GPULuaEnginePlugin *)0;

/** */
extern "C" {
    /** */
    void Java_com_andcreations_gpu_AEGPU_addLuaEnginePlugin(JNIEnv *env);
};

/** */
void Java_com_andcreations_gpu_AEGPU_addLuaEnginePlugin(JNIEnv *env) {
// check if added
    if (plugin != (GPULuaEnginePlugin *)0) {
        Log::warning(logTag,"GPU Lua engine plugin already added. "
            "Skipping adding again.");
        return;
    }
    
// create
    plugin = new (nothrow) GPULuaEnginePlugin();
    if (plugin == (GPULuaEnginePlugin *)0) {
        Log::error(logTag,"Failed to add GPU Lua engine plugin. No memory.");
        return;
    }
    
// add
    aeAddLuaEnginePlugin(plugin);
}