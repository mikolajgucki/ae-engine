#ifndef AE_IOS_LUA_LIB_TAPJOY_H
#define AE_IOS_LUA_LIB_TAPJOY_H

#include <string> 
#include "LuaLibTapjoy.h"

namespace ae {
    
namespace tapjoy {
    
/**
 * \brief The iOS implementation of the Tapjoy Lua library.
 */
class AEiOSLuaLibTapjoy:public LuaLibTapjoy {
    /** */
    static bool debug;
    
public:
    /** */
    AEiOSLuaLibTapjoy():LuaLibTapjoy() {
    }
    
    /**
     * \brief Initializes the Lua library in debug mode.
     */
    static void initDebug();
    
    /**
     * \brief Connects to Tapjoy.
     * \param sdkKey The SDK key.
     */
    static void connect(const std::string &sdkKey);
    
    /**
     * \brief Adds push notification observers.
     */
    static void addSDLPushNotificationObservers();
    
    /**
     * \brief Initializes push notifications.
     * \param application The application.
     * \param launchOptions The launch options.
     */
    static void initPushNotifications(UIApplication *application,
        NSDictionary *launchOptions);
};
    
} // namespace
    
} // namespace

#endif // AE_IOS_LUA_LIB_TAPJOY_H