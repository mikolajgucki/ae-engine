#ifndef AE_JNI_LIST_UTIL_H
#define AE_JNI_LIST_UTIL_H

#include <jni.h>
#include "Error.h"

namespace ae {
    
namespace jni {
    
/**
 * \brief Provides utility methods related to java.util.List.
 */
class JNIListUtil:public Error {
    /// The JNI environment.
    JNIEnv *env;
    
    /// The list object.
    jobject list;
    
public:
    /** */
    JNIListUtil(JNIEnv *env_,jobject list_):Error(),env(env_),list(list_) {
    }      
    
    /** */
    virtual ~JNIListUtil() {
    }  
    
    /**
     * \brief Gets the size of a list.
     * \return The size list.
     */
    int size();
    
    /**
     * \brief Gets an object at an index.
     * \param index The index.
     * \return The object at the index.
     */
    jobject get(int index);
};
    
} // namespace
    
} // namespace

#endif // AE_JNI_LIST_UTIL_H