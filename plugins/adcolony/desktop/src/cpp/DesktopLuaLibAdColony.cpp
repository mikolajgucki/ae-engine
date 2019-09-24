#include <sstream>
#include "Log.h"
#include "DesktopLuaLibAdColony.h"

using namespace std;
using namespace ae::engine;

namespace ae {
    
namespace adcolony {
 
/// The log tag.
const char *logTag = "AE/AdColony";
    
/** */
void DesktopLuaLibAdColony::requestInterstitial(const string &zoneId) {
// log
    ostringstream msg;
    msg << "DesktopLuaLibAdColony::requestInterstitial(" << zoneId << ")";
    dlog->trace(logTag,msg.str());
}

/** */
bool DesktopLuaLibAdColony::isInterstitialExpired(const string &zoneId) {
// log
    ostringstream msg;
    msg << "DesktopLuaLibAdColony::isInterstitialExpired(" << zoneId << ")";
    dlog->trace(logTag,msg.str());
    
    return false;
}

/** */
void DesktopLuaLibAdColony::showInterstitial(const string &zoneId) {
// log
    ostringstream msg;
    msg << "DesktopLuaLibAdColony::showInterstitial(" << zoneId << ")";
    dlog->trace(logTag,msg.str());
}

} // namespace
    
} // namespace
