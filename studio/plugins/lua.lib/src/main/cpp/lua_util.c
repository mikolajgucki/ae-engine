#include <stdio.h>

#include "jni.h"
#include "lua.h"
#include "lauxlib.h"
#include "lualib.h"

#include "string_reader.h"

/** */
JNIEXPORT jstring JNICALL Java_com_andcreations_ae_studio_plugins_lua_lib_NativeLua_compile(
    JNIEnv *env,jclass clazz,jstring jSrc) {
//
    const char *src = (*env)->GetStringUTFChars(env,jSrc,(jboolean *)0);
    
// create Lua state
    lua_State *L = luaL_newstate();
    if (L == (lua_State *)0) {
        // TODO No memory.
        fprintf(stderr,"No memory");
        return (jstring)0;
    }    
  
    jstring jError = (jstring)0;
// compile by loading
    StringReaderData *data = createStringReaderData(src);
    int result = lua_load(L,stringReader,data,"test.lua","t");
    if (result != 0) {
        if (result == LUA_ERRMEM) {
            fprintf(stderr,"LUA_ERRMEM");
            return (jstring)0;
        }
        
        const char *error = lua_tostring(L,-1);
        jError = (*env)->NewStringUTF(env,error);
    }
    
// close Lua state
    lua_close(L);
    
    return jError;
}