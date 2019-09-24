#include <sstream>

#include "Log.h"
#include "JNIAutoLocalRef.h"
#include "JNIAutoLocalStr.h"
#include "JNIThrowableUtil.h"
#include "JNIListUtil.h"
#include "AndroidLuaLibIAP.h"

extern "C" {
// Declaration. Defined in SDL.
JNIEnv *Android_JNI_GetEnv(void);
}

using namespace std;
using namespace ae::jni;

namespace ae {
    
namespace iap {
    
/// The log tag.
static const char *logTag = "AE/IAP";
    
/** The IAP Java class name. */
static const char *jClassName = "com/andcreations/iap/IAP";    
    
/** The IABProduct Java class name. */
static const char *iabProductClassName = "com/andcreations/iab/IABProduct";    

/** */
void AndroidLuaLibIAP::getJNIEnv() {
    env = Android_JNI_GetEnv();
}

/** */
void AndroidLuaLibIAP::init() {
    Log::trace(logTag,"AndroidLuaLibIAP::init()");
    
    getJNIEnv();
// class
    jclass clazz = env->FindClass(jClassName);
    if (clazz == (jclass)0) {
        Log::trace(logTag,"Class not found");
        return;
    }    
    JNIAutoLocalRef clazzLocalRef(env,clazz);
    
// method
    jmethodID method = env->GetStaticMethodID(clazz,"init","()V");
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
bool AndroidLuaLibIAP::isSupported() {
    Log::trace(logTag,"AndroidLuaLibIAP::isSupported()");
    
    getJNIEnv();
// class
    jclass clazz = env->FindClass(jClassName);
    if (clazz == (jclass)0) {
        Log::trace(logTag,"Class not found");
        return false;
    }    
    JNIAutoLocalRef clazzLocalRef(env,clazz);
    
// method
    jmethodID method = env->GetStaticMethodID(clazz,"isSupported","()Z");
    if (method == (jmethodID)0) {
        Log::trace(logTag,"Method not found");
        return false;
    }  
    
// call
    jboolean supported = env->CallStaticBooleanMethod(clazz,method);
    if (JNIThrowableUtil::logException(env,logTag) == true) {
        return false;
    }
    
    return supported == JNI_TRUE;
}


/** */
void AndroidLuaLibIAP::purchase(const string& productId) {
// log
    ostringstream msg;
    msg << "AndroidLuaLibIAP::purchase(" << productId << ")";
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
    jmethodID method = env->GetStaticMethodID(
        clazz,"purchase","(Ljava/lang/String;)V");
    if (method == (jmethodID)0) {
        Log::trace(logTag,"Method not found");
        return;
    }
    
// product
    jstring jProductId = env->NewStringUTF(productId.c_str());
    JNIAutoLocalRef jProductIdLocalRef(env,jProductId);
    
// call
    env->CallStaticVoidMethod(clazz,method,jProductId);
    if (JNIThrowableUtil::logException(env,logTag) == true) {
        return;
    }    
}

/** */
bool AndroidLuaLibIAP::getProducts(std::vector<IAPProduct *> &products) {
    Log::trace(logTag,"AndroidLuaLibIAP::getProducts()");
    
    getJNIEnv();
// class
    jclass clazz = env->FindClass(jClassName);
    if (clazz == (jclass)0) {
        Log::trace(logTag,"Class not found");
        return false;
    }    
    JNIAutoLocalRef clazzLocalRef(env,clazz);    
    
// getIABProducts() method
    jmethodID getIabProductsMethod = env->GetStaticMethodID(
        clazz,"getIABProducts","()Ljava/util/List;");
    if (getIabProductsMethod == (jmethodID)0) {
        setError("Java method not found");
        return false;
    }    
    
// IABProduct class
    jclass iabProductClass = env->FindClass("com/andcreations/iab/IABProduct");
    if (iabProductClass == (jclass)0) {
        setError("Java class com/andcreations/iab/IABProduct not found");
        return false;
    }
    JNIAutoLocalRef iabProductClassLocalRef(env,iabProductClass);
    
// getId() method
    jmethodID getIdMethod = env->GetMethodID(
        iabProductClass,"getId","()Ljava/lang/String;");
    if (getIdMethod == (jmethodID)0) {
        setError("Java method not found");
        return false;
    }
    
// getTitle() method
    jmethodID getTitleMethod = env->GetMethodID(
        iabProductClass,"getTitle","()Ljava/lang/String;");
    if (getTitleMethod == (jmethodID)0) {
        setError("Java method not found");
        return false;
    }
    
// getPrice() method
    jmethodID getPriceMethod = env->GetMethodID(
        iabProductClass,"getPrice","()Ljava/lang/String;");
    if (getPriceMethod == (jmethodID)0) {
        setError("Java method not found");
        return false;
    }
    
// getPriceInMicros() method
    jmethodID getPriceInMicrosMethod = env->GetMethodID(
        iabProductClass,"getPriceInMicros","()I");
    if (getPriceInMicrosMethod == (jmethodID)0) {
        setError("Java method not found");
        return false;
    }
    
// getCurrencyCode() method
    jmethodID getCurrencyCodeMethod = env->GetMethodID(
        iabProductClass,"getCurrencyCode","()Ljava/lang/String;");
    if (getCurrencyCodeMethod == (jmethodID)0) {
        setError("Java method not found");
        return false;
    }
    
// call getIABProducts()
    jobject list = env->CallStaticObjectMethod(clazz,getIabProductsMethod);
    if (JNIThrowableUtil::logException(env,logTag) == true) {
        return false;
    }
    JNIAutoLocalRef listLocalRef(env,list);
    JNIListUtil listUtil(env,list);
    
    int listSize = listUtil.size();
// for each product
    for (int index = 0; index < listSize; index++) {
        jobject iabProduct = listUtil.get(index);
        JNIAutoLocalRef iabProductLocalRef(env,iabProduct);
        
    // identifier
        jstring jId = (jstring)env->CallObjectMethod(iabProduct,getIdMethod);
        if (JNIThrowableUtil::logException(env,logTag) == true) {
            return false;
        }        
        const char *id = env->GetStringUTFChars(jId,(jboolean *)0);
        JNIAutoLocalStr jIdLocalRef(env,jId,id);
        
    // title
        jstring jTitle = (jstring)env->CallObjectMethod(
            iabProduct,getTitleMethod);
        if (JNIThrowableUtil::logException(env,logTag) == true) {
            return false;
        }        
        const char *title = env->GetStringUTFChars(jTitle,(jboolean *)0);
        JNIAutoLocalStr jTitleLocalRef(env,jTitle,title);
        
    // price   
        jstring jPrice = (jstring)env->CallObjectMethod(
            iabProduct,getPriceMethod);
        if (JNIThrowableUtil::logException(env,logTag) == true) {
            return false;
        }        
        const char *price = env->GetStringUTFChars(jPrice,(jboolean *)0);
        JNIAutoLocalStr jPriceLocalRef(env,jPrice,price);
        
    // price in micros and cents
        jint jPriceInMicros = (jlong)env->CallIntMethod(
            iabProduct,getPriceInMicrosMethod);
        if (JNIThrowableUtil::logException(env,logTag) == true) {
            return false;
        }
        int priceInCents = (int)(jPriceInMicros / 10000);
    
    // currency code
        jstring jCurrencyCode = (jstring)env->CallObjectMethod(
            iabProduct,getCurrencyCodeMethod);
        if (JNIThrowableUtil::logException(env,logTag) == true) {
            return false;
        }        
        const char *currencyCode = env->GetStringUTFChars(
            jCurrencyCode,(jboolean *)0);
        JNIAutoLocalStr jCurrencyCodeLocalRef(env,jCurrencyCode,currencyCode);        
        
    // create product
        IAPProduct *product = new (nothrow) IAPProduct(
            id,title,price,priceInCents,currencyCode);
        if (product == (IAPProduct *)0) {
            setNoMemoryError();
            return false;
        }
        products.push_back(product);
    }
    
    return true;
}

/** */
void AndroidLuaLibIAP::restorePurchases() {
    Log::trace(logTag,"AndroidLuaLibIAP::restorePurchases()");
    
    getJNIEnv();
// class
    jclass clazz = env->FindClass(jClassName);
    if (clazz == (jclass)0) {
        Log::trace(logTag,"Class not found");
        return;
    }    
    JNIAutoLocalRef clazzLocalRef(env,clazz);
    
// method
    jmethodID method = env->GetStaticMethodID(clazz,"restorePurchases","()V");
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

} // namespace
    
} // namespace