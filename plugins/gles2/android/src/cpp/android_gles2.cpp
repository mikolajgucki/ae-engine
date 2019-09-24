#include <memory>
#include <jni.h>

#include "Log.h"
#include "ae_sdl2.h"
#include "GLES2LuaEnginePlugin.h"

using namespace std;
using namespace ae;
using namespace ae::gles2;

/// The log cat.
static const char *logTag = "AE/GLES2";

/// The plugin.
static GLES2LuaEnginePlugin *plugin = (GLES2LuaEnginePlugin *)0;

/** */
extern "C" {
    /** */
    void Java_com_andcreations_gles2_AEGLES2_addLuaEnginePlugin(JNIEnv *env);   
};

/** */
void Java_com_andcreations_gles2_AEGLES2_addLuaEnginePlugin(JNIEnv *env) {
// check if added
    if (plugin != (GLES2LuaEnginePlugin *)0) {
        Log::warning(logTag,"GLES2 Lua engine plugin already added. "
            "Skipping adding again.");
        return;
    }
    
// create
    plugin = new (nothrow) GLES2LuaEnginePlugin();
    if (plugin == (GLES2LuaEnginePlugin *)0) {
        Log::error(logTag,"Failed to add GLES2 Lua engine plugin. No memory.");
        return;
    }

// add
    aeAddLuaEnginePlugin(plugin);
}