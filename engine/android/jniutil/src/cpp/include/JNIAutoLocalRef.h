#ifndef AE_JNI_AUTO_LOCAL_REF_H
#define AE_JNI_AUTO_LOCAL_REF_H

#include <jni.h>

namespace ae {
    
namespace jni {
  
/**
 * \brief Automatically deletes local reference in the destructor.
 */
class JNIAutoLocalRef {
    /** */
    JNIEnv *env;
    
    /** */
    jobject localRef;
    
    /** */
    JNIAutoLocalRef(const JNIAutoLocalRef& jniAutoLocalRef) {
    }
    
    /** */
    JNIAutoLocalRef& operator=(const JNIAutoLocalRef& jniAutoLocalRef) {
    	return *this;
    }
    
public:
    /** */
    JNIAutoLocalRef(JNIEnv *env_,jobject localRef_):
        env(env_),localRef(localRef_) {
    }
    
    /** */
    ~JNIAutoLocalRef() {
        env->DeleteLocalRef(localRef);
    }
};
    
} // namespace
    
} // namespace

#endif // AE_JNI_AUTO_LOCAL_REF_H