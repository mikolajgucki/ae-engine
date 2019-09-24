#ifndef AE_ANDROID_LUA_LIB_IAP_H
#define AE_ANDROID_LUA_LIB_IAP_H

#include <jni.h>
#include "LuaLibIAP.h"

namespace ae {

namespace iap {
  
/**
 * \brief The Android implementation of the IAP Lua library.
 */
class AndroidLuaLibIAP:public LuaLibIAP {
    /// The JNI environment.
    JNIEnv *env;    
    
    /** */
    void getJNIEnv();
    
public:
    /** */
    AndroidLuaLibIAP():LuaLibIAP(),env((JNIEnv *)0) {
    }
    
    /** */
    virtual ~AndroidLuaLibIAP() {
    }
    
    /** */
    virtual void init();
    
    /** */
    virtual bool isSupported();
    
    /** */
    virtual void purchase(const std::string& productId);
    
    /** */
    virtual bool getProducts(std::vector<IAPProduct *> &products);
    
    /** */
	virtual void restorePurchases();
};
    
} // namespace
    
} // namespace

#endif // AE_ANDROID_LUA_LIB_IAP_H