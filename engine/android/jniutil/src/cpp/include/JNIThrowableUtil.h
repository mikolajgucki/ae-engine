#ifndef AE_JNI_THROWABLE_UTIL_H
#define AE_JNI_THROWABLE_UTIL_H

#include <jni.h>
#include <string>
#include <vector>

#include "Error.h"

namespace ae {
    
namespace jni {
    
/**
 * \brief Provides utility methods related to java.lang.Throwable.
 */
class JNIThrowableUtil:public Error {
    /// The JNI environment.
    JNIEnv *env;

    
public:
    /** */
    JNIThrowableUtil(JNIEnv *env_):Error(),env(env_) {
    }      
    
    /** */
    virtual ~JNIThrowableUtil() {
    }  
    
    /**
     * \brief Indicates if an exception has been thrown.
     * \return <code>true</code> if thrown, <code>false</code> otherwise.
     */
    bool hasException();
    
    /**
     * \brief Clears the thrown exception.
     */
    void clearException();

    /**
     * \brief Calls the <code>toString()</code> method.
     * \param throwable The throwable.
     * \param str The result string. 
     */
    void toString(jthrowable throwable,std::string &str);
    
    /**
     * \brief Converts an exception to string if one has been thrown. Does not
     *   set the result if no exception has been thrown.
     * \param str The result string.
     * \return <code>true</code> if an exception has been thrown,
     *   <code>false</code> otherwise.
     */    
    bool toString(std::string &str);
    
    /**
     * \brief Gets the stack trace of a throwable.
     * \param throwable The throwable.
     * \param stackTrace The result stack trace.
     */
    void getStackTrace(jthrowable throwable,
        std::vector<std::string> &stackTrace);
    
    /**
     * \brief Logs and clears an exception if occured.
     * \param logTag The log tag.
     * \return <code>true</code> if an exception occured, <code>false</code>
     *   otherwise.
     */
    bool logException(const char *logTag);
    
    /**
     * \brief Logs and clears an exception if occured.
     * \param env The JVM environment.
     * \param logTag The log tag.
     * \return <code>true</code> if an exception occured, <code>false</code>
     *   otherwise.
     */    
    static bool logException(JNIEnv *env,const char *logTag) {
        JNIThrowableUtil util(env);
        return util.logException(logTag);
    }
    
    /**
     * \brief Converts an exception to string if one has been thrown. Does not
     *   set the result if no exception has been thrown.
     * \param env The JVM environment.
     * \param str The result string.
     * \return <code>true</code> if an exception has been thrown,
     *   <code>false</code> otherwise.
     */
    static bool toString(JNIEnv *env,std::string &str) {
        JNIThrowableUtil util(env);
        return util.toString(str);
    }
};
    
} // namespace
    
} // namespace

#endif // AE_JNI_THROWABLE_UTIL_H