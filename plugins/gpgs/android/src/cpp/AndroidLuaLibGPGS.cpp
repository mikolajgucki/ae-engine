#include <sstream>

#include "Log.h"
#include "JNIAutoLocalRef.h"
#include "JNIThrowableUtil.h"
#include "AndroidLuaLibGPGS.h"

using namespace std;
using namespace ae::jni;

extern "C" {
// Declaration. Defined in SDL.
JNIEnv *Android_JNI_GetEnv(void);
}

namespace ae {
    
namespace gpgs {
    
/// The log tag.
static const char *logTag = "AE/GPGS";
    
/** The IAP Java class name. */
static const char *jClassName = "com/andcreations/gpgs/AEGPGS";  
    
/** */
void AndroidLuaLibGPGS::getJNIEnv() {
    env = Android_JNI_GetEnv();
}

/** */
void AndroidLuaLibGPGS::signIn() {
    Log::trace(logTag,"AndroidLuaLibGPGS::signIn()");
    
    getJNIEnv();
// class
    jclass clazz = env->FindClass(jClassName);
    if (clazz == (jclass)0) {
        Log::trace(logTag,"Class not found");
        return;
    }    
    JNIAutoLocalRef clazzLocalRef(env,clazz);
    
// method
    jmethodID method = env->GetStaticMethodID(clazz,"signIn","()V");
    if (method == (jmethodID)0) {
        Log::trace(logTag,"Method not found");
        return;
    }    
    
// call
    env->CallStaticVoidMethod(clazz,method);
    if (JNIThrowableUtil::logException(env,logTag) == true) {
        return;
    }
}
 

/** */
void AndroidLuaLibGPGS::signOut() {
    Log::trace(logTag,"AndroidLuaLibGPGS::signOut()");
    
    getJNIEnv();
// class
    jclass clazz = env->FindClass(jClassName);
    if (clazz == (jclass)0) {
        Log::trace(logTag,"Class not found");
        return;
    }    
    JNIAutoLocalRef clazzLocalRef(env,clazz);
    
// method
    jmethodID method = env->GetStaticMethodID(clazz,"signOut","()V");
    if (method == (jmethodID)0) {
        Log::trace(logTag,"Method not found");
        return;
    }    
    
// call
    env->CallStaticVoidMethod(clazz,method);    
    if (JNIThrowableUtil::logException(env,logTag) == true) {
        return;
    }    
}

/** */
bool AndroidLuaLibGPGS::isSignedIn() {
    Log::trace(logTag,"AndroidLuaLibGPGS::isSignedIn()");
    
    getJNIEnv();
// class
    jclass clazz = env->FindClass(jClassName);
    if (clazz == (jclass)0) {
        Log::trace(logTag,"Class not found");
        return false;
    }    
    JNIAutoLocalRef clazzLocalRef(env,clazz);
    
// method
    jmethodID method = env->GetStaticMethodID(clazz,"isSignedIn","()Z");
    if (method == (jmethodID)0) {
        Log::trace(logTag,"Method not found");
        return false;
    }      
    
// call
    jboolean isSignedIn = env->CallStaticBooleanMethod(clazz,method);
    if (JNIThrowableUtil::logException(env,logTag) == true) {
        return false;
    }    
    
    return isSignedIn == JNI_TRUE;
}

/** */
void AndroidLuaLibGPGS::unlockAchievement(const std::string &id) {
    ostringstream msg;
    msg << "AndroidLuaLibGPGS::unlockAchievement(" << id << ")";
    Log::trace(logTag,msg.str());
    
    getJNIEnv();
// class
    jclass clazz = env->FindClass(jClassName);
    if (clazz == (jclass)0) {
        Log::trace(logTag,"Class not found");
        return;
    }    
    JNIAutoLocalRef clazzLocalRef(env,clazz);
    
// method
    jmethodID method = env->GetStaticMethodID(clazz,"unlockAchievement",
        "(Ljava/lang/String;)V");
    if (method == (jmethodID)0) {
        Log::trace(logTag,"Method not found");
        return;
    }    

// identifier
    jstring jId = env->NewStringUTF(id.c_str());
    JNIAutoLocalRef jIdLocalRef(env,jId);

// call
    env->CallStaticVoidMethod(clazz,method,jId);  
    if (JNIThrowableUtil::logException(env,logTag) == true) {
        return;
    }    
}

/** */
void AndroidLuaLibGPGS::incrementAchievement(const std::string &id,int steps) {
    ostringstream msg;
    msg << "AndroidLuaLibGPGS::incrementAchievement(" <<
        id << "," << steps << ")";
    Log::trace(logTag,msg.str());
    
// does not make any sense
    if (steps <= 0) {
        return;
    }    
    
    getJNIEnv();
// class
    jclass clazz = env->FindClass(jClassName);
    if (clazz == (jclass)0) {
        Log::trace(logTag,"Class not found");
        return;
    }    
    JNIAutoLocalRef clazzLocalRef(env,clazz);
    
// method
    jmethodID method = env->GetStaticMethodID(clazz,"incrementAchievement",
        "(Ljava/lang/String;I)V");
    if (method == (jmethodID)0) {
        Log::trace(logTag,"Method not found");
        return;
    }    

// identifier
    jstring jId = env->NewStringUTF(id.c_str());
    JNIAutoLocalRef jIdLocalRef(env,jId);

// steps
    jint jSteps = (jint)steps;

// call
    env->CallStaticVoidMethod(clazz,method,jId,jSteps);
    if (JNIThrowableUtil::logException(env,logTag) == true) {
        return;
    }    
}

/** */
void AndroidLuaLibGPGS::setAchievementSteps(const std::string &id,int steps) {
    ostringstream msg;
    msg << "AndroidLuaLibGPGS::setAchievementSteps(" <<
        id << "," << steps << ")";
    Log::trace(logTag,msg.str());
    
// Google will reject it anyway
    if (steps <= 0) {
        return;
    }
    
    getJNIEnv();
// class
    jclass clazz = env->FindClass(jClassName);
    if (clazz == (jclass)0) {
        Log::trace(logTag,"Class not found");
        return;
    }    
    JNIAutoLocalRef clazzLocalRef(env,clazz);
    
// method
    jmethodID method = env->GetStaticMethodID(clazz,"setAchievementSteps",
        "(Ljava/lang/String;I)V");
    if (method == (jmethodID)0) {
        Log::trace(logTag,"Method not found");
        return;
    }    

// identifier
    jstring jId = env->NewStringUTF(id.c_str());
    JNIAutoLocalRef jIdLocalRef(env,jId);

// steps
    jint jSteps = (jint)steps;

// call
    env->CallStaticVoidMethod(clazz,method,jId,jSteps); 
    if (JNIThrowableUtil::logException(env,logTag) == true) {
        return;
    }     
}    

/** */
void AndroidLuaLibGPGS::submitScore(const string &leaderboardId,long score) {
    ostringstream msg;
    msg << "AndroidLuaLibGPGS::submitScore(" <<
        leaderboardId << "," << score << ")";
    Log::trace(logTag,msg.str());
    
    getJNIEnv();
// class
    jclass clazz = env->FindClass(jClassName);
    if (clazz == (jclass)0) {
        Log::trace(logTag,"Class not found");
        return;
    }    
    JNIAutoLocalRef clazzLocalRef(env,clazz);
    
// method
    jmethodID method = env->GetStaticMethodID(clazz,"submitScore",
        "(Ljava/lang/String;J)V");
    if (method == (jmethodID)0) {
        Log::trace(logTag,"Method not found");
        return;
    }    

// identifier
    jstring jLeaderboardId = env->NewStringUTF(leaderboardId.c_str());
    JNIAutoLocalRef jLeaderboardIdLocalRef(env,jLeaderboardId);
    
// score
    jlong jScore = (jlong)score;
    
// call
    env->CallStaticVoidMethod(clazz,method,jLeaderboardId,jScore);
    if (JNIThrowableUtil::logException(env,logTag) == true) {
        return;
    }     
}

/** */
void AndroidLuaLibGPGS::displayAchievements() {
    Log::trace(logTag,"AndroidLuaLibGPGS::displayAchievements()");
    
    getJNIEnv();
// class
    jclass clazz = env->FindClass(jClassName);
    if (clazz == (jclass)0) {
        Log::trace(logTag,"Class not found");
        return;
    }    
    JNIAutoLocalRef clazzLocalRef(env,clazz);
    
// method
    jmethodID method = env->GetStaticMethodID(
        clazz,"displayAchievements","()V");
    if (method == (jmethodID)0) {
        Log::trace(logTag,"Method not found");
        return;
    }    
    
// call
    env->CallStaticVoidMethod(clazz,method);  
    if (JNIThrowableUtil::logException(env,logTag) == true) {
        return;
    }     
}

/** */
void AndroidLuaLibGPGS::displayLeaderboard(const string &leaderboardId) {
    ostringstream msg;
    msg << "AndroidLuaLibGPGS::displayLeaderboard(" << leaderboardId << ")";
    Log::trace(logTag,msg.str());
    
    getJNIEnv();
// class
    jclass clazz = env->FindClass(jClassName);
    if (clazz == (jclass)0) {
        Log::trace(logTag,"Class not found");
        return;
    }    
    JNIAutoLocalRef clazzLocalRef(env,clazz);
    
// method
    jmethodID method = env->GetStaticMethodID(clazz,
        "displayLeaderboard","(Ljava/lang/String;)V");
    if (method == (jmethodID)0) {
        Log::trace(logTag,"Method not found");
        return;
    }    

// identifier
    jstring jLeaderboardId = env->NewStringUTF(leaderboardId.c_str());
    JNIAutoLocalRef jLeaderboardIdLocalRef(env,jLeaderboardId);
    
// call
    env->CallStaticVoidMethod(clazz,method,jLeaderboardId);
    if (JNIThrowableUtil::logException(env,logTag) == true) {
        return;
    }    
}

} // namespace
    
} // namespace