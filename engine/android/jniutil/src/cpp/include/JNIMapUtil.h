#ifndef AE_JNI_MAP_UTIL_H
#define AE_JNI_MAP_UTIL_H

#include <string>
#include <jni.h>
#include "Error.h"

namespace ae {
    
namespace jni {
  
/**
 * \brief Provides utility methods related to java.util.Map.
 */
class JNIMapUtil:public Error {
    /// The JNI environment.
    JNIEnv *env;
    
    /// The map object.
    jobject map;
    
public:
    /** */
    JNIMapUtil(JNIEnv *env_):Error(),env(env_),map((jobject)0) {
    }
    
    /**
     * \brief Gets the Java object representing the map.
     * \return The Java object.
     */
    jobject getObject() const {
        return map;
    }    
    
    /**
     * \brief Creates a java.util.HashMap object.
     * \return <code>true</code> on success, sets error and returns
     *   <code>false</code> on error.
     */
    bool createHashMap();
    
    /**
     * \brief Puts a string-string pair.
     * \param key The key.
     * \param value The value.
     * \return <code>true</code> on success, sets error and returns
     *   <code>false</code> on error.
     */
    bool put(const std::string &key,const std::string &value);
};
    
} // namespace
    
} // namespace

#endif // AE_JNI_MAP_UTIL_H