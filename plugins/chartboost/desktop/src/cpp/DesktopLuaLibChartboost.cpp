#include <sstream>
#include "DesktopLuaLibChartboost.h"

using namespace std;
using namespace ae;

namespace ae {
    
namespace chartboost {
    
/// The log tag.
static const char *logTag = "Desktop/Chartboost";
    
/** */
void DesktopLuaLibChartboost::init() {
    log->trace(logTag,"DesktopLuaLibChartboost::initInterstitial()");
}

/** */
void DesktopLuaLibChartboost::cacheInterstitial(const string &location) {
    ostringstream msg;
    msg << "DesktopLuaLibChartboost::cacheInterstitial(" << location << ")";
    log->trace(logTag,msg.str()); 
}

/** */
void DesktopLuaLibChartboost::showInterstitial(const string &location) {
    ostringstream msg;
    msg << "DesktopLuaLibChartboost::showInterstitial(" << location << ")";
    log->trace(logTag,msg.str()); 
}    
    
} // namespace
    
} // namespace