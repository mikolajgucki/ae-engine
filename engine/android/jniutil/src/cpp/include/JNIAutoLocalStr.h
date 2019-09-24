#ifndef AE_JNI_AUTO_LOCAL_STR_H
#define AE_JNI_AUTO_LOCAL_STR_H

#include <jni.h>

namespace ae {
    
namespace jni {
  
/**
 * \brief Automatically deletes local string in the destructor.
 */
class JNIAutoLocalStr {
    /** */
    JNIEnv *env;
    
    /** */
    jstring localRef;
    
    /** */
    const char *str;
    
    /** */
    JNIAutoLocalStr(const JNIAutoLocalStr& jniAutoLocalStr) {
    }
    
    /** */
    JNIAutoLocalStr& operator=(const JNIAutoLocalStr& jniAutoLocalStr) {
    	return *this;
    }
    
public:
    /** */
    JNIAutoLocalStr(JNIEnv *env_,jstring localRef_,const char *str_):
        env(env_),localRef(localRef_),str(str_) {
    }
    
    /** */
    ~JNIAutoLocalStr() {
        env->ReleaseStringUTFChars(localRef,str);
        env->DeleteLocalRef(localRef);
    }
};
    
} // namespace
    
} // namespace

#endif // AE_JNI_AUTO_LOCAL_STR_H