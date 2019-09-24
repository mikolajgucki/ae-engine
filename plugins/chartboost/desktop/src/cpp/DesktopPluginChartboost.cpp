#include "ChartboostLuaExtraLib.h"
#include "DesktopPluginChartboost.h"

namespace ae {
    
namespace chartboost {
    
/** */
DesktopPluginChartboost::~DesktopPluginChartboost() {
    if (luaLibChartboost != (DesktopLuaLibChartboost *)0) {
        delete luaLibChartboost;
    }
}
    
/** */
bool DesktopPluginChartboost::init() {
// create library
    luaLibChartboost = new DesktopLuaLibChartboost(getLog());
    return true;
}

/** */
bool DesktopPluginChartboost::addLuaExtraLibs() {
    getLuaEngine()->addExtraLib(new ChartboostLuaExtraLib(luaLibChartboost));
    return true;
}

} // namespace
    
} // namespace