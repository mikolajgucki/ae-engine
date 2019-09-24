#ifndef AE_IOS_LUA_LIB_VUNGLE_H
#define AE_IOS_LUA_LIB_VUNGLE_H

#import <VungleSDK/VungleSDK.h>
#include "AEVungleSDKDelegate.h"
#include "LuaLibVungle.h"

/**
 * \brief The iOS implementation of the Vungle Lua library.
 */
class AEiOSLuaLibVungle:public ::ae::vungle::LuaLibVungle {
    /// The application identifier.
    std::string appId;
    
    /// The delegate.
    AEVungleSDKDelegate *aeDelegate;
    
public:
    /** */
    AEiOSLuaLibVungle(const std::string &appId_):LuaLibVungle(),appId(appId_) {
    }
    
    /** */
    virtual ~AEiOSLuaLibVungle() {
    }

    /** */
    virtual void init();
    
    /** */
    virtual void playAd();
};

#endif // AE_IOS_LUA_LIB_VUNGLE_H