#include <memory>
#include <jni.h>

#include "Log.h"
#include "JNIAutoLocalStr.h"
#include "AndroidLuaLibHTTP.h"
#include "HTTPLuaExtraLib.h"
#include "ae_sdl2.h"

using namespace std;
using namespace ae;
using namespace ae::jni;
using namespace ae::http;

/// The log cat.
static const char *logTag = "AE/HTTP";

/// The Android implementation of the Lua library.
static AndroidLuaLibHTTP *luaLibHttp;

/** */
extern "C" {
    /** */
    void Java_com_andcreations_http_AEHTTP_loadLuaLib(
        JNIEnv *env,jclass clazz);
    
    /** */
    void Java_com_andcreations_http_AEHTTP_receivedResponse(
        JNIEnv *env,jclass clazz,jstring jId,jint jCode,jstring jBody);
    
    /** */
    void Java_com_andcreations_http_AEHTTP_failedToReceiveResponse(
        JNIEnv *env,jclass clazz,jstring jId,jstring jErrorMsg);
};

/** */
void Java_com_andcreations_http_AEHTTP_loadLuaLib(JNIEnv *env,jclass clazz) {
    if (luaLibHttp != (AndroidLuaLibHTTP *)0) {
        Log::warning(logTag,"HTTP Lua library already loaded. "
            "Skipping loading again.");
        return;
    }    
    
    luaLibHttp = new (nothrow) AndroidLuaLibHTTP();
    if (luaLibHttp == (AndroidLuaLibHTTP *)0) {
        Log::error(logTag,"Failed to load HTTP library. No memory.");
        return;
    }
    
    HTTPLuaExtraLib *lib = new (nothrow) HTTPLuaExtraLib(luaLibHttp);
    if (lib == (HTTPLuaExtraLib *)0) {
        Log::error(logTag,"Failed to load HTTP library. No memory.");
        return;
    }
    
    aeAddLuaExtraLib(lib);
}

/** */
void Java_com_andcreations_http_AEHTTP_receivedResponse(
    JNIEnv *env,jclass clazz,jstring jId,jint jCode,jstring jBody) {
//
    const char *id = env->GetStringUTFChars(jId,(jboolean *)0);
    JNIAutoLocalStr idLocalStr(env,jId,id);
    
    const char *body = env->GetStringUTFChars(jBody,(jboolean *)0);
    JNIAutoLocalStr bodyLocalStr(env,jBody,body);
    
    luaLibHttp->receivedResponse(id,(int)jCode,body);
}

/** */
void Java_com_andcreations_http_AEHTTP_failedToReceiveResponse(
    JNIEnv *env,jclass clazz,jstring jId,jstring jErrorMsg) {
//
    const char *id = env->GetStringUTFChars(jId,(jboolean *)0);
    JNIAutoLocalStr idLocalStr(env,jId,id);
    
    const char *errorMsg = env->GetStringUTFChars(jErrorMsg,(jboolean *)0);
    JNIAutoLocalStr errorMsgLocalStr(env,jErrorMsg,errorMsg);
    
    luaLibHttp->failedToReceiveResponse(id,errorMsg);
}   