#include "GameAnalyticsLuaExtraLib.h"
#include "DesktopPluginGameAnalytics.h"

namespace ae {
    
namespace gameanalytics {
    
/** */
DesktopPluginGameAnalytics::~DesktopPluginGameAnalytics() {
    if (luaLibGameAnalytics != (DesktopLuaLibGameAnalytics *)0) {
        delete luaLibGameAnalytics;
    }        
}
    
/** */
bool DesktopPluginGameAnalytics::init() {
// create library    
    luaLibGameAnalytics = new DesktopLuaLibGameAnalytics(getLog());
    return true;
}

/** */
bool DesktopPluginGameAnalytics::addLuaExtraLibs() {
    getLuaEngine()->addExtraLib(
        new GameAnalyticsLuaExtraLib(luaLibGameAnalytics));
    return true;
}

} // namespace
    
} // namespace