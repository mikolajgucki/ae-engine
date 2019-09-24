#include <sstream>
#include "DesktopLuaLibTapjoy.h"

using namespace std;

namespace ae {
    
namespace tapjoy {

/// The log tag.
static const char *logTag = "AE/Tapjoy";
    
/** */
bool DesktopLuaLibTapjoy::isConnected() {
    return true;
}

/** */
void DesktopLuaLibTapjoy::requestContent(const std::string &placement) {
// log
    ostringstream msg;
    msg << "DesktopLuaLibTapjoy::requestContent(" << placement << ")";
    log->trace(logTag,msg.str());
}

/** */
bool DesktopLuaLibTapjoy::isContentReady(const std::string &placement) {
    return false;
}

/** */
void DesktopLuaLibTapjoy::showContent(const std::string &placement) {
// log
    ostringstream msg;
    msg << "DesktopLuaLibTapjoy::showContent(" << placement << ")";
    log->trace(logTag,msg.str());
}
    
} // namespace    
    
} // namespace