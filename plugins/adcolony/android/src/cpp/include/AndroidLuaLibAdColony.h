#ifndef AE_ANDROID_LUA_LIB_AD_COLONY_H
#define AE_ANDROID_LUA_LIB_AD_COLONY_H

#include <string>
#include <jni.h>
#include "LuaLibAdColony.h"

namespace ae {
    
namespace adcolony {
  
/**
 * \brief The Android implementation of the AdColony Lua library.
 */
class AndroidLuaLibAdColony:public LuaLibAdColony {
    /// The JNI environment (from SDL).
    JNIEnv *env;
    
    /** */
    void getJNIEnv();
    
public:
    /**
     * \brief Constructs an AndroidLuaLibAdColony.
     */
    AndroidLuaLibAdColony():LuaLibAdColony(),env((JNIEnv *)0) {
    }
            
    /** */
    virtual ~AndroidLuaLibAdColony() {
    }
    
    /** */
    virtual void requestInterstitial(const std::string& zoneId);   
    
    /** */
    virtual void showInterstitial(const std::string& zoneId);   
    
    /** */
    virtual bool isInterstitialExpired(const std::string &zoneId);
};
    
} // namespace
    
} // namespace

#endif // AE_ANDROID_LUA_LIB_AD_COLONY_H