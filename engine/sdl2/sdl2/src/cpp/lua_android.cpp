/*
-- @module android
-- @group OS
*/
#include "lua.hpp"

#include "lua_common.h"
#include "JNIAutoLocalRef.h"
#include "JNIThrowableUtil.h"
#include "lua_android.h"

extern "C" {
// Declaration. Defined in SDL.
JNIEnv *Android_JNI_GetEnv(void);
}

using namespace ae::lua;
using namespace ae::jni;

namespace ae {

namespace android {
    
namespace lua {

/// The log tag.
static const char *logTag = "AE/Android";
    
/// The library name.
static const char *androidName = "android";    

/*
-- @name .getSDK
-- @func
-- @brief Gets the Android SDK number.
-- @return The SDK number. 
*/
static int androidGetSDK(lua_State *L) {
    JNIEnv *env = Android_JNI_GetEnv();
    
// class
    jclass clazz = env->FindClass("com/andcreations/ae/AE");
    if (clazz == (jclass)0) {
        luaPushError(L,"Class not found");
        return 0;
    }    
    JNIAutoLocalRef clazzLocalRef(env,clazz);    
    
// method
    jmethodID method = env->GetStaticMethodID(clazz,"getSDK","()I");
    if (method == (jmethodID)0) {
        luaPushError(L,"Method not found");
        return 0;
    }    
    
// call
    int sdkVersion = env->CallStaticIntMethod(clazz,method);
    // TODO Check exception.

// return
    lua_pushinteger(L,(lua_Integer)sdkVersion);
    return 1;
}

/** */
static const struct luaL_Reg androidFuncs[] = {
    {"getSDK",androidGetSDK},
    {0,0}
}; 

/** */
static int androidRequireFunc(lua_State *L) {
    luaL_newlib(L,androidFuncs);
    return 1;    
}

/** */
void loadAndroidLib(DLog *log,::ae::engine::LuaEngine *luaEngine,Error *error) {
    log->trace(logTag,"loadAndroidLib()");
    lua_State *L = luaEngine->getLuaState();
    
// load the library
    luaL_requiref(L,androidName,androidRequireFunc,1);
    lua_pop(L,1);    
}

/** */
void unloadAndroidLib(DLog *log) {
    log->trace(logTag,"unloadAndroidLib()");
}

} // namespace

} // namespace

} // namespace