#ifndef AE_ANDROID_LUA_LIB_GPGS_H
#define AE_ANDROID_LUA_LIB_GPGS_H

#include <jni.h>
#include "LuaLibGPGS.h"

namespace ae {
    
namespace gpgs {
  
/**
 * \brief The Android implementation of te Google Play Game Services Lua
 *   library.
 */
class AndroidLuaLibGPGS:public LuaLibGPGS {
    /// The JNI environment.
    JNIEnv *env;    
    
    /** */
    void getJNIEnv();
    
public:
    /** */
    AndroidLuaLibGPGS():LuaLibGPGS(),env((JNIEnv *)0) {
    }
    
    /** */
    virtual ~AndroidLuaLibGPGS() {
    }
    
    /** */
    virtual void signIn();
    
    /** */
    virtual void signOut();
    
    /** */
    virtual bool isSignedIn();
    
    /** */
    virtual void unlockAchievement(const std::string &id);
    
    /** */
    virtual void incrementAchievement(const std::string &id,int steps);
    
    /** */
    virtual void setAchievementSteps(const std::string &id,int steps);    
    
    /** */
    virtual void submitScore(const std::string &leaderboardId,long score);
    
    /** */
    virtual void displayAchievements();
    
    /** */
    virtual void displayLeaderboard(const std::string &leaderboardId);   
};
    
} // namespace
    
} // namespace

#endif // AE_ANDROID_LUA_LIB_GPGS_H