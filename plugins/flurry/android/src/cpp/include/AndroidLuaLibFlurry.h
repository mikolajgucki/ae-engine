#ifndef AE_ANDROID_LUA_LIB_FLURRY_H
#define AE_ANDROID_LUA_LIB_FLURRY_H

#include <jni.h>
#include "LuaLibFlurry.h"

namespace ae {
    
namespace flurry {
    
/**
 * \brief The Android implementation of the Flurry Lua library.
 */
class AndroidLuaLibFlurry:public LuaLibFlurry {
    /// The JNI environment.
    JNIEnv *env;    
    
    /** */
    void getJNIEnv();
    
    /**
     * \brief Creates a Java map object from parameters.
     * \param parameters The parameters.
     * \return The Java object or zero on error.
     */
    jobject createMap(std::map<std::string,std::string> &parameters);

public:
    /** */
    AndroidLuaLibFlurry():LuaLibFlurry(),env((JNIEnv *)0) {
    }
    
    /** */
    virtual ~AndroidLuaLibFlurry() {
    }
    
    /** */
    virtual void init();

    /** */
    virtual void logEvent(const std::string &eventId,bool timed);
    
    /** */
    virtual void logEvent(const std::string &eventId,
        std::map<std::string,std::string> &parameters,bool timed);
    
    /** */
    virtual void endTimedEvent(const std::string &eventId);
    
    /** */
    virtual void endTimedEvent(const std::string &eventId,
        std::map<std::string,std::string> &parameters);    
};
    
} // namespace
    
} // namespace

#endif // AE_ANDROID_LUA_LIB_FLURRY_H