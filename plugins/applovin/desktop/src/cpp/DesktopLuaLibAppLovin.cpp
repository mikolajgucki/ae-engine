#include <sstream>

#include "DesktopLuaLibAppLovin.h"

using namespace std;
using namespace ae;

namespace ae {
    
namespace applovin {
    
/// The log tag.
static const char *logTag = "Desktop/AppLovin";

/** */
void DesktopLuaLibAppLovin::init(const string &bannerSize) {
    ostringstream msg;
    msg << "DesktopLuaLibAppLovin::init(" << bannerSize << ")";
    log->trace(logTag,msg.str());    
}

/** */
bool DesktopLuaLibAppLovin::isInterstitialAdReadyForDisplay() {
    log->trace(logTag,
        "DesktopLuaLibAppLovin::isInterstitialAdReadyForDisplay()");
    return false;
}

/** */
void DesktopLuaLibAppLovin::showInterstitialAd() {
    log->trace(logTag,"DesktopLuaLibAppLovin::showInterstitialAd()");
}
    
} // namespace
    
} // namespace