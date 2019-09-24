#include <memory>
#include <jni.h>

#include "Log.h"
#include "ae_sdl2.h"
#include "GPUGLESLuaEnginePlugin.h"

using namespace std;
using namespace ae;
using namespace ae::gles;

/// The log cat.
static const char *logTag = "AE/GPUGLES";

/// The plugin.
static GPUGLESLuaEnginePlugin *plugin = (GPUGLESLuaEnginePlugin *)0;

/** */
extern "C" {
    /** */
    void Java_com_andcreations_gpugles_AEGPUGLES_addLuaEnginePlugin(
        JNIEnv *env);
};

/** */
void Java_com_andcreations_gpugles_AEGPUGLES_addLuaEnginePlugin(JNIEnv *env) {
// check if added
    if (plugin != (GPUGLESLuaEnginePlugin *)0) {
        Log::warning(logTag,"GPU/GLES Lua engine plugin already added. "
            "Skipping adding again.");
        return;
    }
    
// create
    plugin = new (nothrow) GPUGLESLuaEnginePlugin();
    if (plugin == (GPUGLESLuaEnginePlugin *)0) {
        Log::error(logTag,"Failed to add GPU/GLES Lua engine plugin. "
            "No memory.");
        return;
    }
    
// add
    aeAddLuaEnginePlugin(plugin);
}